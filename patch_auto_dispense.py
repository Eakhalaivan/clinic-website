import re

with open('backend/src/main/java/com/healthcare/clinic/pharmacy/controller/PrescriptionController.java', 'r') as f:
    content = f.read()

# Add missing imports for auto dispense
imports = """
import com.healthcare.clinic.pharmacy.entity.PharmacyPrescriptionRecord;
import com.healthcare.clinic.pharmacy.entity.PharmacyPrescriptionItem;
import com.healthcare.clinic.pharmacy.entity.Medicine;
import com.healthcare.clinic.pharmacy.repository.MedicineRepository;
import com.healthcare.clinic.pharmacy.dto.DispenseItemRequest;
import java.util.ArrayList;
import java.util.List;
"""
content = content.replace('import java.util.Map;', 'import java.util.Map;\n' + imports.strip())

# Add medicineRepository to constructor
constructor_replace = """
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionVerificationService verificationService;
    private final UserRepository userRepository;
    private final PharmacyDispensingService pharmacyDispensingService;
    private final MedicineRepository medicineRepository;

    public PrescriptionController(
            PrescriptionRepository prescriptionRepository,
            PrescriptionVerificationService verificationService,
            UserRepository userRepository,
            PharmacyDispensingService pharmacyDispensingService,
            MedicineRepository medicineRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.verificationService = verificationService;
        this.userRepository = userRepository;
        this.pharmacyDispensingService = pharmacyDispensingService;
        this.medicineRepository = medicineRepository;
    }
"""

content = re.sub(r'    private final PrescriptionRepository prescriptionRepository;.*?    \}', constructor_replace.strip('\n'), content, flags=re.DOTALL)

# Update dispense method to auto-build request if missing
dispense_replace = """
    /** Dispense a prescription — marks it DISPENSED, deducts stock, and syncs back to clinical record */
    @PostMapping("/{id}/dispense")
    @PreAuthorize("hasAnyAuthority('ROLE_PHARMACIST','ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PrescriptionDispensed>> dispense(@PathVariable Long id, @RequestBody(required = false) DispenseRequest request) {
        User pharmacist = getCurrentUser();
        
        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            // Auto-build the dispense request based on the prescription items
            PharmacyPrescriptionRecord record = prescriptionRepository.findById(id).orElseThrow();
            request = DispenseRequest.builder().prescriptionId(id).items(new ArrayList<>()).build();
            
            for (PharmacyPrescriptionItem item : record.getItems()) {
                // Try to find the medicine by name
                List<Medicine> meds = medicineRepository.findByNameContainingIgnoreCase(item.getMedicationName());
                if (!meds.isEmpty()) {
                    Medicine med = meds.get(0);
                    // Default to 1 quantity or parse dosage if possible, but 1 is safe for testing
                    int qty = 1; 
                    try {
                        if (item.getDuration() != null && item.getDuration().contains("day")) {
                             String num = item.getDuration().replaceAll("[^0-9]", "");
                             if (!num.isEmpty()) qty = Integer.parseInt(num);
                        }
                    } catch(Exception ignored) {}
                    
                    request.getItems().add(DispenseItemRequest.builder()
                            .medicineId(med.getId())
                            .quantity(qty)
                            .build());
                } else {
                     throw new RuntimeException("Could not find stock for medication: " + item.getMedicationName());
                }
            }
        }
        
        request.setPrescriptionId(id);
        return ResponseEntity.ok(ApiResponse.success(
                pharmacyDispensingService.dispensePrescription(request, pharmacist), "Prescription dispensed"));
    }
"""

content = re.sub(r'    /\*\* Dispense a prescription — marks it DISPENSED, deducts stock, and syncs back to clinical record \*/.*?    // ── helpers ─+', dispense_replace.strip('\n') + '\n    // ── helpers ─', content, flags=re.DOTALL)

with open('backend/src/main/java/com/healthcare/clinic/pharmacy/controller/PrescriptionController.java', 'w') as f:
    f.write(content)
