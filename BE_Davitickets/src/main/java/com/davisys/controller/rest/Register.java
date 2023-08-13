package com.davisys.controller.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.config.JwtTokenUtil;
import com.davisys.dao.RoleDAO;
import com.davisys.dao.UserDAO;
import com.davisys.entity.Account;
import com.davisys.entity.Roles;
import com.davisys.entity.StatusLogin;
import com.davisys.entity.Users;
import com.davisys.service.MailerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class Register {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	private Users user = new Users();
	static String codeMail = "";
	static int checkCount = 0;
	HashMap<String, String> map = new HashMap<>();
	@Autowired
	MailerServiceImpl mailer;

	@PostMapping("/user/register")
	public ResponseEntity<StatusLogin> register(HttpServletRequest request, @RequestBody Account account) {
		HttpSession session = request.getSession();
		try {
			Users checkUser = userDAO.findPhoneAndEmailUser(account.getEmail(), account.getPhone());
			System.out.println("pass: "+account.getPassword());
			if (checkUser == null) {
				Users user = new Users();
				user.setFull_name(account.getFull_name());
				user.setUser_name(account.getEmail().substring(0,account.getEmail().indexOf("@")));
				user.setGender("Nam");
				user.setUser_password(passwordEncoder.encode(account.getPassword()));
				user.setPhone(account.getPhone());
				user.setEmail(account.getEmail());
				user.setProfile_picture(null);
				user.setAccount_status(true);
				user.setProcessed_by(true);
//				user.setUser_role(true);
				Roles role= roleDAO.findById(2).orElseThrow();
				role.setName(role.getName());
				role.setRole_des(role.getRole_des());
				user.getRoles().add(role);
				user.setUser_dayjoin(new Date());
				this.user = user;
				random();
				String subject = "Mã xác nhận của bạn";
				mailer.send(this.user.getEmail(), subject, codeMail);
				request.getSession().setAttribute("codeMail", codeMail);
				map.put(this.user.getEmail(), codeMail);
				codeMail = "";
				return ResponseEntity.status(200).body(null);
			} else {
				return ResponseEntity.status(401).body(null);
			}
		} catch (Exception e) {
			System.out.println("erorr: " + e);
			throw e;
		}
	}

	@PostMapping("/user/checkCodeMail")
	public ResponseEntity<StatusLogin> checkMail(HttpServletRequest request, @RequestBody String maxn) {
		StatusLogin login = new StatusLogin();
		if (checkCount == 3) {
			checkCount = 0;
			login.setRole(true);
			login.setUser(null);
			login.setSesionId("error");
			return ResponseEntity.status(400).body(login);
		}
		for (String key : map.keySet()) {
			String value = map.get(key);
			if (this.user.getEmail().equals(key) && maxn.contains(value)) {
				userDAO.saveAndFlush(this.user);
				checkCount = 0;
				login.setRole(true);
				login.setUser(null);
				login.setSesionId("success");
				return ResponseEntity.status(200).body(login);
			} else if (this.user.getEmail().equals(key) && !maxn.contains(value)) {
				checkCount++;
				login.setRole(true);
				login.setUser(null);
				login.setSesionId("errorPass");
				return ResponseEntity.status(401).body(login);
			}
		}
		throw null;

	}

	@GetMapping("/user/register_login")
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

	public static void random() {
		for (int i = 0; i < 6; i++) {
			codeMail = codeMail + (int) (Math.floor(Math.random() * 9));
		}
	}
}
