package com.healthcare.clinic.analytics.core;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Service
public class ReportExportService {

    /**
     * Generates a CSV file from a list of rows (maps of column name to value)
     */
    public byte[] generateCsv(List<String> headers, List<Map<String, Object>> rows) {
        StringBuilder csv = new StringBuilder();
        
        // Headers
        csv.append(String.join(",", headers)).append("\n");
        
        // Data
        for (Map<String, Object> row : rows) {
            boolean first = true;
            for (String header : headers) {
                if (!first) {
                    csv.append(",");
                }
                Object val = row.get(header);
                if (val != null) {
                    // Basic escape for CSV
                    String strVal = val.toString().replace("\"", "\"\"");
                    if (strVal.contains(",") || strVal.contains("\n") || strVal.contains("\"")) {
                        csv.append("\"").append(strVal).append("\"");
                    } else {
                        csv.append(strVal);
                    }
                }
                first = false;
            }
            csv.append("\n");
        }
        
        return csv.toString().getBytes();
    }

    /**
     * Generates a PDF file from a list of rows
     */
    public byte[] generatePdf(String title, List<String> headers, List<Map<String, Object>> rows) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Paragraph titlePara = new Paragraph(title, titleFont);
            titlePara.setSpacingAfter(20);
            document.add(titlePara);

            if (headers.isEmpty()) {
                document.add(new Paragraph("No data available."));
            } else {
                PdfPTable table = new PdfPTable(headers.size());
                table.setWidthPercentage(100);

                Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
                for (String header : headers) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                    cell.setPadding(5);
                    table.addCell(cell);
                }

                Font cellFont = new Font(Font.HELVETICA, 10, Font.NORMAL);
                for (Map<String, Object> row : rows) {
                    for (String header : headers) {
                        Object val = row.get(header);
                        String strVal = val != null ? val.toString() : "";
                        PdfPCell cell = new PdfPCell(new Phrase(strVal, cellFont));
                        cell.setPadding(5);
                        table.addCell(cell);
                    }
                }

                document.add(table);
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
