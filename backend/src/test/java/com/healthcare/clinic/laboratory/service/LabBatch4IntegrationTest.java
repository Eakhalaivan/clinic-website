package com.healthcare.clinic.laboratory.service;

import com.healthcare.clinic.branch.entity.Branch;
import com.healthcare.clinic.branch.repository.BranchRepository;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.healthcare.clinic.laboratory.entity.LabInventoryItem;
import com.healthcare.clinic.laboratory.entity.LabQualityControl;
import com.healthcare.clinic.laboratory.entity.LabTestCatalog;
import com.healthcare.clinic.laboratory.repository.LabInventoryItemRepository;
import com.healthcare.clinic.laboratory.repository.LabQualityControlRepository;
import com.healthcare.clinic.laboratory.repository.LabTestCatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LabBatch4IntegrationTest {

    @Autowired
    private LabOperationalService operationalService;

    @Autowired
    private LabInventoryItemRepository inventoryRepository;

    @Autowired
    private LabQualityControlRepository qcRepository;

    @Autowired
    private LabTestCatalogRepository catalogRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private UserRepository userRepository;

    private Branch branch;
    private LabTestCatalog catalog;
    private User labTech;

    @BeforeEach
    public void setup() {
        inventoryRepository.deleteAll();
        qcRepository.deleteAll();
        catalogRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();

        labTech = new User();
        labTech.setFirstName("Tech");
        labTech.setLastName("One");
        labTech.setEmail("tech1@clinic.com");
        labTech.setPasswordHash("test");
        userRepository.save(labTech);

        branch = new Branch();
        branch.setName("Main");
        branch.setAddress("123 Main St");
        branch.setCity("City");
        branch.setState("State");
        branch.setCountry("Country");
        branch.setPostalCode("12345");
        branch.setTimezone("UTC");
        branch.setPhoneNumber("1234567890");
        branch.setEmail("main@clinic.com");
        branch = branchRepository.save(branch);

        catalog = new LabTestCatalog();
        catalog.setTestCode("CBC");
        catalog.setTestName("Complete Blood Count");
        catalog.setDepartment("Hematology");
        catalog.setPrice(new BigDecimal("20.00"));
        catalog.setBranch(branch);
        catalog = catalogRepository.save(catalog);

        LabInventoryItem reagent = LabInventoryItem.builder()
                .itemName("CBC Reagent Pack")
                .sku("REAG-CBC-01")
                .quantity(50)
                .minimumThreshold(10)
                .unit("tests")
                .branch(branch)
                .build();
        inventoryRepository.save(reagent);
    }

    @Test
    public void testInventoryDeduction() {
        LabInventoryItem item = operationalService.deductInventory("REAG-CBC-01", 5);
        assertThat(item.getQuantity()).isEqualTo(45);
    }

    @Test
    public void testInventoryDeductionInsufficient() {
        assertThrows(IllegalStateException.class, () -> {
            operationalService.deductInventory("REAG-CBC-01", 60);
        });
    }

    @Test
    public void testQualityControlValidationPass() {
        operationalService.recordQualityControl(catalog.getId(), "PASSED", "All good", labTech);
        // Should not throw exception
        operationalService.validateQcPassed(catalog.getId());
    }

    @Test
    public void testQualityControlValidationFail() {
        operationalService.recordQualityControl(catalog.getId(), "FAILED", "Calibration out of range", labTech);
        
        Exception ex = assertThrows(IllegalStateException.class, () -> {
            operationalService.validateQcPassed(catalog.getId());
        });
        
        assertThat(ex.getMessage()).contains("Quality Control FAILED");
    }

    @Test
    public void testDashboardStats() {
        // Create another item with low stock
        LabInventoryItem lowStockReagent = LabInventoryItem.builder()
                .itemName("Glucose Reagent")
                .sku("REAG-GLU-01")
                .quantity(5)
                .minimumThreshold(10)
                .unit("tests")
                .branch(branch)
                .build();
        inventoryRepository.save(lowStockReagent);

        Map<String, Object> stats = operationalService.getDashboardStats(branch.getId());
        
        // lowStockItems should be 1
        assertThat((Long) stats.get("lowStockItems")).isEqualTo(1L);
    }
}
