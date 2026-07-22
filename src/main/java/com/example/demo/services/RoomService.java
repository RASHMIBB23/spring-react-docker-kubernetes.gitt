package com.example.demo.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.data.Room;

public interface RoomService {

	List<Room> getAllRooms();
	Room getRoomById(long id);
	Room saveRoom(Room room);
	void deleteRoom(long id);
	void loadExcelToRoom(MultipartFile file);
}
