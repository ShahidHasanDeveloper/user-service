package com.epam.users.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Validated
@Api(tags="User Management RESTful Services", description="Controller for user service")
public class UserController {

	@Autowired
	private UserService userService;

	
	@GetMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "List of all users")
	public List<User> getAllUsers(){
		return userService.getAllUsers();
	}

	@PostMapping(value="/users",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Add a user")
	public ResponseEntity<Void> createUser(@Valid @RequestBody User user, UriComponentsBuilder builder) throws UserExistsException {
		
			User createdUser= userService.createUser(user);
			HttpHeaders headers= new HttpHeaders();
			headers.setLocation(builder.path("/users/{user_id}").buildAndExpand(createdUser.getId()).toUri());
			return  new ResponseEntity<>(headers, HttpStatus.CREATED);

	}

	@GetMapping(value="/users/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get user by id")
	public User getUserById(@PathVariable("user_id")  @Min(1) Long id){
		try {
			Optional<User>optionalUser=userService.getUserById(id);
			return optionalUser.get();
		} catch(UserNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}

	}


	@PutMapping(value="/users/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE) 
	@ApiOperation(value = "Update a user")
	public User updateUserById(@PathVariable("user_id") Long id, @RequestBody User user) {
		try {
			return userService.updateUserById(id, user);
		} catch (UserNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/users/{user_id}")
	@ApiOperation(value = "Delete a user")
	public void deleteUserById(@PathVariable("user_id") Long id ) {
		userService.deleteUserById(id);
	}
}
