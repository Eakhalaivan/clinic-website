import re
import os

repo_path = "backend/src/main/java/com/healthcare/clinic/pharmacy/repository/MedicineRepository.java"
controller_path = "backend/src/main/java/com/healthcare/clinic/pharmacy/controller/MedicineController.java"

# Update Repository
repo_content = open(repo_path).read()

repo_query = """    @org.springframework.data.jpa.repository.Query("SELECT m FROM Medicine m WHERE " +
       "(:search IS NULL OR :search = '' OR LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(m.genericName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(m.medicineCode) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
       "(:drugClass IS NULL OR :drugClass = '' OR :drugClass = 'ALL' OR m.drugClass = :drugClass) AND " +
       "(:schedule IS NULL OR :schedule = '' OR :schedule = 'ALL' OR m.schedule = :schedule) AND " +
       "(:productType IS NULL OR :productType = '' OR :productType = 'ALL' OR m.productType = :productType)")
    org.springframework.data.domain.Page<Medicine> searchMedicines(
        @org.springframework.data.repository.query.Param("search") String search, 
        @org.springframework.data.repository.query.Param("drugClass") String drugClass, 
        @org.springframework.data.repository.query.Param("schedule") String schedule, 
        @org.springframework.data.repository.query.Param("productType") String productType, 
        org.springframework.data.domain.Pageable pageable);
"""

if "searchMedicines" not in repo_content:
    repo_content = repo_content.replace("}", repo_query + "\n}")
    open(repo_path, "w").write(repo_content)
    print("Updated Repository")

# Update Controller
ctrl_content = open(controller_path).read()
ctrl_method = """    @GetMapping("/medicines")
    public ResponseEntity<ApiResponse<org.springframework.data.domain.Page<MedicineDTO>>> getAllMedicines(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String drugClass,
            @RequestParam(required = false) String schedule,
            @RequestParam(required = false) String productType,
            @org.springframework.data.web.PageableDefault(size = 20) org.springframework.data.domain.Pageable pageable) {
        
        org.springframework.data.domain.Page<Medicine> medicines = medicineRepository.searchMedicines(search, drugClass, schedule, productType, pageable);"""

ctrl_content = re.sub(r'@GetMapping\("/medicines"\)\s*public ResponseEntity<ApiResponse<org\.springframework\.data\.domain\.Page<MedicineDTO>>> getAllMedicines\([^\)]+\)\s*\{\s*org\.springframework\.data\.domain\.Page<Medicine> medicines = medicineRepository\.findAll\(pageable\);', ctrl_method, ctrl_content)

open(controller_path, "w").write(ctrl_content)
print("Updated Controller")
