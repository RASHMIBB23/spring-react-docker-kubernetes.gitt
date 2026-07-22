package com.example.demo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.example.demo.controller.RoomControllerTest;
import com.example.demo.data_repo.RoomRepositoryTest;
import com.example.demo.services.RoomServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	RoomControllerTest.class,
	RoomRepositoryTest.class,
	RoomServiceTest.class
})
public class ContinuousIntegrationTestSuit {

}
