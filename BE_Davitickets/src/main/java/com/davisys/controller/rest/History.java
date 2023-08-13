package com.davisys.controller.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.config.JwtTokenUtil;
import com.davisys.dao.UserDAO;
import com.davisys.entity.Users;

@RestController
@CrossOrigin()
@RolesAllowed("ROLE_USER")
public class History {
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@GetMapping("/history/data")
	public ResponseEntity<List<Object[]> > loadHistory(HttpServletRequest request){
		try {
			String email = jwtTokenUtil.getEmailFromHeader(request);
			System.out.println("Email nè: " + request);
			Users user = userDAO.findEmailUser(email);
			List<Object[]> listHistory = userDAO.loadDataHistory(user.getUserid());
			
			return ResponseEntity.status(200).body(listHistory);
		} catch (Exception e) {
			System.out.println("Error: " + e);
			return ResponseEntity.status(403).body(null);
		}
	}
}
