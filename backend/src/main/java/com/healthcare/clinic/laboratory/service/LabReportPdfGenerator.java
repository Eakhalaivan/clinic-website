package com.healthcare.clinic.laboratory.service;

import com.healthcare.clinic.laboratory.entity.LabResult;
import com.healthcare.clinic.laboratory.entity.LabTestRequest;
import com.healthcare.clinic.patient.entity.PatientProfile;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabReportPdfGenerator {

    private final UserRepository userRepository;

    public byte[] generateLabReport(LabTestRequest request, LabResult result) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Title
            Paragraph title = new Paragraph("CLINIC LABORATORY REPORT", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Patient Info
            PatientProfile patient = request.getPatient();
            User patientUser = userRepository.findById(patient.getUserId()).orElse(null);
            String patientName = patientUser != null ? patientUser.getFirstName() + " " + patientUser.getLastName() : "Unknown";

            document.add(new Paragraph("Patient Name: " + patientName, normalFont));
            document.add(new Paragraph("Request ID: " + request.getId(), normalFont));
            document.add(new Paragraph("Requested On: " + request.getRequestedAt().format(DateTimeFormatter.RFC_1123_DATE_TIME), normalFont));
            document.add(new Paragraph(" "));

            // Results Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            
            table.addCell(new PdfPCell(new Phrase("Test Name", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Result", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Reference Range", headerFont)));
            table.addCell(new PdfPCell(new Phrase("Flag", headerFont)));

            table.addCell(new Phrase(request.getTestCatalog().getTestName(), normalFont));
            table.addCell(new Phrase(result.getResultValue() + " " + (result.getUnit() != null ? result.getUnit() : ""), normalFont));
            table.addCell(new Phrase(result.getReferenceRange() != null ? result.getReferenceRange() : "N/A", normalFont));
            
            String flag = "NORMAL";
            if (Boolean.TRUE.equals(result.getIsCritical())) {
                flag = "CRITICAL";
            } else if (Boolean.TRUE.equals(result.getIsAbnormal())) {
                flag = "ABNORMAL";
            }
            table.addCell(new Phrase(flag, normalFont));

            document.add(table);
            document.add(new Paragraph(" "));

            // Signatures
            if (result.getVerifiedBy() != null) {
                document.add(new Paragraph("Verified By: " + result.getVerifiedBy().getFirstName() + " " + result.getVerifiedBy().getLastName(), headerFont));
                document.add(new Paragraph("Verified At: " + result.getVerifiedAt().format(DateTimeFormatter.RFC_1123_DATE_TIME), normalFont));
                
                if (result.getPathologistComments() != null && !result.getPathologistComments().isEmpty()) {
                    document.add(new Paragraph("Pathologist Comments: " + result.getPathologistComments(), normalFont));
                }
            } else {
                document.add(new Paragraph("Status: " + request.getStatus(), headerFont));
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generating PDF for request {}", request.getId(), e);
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }
}
