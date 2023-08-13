package com.davisys.controller.mvc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.davisys.dao.RoleDAO;
import com.davisys.dao.UserDAO;
import com.davisys.encrypt.AES;
import com.davisys.entity.Roles;
import com.davisys.entity.StatusLogin;
import com.davisys.entity.Users;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class GGCloud {
	@Autowired
	private UserDAO userDAO;

	@Autowired
	HttpServletRequest httpServletRequest;

	@GetMapping("tohome")
	public String redec(Model m) {
		return "index";
	}

	@Autowired
	AES aes;

	@Autowired
	RoleDAO roleDAO;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("login/oauth")
	public String login(Model m) {
		try {
			System.out.println("s: " + httpServletRequest.getParameter("credential"));
			StatusLogin statusLogin = new StatusLogin();
			String token = httpServletRequest.getParameter("credential");
			String[] chunks = token.split("\\.");

			Base64.Decoder decoder = Base64.getUrlDecoder();

			String header = new String(decoder.decode(chunks[0]));
			String payload = new String(decoder.decode(chunks[1]));

			// UserGoogleCloud ugc = new Gson().fromJson(payload, UserGoogleCloud.class);

			ObjectMapper mapper = new ObjectMapper();
			// UserGoogleCloud userGoogleCloud = mapper.readValue(payload,
			// UserGoogleCloud.class);
			JsonNode usgc = mapper.readTree(payload);

			// System.out.println(">> Name : " + usgc.get("email").asText());
			/*
			 * usgc.iterator().forEachRemaining(u -> {
			 * System.out.println(">> Name : "+u.get(0).asText()); });
			 */

			Users checkUser = userDAO.findEmailUser(usgc.get("email").asText());

			Users user = new Users();
			String uname = usgc.get("email").asText();
			user.setUser_name(uname.substring(0, uname.indexOf("@")));
			user.setFull_name(usgc.get("name").asText());
			user.setGender("Nam");
			user.setUser_password(passwordEncoder.encode(usgc.get("sub").asText()));
			user.setPhone(null);
			user.setEmail(usgc.get("email").asText());
			user.setProfile_picture(usgc.get("picture").asText());
			user.setAccount_status(true);
			user.setProcessed_by(true);
			
			String pattern = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			Date birthdayDate = dateFormat.parse("2000-1-1");
			user.setUser_birtday(birthdayDate);

			user.setGg_id(usgc.get("sub").asText());
			if (checkUser == null) {
				// user.setUser_role(false);
				Roles role = roleDAO.findById(2).orElseThrow();
				role.setName(role.getName());
				role.setRole_des(role.getRole_des());
//				user.getRoles().s;
				user.getRoles().add(role);
				userDAO.save(user);
			} else {
				System.out.println("CO trong DB");
			}

			String emailEnc = aes.EncryptAESfinal(user.getEmail());

			System.err.println(emailEnc);
			m.addAttribute("dataEnc", emailEnc);
			m.addAttribute("email", user.getEmail());

//			return ResponseEntity.status(200).body(statusLogin);
			return "oauth/login";

		} catch (Exception e) {
			System.out.println("error: " + e);
			return "oauth/login";
		}
	}
}
