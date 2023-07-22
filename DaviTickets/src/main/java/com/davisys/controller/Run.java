package com.davisys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.davisys.dao.MovieDAO;
import com.davisys.entity.Movie;

@Controller
public class Run {
	@Autowired
	MovieDAO moviedao;
	
	@GetMapping("/")
	public String index(Model m) {
		return "admin/index";
	}

	@GetMapping("/movie")
	public String movie(Model m) {
		return "admin/formmovie";
	}

	@GetMapping("/users")
	public String users(Model m) {
		return "admin/formusers";
	}

	@GetMapping("/showtime")
	public String showtime(Model m) {
		return "admin/showtime";
	}

	@GetMapping("/tablesmovie")
	public String tablesmovie(Model m) {
		testLoadData(m);
		return "admin/tablesmovie";
	}

	@GetMapping("/tablesusers")
	public String tablesusers(Model m) {
		return "admin/tablesusers";
	}

	@GetMapping("/error")
	public String error(Model m) {
		return "error";
	}
	
	public void testLoadData(Model model) {
		List<Movie> listMovie = moviedao.findAll();
		model.addAttribute("listMovie", listMovie);
 	}
}
