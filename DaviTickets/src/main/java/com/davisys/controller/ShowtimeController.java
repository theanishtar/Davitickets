package com.davisys.controller;

import java.sql.Time;
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

import com.davisys.dao.MovieDAO;
import com.davisys.dao.ShowtimeDAO;
import com.davisys.entity.Movie;
import com.davisys.entity.Showtime;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShowtimeController {
	@Autowired
	ShowtimeDAO showtimeDao;
	@Autowired
	MovieDAO movieDao;
	@Autowired
	HttpServletRequest request;
	
	String editORadd = "add";
	String update = "";
	String create = "";

	@GetMapping("/showtime")
	public String showtime(Model m) {
		List<Movie> listMovie = movieDao.findAll();
		editORadd = "add";
		m.addAttribute("editORadd", editORadd);
		
		Showtime showtime = new Showtime();
		if(create == "true") {
			m.addAttribute("alertCreate", "true");
		}
		if(create == "false") {
			m.addAttribute("alertCreate", "false");
		}
		create = "";
		m.addAttribute("listMovie", listMovie);
		m.addAttribute("forms", showtime);
		m.addAttribute("actives", "active");
		return "admin/formshowtime";
	}

	@GetMapping("/tablesshowtime")
	public String tablesshowtime(Model m) {
		loadDataShowtime(m);
		m.addAttribute("actives", "active");
		return "admin/tablesshowtime";
	}

	public void loadDataShowtime(Model m) {
		List<Showtime> listShowtime = showtimeDao.findAll();
		m.addAttribute("listShowtime", listShowtime);
	}
	
	
	@RequestMapping("/editshowtime/{showtime_id}")
	public String editMovie(Model m, @PathVariable("showtime_id") int showtime_id) {
		editORadd = "edit";
		m.addAttribute("editORadd", editORadd);
		List<Movie> listMovie = movieDao.findAll();
		Showtime showtime = showtimeDao.findIdShowtime(showtime_id);
		m.addAttribute("listMovie", listMovie);
		m.addAttribute("forms", showtime);
		if(update == "true") {
			m.addAttribute("alertUpdate", "true");
		}if(update == "false") {
			m.addAttribute("alertUpdate", "false");
		}
		update = "";
		return "admin/formshowtime";
	}

	@PostMapping("/updateShowtime/{showtime_id}")
	public String updateShowtime(Model m, @PathVariable("showtime_id") int showtime_id) {
		try {
			update = "true";
			Showtime showtime = showtimeDao.findIdShowtime(showtime_id);
			String movie_id = request.getParameter("movie.movie_id");
			Movie movie = movieDao.findIdMovie(Integer.valueOf(movie_id));
			;
			showtime.setMovie(movie);
			showtime.setShowtime_id(showtime_id);

			// cap nhat ngay
			String showdate = request.getParameter("showdate");
			String pattern = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			try {
				Date date = dateFormat.parse(showdate);
				showtime.setShowdate(date);
			} catch (Exception e) {
//				System.out.println(e + " loi date");
				update = "false";
			}

			// cap nhat gio
			String start_time = request.getParameter("start_time");
			String end_time = request.getParameter("end_time");
			try {
				showtime.setStart_time(Time.valueOf(start_time));
				showtime.setEnd_time(Time.valueOf(end_time));
			} catch (Exception e) {
//				System.out.println(e + " loi time");
				update = "false";
			}
			String price = request.getParameter("price");
			showtime.setPrice(Integer.valueOf(price));
			showtimeDao.saveAndFlush(showtime);
			
			return "redirect:/editshowtime/{showtime_id}";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			update = "false";
			throw e;
		}

	}
	
	@PostMapping("/createShowtime")
	public String createShowtime(Model m) {
		try {
			create = "true";
			Showtime showtime = new Showtime();
			String movie_id = request.getParameter("movie.movie_id");
			Movie movie = movieDao.findIdMovie(Integer.valueOf(movie_id));
			showtime.setMovie(movie);

			// cap nhat ngay
			String showdate = request.getParameter("showdate");
			String pattern = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			try {
				Date date = dateFormat.parse(showdate);
				showtime.setShowdate(date);
			} catch (Exception e) {
				System.out.println(e + " loi date");
				create = "false";
			}

			// cap nhat gio
			String start_time = request.getParameter("start_time");
			String end_time = request.getParameter("end_time");
			try {
				showtime.setStart_time(Time.valueOf(start_time));
				showtime.setEnd_time(Time.valueOf(end_time));
			} catch (Exception e) {
				System.out.println(e + " loi time");
				create = "false";
			}
			
			String price = request.getParameter("price");
			showtime.setPrice(Integer.valueOf(price));
			
			showtimeDao.saveAndFlush(showtime);
			
			return "redirect:/showtime";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			create = "false";
			throw e;
		}

	}

}
