package com.davisys.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.dao.UserDAO;
import com.davisys.entity.Account;
import com.davisys.entity.StatusLogin;
import com.davisys.entity.UserGoogleCloud;
import com.davisys.entity.Users;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin("*")
public class Login {
	@Autowired
	private UserDAO userDAO;

	@PostMapping("/rest/login")
	public ResponseEntity<StatusLogin> login(HttpSession session, @RequestBody Account account) {
		StatusLogin statusLogin = new StatusLogin();
		try {
			Users user = userDAO.findEmaiAndPhonelUser(account.getEmail());
			statusLogin.setSesionId(session.getId());
			if (user != null && user.getUser_password().equals(account.getPassword())) {
//				statusLogin.setStatus("successfull");
				session.setAttribute("isUserLoggedIn", user);
				if (user.getUser_role() == true) {
					statusLogin.setRole(true);
				} else {
					statusLogin.setRole(false);
				}
				statusLogin.setUser(user);
				statusLogin.setSesionId(session.getId());
				return ResponseEntity.status(200).body(statusLogin);
			} else {
				return ResponseEntity.status(401).body(null);
			}
		} catch (Exception e) {
			System.out.println("error: " + e);
			throw e;
		}
	}

	@PostMapping("/rest/loginWithGG")
	public ResponseEntity<UserGoogleCloud> loginWithGG(HttpServletRequest req, HttpSession session) {
		try {
			// System.out.println("s: "+req.getParameter("credential"));
			StatusLogin statusLogin = new StatusLogin();
			String token = req.getParameter("credential");
			String[] chunks = token.split("\\.");

			Base64.Decoder decoder = Base64.getUrlDecoder();

			String header = new String(decoder.decode(chunks[0]));
			String payload = new String(decoder.decode(chunks[1]));

			// UserGoogleCloud ugc = new Gson().fromJson(payload, UserGoogleCloud.class);

			ObjectMapper mapper = new ObjectMapper();
			// UserGoogleCloud userGoogleCloud = mapper.readValue(payload,
			// UserGoogleCloud.class);
			JsonNode usgc = mapper.readTree(payload);

			System.out.println(">> Name : " + usgc.get("email").asText());
			/*
			 * usgc.iterator().forEachRemaining(u -> {
			 * System.out.println(">> Name : "+u.get(0).asText()); });
			 */

			Users checkUser = userDAO.findEmailUser(usgc.get("email").asText());
			Users user = new Users();
			if (checkUser == null) {
				user.setFull_name(usgc.get("name").asText());
				user.setGender("Nam");
				user.setUser_password(usgc.get("sub").asText());
				user.setPhone(null);
				user.setEmail(usgc.get("email").asText());
				user.setProfile_picture(usgc.get("picture").asText());
				user.setAccount_status(true);
				user.setProcessed_by(true);
				user.setUser_birtday(null);
				user.setGg_id(usgc.get("sub").asText());
				user.setUser_role(false);
				userDAO.save(user);

				session.setAttribute("isUserLoggedIn", user);
				statusLogin.setRole(user.getUser_role());
				statusLogin.setSesionId(session.getId());
				statusLogin.setUser(user);
			} else {
				session.setAttribute("isUserLoggedIn", checkUser);
				statusLogin.setRole(checkUser.getUser_role());
				statusLogin.setSesionId(session.getId());
				statusLogin.setUser(checkUser);
			}

//			return ResponseEntity.status(200).body(statusLogin);
			return ResponseEntity.status(200).body(null);

		} catch (Exception e) {
			System.out.println("error: " + e);
			return ResponseEntity.status(200).body(null);
		}

//		try {
//			StatusLogin statusLogin = new StatusLogin();
//			Users checkUser = userDAO.findEmailUser(userGoogleCloud.getEmail());
//			if (checkUser == null) {
//			System.out.println("S: " + userGoogleCloud.getEmail());
//				Users user = new Users();
//				user.setFull_name(userGoogleCloud.getName());
//				user.setGender("Nam");
//				user.setUser_password(userGoogleCloud.getId());
//				user.setPhone(null);
//				user.setEmail(userGoogleCloud.getEmail());
//				user.setProfile_picture(userGoogleCloud.getPhotoUrl());
//				user.setAccount_status(true);
//				user.setProcessed_by(true);
//				user.setUser_birtday(null);
//				user.setGg_id(userGoogleCloud.getId());
//				user.setUser_role(false);
//				userDAO.save(user);
//
//				session.setAttribute("isUserLoggedIn", user);
//				statusLogin.setRole(user.getUser_role());
//				statusLogin.setSesionId(session.getId());
//				statusLogin.setUser(user);
//				return ResponseEntity.status(200).body(statusLogin);
//
//			} else {
////				userDAO.insertUser(Integer.valueOf(userGoogleCloud.getId()), userGoogleCloud.getName(), "Nam",
////						userGoogleCloud.getIdToken(), "", userGoogleCloud.getEmail(), userGoogleCloud.getPhotoUrl(),
////						Boolean.valueOf(true), Boolean.valueOf(true), "", Boolean.valueOf(true));
//				session.setAttribute("isUserLoggedIn", checkUser);
//				statusLogin.setRole(false);
//				statusLogin.setSesionId(session.getId());
//				statusLogin.setUser(checkUser);
//				return ResponseEntity.status(200).body(statusLogin);
//
//			}
//		} catch (Exception e) {
//			System.out.println("error: " + e);
//			throw e;
//		}
	}

	@GetMapping("/rest/getAccount/{email}")
	public ResponseEntity<Users> getAccountGGCloud(@PathVariable String email) {

		Users checkUser = userDAO.findEmailUser(email);
		System.out.println("First S: " + checkUser.getEmail());
		if (checkUser != null) {
			return ResponseEntity.status(200).body(checkUser);
		}
		return ResponseEntity.status(404).body(null);
	}
	
	@GetMapping("/rest/getAccounts")
	public ResponseEntity<List<Users>> getAll() {

		List<Users> list = userDAO.findAll();
		return ResponseEntity.status(200).body(list);
	}

}
