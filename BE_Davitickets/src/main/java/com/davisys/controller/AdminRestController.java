package com.davisys.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Printer;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.dao.MovieDAO;
import com.davisys.dao.PaymentDAO;
import com.davisys.dao.SeatDAO;
import com.davisys.dao.ShowtimeDAO;
import com.davisys.entity.Movie;
import com.davisys.entity.Seat;
import com.davisys.entity.Showtime;
import com.davisys.service.SessionService;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin
@RolesAllowed("ROLE_ADMIN")
public class AdminRestController {
	@Autowired
	PaymentDAO paymentDAO;
	@Autowired
	ShowtimeDAO showtimeDAO;
	@Autowired
	HttpServletRequest request;
	@Autowired
	MovieDAO movieDAO;
	@Autowired
	SeatDAO seatDao;
	@Autowired
	SessionService sessionService;
	@Autowired
	HttpServletResponse response;

	@GetMapping("/loadImg")
	public String getImg(Model m) {
		String cbtId = request.getParameter("cbt");
		Movie movie = movieDAO.findIdMovie(Integer.valueOf(cbtId));
		return movie.getPoster();

	}

	@GetMapping("/loadSeat")
	public void getSeat(Model m) {
		String cbtId = request.getParameter("cbt");
		System.out.println("yo");
		List<Seat> listSeat = seatDao.getListSeatByRoom(Integer.valueOf(cbtId));
		try {
			PrintWriter out = response.getWriter();
			out.print("<div class=\"row screen\"></div>");
			out.print("<div class=\"row\">");
			for (Seat seat : listSeat) {
				if(seat.getRow_symbol().equals("A")) {
					setStyle(out, seat);
				}	
			}
			out.print("</div>");
			out.print("<div class=\"row\">");
			for (Seat seat : listSeat) {
				if(seat.getRow_symbol().equals("B")) {
					setStyle(out, seat);
				}	
			}
			out.print("</div>");
			out.print("<div class=\"row\">");
			for (Seat seat : listSeat) {
				if(seat.getRow_symbol().equals("C")) {
					setStyle(out, seat);
				}	
			}
			out.print("</div>");
			out.print("<div class=\"row\">");
			for (Seat seat : listSeat) {
				if(seat.getRow_symbol().equals("D")) {
					setStyle(out, seat);
				}	
			}
			out.print("</div>");
			out.print("<div class=\"row\">");
			for (Seat seat : listSeat) {
				if(seat.getRow_symbol().equals("E")) {
					setStyle(out, seat);
				}	
			}
			out.print("</div>");
			out.print("<div class=\"row\">");
			for (Seat seat : listSeat) {
				if(seat.getRow_symbol().equals("F")) {
					setStyle(out, seat);
				}	
			}
			out.print("</div>");
			out.print("<div class=\"row\">");
			for (Seat seat : listSeat) {
				if(seat.getRow_symbol().equals("G")) {
					setStyle(out, seat);
				}	
			}
			out.print("</div>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setStyle(PrintWriter out ,Seat seat) {
		if(seat.getSeat_num() == 2 || seat.getSeat_num() == 9) {
			checkMargin(out, seat, "yes");
		}else {
			checkMargin(out, seat, "no");
		}
	}
	
	public void checkMargin(PrintWriter out ,Seat seat, String margin) {
		String style = "";
		if(margin.equals("yes")) {
			style = "style=\"margin-right: 18px;'\"";
		}
		if(seat.getVip() == true) {
			if(seat.getAvailable() == false) {
				out.print("<div>\r\n"
						+ "		<div class=\"seat vip notselected\" "+style+" >\r\n"
						+ "			<a href=\"/admin/editSeat/"+seat.getSeat_id()+"\">"+seat.getRow_symbol()+seat.getSeat_num()+"</a>\r\n"
						+ "		</div>\r\n"
						+ "</div>");
			}else {
				out.print("<div>\r\n"
						+ "		<div class=\"seat vip\" "+style+"  >\r\n"
						+ "			<a href=\"/admin/editSeat/"+seat.getSeat_id()+"\">"+seat.getRow_symbol()+seat.getSeat_num()+"</a>\r\n"
						+ "		</div>\r\n"
						+ "</div>");
			}
		}else {
			if(seat.getAvailable() == false) {
				out.print("<div>\r\n"
						+ "		<div class=\"seat notselected\" "+style+"  >\r\n"
						+ "			<a href=\"/admin/editSeat/"+seat.getSeat_id()+"\">"+seat.getRow_symbol()+seat.getSeat_num()+"</a>\r\n"
						+ "		</div>\r\n"
						+ "</div>");
			}else {
				out.print("<div>\r\n"
						+ "		<div class=\"seat\" "+style+"  >\r\n"
						+ "			<a href=\"/admin/editSeat/"+seat.getSeat_id()+"\">"+seat.getRow_symbol()+seat.getSeat_num()+"</a>\r\n"
						+ "		</div>\r\n"
						+ "</div>");
			}
		}
	}

	@GetMapping("/getTotalMonth")
	public List<Integer> totalPostAllMonth(Model model) {
		List<Object[]> listEarnings = paymentDAO.getlistEarnings();
		List<Integer> listAll = new ArrayList<>();
		for (Object[] oj : listEarnings) {
			listAll.add(Integer.valueOf(String.valueOf(oj[1])));
		}

		return listAll;
	}

	@GetMapping("/getTop3Hour")
	public List<Object[]> top3Hour(Model model) {
		List<Object[]> listTop3 = showtimeDAO.getTop3GoldenHour();
		List<Object[]> listCombine = new ArrayList<>();
		float sum = 0;
		for (Object[] oj : listTop3) {
			sum = sum + Float.valueOf(String.valueOf(oj[1]));
		}
		for (Object[] oj : listTop3) {
			float percent = (Float.valueOf(String.valueOf(oj[1])) / sum) * 100;
			String name = String.valueOf(oj[0]);
			listCombine.add(new Object[] { name, percent });
		}
		return listCombine;
	}

}
