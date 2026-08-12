package com.healthcare.clinic.laboratory.service;

import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.laboratory.entity.LabBarcode;
import com.healthcare.clinic.laboratory.entity.LabTestRequest;
import com.healthcare.clinic.laboratory.repository.LabBarcodeRepository;
import com.healthcare.clinic.laboratory.repository.LabTestRequestRepository;
import com.healthcare.clinic.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabBarcodeService {

    private final LabBarcodeRepository barcodeRepository;
    private final LabTestRequestRepository requestRepository;
    private final UserRepository userRepository;

    @Transactional
    public String generateLabRequestNumber() {
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String uuidPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "LAB-" + datePrefix + "-" + uuidPart;
    }

    @Transactional
    public List<LabBarcode> generateBarcodesForRequests(List<Long> requestIds) {
        if (requestIds == null || requestIds.isEmpty()) {
            throw new IllegalArgumentException("No requests provided");
        }
        
        List<LabTestRequest> requests = requestRepository.findAllById(requestIds);
        if (requests.size() != requestIds.size()) {
            throw new IllegalArgumentException("One or more requests not found");
        }

        // Validate that they belong to the same patient
        Long patientId = requests.get(0).getPatient().getId();
        boolean samePatient = requests.stream().allMatch(r -> r.getPatient().getId().equals(patientId));
        if (!samePatient) {
            throw new IllegalArgumentException("Barcodes can only be generated for requests belonging to the same patient");
        }

        // Determine if they already share a labRequestNumber. If not, generate one.
        String labReqNum = requests.stream()
                .filter(r -> r.getLabRequestNumber() != null)
                .map(LabTestRequest::getLabRequestNumber)
                .findFirst()
                .orElseGet(this::generateLabRequestNumber);

        // Group by specimenType to generate one barcode per specimen type
        List<String> distinctSpecimenTypes = requests.stream()
                .map(r -> r.getTestCatalog().getSpecimenType() != null ? r.getTestCatalog().getSpecimenType() : "General")
                .distinct()
                .collect(Collectors.toList());

        Long currentUserId = SecurityUtils.getCurrentUserId();
        User currentUser = null;
        if (currentUserId != null) {
            currentUser = userRepository.findById(currentUserId).orElse(null);
        }

        List<LabBarcode> createdBarcodes = new ArrayList<>();
        
        for (String specimenType : distinctSpecimenTypes) {
            // Find container type from first matching request
            String containerType = requests.stream()
                    .filter(r -> specimenType.equals(r.getTestCatalog().getSpecimenType() != null ? r.getTestCatalog().getSpecimenType() : "General"))
                    .map(r -> r.getTestCatalog().getContainerType())
                    .findFirst()
                    .orElse(null);

            String barcodeValue = labReqNum + "-" + specimenType.substring(0, Math.min(3, specimenType.length())).toUpperCase();
            
            // Check if already exists
            Optional<LabBarcode> existing = barcodeRepository.findByBarcodeValue(barcodeValue);
            LabBarcode barcode;
            if (existing.isPresent()) {
                barcode = existing.get();
            } else {
                barcode = LabBarcode.builder()
                        .barcodeValue(barcodeValue)
                        .patient(requests.get(0).getPatient())
                        .labRequestNumber(labReqNum)
                        .specimenType(specimenType)
                        .containerType(containerType)
                        .generatedBy(currentUser)
                        .status("PRINTED")
                        .build();
                barcode = barcodeRepository.save(barcode);
                createdBarcodes.add(barcode);
            }

            // Assign barcode to requests
            for (LabTestRequest req : requests) {
                String reqSpecimenType = req.getTestCatalog().getSpecimenType() != null ? req.getTestCatalog().getSpecimenType() : "General";
                if (reqSpecimenType.equals(specimenType)) {
                    req.setLabRequestNumber(labReqNum);
                    req.setSampleBarcodeId(barcode.getBarcodeValue());
                }
            }
        }
        
        requestRepository.saveAll(requests);
        return createdBarcodes;
    }
}
