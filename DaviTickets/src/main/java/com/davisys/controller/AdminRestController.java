package com.davisys.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.dao.MovieDAO;
import com.davisys.dao.PaymentDAO;
import com.davisys.dao.ShowtimeDAO;
import com.davisys.entity.Movie;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class AdminRestController {
	@Autowired
	PaymentDAO paymentDAO;
	@Autowired
	ShowtimeDAO showtimeDAO;
	@Autowired
	HttpServletRequest request;
	@Autowired
	MovieDAO movieDAO;

	
	
	@GetMapping("/loadImg")
	public String getImg() {
		String cbtId = request.getParameter("cbt");
		Movie movie = movieDAO.findIdMovie(Integer.valueOf(cbtId));
		return movie.getPoster();
		
	}
	
	@GetMapping("/getTotalMonth")
	public List<Integer> totalPostAllMonth(Model model) {
		List<Object[]> listEarnings = paymentDAO.getlistEarnings();
		List<Integer> listAll = new ArrayList<>();
		for(Object[] oj: listEarnings) {
			listAll.add(Integer.valueOf(String.valueOf(oj[1])));
		}
		
		return listAll;
	}
	
	@GetMapping("/getTop3Hour")
	public List<Object[]> top3Hour(Model model) {
		List<Object[]> listTop3 = showtimeDAO.getTop3GoldenHour();
		List<Object[]> listCombine = new ArrayList<>();
		float sum = 0;
		for(Object[] oj: listTop3) {
			sum = sum + Float.valueOf(String.valueOf(oj[1]));
		}
		for(Object[] oj: listTop3) {
			float percent = (Float.valueOf(String.valueOf(oj[1])) / sum) * 100;
			String name = String.valueOf(oj[0]);
			listCombine.add(new Object[] { name, percent});
		}
		return listCombine;
	}
	
		@RequestMapping("/logout")
		public String logout(Model m) {
//				sessionService.set("user", null);
			return "redirect:/index";
		}

}
