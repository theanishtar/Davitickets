package com.davisys.controller.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.config.JwtTokenUtil;
import com.davisys.dao.UserDAO;
import com.davisys.entity.Users;

@RestController
@CrossOrigin("*")
@RequestMapping
public class Profile {
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	UserDAO dao;
	
	@GetMapping("/profile")
	public ResponseEntity<Users> loadProfile(HttpServletRequest request){
		try {
			String email = jwtTokenUtil.getEmailFromHeader(request);
			System.out.println("Profile: "+email);
			Users user = dao.findEmailUser(email);
			return ResponseEntity.status(200).body(user);
		} catch (Exception e) {
			return ResponseEntity.status(403).body(null);
		}
	}
	
	@PutMapping("/profile/update")
	public ResponseEntity<Users> update(HttpServletRequest request, @RequestBody Users userUpdate){
		String email = jwtTokenUtil.getEmailFromHeader(request);
		Users user = dao.findEmailUser(email);
		try {
			if(user.getUserid() == userUpdate.getUserid()) {
				user.setFull_name(userUpdate.getFull_name());
				user.setUser_password(userUpdate.getPassword());
				user.setGender(userUpdate.getGender());
				user.setPhone(userUpdate.getPhone());
				user.setUser_birtday(userUpdate.getUser_birtday());
				user.setProfile_picture(userUpdate.getProfile_picture());
				//cập nhật
				userUpdate.setUserid(user.getUserid());
				dao.saveAndFlush(userUpdate);
			}
		} catch (Exception e) {
			return ResponseEntity.ok(null);
		}
		return ResponseEntity.ok(userUpdate);
	}
}