package com.davisys.controller;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.davisys.dao.UserDAO;
import com.davisys.entity.StatusLogin;
import com.davisys.entity.Users;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class OAuth {

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	HttpServletRequest httpServletRequest;
	
	@GetMapping("tohome")
	public String redec(Model m) {
		return "index";
	}
	
	@PostMapping("login/oauth")
	public String login(Model m) {
		try {
			// System.out.println("s: "+req.getParameter("credential"));
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

			//System.out.println(">> Name : " + usgc.get("email").asText());
			/*
			 * usgc.iterator().forEachRemaining(u -> {
			 * System.out.println(">> Name : "+u.get(0).asText()); });
			 */

			Users checkUser = userDAO.findEmailUser(usgc.get("email").asText());
			Users user = new Users();
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
			if (checkUser == null) {
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
			
			System.err.println(user.getEmail());
			m.addAttribute("user",user);

//			return ResponseEntity.status(200).body(statusLogin);
			return "tohome/redirect";

		} catch (Exception e) {
			System.out.println("error: " + e);
			return "tohome/redirect";
		}
	}
}
