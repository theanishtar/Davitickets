package com.davisys.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
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
import com.davisys.entity.Movie;
import com.davisys.entity.Showtime;
import com.davisys.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.servlet.http.HttpServletRequest;
import lombok.Data;

@Data
@Controller
public class UserController {
	@Autowired
	UserDAO userDao;
	@Autowired
	HttpServletRequest request;
	
	
	String editUser = "add";
	String update = "";
	String disable = "";
	
	public static String renameFile(String fileName, String id) {
		return FilenameUtils.getBaseName(id) + ".png";
	}
	
	@GetMapping("/user")
	public String user(Model m) {
		editUser="add";
		m.addAttribute("editUser", editUser);
		m.addAttribute("activem", "active");
		Users user = new Users();
		if(disable == "true") {
			m.addAttribute("alertDisable", "true");
		}
		if(disable == "false") {
			m.addAttribute("alertDisable", "false");
		}
		disable = "";
		
		//user.setPoster("/img/user.png");
		m.addAttribute("formuser", user);
		return "admin/formuser";
	}

 
	@GetMapping("/tablesusers")
	public String tablesusers(Model m) {
		testLoadData(m);
		m.addAttribute("activeu","active");
		return "admin/tablesusers";
	}
	@RequestMapping("/editUsers/{userid}")
	public String editUser(Model m, @PathVariable("userid") int userid) {
		Users user = userDao.findIdUsers(userid);
		m.addAttribute("formuser", user);
		if(update == "true") {
			m.addAttribute("alertUpdate", "true");
		}if(update == "false") {
			m.addAttribute("alertUpdate", "false");
		}
		if(disable == "true") {
			m.addAttribute("alertDisable", "true");
		}if(disable == "false") {
			m.addAttribute("alertDisable", "false");
		}
		update = "";
		disable = "";
		return "admin/formusers";
	}
	@PostMapping("/updateUsers/{userid}")
	public String updateUser(Model m, @RequestParam("file") MultipartFile file,
			@PathVariable("userid") int userid) {
		try {
			update = "true";
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
				update = "false";
				e.printStackTrace();
			}

			
			userDao.saveAndFlush(user);
			return "redirect:/editUsers/{userid}";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			update = "false";
			throw e;
		}

	}
	@PostMapping("/disableUsers/{userid}")
	public String disableUser(Model m, @RequestParam("file") MultipartFile file,
			@PathVariable("userid") int userid) {
		try {
			disable = "true";
			Users user = userDao.findIdUsers(userid);
			user.setAccount_status(false);
			
			userDao.saveAndFlush(user);
			return "redirect:/editUsers/{userid}";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			disable = "false";
			throw e;
		}

	}
	public void testLoadData(Model model) {
		List<Users> listUser = userDao.findAll();
		model.addAttribute("listUser",listUser );
	}
	
}
