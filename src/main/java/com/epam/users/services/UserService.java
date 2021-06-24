package com.epam.users.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.epam.users.entities.User;
import com.epam.users.exceptions.UserExistsException;
import com.epam.users.exceptions.UserNotFoundException;
import com.epam.users.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;


	public List<User> getAllUsers(){
		return userRepository.findAll();
	}

	public User createUser(User user) throws UserExistsException {
		Optional<User>userFoundByUsername= userRepository.findByUsername(user.getUsername());
		Optional<User>userFoundBySsn= userRepository.findBySsn(user.getSsn());

		if(userFoundByUsername.isPresent()) {
			throw new UserExistsException("User is already present with given user name");
		}
		else if(userFoundBySsn.isPresent()) {
			throw new UserExistsException("User is already present with given ssn");
		}
		return userRepository.save(user);
	}

	public Optional<User> getUserById(Long id) throws UserNotFoundException {

		Optional<User> user=  userRepository.findById(id);
		if(!user.isPresent()) {
			throw new UserNotFoundException("User not found");
		}
		return user;

	}

	public User updateUserById(Long id, User user) throws UserNotFoundException{
		Optional<User> optionalUser=  userRepository.findById(id);
		if(!optionalUser.isPresent()) {
			throw new UserNotFoundException("User not found for given user id for update");
		}
		user.setId(id);
		return userRepository.save(user);
	}

	public void deleteUserById(Long id) throws ResponseStatusException {
		Optional<User> optionalUser=  userRepository.findById(id);
		if(!optionalUser.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User not found for given user id for delete");
		}
		userRepository.deleteById(id);


	}
}
