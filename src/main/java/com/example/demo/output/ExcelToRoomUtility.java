package com.example.demo.output;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.data.Room;

public class ExcelToRoomUtility {

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "ID", "NAME", "Room Number", "Bed Info"};
    static String SHEET = "Room Data";
    public static boolean hasExcelFormat(MultipartFile file) {
      if (!TYPE.equals(file.getContentType())) {
        return false;
      }
      return true;
    }
    public static List<Room> excelToRoomList(InputStream is) {
      try {
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheet(SHEET);
        Iterator<Row> rows = sheet.iterator();
        List<Room> roomList = new ArrayList<Room>();
        Set<String> existingRoomNumbers = new HashSet<>(); // Keep track of room numbers
        int rowNumber = 0;
        while (rows.hasNext()) {
          Row currentRow = rows.next();
          // skip header
          if (rowNumber == 0) {
            rowNumber++;
            continue;
          }
          Iterator<Cell> cellsInRow = currentRow.iterator();
          Room room = new Room();
          int cellIdx = 0;
          while (cellsInRow.hasNext()) {
            Cell currentCell = cellsInRow.next();
            switch (cellIdx) {
            // Skip setting ID
            case 0:
            	// Skip setting ID, as it's auto-generated
              break;
            case 1:
            	room.setName(currentCell.getStringCellValue());
              break;
            case 2:
            	String roomNumber = currentCell.getStringCellValue();
                if (existingRoomNumbers.contains(roomNumber)) {
                	System.out.println("Skipping duplicate room number: " + roomNumber);
                    room = null; // Skip this room
                    break;
                }
                room.setRoomNumber(roomNumber);
                existingRoomNumbers.add(roomNumber);
              break;
            case 3:
            	room.setBedInfo(currentCell.getStringCellValue());
              break;
            default:
              break;
            }
            cellIdx++;
          }
          roomList.add(room);
        }
        workbook.close();
        return roomList;
      } catch (IOException e) {
        throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
      }
    }
    
    public static void displayRoomList(List<Room> roomList) {
    	System.out.println("Rooms from Excel:");
    	roomList.stream()
    	.forEach(s -> System.out.println(s));
    }
}
