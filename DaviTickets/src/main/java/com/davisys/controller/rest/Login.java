package com.davisys.controller.rest;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.auth.AuthenticationRequest;
import com.davisys.auth.AuthenticationResponse;
import com.davisys.dao.UserDAO;
import com.davisys.entity.Users;
import com.davisys.reponsitory.UsersReponsitory;
import com.davisys.service.AuthenticationService;

@RestController
@CrossOrigin("*")
@RequestMapping
public class Login {
	@Autowired
	UsersReponsitory usersReponsitory;

	@Autowired
	UserDAO dao;

	@Autowired
	AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder pe;
    
	@PostMapping("/oauth/login")
	public ResponseEntity<AuthenticationResponse> authLog(@RequestBody AuthenticationRequest authenticationRequest) {
		if(authenticationRequest.getEmail()==null || authenticationRequest.getPassword()==null)
			return ResponseEntity.status(400).body(null);
		Users user = dao.findEmailUser(authenticationRequest.getEmail());
		System.out.println(user.getFull_name());
		Boolean checkPass = (pe.matches(authenticationRequest.getPassword(), user.getPassword()));
		if (user != null && checkPass) {
			return ResponseEntity.ok(authenticationService.authenticationResponse(authenticationRequest));
		}
		return ResponseEntity.status(403).body(null);

	}
}
