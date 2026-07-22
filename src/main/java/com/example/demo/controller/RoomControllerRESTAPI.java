package com.example.demo.controller;


import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.data.Room;
import com.example.demo.output.ExcelToRoomUtility;
import com.example.demo.output.RoomToExcel;
import com.example.demo.output.RoomToPDF;
import com.example.demo.services.RoomService;

@RestController
@RequestMapping(path="/rooms/api")
@CrossOrigin(origins = "*")
public class RoomControllerRESTAPI {

	@Autowired	private RoomService roomService;
	
	@Autowired private RoomToExcel roomToExcel;

	@Autowired private RoomToPDF roomToPDF;

	// Map all requests except those starting with "/room" to this method
    @GetMapping(value = {"", "/", "/{path:^(?!room).*$}"})
    public ResponseEntity serveIndexHtml() throws IOException {
        Resource indexHtml = new ClassPathResource("static/build/index.html");
        // Check if the resource exists
        if (indexHtml.exists()) {
            return ResponseEntity.ok().body(indexHtml.getInputStream().readAllBytes());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

	//this method sends data as JSON When /rooms is called in URL
	@GetMapping("/getAll")
	public ResponseEntity<List<Room>> getAllRooms(){
		List<Room> rooms = roomService.getAllRooms();
		return ResponseEntity.ok(rooms);
	}
	
	//Lets crate data to get based on id from Room table and returns as JSON
	@GetMapping("/{id}")
	public ResponseEntity<Room> getRoomById(@PathVariable long id){
		Room room = roomService.getRoomById(id);
		if(room != null) {
			return ResponseEntity.ok(room);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//create a method that can save data into Room table
	@PostMapping("/add")
	public ResponseEntity<Room> createRoom(@RequestBody Room room){
		System.out.println(room);
		Room createRoom = roomService.saveRoom(room);
		return ResponseEntity.status(HttpStatus.CREATED).body(createRoom);
	}
	
	//update room based on rood id
	@PutMapping("/{id}")
	public ResponseEntity<Room> updateRoom(@PathVariable long id, @RequestBody Room roomDetail){
		Room room = roomService.getRoomById(id);
		if(room != null) {
			room.setId(roomDetail.getId());
			room.setName(roomDetail.getName());
			room.setRoomNumber(roomDetail.getRoomNumber());
			room.setBedInfo(roomDetail.getBedInfo());
			Room updateRoom = roomService.saveRoom(room);
			return ResponseEntity.ok(updateRoom);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//Create our delete request
	@DeleteMapping("/{id}")
	public ResponseEntity<Room> deleteRoom(@PathVariable long id){
		Room room = roomService.getRoomById(id);
		if(room != null) {
			roomService.deleteRoom(id);
			return ResponseEntity.noContent().build();
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	//load excel content to Room table:
	@PostMapping("/excel/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){
		String message = "";
		if(ExcelToRoomUtility.hasExcelFormat(file)) {
			try {
				roomService.loadExcelToRoom(file);
				message = "The Excel file is uploaded: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(message);
			} catch(Exception e) {
				message = "The Excel file is not upload: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
			}
		}
		message = "Please upload an Excel file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}
	
	// show the uploaded file in view
	@GetMapping("/excel/room-list")
	public ResponseEntity<?> getRooms(){
		Map<String, Object> respRoom = new LinkedHashMap<String, Object>();
		List<Room> roomList = roomService.getAllRooms();
		if(!roomList.isEmpty()) {
			respRoom.put("status", 1);
			respRoom.put("data", roomList);
			return new ResponseEntity<>(respRoom, HttpStatus.OK);
		}else {
			respRoom.clear();
			respRoom.put("status", 0);
			respRoom.put("message", "Data is not found");
			return new ResponseEntity<>(respRoom, HttpStatus.NOT_FOUND);
		}
	}

	// create a method that handle the output to MS Excel
	@GetMapping("/excel")
	public ResponseEntity<byte[]> createExcelOutput() throws IOException {
		List<Room> roomList = roomService.getAllRooms();
	    byte[] excelBytes = roomToExcel.generateExcel(roomList);
	    // Set Excel content type and headers
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
	    headers.setContentDispositionFormData("attachment", "room_data.xlsx");

	    return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
	}
	
	// Your existing controller method
	@GetMapping("/pdf")
	public ResponseEntity<byte[]> createPDFOutput() throws IOException {
	    List<Room> roomList = roomService.getAllRooms();
	    byte[] pdfBytes = roomToPDF.generatePDF(roomList);
	    // Set PDF content type and headers
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("attachment", "room_data.pdf");
	    return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}
		
}
