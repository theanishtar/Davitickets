package com.davisys.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.config.JwtTokenUtil;
import com.davisys.dao.UserDAO;
import com.davisys.entity.StatusLogin;
import com.davisys.entity.Users;
import com.davisys.service.MailerServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class ForgotpasswordRescontroller {
	@Autowired
	UserDAO userDAO;
	private Users user = new Users();
	@Autowired
	MailerServiceImpl mailer;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	int checkCount = 0;
	static String codeMail = "";

	@PostMapping("/rest/forgotpassword")
	public ResponseEntity<StatusLogin> forgotpass(@RequestBody String email) throws Exception {
		try {
//			checkCount = 0;
			 codeMail = "";
			StatusLogin statusLogin = new StatusLogin();
			statusLogin.setRole(true);
			statusLogin.setUser(null);

			ObjectMapper mapper = new ObjectMapper();
			JsonNode usgc = mapper.readTree(email);
			String mail = usgc.get("email").asText();
			this.user = userDAO.findEmailUser(mail);
			if (this.user == null) {
				statusLogin.setSesionId("errorusernull");
				System.out.println(1);
				return ResponseEntity.status(401).body(statusLogin);
			} else {
				random();
				System.out.println(codeMail);
				String subject = "Mã xác nhận của bạn";
				mailer.send(this.user.getEmail(), subject, codeMail);
				statusLogin.setSesionId("success");
				System.out.println("thành công");
				return ResponseEntity.status(200).body(statusLogin);
			}
		} catch (Exception e) {
			System.out.println("Error forgotpass: " + e);
			throw e;
		}

	}

	@PostMapping("/rest/checkForgotpassword")
	public ResponseEntity<StatusLogin> checkValidCode(@RequestBody String code) {
		try {
			StatusLogin statusLogin = new StatusLogin();
			statusLogin.setRole(true);
			statusLogin.setUser(null);
			if (checkCount == 3) {
				checkCount = 0;
				statusLogin.setSesionId("errorcountcode");
				codeMail = "";
				return ResponseEntity.status(401).body(statusLogin);
			}
			if (code.contains(codeMail)) {
				statusLogin.setSesionId("success");
				codeMail = "";
				checkCount = 0;
				return ResponseEntity.status(200).body(statusLogin);
			} else {
				checkCount++;
				statusLogin.setSesionId("errorcode");
				return ResponseEntity.status(401).body(statusLogin);
			}
		} catch (Exception e) {
			System.out.println("Error checkValidcode: " + e);
			throw e;
		}
	}

	@PostMapping("/rest/changePassword")
	public ResponseEntity<StatusLogin> changePassword(@RequestBody String newPass) throws Exception {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode usgc = mapper.readTree(newPass);
			String pass = usgc.get("newPass").asText();
			user.setUser_password(passwordEncoder.encode(pass));
			userDAO.saveAndFlush(this.user);
			return ResponseEntity.status(200).body(null);
		} catch (Exception e) {
			System.out.println("Error changePassword: " + e);
			throw e;
		}
	}

	public static void random() {
		for (int i = 0; i < 6; i++) {
			codeMail = codeMail + (int) (Math.floor(Math.random() * 9));
		}
	}
}
