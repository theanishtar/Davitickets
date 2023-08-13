package com.davisys.controller;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.davisys.dao.UserDAO;
import com.davisys.entity.Users;
import com.davisys.service.SessionService;

@Controller
@RequestMapping("/admin")
@RolesAllowed("ROLE_ADMIN")
public class ChangePassWordController {

	@Autowired
	UserDAO userDao;
	@Autowired
	SessionService sessionService;
	@Autowired
	HttpServletRequest request;
	@Autowired
	private PasswordEncoder passwordEncoder;
	String alert = "";

	@GetMapping("/changepassword")
	public String changepass(Model m) {
		m.addAttribute("alert", alert);
		alert = "";
		return "admin/changepassword";
	}

	@PostMapping("/changepass")
	public String change(Model m) {
		Users user = sessionService.get("user");
		Users userId = userDao.findIdUsers(user.getUserid());
		String password = request.getParameter("password");
		String newPassword = request.getParameter("newPassword");
		System.out.println(newPassword);
		String confirmPassword = request.getParameter("confirmPassword");
		System.out.println(confirmPassword);

		if (passwordEncoder.matches(password, user.getPassword()) && newPassword.equals(confirmPassword)) {
			alert = "updateSuccess";
			userId.setUser_password(passwordEncoder.encode(confirmPassword));
			userDao.saveAndFlush(userId);
		} else if (!password.equals(userId.getUser_password())) {
			alert = "passwordFail";
			return "redirect:/admin/changepassword";
		} else {
			alert = "updateFail";
			return "redirect:/admin/changepassword";
		}
		m.addAttribute("alert", alert);
		alert = "";
		return "admin/dasboard";
	}
}
