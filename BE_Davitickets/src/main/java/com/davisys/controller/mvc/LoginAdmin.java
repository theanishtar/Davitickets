package com.davisys.controller.mvc;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.davisys.constant.SessionAttribute;
import com.davisys.dao.UserDAO;
import com.davisys.entity.Users;
import com.davisys.service.CookieService;
import com.davisys.service.ParamService;
import com.davisys.service.SessionService;

@Controller
@RequestMapping("/oauth")
public class LoginAdmin {

	@Autowired
	HttpServletRequest request;

	@Autowired
	ParamService paramService;

	@Autowired
	CookieService cookieService;

	@Autowired
	SessionService sessionService;
	@Autowired
	UserDAO dao;

	@Autowired
	PasswordEncoder encoder;

	// oauth/admin/rec/{email}/{password}
	@GetMapping("/rec/{email}/{password}")
	public String recToHome(@PathVariable("email") String email, @PathVariable("password") String password,
			HttpServletResponse response) {
		System.out.println("REC: " + email + password);
		Users user = dao.findEmailUser(email);
		if (user != null && encoder.matches(password, user.getPassword())) {
			boolean isAdmin = Arrays.asList(user.getAuth()).contains("ADMIN");
			System.out.println(Arrays.asList(user.getAuth()));
			if (isAdmin) {
				System.out.println("IS ADMIN -> SET SESSION");
				sessionService.set(SessionAttribute.CURRENT_USER, user);

			} else {
				System.out.println("IS USER -> DONT SET SESSION");
			}
		}
//		Cookie cookie = new Cookie("JSESSIONID", "");
//		Cookie cookie2 = new Cookie("jsessionid", "");
//		cookie.setMaxAge(0);
//		cookie2.setMaxAge(0);
//		response.addCookie(cookie);
//		response.addCookie(cookie2);
		return "redirect:/admin";
	}

	@GetMapping("/authen/index")
	public String toAdmin(Model model) {
		model.addAttribute("user", sessionService.get("user"));
		return "oauth/rectohome";
	}
}
