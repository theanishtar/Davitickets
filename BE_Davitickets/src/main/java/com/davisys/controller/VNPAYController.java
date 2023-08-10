package com.davisys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.davisys.config.VNPAYConfig;
import com.davisys.controller.rest.MovieRescontroller;
import com.davisys.dao.BookingDAO;
import com.davisys.dao.PaymentDAO;
import com.davisys.dao.RoomDAO;
import com.davisys.dao.SeatDAO;
import com.davisys.dao.ShowtimeDAO;
import com.davisys.dao.UserDAO;
import com.davisys.entity.Booking;
import com.davisys.entity.BookingPayment;
import com.davisys.entity.Payment;
import com.davisys.entity.Seat;
import com.davisys.entity.Showtime;
import com.davisys.entity.Users;
import com.davisys.service.EmailService;
import com.davisys.service.MailerServiceImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
public class VNPAYController {
	@Autowired
	HttpServletRequest request;
	@Autowired
	private ShowtimeDAO showtimeDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private BookingDAO bookingDAO;
	@Autowired
	private SeatDAO seatDAO;
	@Autowired
	private RoomDAO roomDAO;
	@Autowired
	private PaymentDAO paymentDAO;
	@Autowired
	MailerServiceImpl mailer;
	@Autowired
	EmailService emailService;

	public int orderReturn(HttpServletRequest request) {
		BookingPayment bookingPayment = MovieRescontroller.bookingPayment;
		Map fields = new HashMap();
		for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
			String fieldName = null;
			String fieldValue = null;
			try {
				fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
				fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				fields.put(fieldName, fieldValue);
			}
		}

		String vnp_SecureHash = request.getParameter("vnp_SecureHash");
		if (fields.containsKey("vnp_SecureHashType")) {
			fields.remove("vnp_SecureHashType");
		}
		if (fields.containsKey("vnp_SecureHash")) {
			fields.remove("vnp_SecureHash");
		}
		String signValue = VNPAYConfig.hashAllFields(fields);
		if (signValue.equals(vnp_SecureHash)) {
			if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
				try {
					int showtimeId = Integer.valueOf(bookingPayment.getShowtimeId());
					Users user = userDAO.findEmailUser(bookingPayment.getUserEmail());
					Showtime showtime = showtimeDAO.findIdShowtime(showtimeId);
					String[] seats = bookingPayment.getSeat().split(",");
					int tickets = 0;
					for (String s : seats) {
						Booking booking = new Booking();
						booking.setShowtime(showtime);
						booking.setUsers(user);
						booking.setBooking_date(new Date());
						String sym_bol = s.substring(0, 1);
						int num = Integer.valueOf(s.substring(1, s.length()).trim());
						Seat seat = seatDAO.findIdSeatandsymbol(sym_bol, num);
						booking.setSeat(seat);
						booking.setBooking_id(null);
						bookingDAO.save(booking);
						Payment payment = new Payment();
						payment.setBooking(booking);
						payment.setPayment_date(new Date());
						int total = total = showtime.getPrice();
						if (seat.getVip() == true) {
							total = showtime.getPrice() + 10000;
						}
//						payment.setTotal_amount(total);
						payment.setTotal_amount(total);
						paymentDAO.save(payment);
						tickets++;
					}
					emailService.sendEmailWithImage(bookingPayment.getQrCode(), user, showtime,
							bookingPayment.getSeat(),tickets,Integer.valueOf(bookingPayment.getMoney()));

				} catch (Exception e) {
					System.out.println("error 1: " + e);
				}

				return 1;
			} else {
				return 0;
			}
		} else {
			return -1;
		}
	}

	@GetMapping("/vnpay-payment")
	public String GetMapping(HttpServletRequest request, Model model) {
		int paymentStatus = orderReturn(request);

		String orderInfo = request.getParameter("vnp_OrderInfo");
		String paymentTime = request.getParameter("vnp_PayDate");
		String transactionId = request.getParameter("vnp_TransactionNo");
		String totalPrice = request.getParameter("vnp_Amount");

		model.addAttribute("orderId", orderInfo);
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("paymentTime", paymentTime);
		model.addAttribute("transactionId", transactionId);

		return paymentStatus == 1 ? "ordersuccess" : "orderfail";
	}

}
