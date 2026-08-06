package com.healthcare.clinic.inventory.service;

import com.healthcare.clinic.pharmacy.entity.Medicine;
import com.healthcare.clinic.pharmacy.entity.MedicineStock;
import com.healthcare.clinic.pharmacy.entity.StockAlert;
import com.healthcare.clinic.pharmacy.entity.PharmacyUser;
import com.healthcare.clinic.pharmacy.repository.MedicineRepository;
import com.healthcare.clinic.pharmacy.repository.MedicineStockRepository;
import com.healthcare.clinic.pharmacy.repository.StockAlertRepository;
import com.healthcare.clinic.pharmacy.repository.PharmacyUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service("pharmacyStockAlertService")
public class StockAlertService {

    private final MedicineRepository medicineRepository;
    private final MedicineStockRepository stockRepository;
    private final StockAlertRepository alertRepository;
    private final PharmacyUserRepository userRepository;
    private final EmailService emailService;

    @Value("${app.stock-alert.cooldown-hours:24}")
    private int cooldownHours;

    public StockAlertService(MedicineRepository medicineRepository,
                             MedicineStockRepository stockRepository,
                             StockAlertRepository alertRepository,
                             PharmacyUserRepository userRepository,
                             EmailService emailService) {
        this.medicineRepository = medicineRepository;
        this.stockRepository = stockRepository;
        this.alertRepository = alertRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Async("alertExecutor")
    public void checkAndAlert(Long medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId).orElse(null);
        if (medicine == null || medicine.getReorderLevel() == null) return;

        // Use the updated repository method that filters out deleted batches
        int totalStock = stockRepository.findByMedicineIdAndDeletedFalse(medicineId).stream()
                .mapToInt(MedicineStock::getQuantityAvailable)
                .sum();

        if (totalStock <= medicine.getReorderLevel()) {
            // Check if alert was sent in the cooldown period
            LocalDateTime cooldownLimit = LocalDateTime.now().minusHours(cooldownHours);
            boolean alreadySent = alertRepository.findTopByMedicineIdAndCreatedAtAfterOrderByCreatedAtDesc(medicineId, cooldownLimit).isPresent();

            if (!alreadySent) {
                // Fetch all active users who are admins or supervisors
                List<PharmacyUser> recipientsUsers = userRepository.findByStatus("ACTIVE").stream()
                        .filter(u -> u.getRoles() != null && u.getRoles().stream().anyMatch(r -> 
                            r.getName().contains("ADMIN") || r.getName().contains("SUPERVISOR")))
                        .collect(Collectors.toList());

                List<String> recipients = recipientsUsers.stream()
                        .map(PharmacyUser::getEmail)
                        .filter(e -> e != null && !e.isEmpty())
                        .collect(Collectors.toList());

                if (!recipients.isEmpty()) {
                    emailService.sendLowStockAlert(medicine, recipients);
                    StockAlert alert = new StockAlert(medicine, "EMAIL", String.join(", ", recipients));
                    alertRepository.save(alert);
                }
            }
        }
    }

    public long getLowStockCount() {
        return alertRepository.count();
    }
}
