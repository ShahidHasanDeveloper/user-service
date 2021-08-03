package com.epam.users.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import lombok.Data;


@Data
@Entity
@Table(name="user")
@ApiModel(description = "This model is to create a user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="USER_ID")
	private Long id;
	
	
	@Column(name="USER_NAME", length=50, nullable=false, unique = true)
	@NotEmpty(message = "User name is mandatory , please provide username")
	private String username;
	
	@Column(name="FIRST_NAME", length=50, nullable=false)
	@Size(min=2, message="First Name should have minimum 2 characters")
	private String firstname;
	
	@Column(name="LAST_NAME", length=50, nullable=false)
	private String lastname;
	
	@Column(name="EMAIL_ADDRESS", length=50, nullable=false)
	private String email;
	
	@Column(name="ROLE", length=50, nullable=false)
	private String role;
	
	@Column(name="SSN", length=50, nullable=false, unique = true)
	private String ssn;
	
	
}
