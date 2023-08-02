package com.davisys.controller;

import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.davisys.reponsitory.UsersReponsitory;
import com.davisys.service.AuthenticationService;
import com.davisys.entity.*;

@RestController
@CrossOrigin("*")
@RequestMapping
public class Test {
	@Autowired
	UsersReponsitory usersReponsitory;

	@Autowired
	UserDAO dao;
	
	@Autowired
	AuthenticationService authenticationService;

	@GetMapping("/auth/login/{email}")
	public ResponseEntity<Optional<Users>> getUsers(@PathVariable String email) {
		System.out.println("email: "+email);
		try {
//			return ResponseEntity.ok(authenticationService.authenticationResponse(authenticationRequest));
			Optional<Users> user = usersReponsitory.findByEmail(email);
//			Users user = userDAO.findEmailUser(email);
			return ResponseEntity.status(200).body(user);
		} catch (Exception e) {
			System.out.println("error testLogin: "+e);
			throw e;
		}
		
	}
	
	@PostMapping("/anony/login")
	public ResponseEntity<AuthenticationResponse> authLog(@RequestBody AuthenticationRequest authenticationRequest){
		return ResponseEntity.ok(authenticationService.authenticationResponse(authenticationRequest));
	}
	
	@GetMapping("/user")
	@RolesAllowed("ROLE_USER")
	public ResponseEntity<String> s(){
		return ResponseEntity.ok("User successs");
	}
	
	@GetMapping("/user/test")
	@RolesAllowed("ROLE_USER")
	public ResponseEntity<String> sa(){
		return ResponseEntity
				.ok("User successs");
	}
	
	@GetMapping("/admin")
	@RolesAllowed("ROLE_ADMIN")
	public ResponseEntity<String> c(){
		return ResponseEntity.ok("ADMin succcesss");
	}
	
	@GetMapping("/admin/test")
	@RolesAllowed("ROLE_ADMIN")
	public ResponseEntity<String> testAdmin() {
		return ResponseEntity.ok("I am admin");
	}
	
	@GetMapping("/anony")
	public ResponseEntity<String> getUs() {
		return ResponseEntity.status(200).body("success");
		
	}
	@GetMapping("/anony/test")
	public ResponseEntity<String> gets() {
		Users u = dao.findEmailUser("nghiaqh@fe.edu.vn");
		System.out.println(u.getGender());
		
		return ResponseEntity.status(200).body("g");
		
	}
}
