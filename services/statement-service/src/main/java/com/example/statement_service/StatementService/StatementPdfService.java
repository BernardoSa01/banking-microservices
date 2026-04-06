package com.example.statement_service.StatementService;

import com.example.statement_service.model.Statement;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class StatementPdfService {

    public byte[] generatePdf(String accountId, LocalDateTime start, LocalDateTime end, List<Statement> statements) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document();

            PdfWriter.getInstance(document, out);

            document.open();

            //Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            //Paragraph company = new Paragraph("BANKING MICROSERVICES INC.", titleFont);
            //company.setAlignment(Element.ALIGN_CENTER);

            //document.add(company);
            document.add(new Paragraph("Banking Microservices Inc.", subtitleFont));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Account Statement", subtitleFont));
            document.add(new Paragraph("Account: " + accountId, subtitleFont));
            DateTimeFormatter periodFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            document.add(new Paragraph(
                    "Period:" +
                            start.format(periodFormatter) +
                            " - " +
                            end.format(periodFormatter)));

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

//            table.addCell(new PdfPCell(new Phrase("Date")));
//            table.addCell(new PdfPCell(new Phrase("Type")));
//            table.addCell(new PdfPCell(new Phrase("Amount")));
//            table.addCell(new PdfPCell(new Phrase("Status")));
            table.addCell(createHeaderCell("Date"));
            table.addCell(createHeaderCell("Type"));
            table.addCell(createHeaderCell("Amount"));
            table.addCell(createHeaderCell("Status"));

            // Ordena transações por data
            statements.sort(Comparator.comparing(Statement::getTimestamp));

            long debitTotal = 0;
            long creditTotal = 0;

            for (Statement s : statements) {
                table.addCell(createCell(s.getTimestamp().format(dateTimeFormatter)));
                table.addCell(createCell(s.getType()));

                PdfPCell amountCell =
                        createCell(currencyFormatter.format(s.getAmount()));

                if ("DEBIT".equalsIgnoreCase(s.getType())) {

                    amountCell.setPhrase(new Phrase(
                            currencyFormatter.format(s.getAmount()),
                            FontFactory.getFont(
                                    FontFactory.HELVETICA,
                                    12,
                                    Font.BOLD,
                                    Color.BLACK)));
                    debitTotal += s.getAmount();
                }

                if ("CREDIT".equalsIgnoreCase(s.getType())) {

                    amountCell.setPhrase(new Phrase(
                            currencyFormatter.format(s.getAmount()),
                            FontFactory.getFont(
                                    FontFactory.HELVETICA,
                                    12,
                                    Font.BOLD,
                                    Color.DARK_GRAY)));
                    creditTotal += s.getAmount();
                }

                table.addCell(amountCell);
                table.addCell(createCell(s.getStatus()));
            }

            document.add(table);

            document.add(new Paragraph(" "));

            long totalSpent = debitTotal + creditTotal;

            document.add(new Paragraph(
                    "Purchases (Debit): " +
                            currencyFormatter.format(debitTotal)));

            document.add(new Paragraph(
                    "Purchases (Credit): " +
                            currencyFormatter.format(creditTotal)));

            document.add(new Paragraph(" "));

            document.add(new Paragraph(
                    "Total spent on period (Debit + Credit): " +
                            currencyFormatter.format(totalSpent)));

            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

    }

    private PdfPCell createCell(String content) {

        PdfPCell cell = new PdfPCell(new Phrase(content));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(6);

        return cell;
    }

    private PdfPCell createHeaderCell(String content) {

        PdfPCell cell = new PdfPCell(new Phrase(content));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setPadding(6);

        return cell;
    }
}
