package com.davisys.controller;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
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
import com.davisys.service.SessionService;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
@RolesAllowed("ROLE_ADMIN")
public class ShowtimeController {
	@Autowired
	ShowtimeDAO showtimeDAO;
	@Autowired
	MovieDAO movieDAO;
	@Autowired
	RoomDAO roomDAO;
	@Autowired
	HttpServletRequest request;
	@Autowired
	SessionService sessionService;
	String editORcreate = "create";
	String alert = "";
	String imgDavi = "https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/davi.png?alt=media&token=c3f7e0b9-b0a4-481f-8929-a47e02c4ed21";

	@GetMapping("/tablesshowtime")
	public String tablesshowtime(Model m) {
		System.out.println("start tablesshowtime");
		loadDataShowtime(m);
		m.addAttribute("alert", alert);
		alert = "";
		m.addAttribute("actives", "active");
		return "admin/tablesshowtime";
	}

	public void loadDataShowtime(Model m) {
		List<Showtime> listShowtime = showtimeDAO.findAll();
		m.addAttribute("listShowtime", listShowtime);
	}

	@GetMapping("/showtime")
	public String showtime(Model m) throws ParseException {
		editORcreate = "create";
		m.addAttribute("editORcreate", editORcreate);
		List<Movie> listMovie = movieDAO.findAll();
		List<Room> listRoom = roomDAO.findAll();
		Showtime showtime = new Showtime();
		Movie movie = new Movie();
		movie.setPoster(listMovie.get(0).getPoster());
		showtime.setMovie(movie);
		showtime.setPrice(0000);
		m.addAttribute("alert", alert);
		alert = "";
		m.addAttribute("listMovie", listMovie);
		m.addAttribute("listRoom", listRoom);
		m.addAttribute("forms", showtime);
		m.addAttribute("actives", "active");
		return "admin/formshowtime";
	}

	@PostMapping("/clearShowtime")
	public String clearShowtime(Model m) {
		return "redirect:/admin/showtime";
	}

	@RequestMapping("/editshowtime/{showtime_id}")
	public String editMovie(Model m, @PathVariable("showtime_id") int showtime_id) {
		editORcreate = "edit";
		m.addAttribute("editORcreate", editORcreate);
		List<Movie> listMovie = movieDAO.findAll();
		List<Room> listRoom = roomDAO.findAll();
		List<Showtime> listShowtimeNotUse = showtimeDAO.getShowtimeNotUse();
		Showtime showtime = showtimeDAO.findIdShowtime(showtime_id);
		m.addAttribute("listMovie", listMovie);
		m.addAttribute("listRoom", listRoom);
		m.addAttribute("listShowtimeNotUse", listShowtimeNotUse);
		m.addAttribute("forms", showtime);
		m.addAttribute("alert", alert);
		alert = "";
		return "admin/formshowtime";
	}

	@PostMapping("/updateShowtime/{showtime_id}")
	public String updateShowtime(Model m, @PathVariable("showtime_id") int showtime_id) {
		try {
			alert = "updateSuccess";
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
			String patternTime = "yyyy-MM-dd HH:mm";
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			DateFormat timeFormat = new SimpleDateFormat(patternTime);
			try {
				Date date = dateFormat.parse(showdate);
				String start_time = showdate + " " + request.getParameter("start_time");
				String end_time = showdate + " " + request.getParameter("end_time"); 
				Date dateStart = timeFormat.parse(start_time);
				Date dateEnd = timeFormat.parse(end_time);
				showtime.setShowdate(date);
				showtime.setStart_time(dateStart);
				showtime.setEnd_time(dateEnd);
			} catch (Exception e) {
				alert = "updateFail";
				e.printStackTrace();
				System.out.println("time");
			}
			String price = request.getParameter("price");
			showtime.setPrice(Integer.valueOf(price));
			showtimeDAO.saveAndFlush(showtime);

			return "redirect:/admin/editshowtime/{showtime_id}";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			alert = "updateFail";
			throw e;
		}

	}

	@PostMapping("/createShowtime")
	public String createShowtime(Model m) throws Exception {
		try {
			alert = "createSuccess";
			Showtime showtime = new Showtime();
			String movie_id = request.getParameter("movie.movie_id");
			String room_id = request.getParameter("room.room_id");
			Movie movie = movieDAO.findIdMovie(Integer.valueOf(movie_id));
			Room room = roomDAO.findIdRoom(Integer.valueOf(room_id));
			showtime.setMovie(movie);
			showtime.setRoom(room);
			//cap nhat ngay
			String showdate = request.getParameter("showdate");
			String pattern = "yyyy-MM-dd";
			String patternTime = "yyyy-MM-dd HH:mm";
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			DateFormat timeFormat = new SimpleDateFormat(patternTime);
			try {
				Date date = dateFormat.parse(showdate);
				String start_time = showdate + " " + request.getParameter("start_time");
				String end_time = showdate + " " + request.getParameter("end_time"); 
				Date dateStart = timeFormat.parse(start_time);
				Date dateEnd = timeFormat.parse(end_time);
				showtime.setShowdate(date);
				showtime.setStart_time(dateStart);
				showtime.setEnd_time(dateEnd);
			} catch (Exception e) {
				alert = "updateFail";
				e.printStackTrace();
				System.out.println("time");
			}
			String price = request.getParameter("price");
			showtime.setPrice(Integer.valueOf(price));
			
			//get compare hour
			String start_time = showdate + " " + request.getParameter("start_time");
			Date dateStart = timeFormat.parse(start_time);
			int startTimeHour = dateStart.getHours();
			int resultCompareHour = 0;
			
			//get compare date
			Date dateAdd = dateFormat.parse(showdate);
			//0 là chưa tồn tại ngày đó 
			int resultCompareDay = 0;
			
			List<Showtime> showtimeInRoom = showtimeDAO.findIdShowtimeByIdRoom(Integer.valueOf(room_id));
			for (Showtime showtimeChild : showtimeInRoom) {
				int showtimeHour = showtimeChild.getStart_time().getHours();
				if(startTimeHour == showtimeHour) {
					resultCompareHour = 1;
				}
				int result = dateAdd.compareTo(showtimeChild.getShowdate());
				if(result == 0) {
					resultCompareDay = 1;
				}
			}
			if(resultCompareHour == 1 && resultCompareDay == 1) {
				alert = "createSameDayAndHour";
				return "redirect:/admin/showtime";
			}else {
				showtimeDAO.saveAndFlush(showtime);
			}
			return "redirect:/admin/tablesshowtime";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			alert = "createFail";
			throw e;
		}

	}

	@PostMapping("/deleteShowtime/{showtime_id}")
	public String deleteMovie(Model m, @PathVariable("showtime_id") int showtime_id) {
		try {
			alert = "deleteSuccess";
			Showtime showtime = showtimeDAO.findIdShowtime(showtime_id);
			showtimeDAO.delete(showtime);
		} catch (Exception e) {
			System.out.println("errr: " + e);
			alert = "deleteFail";
		}
		return "redirect:/admin/tablesshowtime";
	}

}
