package com.davisys.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.dao.UserDAO;
import com.davisys.entity.Users;
import com.davisys.service.SessionService;
import com.davisys.utils.SessionUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin("*")
public class SessionController {
	@Autowired
	SessionService service;
	
	@Autowired
	UserDAO userDAO;
	
	
	@Autowired
	SessionUtils sessionUtils;
	
	@GetMapping("/rest/session/get")
	public ResponseEntity<String> getSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return ResponseEntity.status(200).body("This is server: "+session.getId());
	}
	
	
	@GetMapping("/rest/session/{sessionId}")
	public ResponseEntity<Users> findSession(@PathVariable String sessionId, HttpServletRequest httpServletRequest) {
		Users u = new Users(); 

		HttpSession session = httpServletRequest.getSession();
		u = (Users) session.getAttribute("currentUser");
		
		String id = (String) session.getAttribute("currentUserId");
		
		System.out.println(session.getId()+":::"+id);

		/*
		 * System.out.println("SESSION ID PARAMS: "+service.getSessionId(
		 * "isUserLoggedIn"));
		 * 
		 * ;
		 */
		//System.out.println("SESSION ID GET: "+sessionUtils.getSessionById(sessionId));
		return ResponseEntity.status(200).body(u);
	}
}
