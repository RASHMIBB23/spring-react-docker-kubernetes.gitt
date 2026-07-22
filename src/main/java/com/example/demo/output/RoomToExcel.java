package com.example.demo.output;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.example.demo.data.Room;

@Component
public class RoomToExcel {

    public byte[] generateExcel(List<Room> roomList) throws IOException {
        //create Excel workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Room Data");

        // Create titles
        Row titles = sheet.createRow(0);
        titles.createCell(0).setCellValue("ID");
        titles.createCell(1).setCellValue("Name");
        titles.createCell(2).setCellValue("Room Number");
        titles.createCell(3).setCellValue("Bed Info");

        //Create data file
        int rowNum = 1;
        for (Room room : roomList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(room.getId());
            row.createCell(1).setCellValue(room.getName());
            row.createCell(2).setCellValue(room.getRoomNumber());
            row.createCell(3).setCellValue(room.getBedInfo());
        }

        //convert workbook to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream.toByteArray();
    }

}
