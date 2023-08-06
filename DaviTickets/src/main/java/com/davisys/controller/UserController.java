package com.davisys.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.davisys.dao.UserDAO;
import com.davisys.entity.Users;
import com.davisys.service.SessionService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class UserController {
	@Autowired
	UserDAO userDao;
	@Autowired
	HttpServletRequest request;
	@Autowired
	SessionService sessionService;
	String alert = "";
	String daviUser = "https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/daviuser.png?alt=media&token=f04558c5-927c-46b5-a7ab-379411e4a3e20";

	@GetMapping("/tablesusers")
	public String tablesusers(Model m) {
		loadDataUser(m);
		m.addAttribute("user", sessionService.get("user"));
		m.addAttribute("activeu", "active");
		return "admin/tablesusers";
	}
	

	public void loadDataUser(Model model) {
		List<Users> listUser = userDao.findAll();
		model.addAttribute("listUser", listUser);
	}

	@RequestMapping("/editUsers/{userid}")
	public String editUser(Model m, @PathVariable("userid") int userid) {
		m.addAttribute("user", sessionService.get("user"));
		Users user = userDao.findIdUsers(userid);
		String role = userDao.findRoleUser(userid);
		m.addAttribute("formuser", user);
		m.addAttribute("userRole", role);
		m.addAttribute("alert", alert);
		alert = "";
		return "admin/formusers";
	}

	@PostMapping("/updateUsers/{userid}")
	public String updateUser(Model m, @PathVariable("userid") int userid) {
		try {
			alert = "updateSuccess";
			Users user = userDao.findIdUsers(userid);
			String full_name = request.getParameter("full_name");
			String gender = request.getParameter("gender");
			String phone = request.getParameter("phone");
			String user_birtday = request.getParameter("user_birtday");
			String user_dayjoin = request.getParameter("user_dayjoin");
			String pattern = "yyyy-MM-dd";

			user.setUserid(userid);
			user.setFull_name(full_name);
			user.setGender(gender);
			user.setPhone(phone);

			DateFormat dateFormat = new SimpleDateFormat(pattern);
			try {
				Date birthdayDate = dateFormat.parse(user_birtday);
				user.setUser_birtday(birthdayDate);
				Date dayjoinDate = dateFormat.parse(user_dayjoin);
				user.setUser_dayjoin(dayjoinDate);
			} catch (Exception e) {
				alert = "updateFail";
				e.printStackTrace();
			}

			userDao.saveAndFlush(user);
			return "redirect:admin/editUsers/{userid}";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			alert = "updateFail";
			throw e;
		}

	}

	@PostMapping("/disableUsers/{userid}")
	public String disableUser(Model m, @PathVariable("userid") int userid) {
		try {
			alert = "disableSuccess";
			Users user = userDao.findIdUsers(userid);
			user.setAccount_status(false);

			userDao.saveAndFlush(user);
			return "redirect:/admin/editUsers/{userid}";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			alert = "disableFail";
			throw e;
		}

	}

	@PostMapping("/activeUsers/{userid}")
	public String activeUsers(Model m, @PathVariable("userid") int userid) {
		try {
			alert = "activeSuccess";
			Users user = userDao.findIdUsers(userid);
			user.setAccount_status(true);

			userDao.saveAndFlush(user);
			return "redirect:/admin/editUsers/{userid}";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			alert = "activeFail";
			throw e;
		}

	}


}
