package com.healthcare.clinic.hr.service;

import com.healthcare.clinic.finance.entity.LedgerEntry;
import com.healthcare.clinic.finance.repository.LedgerRepository;
import com.healthcare.clinic.hr.entity.PayrollRun;
import com.healthcare.clinic.hr.entity.Payslip;
import com.healthcare.clinic.hr.repository.PayslipRepository;
import com.healthcare.clinic.hr.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PayrollFinanceIntegrationService {

    private final PayslipRepository payslipRepository;
    private final LedgerRepository ledgerRepository;
    private final EmployeeRepository employeeRepository;

    public PayrollFinanceIntegrationService(PayslipRepository payslipRepository, 
                                            LedgerRepository ledgerRepository,
                                            EmployeeRepository employeeRepository) {
        this.payslipRepository = payslipRepository;
        this.ledgerRepository = ledgerRepository;
        this.employeeRepository = employeeRepository;
    }

    public void syncPayrollToLedger(PayrollRun run) {
        List<Payslip> payslips = payslipRepository.findByPayrollRunId(run.getId());

        BigDecimal totalNetPay = payslips.stream()
                .map(Payslip::getNetPay)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Dummy ledger entry for compilation. In a real system, we would construct
        // a full JournalEntry and link it with correct ChartOfAccount.
        LedgerEntry entry = LedgerEntry.builder()
                .debitAmount(totalNetPay)
                .description("Payroll run: " + run.getName())
                .build();

        ledgerRepository.save(entry);
    }
}
