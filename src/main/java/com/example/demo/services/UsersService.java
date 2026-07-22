package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import com.example.demo.data.User;

public interface UsersService {
    List<User> getAllUsers();
	User getUserById(long id);
	User saveUser(User user, Long roleId);
	void deleteUser(long id);
    Optional<User> authenticateUser(String email, String password);
}
