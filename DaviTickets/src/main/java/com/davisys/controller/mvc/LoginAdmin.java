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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.davisys.auth.AuthenticationRequest;
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

	@PostMapping("/login/admin")
	public String log2(Model model, @RequestBody AuthenticationRequest authenticationRequest) {
		System.out.println(authenticationRequest.getEmail() + "000000000000000000000000000");
		Users user = dao.findEmailUser(authenticationRequest.getEmail());

		if (user != null && encoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
			boolean isAdmin = Arrays.asList(user.getAuth()).contains("ADMIN");
			System.out.println(Arrays.asList(user.getAuth()));
			if(isAdmin) {
				System.out.println("IS ADMIN -> SET SESSION");
				sessionService.set("user", user);
				model.addAttribute("user", user);
			} else {
				System.out.println("IS USER -> DONT SET SESSION");
			}
		}
		return "oauth/index";
	}
	
	//oauth/admin/rec/{email}/{password}
	@GetMapping("/rec/{email}/{password}")
	public String recToHome(@PathVariable("email") String email,
            @PathVariable("password") String password,
            HttpServletResponse response) {

		System.out.println("REC: "+email+password);
		Users user = dao.findEmailUser(email);
		if (user != null && encoder.matches(password, user.getPassword())) {
			boolean isAdmin = Arrays.asList(user.getAuth()).contains("ADMIN");
			System.out.println(Arrays.asList(user.getAuth()));
			if(isAdmin) {
				System.out.println("IS ADMIN -> SET SESSION");
				sessionService.set(SessionAttribute.CURRENT_USER, user);
				
			} else {
				System.out.println("IS USER -> DONT SET SESSION");
			}
		}
		Cookie cookie = new Cookie("JSESSIONID", "");
        // Đặt ngày hết hạn là 0, tức là giá trị ở quá khứ
        cookie.setMaxAge(0);
        // Đặt domain, path, secure, httponly nếu cần
        // ...
        // Thêm cookie vào response
        response.addCookie(cookie);
		return "redirect:/admin";
	}

	@GetMapping("/authen/index")
	public String toAdmin(Model model) {
		model.addAttribute("user", sessionService.get("user"));
		return "oauth/rectohome";
	}
}
