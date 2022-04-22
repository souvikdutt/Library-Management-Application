package com.epam.usermanager.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
	@Id
	@GenericGenerator(name = "user_seq", strategy = "com.epam.usermanager.util.CustomIdGenerator")
	@GeneratedValue(generator = "user_seq")
	private String username;
	@Email
	private String email;
	@NotBlank
	@NotNull
	private String name;
}
