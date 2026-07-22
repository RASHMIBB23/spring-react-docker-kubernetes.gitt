package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.data.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
	List<Room> findByName(String name);
}
