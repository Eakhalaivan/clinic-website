import re

with open('backend/src/main/java/com/healthcare/clinic/pharmacy/controller/PrescriptionController.java', 'r') as f:
    content = f.read()

# Add imports
imports = """
import com.healthcare.clinic.pharmacy.service.PharmacyDispensingService;
import com.healthcare.clinic.pharmacy.dto.DispenseRequest;
import com.healthcare.clinic.pharmacy.entity.PrescriptionDispensed;
import com.healthcare.clinic.identity.entity.User;
"""
content = content.replace('import java.util.Map;', 'import java.util.Map;\n' + imports.strip())

# Add field and constructor
constructor_replace = """
    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionVerificationService verificationService;
    private final UserRepository userRepository;
    private final PharmacyDispensingService pharmacyDispensingService;

    public PrescriptionController(
            PrescriptionRepository prescriptionRepository,
            PrescriptionVerificationService verificationService,
            UserRepository userRepository,
            PharmacyDispensingService pharmacyDispensingService) {
        this.prescriptionRepository = prescriptionRepository;
        this.verificationService = verificationService;
        this.userRepository = userRepository;
        this.pharmacyDispensingService = pharmacyDispensingService;
    }
"""

content = re.sub(r'    private final PrescriptionRepository prescriptionRepository;.*?    \}', constructor_replace.strip('\n'), content, flags=re.DOTALL)


# Update dispense method
dispense_replace = """
    /** Dispense a prescription — marks it DISPENSED, deducts stock, and syncs back to clinical record */
    @PostMapping("/{id}/dispense")
    @PreAuthorize("hasAnyAuthority('ROLE_PHARMACIST','ROLE_ADMIN','ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PrescriptionDispensed>> dispense(@PathVariable Long id, @RequestBody DispenseRequest request) {
        User pharmacist = getCurrentUser();
        request.setPrescriptionId(id);
        return ResponseEntity.ok(ApiResponse.success(
                pharmacyDispensingService.dispensePrescription(request, pharmacist), "Prescription dispensed"));
    }

    // ── helpers ───────────────────────────────────────────────────────────────
    private User getCurrentUser() {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            return userRepository.findById(userId).orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("User not found");
        }
    }
"""

content = re.sub(r'    /\*\* Dispense a prescription — marks it DISPENSED and syncs back to clinical record \*/.*?    // ── helpers ─+', dispense_replace.strip('\n') + '\n    // ── helpers ─', content, flags=re.DOTALL)

with open('backend/src/main/java/com/healthcare/clinic/pharmacy/controller/PrescriptionController.java', 'w') as f:
    f.write(content)
