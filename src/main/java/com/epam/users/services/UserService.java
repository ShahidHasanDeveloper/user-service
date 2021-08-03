package com.epam.users.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.epam.users.entities.User;
import com.epam.users.exceptions.UserExistsException;
import com.epam.users.exceptions.UserNotFoundException;
import com.epam.users.repositories.UserRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class UserService {
	private final Logger logger= LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserRepository userRepository;

	@HystrixCommand(fallbackMethod = "fallbackGetAllUsers")
	public List<User> getAllUsers(){
		return userRepository.findAll();
	}

	@HystrixCommand
	public User createUser(User user) throws UserExistsException {
		Optional<User>userFoundByUsername= userRepository.findByUsername(user.getUsername());
		Optional<User>userFoundBySsn= userRepository.findBySsn(user.getSsn());

		if(userFoundByUsername.isPresent()) {
			logger.info("UserService | createUser | User is already present with given user nam");
			throw new UserExistsException("User is already present with given user name");
		}
		else if(userFoundBySsn.isPresent()) {
			logger.info("UserService | createUser | User is already present with given ssn");
			throw new UserExistsException("User is already present with given ssn");
		}
		return userRepository.save(user);
	}

	@HystrixCommand
	public Optional<User> getUserById(Long id) throws UserNotFoundException {

		Optional<User> user=  userRepository.findById(id);
		if(!user.isPresent()) {
			logger.info("UserService | getUserById | User not found");
			throw new UserNotFoundException("User not found");
		}
		return user;

	}

	@HystrixCommand
	public User updateUserById(Long id, User user) throws UserNotFoundException{
		Optional<User> optionalUser=  userRepository.findById(id);
		if(!optionalUser.isPresent()) {
			logger.info("UserService | updateUserById | User not found for given user id for update");
			throw new UserNotFoundException("User not found for given user id for update");
		}
		user.setId(id);
		return userRepository.save(user);
	}

	@HystrixCommand
	public void deleteUserById(Long id) throws ResponseStatusException {
		Optional<User> optionalUser=  userRepository.findById(id);
		if(!optionalUser.isPresent()) {
			logger.info("UserService | deleteUserById | User not found for given user id for delete");
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found for given user id for delete");
		}
		userRepository.deleteById(id);


	}
	
	public List<User> fallbackGetAllUsers(){
		logger.info("UserService | fallbackGetAllUsers | serving from fallback implementation");
		User user= new User();
		user.setEmail("Unknown");
		user.setUsername("Uknown");
		user.setFirstname("Unknown");
		user.setLastname("Unknown");
		user.setRole("Uknown");
		user.setSsn("Uknown");
		return Collections.singletonList(user);
	}
}
