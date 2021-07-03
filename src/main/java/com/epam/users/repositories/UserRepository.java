package com.epam.users.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.users.entities.User;

public interface UserRepository extends JpaRepository<User, Long>  {

	public Optional<User> findByUsername(String username);
	public Optional<User> findBySsn(String ssn);
	
}
