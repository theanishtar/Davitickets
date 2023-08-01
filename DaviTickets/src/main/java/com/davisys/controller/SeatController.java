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

import com.davisys.dao.SeatDAO;
import com.davisys.dao.UserDAO;
import com.davisys.entity.Seat;
import com.davisys.entity.Users;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SeatController {
	@Autowired
	SeatDAO seatDao;
	@Autowired
	HttpServletRequest request;

	String update = "";
	String disable = "";
	String daviSeat = "https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/daviseat.png?alt=media&token=f04558c5-927c-46b5-a7ab-379411e4a3e20";

	@GetMapping("/tableseat")
	public String tableseat(Model m) {
		testLoadData(m);
		m.addAttribute("activeu", "active");
		return "admin/tableseat";
	}

	public void testLoadData(Model model) {
		List<Seat> listSeat = seatDao.findAll();
		model.addAttribute("listSeat", listSeat);
	}

	@RequestMapping("/editSeat/{seat_id}")
	public String editSeat(Model m, @PathVariable("seat_id") int seat_id) {
		Seat seat = seatDao.findIdSeats(seat_id);
		m.addAttribute("formseat", seat);
		return "admin/formseat";
	}

	@PostMapping("/updateSeat/{seat_id}")
	public String updateSeat(Model m, @PathVariable("seat_id") int seat_id) {
		try {
			update = "true";
			Seat seat = seatDao.findIdSeats(seat_id);
			String vip = request.getParameter("vip");
			String row_symbol = request.getParameter("row_symbol");
			String seat_num = request.getParameter("seat_num");

			seat.setSeat_id(seat_id);
			seat.setVip(Boolean.valueOf(vip));
			seat.setRow_symbol(row_symbol);
			seat.setSeat_num(Integer.valueOf(seat_num));

			seatDao.saveAndFlush(seat);
			return "redirect:/editSeat/{seat_id}";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			update = "false";
			throw e;
		}

	}

	@PostMapping("/disableSeat/{seat_id}")
	public String disableSeat(Model m, @PathVariable("seat_id") int seat_id) {
		try {
			disable = "true";
			Seat seat = seatDao.findIdSeats(seat_id);
			seat.setAvailable(false);

			seatDao.saveAndFlush(seat);
			return "redirect:/editSeat/{seat_id}";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			disable = "false";
			throw e;
		}

	}
	
	@PostMapping("/activeSeat/{seat_id}")
	public String activeSeat(Model m, @PathVariable("seat_id") int seat_id) {
		try {
			disable = "true";
			Seat seat = seatDao.findIdSeats(seat_id);
			seat.setAvailable(true);

			seatDao.saveAndFlush(seat);
			return "redirect:/editSeat/{seat_id}";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			disable = "false";
			throw e;
		}

	}


}
