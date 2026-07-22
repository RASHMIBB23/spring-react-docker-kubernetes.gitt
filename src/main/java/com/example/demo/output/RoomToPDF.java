package com.example.demo.output;

import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.example.demo.data.Room;

@Component
public class RoomToPDF {

    public byte[] generatePDF(List<Room> roomList) throws IOException {
        // Create a new PDF document
        PDDocument document = new PDDocument();
        
        // Add a page to the document
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        // Create a font
        PDFont font = PDType1Font.HELVETICA_BOLD;

        float pageHeight = PDRectangle.A4.getHeight();
        float headerY = pageHeight - 20; // Adjust as needed
        float footerY = 20; // Adjust as needed

        // Start a new content stream which will "hold" the PDF content
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Set the font for header and footer
        contentStream.setFont(font, 10);

        // Draw header
        String headerText = "Header line #1\nHeader line #2\nHeader line #3";
        String[] headerLines = headerText.split("\n");
        for(int i=0; i< headerLines.length; i++) {
        	contentStream.beginText();
            contentStream.setFont(font, 10);
        	contentStream.newLineAtOffset(100, headerY);
        	contentStream.showText(headerLines[i]);
        	contentStream.endText();
        	headerY -=20;
        }

        // Draw footer
        contentStream.beginText();
        contentStream.newLineAtOffset(100, footerY);
        contentStream.showText("Page 1 of " + document.getNumberOfPages()); // Assuming this is the first page
        contentStream.endText();

        // Draw the table headers
        contentStream.setFont(font, 10);
        float tableY = headerY - 40; // Adjust based on header size
        contentStream.beginText();
        contentStream.newLineAtOffset(100, tableY);
        contentStream.showText("ID");
        contentStream.newLineAtOffset(100, 0);
        contentStream.showText("Name");
        contentStream.newLineAtOffset(100, 0);
        contentStream.showText("Room Number");
        contentStream.newLineAtOffset(100, 0);
        contentStream.showText("Bed Info");
        contentStream.endText();

        // Draw the table data
        int y = (int) (tableY - 20); // Start below headers
        for (Room room : roomList) {
            contentStream.beginText();
            contentStream.setFont(font, 10);
            contentStream.newLineAtOffset(100, y);
            contentStream.showText(String.valueOf(room.getId()));
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(room.getName());
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(room.getRoomNumber());
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText(room.getBedInfo());
            contentStream.endText();
            y -= 20; // Move to the next row
        }

        // Close the content stream
        contentStream.close();

        // Save the document to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }
}
