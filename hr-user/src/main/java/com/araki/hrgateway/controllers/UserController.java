package com.araki.hrgateway.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.araki.hrgateway.entities.User;
import com.araki.hrgateway.services.KeyCloakAdminClientService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	KeyCloakAdminClientService kcAdminClient;

	@PostMapping
	public ResponseEntity<Void> createUser(@RequestBody User user) {
		kcAdminClient.addUser(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}