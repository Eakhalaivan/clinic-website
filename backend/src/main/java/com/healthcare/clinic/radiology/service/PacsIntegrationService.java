package com.healthcare.clinic.radiology.service;

import com.healthcare.clinic.radiology.entity.DicomStudy;
import com.healthcare.clinic.radiology.entity.ImagingRequest;
import com.healthcare.clinic.radiology.repository.DicomStudyRepository;
import com.healthcare.clinic.radiology.repository.ImagingRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Base64;



@Service
@RequiredArgsConstructor
@Slf4j
public class PacsIntegrationService {

    private final DicomStudyRepository studyRepository;
    private final ImagingRequestRepository requestRepository;

    @Value("${orthanc.url:http://localhost:8042}")
    private String orthancUrl;

    @Value("${orthanc.username:orthanc}")
    private String orthancUsername;

    @Value("${orthanc.password:orthanc}")
    private String orthancPassword;

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public DicomStudy ingestDicomFile(Long requestId, MultipartFile file, String technicianId) {
        ImagingRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Imaging Request not found: " + requestId));

        if (!"CHECKED_IN".equals(request.getStatus()) && !"ACQUIRED".equals(request.getStatus())) {
            throw new IllegalStateException("Cannot ingest DICOM for request in status: " + request.getStatus());
        }

        try {
            // Push DICOM to Orthanc
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            if (orthancUsername != null && !orthancUsername.isEmpty()) {
                String auth = orthancUsername + ":" + orthancPassword;
                byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
                headers.add("Authorization", "Basic " + new String(encodedAuth));
            }

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(file.getBytes(), headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    orthancUrl + "/instances",
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map<String, Object> body = response.getBody();
            if (body == null || !body.containsKey("ParentStudy")) {
                throw new RuntimeException("Invalid response from Orthanc: " + body);
            }

            String orthancStudyId = (String) body.get("ParentStudy");

            // Fetch actual StudyInstanceUID from Orthanc
            HttpEntity<Void> getEntity = new HttpEntity<>(headers);
            ResponseEntity<Map> studyResponse = restTemplate.exchange(
                    orthancUrl + "/studies/" + orthancStudyId,
                    HttpMethod.GET,
                    getEntity,
                    Map.class
            );

            Map<String, Object> studyBody = studyResponse.getBody();
            Map<String, String> mainDicomTags = (Map<String, String>) studyBody.get("MainDicomTags");
            String studyInstanceUid = mainDicomTags.get("StudyInstanceUID");
            
            // Check if study already exists for this request
            DicomStudy study = studyRepository.findByRequestId(requestId).orElse(null);
            if (study == null) {
                study = DicomStudy.builder()
                        .studyInstanceUid(studyInstanceUid)
                        .request(request)
                        .patient(request.getPatient())
                        .modality(request.getProcedure().getModality())
                        .status("AVAILABLE_FOR_REPORTING")
                        .storagePath(orthancStudyId) // store internal ID just in case
                        .build();
                study = studyRepository.save(study);
            } else {
                study.setInstanceCount(study.getInstanceCount() + 1);
                study = studyRepository.save(study);
            }

            request.setStatus("REPORTING");
            requestRepository.save(request);

            return study;
        } catch (Exception e) {
            log.error("Failed to store DICOM file to Orthanc", e);
            throw new RuntimeException("Failed to store DICOM file to Orthanc", e);
        }
    }

    public Resource loadDicomAsResource(String studyInstanceUid, String filename) {
        try {
            // Here studyInstanceUid is actually our Orthanc ParentStudy internal ID we stored in storagePath.
            // But wait, the parameter might be the actual StudyInstanceUID from DICOM tags if the controller passes that.
            // Let's assume we can fetch the study archive from Orthanc using the orthanc internal ID, which we stored in storagePath.
            // We need to fetch the study entity to get the storagePath (which is the orthanc internal ID).
            DicomStudy study = studyRepository.findByStudyInstanceUid(studyInstanceUid).orElse(null);
            if (study == null) {
                 throw new RuntimeException("Study not found: " + studyInstanceUid);
            }
            String orthancStudyId = study.getStoragePath();

            // To get a specific instance, we could query Orthanc for instances in the study.
            // For simplicity, let's download the entire study archive (ZIP) or just the first instance.
            // The viewer might just need a valid DICOM file. Let's fetch the instances list.
            HttpHeaders headers = new HttpHeaders();
            if (orthancUsername != null && !orthancUsername.isEmpty()) {
                String auth = orthancUsername + ":" + orthancPassword;
                byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
                headers.add("Authorization", "Basic " + new String(encodedAuth));
            }
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            
            // Get instances for the study
            ResponseEntity<java.util.List> instancesResponse = restTemplate.exchange(
                orthancUrl + "/studies/" + orthancStudyId + "/instances",
                HttpMethod.GET,
                requestEntity,
                java.util.List.class
            );
            
            java.util.List<Map> instances = instancesResponse.getBody();
            if (instances == null || instances.isEmpty()) {
                throw new RuntimeException("No instances found for study");
            }
            
            String instanceId = (String) instances.get(0).get("ID");
            
            // Download the instance DICOM file
            ResponseEntity<byte[]> fileResponse = restTemplate.exchange(
                orthancUrl + "/instances/" + instanceId + "/file",
                HttpMethod.GET,
                requestEntity,
                byte[].class
            );
            
            return new org.springframework.core.io.ByteArrayResource(fileResponse.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Could not read file from Orthanc for study: " + studyInstanceUid, e);
        }
    }

    public java.util.Optional<DicomStudy> getStudyByRequestId(Long requestId) {
        return studyRepository.findByRequestId(requestId);
    }
}
