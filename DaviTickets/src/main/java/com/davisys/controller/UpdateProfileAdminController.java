package com.davisys.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.davisys.dao.UserDAO;
import com.davisys.entity.Users;
import com.davisys.service.SessionService;

import javax.servlet.http.HttpServletRequest;


@Controller
public class UpdateProfileAdminController {

	@Autowired
	SessionService sessionService;
	@Autowired
	HttpServletRequest request;
	@Autowired
	UserDAO userDao;
	

	String alert = "";
	
	@GetMapping("/updateprofileadmin")
	public String updateadmin(Model m) {
		try {
			m.addAttribute("alert", alert);
			Users user = sessionService.get("user");
			Users userId = userDao.findIdUsers(user.getUserid());
			System.out.println(userId.getUser_birtday());
			m.addAttribute("user", userId);
		return "admin/profileadmin";
		} catch (Exception e) {
			System.out.println(e);
			throw e;
		}
		
		
	}
	
	@PostMapping("/updateadmin")
	public String updateprofileadmin(Model m,@RequestParam("file") MultipartFile file) {
		try {
			alert = "updateSuccess";
			String avarta = request.getParameter("avarta");
			String fullname = request.getParameter("full_name");
			String gender = request.getParameter("gender");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String birtday = request.getParameter("user_birtday");
			String password = request.getParameter("user_password");
			String pattern = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			Users userSession = sessionService.get("user");
			System.out.println(userSession.getFull_name());
			System.out.println(password);
			Users user = userDao.findIdUsers(userSession.getUserid());
			try {
				Date date = dateFormat.parse(birtday);
				user.setUser_birtday(date);
			} catch (Exception e) {
				alert = "updateFail";
				e.printStackTrace();
				System.out.println("E"+e);
			}
			user.setUserid(user.getUserid());
			user.setFull_name(fullname);
			user.setPhone(phone);
			user.setGender(gender);
			user.setEmail(email);
			user.setUser_password(password);
			
			if (!file.isEmpty()) {
				user.setProfile_picture(avarta);
			} else {
				user.setProfile_picture(user.getProfile_picture());
			}
			sessionService.set("user" ,user);
			userDao.saveAndFlush(user);
			
		} catch (Exception e) {
			alert = "updateFail";
			System.out.println(e);
		}
		return "redirect:/updateprofileadmin";
	}
	@RequestMapping("/huy")
	public String huy() {
		return "redirect:/updateprofileadmin";
	}
}
