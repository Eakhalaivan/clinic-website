import re

with open('backend/src/main/java/com/healthcare/clinic/laboratory/controller/LabController.java', 'r') as f:
    content = f.read()

# Add imports
imports = """
import com.healthcare.clinic.laboratory.dto.LabOrderRequestDto;
import com.healthcare.clinic.laboratory.entity.LabTestRequest;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.laboratory.entity.LabTestCatalog;
import com.healthcare.clinic.doctor.entity.Doctor;
import com.healthcare.clinic.doctor.repository.DoctorRepository;
import java.util.ArrayList;
import java.util.List;
"""
content = content.replace('import java.util.Map;', 'import java.util.Map;\n' + imports.strip())

# Add doctorRepository to constructor
constructor_replace = """
    private final LabTestCatalogRepository catalogRepository;
    private final LabTestRequestRepository requestRepository;
    private final LabResultRepository resultRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final com.healthcare.clinic.notification.service.InAppNotificationService notificationService;
    private final com.healthcare.clinic.identity.service.AuditLogService auditLogService;
    private final DoctorRepository doctorRepository;

    public LabController(
            LabTestCatalogRepository catalogRepository,
            LabTestRequestRepository requestRepository,
            LabResultRepository resultRepository,
            ApplicationEventPublisher eventPublisher,
            UserRepository userRepository,
            PatientProfileRepository patientProfileRepository,
            com.healthcare.clinic.notification.service.InAppNotificationService notificationService,
            com.healthcare.clinic.identity.service.AuditLogService auditLogService,
            DoctorRepository doctorRepository) {
        this.catalogRepository = catalogRepository;
        this.requestRepository = requestRepository;
        this.resultRepository = resultRepository;
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.patientProfileRepository = patientProfileRepository;
        this.notificationService = notificationService;
        this.auditLogService = auditLogService;
        this.doctorRepository = doctorRepository;
    }
"""
content = re.sub(r'    private final LabTestCatalogRepository catalogRepository;.*?    // ─── Patient: own lab reports ─+', constructor_replace.strip('\n') + '\n\n    // ─── Patient: own lab reports ─', content, flags=re.DOTALL)


# Add DTO definition at bottom
dto = """
@Data
class LabOrderRequestDto {
    private Long patientId;
    private List<Long> testIds;
    private String clinicalNotes;
    private String priority;
}
"""
if "class LabOrderRequestDto" not in content:
    content += "\n" + dto

# Update POST /requests to handle both single LabTestRequest and DTO
create_replace = """
    @PostMapping("/requests")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> createRequest(@RequestBody com.fasterxml.jackson.databind.JsonNode payload) {
        // Check if it's the DTO from frontend
        if (payload.has("testIds")) {
            Long patientId = payload.has("patientId") && !payload.get("patientId").isNull() ? payload.get("patientId").asLong() : null;
            String priority = payload.has("priority") && !payload.get("priority").isNull() ? payload.get("priority").asText() : "ROUTINE";
            String clinicalNotes = payload.has("clinicalNotes") && !payload.get("clinicalNotes").isNull() ? payload.get("clinicalNotes").asText() : null;
            
            PatientProfile patient = patientProfileRepository.findById(patientId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
                    
            Long userId = SecurityUtils.getCurrentUserId();
            Doctor doctor = null;
            if (userId != null) {
                 doctor = doctorRepository.findByUserId(userId).orElse(null);
            }
            
            List<LabTestRequest> savedRequests = new ArrayList<>();
            for (com.fasterxml.jackson.databind.JsonNode testIdNode : payload.get("testIds")) {
                 Long testId = testIdNode.asLong();
                 LabTestCatalog test = catalogRepository.findById(testId)
                         .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Test not found"));
                         
                 LabTestRequest req = new LabTestRequest();
                 req.setPatient(patient);
                 req.setTestCatalog(test);
                 req.setDoctor(doctor);
                 req.setPriority(priority);
                 req.setClinicalNotes(clinicalNotes);
                 req.setStatus("REQUESTED");
                 req.setRequestedAt(ZonedDateTime.now());
                 req.setLabRequestNumber("LAB-" + ZonedDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                 
                 LabTestRequest saved = requestRepository.save(req);
                 savedRequests.add(saved);
                 
                 auditLogService.logAction(
                     userId != null ? userRepository.findById(userId).orElse(null) : null,
                     "LAB_REQUEST_CREATED",
                     "LabTestRequest",
                     saved.getId().toString(),
                     java.util.Map.of("labRequestNumber", saved.getLabRequestNumber(), "testCode", saved.getTestCatalog() != null ? saved.getTestCatalog().getTestCode() : "N/A")
                 );
                 
                 eventPublisher.publishEvent(com.healthcare.clinic.clinicaldecision.event.LabTestOrderedEvent.builder()
                         .patientId(saved.getPatient() != null ? saved.getPatient().getId() : null)
                         .labRequestId(saved.getId())
                         .testName(saved.getTestCatalog() != null ? saved.getTestCatalog().getTestName() : "Unknown")
                         .doctorId(saved.getDoctor() != null ? saved.getDoctor().getUserId() : null)
                         .build());
            }
            return ResponseEntity.ok(savedRequests);
        } else {
            // Old format
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            LabTestRequest request = mapper.convertValue(payload, LabTestRequest.class);
            request.setStatus("REQUESTED");
            request.setRequestedAt(ZonedDateTime.now());
            request.setLabRequestNumber("LAB-" + ZonedDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            LabTestRequest saved = requestRepository.save(request);
            
            auditLogService.logAction(
                SecurityUtils.getCurrentUserId() != null ? userRepository.findById(SecurityUtils.getCurrentUserId()).orElse(null) : null,
                "LAB_REQUEST_CREATED",
                "LabTestRequest",
                saved.getId().toString(),
                java.util.Map.of("labRequestNumber", saved.getLabRequestNumber(), "testCode", saved.getTestCatalog() != null ? saved.getTestCatalog().getTestCode() : "N/A")
            );
            
            eventPublisher.publishEvent(com.healthcare.clinic.clinicaldecision.event.LabTestOrderedEvent.builder()
                    .patientId(saved.getPatient() != null ? saved.getPatient().getId() : null)
                    .labRequestId(saved.getId())
                    .testName(saved.getTestCatalog() != null ? saved.getTestCatalog().getTestName() : "Unknown")
                    .doctorId(saved.getDoctor() != null ? saved.getDoctor().getUserId() : null)
                    .build());
                    
            return ResponseEntity.ok(saved);
        }
    }
"""

content = re.sub(r'    @PostMapping\("/requests"\)\s+@PreAuthorize\("hasRole\(\'DOCTOR\'\) or hasRole\(\'SUPER_ADMIN\'\)"\)\s+public ResponseEntity<LabTestRequest> createRequest\(@RequestBody LabTestRequest request\) \{.*?    @GetMapping\("/requests/status/\{status\}"\)', create_replace.strip('\n') + '\n\n    @GetMapping("/requests/status/{status}")', content, flags=re.DOTALL)


with open('backend/src/main/java/com/healthcare/clinic/laboratory/controller/LabController.java', 'w') as f:
    f.write(content)
