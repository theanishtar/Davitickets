package com.davisys.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.dao.UserDAO;
import com.davisys.entity.Account;
import com.davisys.entity.StatusLogin;
import com.davisys.entity.Users;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class Register {
	@Autowired
	private UserDAO userDAO;
	private Users user = new Users();

	@PostMapping("/rest/register")
	public ResponseEntity<StatusLogin> register(HttpSession session, @RequestBody Account account) {
		try {
			Users checkUser = userDAO.findPhoneAndEmailUser(account.getEmail(), account.getPhone());
			if (checkUser == null) {
				Users user = new Users();
				user.setFull_name(account.getFull_name());
				user.setGender("Nam");
				user.setUser_password(account.getPassword());
				user.setPhone(account.getPhone());
				user.setEmail(account.getEmail());
				user.setProfile_picture(null);
				user.setAccount_status(true);
				user.setProcessed_by(true);
				user.setUser_role(true);
				userDAO.saveAndFlush(user);
				this.user = user;
				return ResponseEntity.status(200).body(null);
			} else {
				return ResponseEntity.status(401).body(null);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/rest/register_login")
	public ResponseEntity<StatusLogin> login(HttpSession session) {
		try {
			StatusLogin statusLogin = new StatusLogin();
			session.setAttribute("isUserLoggedIn", user);
			statusLogin.setSesionId(session.getId());
			statusLogin.setRole(false);
			statusLogin.setUser(user);
			return ResponseEntity.status(201).body(statusLogin);
		} catch (Exception e) {
			throw e;
		}

	}
}
