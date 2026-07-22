package com.example.demo.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.data.Room;
import com.example.demo.services.RoomService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoomControllerTest {

	@Autowired private RoomService roomService;
	
	private Room savedRoom;
	
	@Test // add a new room from web page
	public void creatRoomTest() {
		Room room = new Room();
		room.setName("test Contr");
		room.setRoomNumber("TC1");
		
		//store the saved room for cleanup
		savedRoom = roomService.getRoomById(room.getId());
	}
	
	//clean the inserted data
	@AfterEach
	public void cleanup() {
		// Delete the test data after each test
		if (savedRoom != null && savedRoom.getId() != 0) {
			roomService.deleteRoom(savedRoom.getId());
		}
	}
	
}
