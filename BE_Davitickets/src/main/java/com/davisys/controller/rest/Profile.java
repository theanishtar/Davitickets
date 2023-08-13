package com.davisys.controller.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.config.JwtTokenUtil;
import com.davisys.dao.UserDAO;
import com.davisys.entity.Users;
import com.davisys.model.ProfileModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin()
@RolesAllowed("ROLE_USER")
public class Profile {
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	UserDAO dao;

	@Autowired
	HttpServletRequest request;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/profile/data")
	public ResponseEntity<ProfileModel> loadProfile(HttpServletRequest request) {
		try {
			String email = jwtTokenUtil.getEmailFromHeader(request);
			System.out.println("Email nè: " + request);
			Users user = dao.findEmailUser(email);
			ProfileModel profile = new ProfileModel(user.getFull_name(), user.getEmail(), user.getProfile_picture(),
					user.getPhone(), user.getUser_birtday(), null, user.getGender());
			return ResponseEntity.status(200).body(profile);
		} catch (Exception e) {
			System.out.println("Error: " + e);
			return ResponseEntity.status(403).body(null);
		}
	}

	@PostMapping("/profile/update")
	public ResponseEntity<ProfileModel> update(HttpServletRequest request, @RequestBody ProfileModel userUpdate) {
		System.out.println("Hello: " + userUpdate.getPic() + "sd");
		try {
			String email = jwtTokenUtil.getEmailFromHeader(request);
			Users user = dao.findEmailUser(email);
			user.setFull_name(userUpdate.getFullname());
			user.setGender(userUpdate.getGender());
			user.setPhone(userUpdate.getPhone());
			user.setUser_birtday(userUpdate.getBirtday());
			if (userUpdate.getPassword() == null) {
				user.setUser_password(user.getPassword());
			} else {
				user.setUser_password(userUpdate.getPassword());
			}

			if ("".equals(userUpdate.getPic())) {
				user.setProfile_picture(user.getProfile_picture());
			} else {
				user.setProfile_picture(userUpdate.getPic());
			}
			System.out.println("xem nè: " + userUpdate.getFullname());
			// cập nhật
			dao.saveAndFlush(user);
			return ResponseEntity.ok(userUpdate);
		} catch (Exception e) {
			System.out.println("Lỗi nè: " + e);
			throw e;
		}
	}

	@PostMapping("/profile/changePassword")
	public ResponseEntity<List<String>> changePass(@RequestBody String pass, HttpServletRequest request) throws Exception {
		try {
			String email = jwtTokenUtil.getEmailFromHeader(request);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode changePass = mapper.readTree(pass);
			Users user = dao.findEmailUser(email);
			List<String> status=new ArrayList<>();
			System.out.println("oldPass: "+changePass.get("oldPass").asText());
			System.out.println("newPass :"+changePass.get("newPass").asText());
//			status.add("error1");
			if (!passwordEncoder.matches(changePass.get("oldPass").asText(), user.getUser_password())) {
				System.out.println("error");
				status.add("error");
//				return ResponseEntity.status(200).body(status);
			} else {
				System.out.println("success");
				user.setUser_password(passwordEncoder.encode(changePass.get("newPass").asText()));
				dao.saveAndFlush(user);
				status.add("success");
			
			}
			return ResponseEntity.status(200).body(status);
		} catch (Exception e) {
			System.out.println("Error changePass: " + e);
			throw e;
		}
	}

}