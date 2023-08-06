package com.davisys.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.config.JwtTokenUtil;

//@RestController
//@CrossOrigin("*")
//@RequestMapping
//public class Profile {
//	@Autowired
//	JwtTokenUtil jwtTokenUtil;
//	
//	
//	
//	@PutMapping("/profile")
//	public ResponseEntity<String> loadProfile(HttpServletRequest request){
//		
//		String email = jwtTokenUtil.getEmailFromHeader(request);
//		System.out.println("Profile: "+email);
//		
//		return ResponseEntity.ok(email);
//	}
//	
//	@GetMapping("/profile2")
//	public ResponseEntity<String> loadProfile2(){
//		
//		return ResponseEntity.ok("HELLO");
//	}
//}
