package com.healthcare.clinic.laboratory.service;

import com.healthcare.clinic.laboratory.entity.LabResult;
import com.healthcare.clinic.laboratory.entity.LabTestRequest;
import com.healthcare.clinic.laboratory.repository.LabResultRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.healthcare.clinic.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class LabPdfService {

    private final LabResultRepository labResultRepository;
    private final UserRepository userRepository;

    public byte[] generateLabResultPdf(Long resultId) {
        LabResult result = labResultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Lab Result not found"));
        LabTestRequest request = result.getRequest();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            
            // Header
            Paragraph title = new Paragraph("LABORATORY TEST REPORT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            LineSeparator ls = new LineSeparator();
            document.add(new Chunk(ls));
            
            // Patient & Request Info
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(10);
            infoTable.setSpacingAfter(20);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
            
            String patientName = "Walk-in";
            if (request.getPatient() != null) {
                patientName = userRepository.findById(request.getPatient().getUserId())
                        .map(u -> u.getFirstName() + " " + u.getLastName())
                        .orElse("Patient ID: " + request.getPatient().getUserId());
            }
            
            String doctorName = "Self/Walk-in";
            if (request.getDoctor() != null) {
                doctorName = userRepository.findById(request.getDoctor().getUserId())
                        .map(u -> "Dr. " + u.getFirstName() + " " + u.getLastName())
                        .orElse("Doctor ID: " + request.getDoctor().getUserId());
            }
                
            infoTable.addCell(getCell("Patient Name: " + patientName, normalFont, false));
            infoTable.addCell(getCell("Test ID: " + request.getId(), normalFont, false));
            
            infoTable.addCell(getCell("Requested By: " + doctorName, normalFont, false));
            infoTable.addCell(getCell("Requested On: " + (request.getRequestedAt() != null ? request.getRequestedAt().format(formatter) : "N/A"), normalFont, false));
            
            infoTable.addCell(getCell("Test Name: " + (request.getTestCatalog() != null ? request.getTestCatalog().getTestName() : "N/A"), normalFont, false));
            infoTable.addCell(getCell("Status: " + request.getStatus(), normalFont, false));
            
            document.add(infoTable);
            
            document.add(new Chunk(ls));
            
            // Results
            Paragraph resultHeader = new Paragraph("Test Results", headerFont);
            resultHeader.setSpacingBefore(20);
            resultHeader.setSpacingAfter(10);
            document.add(resultHeader);
            
            PdfPTable resultTable = new PdfPTable(3);
            resultTable.setWidthPercentage(100);
            resultTable.setWidths(new float[]{3f, 2f, 2f});
            
            resultTable.addCell(getCell("Parameter", headerFont, true));
            resultTable.addCell(getCell("Value", headerFont, true));
            resultTable.addCell(getCell("Reference Range", headerFont, true));
            
            resultTable.addCell(getCell("Observed Value", normalFont, false));
            resultTable.addCell(getCell(result.getResultValue() != null ? result.getResultValue() : "-", normalFont, false));
            resultTable.addCell(getCell("-", normalFont, false)); 
            
            document.add(resultTable);
            
            // Footer (Verification)
            Paragraph verification = new Paragraph();
            verification.setSpacingBefore(40);
            verification.setFont(normalFont);
            if (result.getVerifiedBy() != null) {
                verification.add("Verified By: " + result.getVerifiedBy().getFirstName() + " " + result.getVerifiedBy().getLastName() + "\n");
                verification.add("Verified On: " + (result.getVerifiedAt() != null ? result.getVerifiedAt().format(formatter) : "N/A"));
            } else {
                verification.add("Status: Pending Verification");
            }
            
            verification.setAlignment(Element.ALIGN_RIGHT);
            document.add(verification);
            
            document.close();
            
        } catch (DocumentException e) {
            throw new RuntimeException("Error generating Lab Result PDF", e);
        }
        
        return baos.toByteArray();
    }
    
    private PdfPCell getCell(String text, Font font, boolean isHeader) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        if (isHeader) {
            cell.setBackgroundColor(new java.awt.Color(230, 230, 230));
        } else {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        return cell;
    }
}
