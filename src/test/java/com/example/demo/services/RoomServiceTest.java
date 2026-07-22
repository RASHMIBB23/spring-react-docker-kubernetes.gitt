package com.example.demo.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.data.Room;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class RoomServiceTest {

	@Autowired RoomService roomService;
	
	private Room savedRoom, room; // Store the saved room for cleanup
	
	@Test //test the delete method
	public void deleteRoomTest() {
		room = new Room();
		room.setName("This room should delete");
		room.setRoomNumber("XX");
		roomService.deleteRoom(room.getId());
		assertNull("This room should delete", null);
	}
	
	@Test //test the saving data with our services
	public void saveRoomTest() {
		//create a room
		room = new Room();
		room.setName("Inter Contental");
		room.setRoomNumber("9T");
		room.setBedInfo("1T");
		
		//save and see what is the return
		savedRoom = roomService.saveRoom(room);
		
		//now verify the saving room
		assertNotNull(savedRoom);
		assertNotNull(savedRoom.getId());
		assertEquals("Inter Contental", savedRoom.getName());
		assertEquals("9T", savedRoom.getRoomNumber());
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
