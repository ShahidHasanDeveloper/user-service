package com.epam.users.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.epam.users.entities.User;
import com.epam.users.exceptions.UserExistsException;
import com.epam.users.exceptions.UserNotFoundException;
import com.epam.users.services.UserService;

@RestController
@Validated
public class UserController {

	@Autowired
	private UserService userService;


	@GetMapping("/users")
	public List<User> getAllUsers(){
		return userService.getAllUsers();
	}

	@PostMapping("/users")
	public ResponseEntity<Void> createUser(@Valid @RequestBody User user, UriComponentsBuilder builder) throws UserExistsException {
		
			User createdUser= userService.createUser(user);
			HttpHeaders headers= new HttpHeaders();
			headers.setLocation(builder.path("/users/{user_id}").buildAndExpand(createdUser.getId()).toUri());
			return  new ResponseEntity<>(headers, HttpStatus.CREATED);

	}

	@GetMapping("/users/{user_id}")
	public Optional<User> getUserById(@PathVariable("user_id")  @Min(1) Long id){
		try {
			return userService.getUserById(id);
		} catch(UserNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}

	}


	@PutMapping("/users/{user_id}") 
	public User updateUserById(@PathVariable("user_id") Long id, @RequestBody User user) {
		try {
			return userService.updateUserById(id, user);
		} catch (UserNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/users/{user_id}")
	public void deleteUserById(@PathVariable("user_id") Long id ) {
		userService.deleteUserById(id);
	}
}
