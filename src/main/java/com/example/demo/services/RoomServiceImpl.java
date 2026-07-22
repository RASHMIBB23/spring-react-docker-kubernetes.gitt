package com.example.demo.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.data.Room;
import com.example.demo.output.ExcelToRoomUtility;
import com.example.demo.repo.RoomRepository;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;
	
	//now implement our room services that is initialize in RoomService
	@Override
	public List<Room> getAllRooms(){ //get all records from Room table
		Iterable<Room> iterable = roomRepository.findAll();
		List<Room> roomList = new ArrayList<>();
		iterable.forEach(roomList::add);
		
		// for sorting use static sort order
		roomList.sort(Room.NAME_COMPARATOR);
		return roomList;
	}
	
	@Override
	public Room getRoomById(long id) { //Get data from Room table based on id
		return roomRepository.findById(id).orElse(null);
	}
	
	@Override
	public Room saveRoom(Room room) { // insert new records from form int Room table
		return roomRepository.save(room);
	}
	
	@Override
	public void deleteRoom(long id) {
		roomRepository.deleteById(id);
	}

	@Override
	public void loadExcelToRoom(MultipartFile file) {
		try {
			List<Room> roomList = ExcelToRoomUtility.excelToRoomList(file.getInputStream());
			ExcelToRoomUtility.displayRoomList(roomList);
			roomRepository.saveAll(roomList);
			
		} catch (IOException ex) {
			throw new RuntimeException("Excel data is failed to store: " + ex.getMessage());
		}
		
	}

}
