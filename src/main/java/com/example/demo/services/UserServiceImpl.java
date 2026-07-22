package com.example.demo.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.data.Role;
import com.example.demo.data.User;
import com.example.demo.data.Users_Roles;
import com.example.demo.repo.RoleRepository;
import com.example.demo.repo.UserRepository;
import com.example.demo.repo.UsersRolesRepository;

@Service
public class UserServiceImpl implements UsersService {
    @Autowired
	private UserRepository userRepository;

	@Autowired
	private UsersRolesRepository usersRolesRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;

	//now implement our Users services that is initialize in UsersService
	@Override
	public List<User> getAllUsers(){ //get all records from Users table
		Iterable<User> iterable = userRepository.findAll();
		List<User> UsersList = new ArrayList<>();
		iterable.forEach(UsersList::add);
		return UsersList;
	}
	
	@Override
	public User getUserById(long id) { //Get data from Users table based on id
		return userRepository.findById(id).orElse(null);
	}
	
	@Override
	public User saveUser(User user, Long roleId) { // insert new records from form int Users table
		// Check if email already exists
		if(userRepository.findByEmail(user.getEmail()).isPresent()){
			throw new IllegalStateException("Email already exists");
		}

		// Encode the user's password
		user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

		// Fetch and assign the role
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRole(roles);
        } else {
            throw new IllegalArgumentException("Invalid role ID");
        }

		// Save the user to the Users table
		User savedUser = userRepository.save(user);

		// Create and save the user-role relationship
		Users_Roles userRole = new Users_Roles();
		userRole.setUserId(savedUser.getId());
		userRole.setRoleId(roleId);
		usersRolesRepository.save(userRole);
		
		return savedUser;
	}
	
	@Override
	public void deleteUser(long id) {
		userRepository.deleteById(id);
	}

	public Optional<User> authenticateUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getPasswordHash())) {
            return userOptional;
        } else {
            return Optional.empty();
        }
    }
}
