package com.example.demo.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.data.Role;
import com.example.demo.data.User;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.SignupRequest;
import com.example.demo.services.RoleService;
import com.example.demo.services.UsersService;

@RestController
@RequestMapping(path="/auth")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired private UsersService userService;
    @Autowired private RoleService roleService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody SignupRequest signupRequest) {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPasswordHash(signupRequest.getPassword()); // Make sure to hash the password properly
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());

        Optional<Role> role = roleService.findByName(signupRequest.getRole()); // Assuming this method exists
        if (role.isPresent()) {
            Long roleId = role.get().getId();
            User registeredUser = userService.saveUser(user, roleId);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Invalid role name
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        System.out.println("password!---: "+ loginRequest.getEmail()+" "+ loginRequest.getPassword()+" role: ");
        Optional<User> userOptional = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Extract roles
            Set<String> roles = user.getRole().stream()
                                    .map(Role::getName) 
                                    .collect(Collectors.toSet());

            // Create Response object
            LoginResponse response = new LoginResponse();
            response.setId(user.getId());
            response.setEmail(user.getEmail());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            response.setRoles(roles);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}
