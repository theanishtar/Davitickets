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
import com.davisys.dao.RoomDAO;
import com.davisys.dao.ShowtimeDAO;
import com.davisys.entity.Movie;
import com.davisys.entity.Room;
import com.davisys.entity.Showtime;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ShowtimeController {
	@Autowired
	ShowtimeDAO showtimeDAO;
	@Autowired
	MovieDAO movieDAO;
	@Autowired
	RoomDAO roomDAO;
	@Autowired
	HttpServletRequest request;
	
	String editORadd = "add";
	String update = "";
	String create = "";
	String imgDavi = "https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/davi.png?alt=media&token=c3f7e0b9-b0a4-481f-8929-a47e02c4ed21";

	@GetMapping("/showtime")
	public String showtime(Model m) {
		List<Movie> listMovie = movieDAO.findAll();
		List<Room> listRoom = roomDAO.findAll();
		editORadd = "add";
		m.addAttribute("editORadd", editORadd);
		
		Showtime showtime = new Showtime();
		Movie movie = new Movie();
		movie.setPoster(imgDavi);
		showtime.setMovie(movie);
		if(create == "true") {
			m.addAttribute("alertCreate", "true");
		}
		if(create == "false") {
			m.addAttribute("alertCreate", "false");
		}
		create = "";
		
		m.addAttribute("listMovie", listMovie);
		m.addAttribute("listRoom", listRoom);
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
		List<Showtime> listShowtime = showtimeDAO.findAll();
		m.addAttribute("listShowtime", listShowtime);
	}
	
	
	@RequestMapping("/editshowtime/{showtime_id}")
	public String editMovie(Model m, @PathVariable("showtime_id") int showtime_id) {
		editORadd = "edit";
		m.addAttribute("editORadd", editORadd);
		List<Movie> listMovie = movieDAO.findAll();
		List<Room> listRoom = roomDAO.findAll();
		List<Showtime> listShowtimeNotUse = showtimeDAO.getShowtimeNotUse();
		Showtime showtime = showtimeDAO.findIdShowtime(showtime_id);
		m.addAttribute("listMovie", listMovie);
		m.addAttribute("listRoom", listRoom);
		m.addAttribute("listShowtimeNotUse", listShowtimeNotUse);
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
			Showtime showtime = showtimeDAO.findIdShowtime(showtime_id);
			String movie_id = request.getParameter("movie.movie_id");
			String room_id = request.getParameter("room.room_id");
			Room room = roomDAO.findIdRoom(Integer.valueOf(room_id));
			Movie movie = movieDAO.findIdMovie(Integer.valueOf(movie_id));
			showtime.setShowtime_id(showtime_id);
			showtime.setRoom(room);
			showtime.setMovie(movie);
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
			showtimeDAO.saveAndFlush(showtime);
			
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
			String room_id = request.getParameter("room.room_id");
			Movie movie = movieDAO.findIdMovie(Integer.valueOf(movie_id));
			Room room = roomDAO.findIdRoom(Integer.valueOf(room_id));
			showtime.setMovie(movie);
			showtime.setRoom(room);
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
			
			showtimeDAO.saveAndFlush(showtime);
			
			return "redirect:/showtime";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			create = "false";
			throw e;
		}

	}
	
	@PostMapping("/deleteShowtime/{showtime_id}")
	public String deleteMovie(Model m, @PathVariable("showtime_id") int showtime_id) {
		Showtime showtime = showtimeDAO.findIdShowtime(showtime_id);
		showtimeDAO.delete(showtime);
		return "redirect:/tablesshowtime";
	}

}
