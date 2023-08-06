package com.davisys.controller.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.config.JwtTokenUtil;
import com.davisys.config.VNPAYConfig;
import com.davisys.dao.BookingDAO;
import com.davisys.dao.MovieDAO;
import com.davisys.dao.PaymentDAO;
import com.davisys.dao.RoomDAO;
import com.davisys.dao.SeatDAO;
import com.davisys.dao.ShowtimeDAO;
import com.davisys.dao.UserDAO;
import com.davisys.entity.Booking;
import com.davisys.entity.BookingPayment;
import com.davisys.entity.DataBooking;
import com.davisys.entity.DataBookingDetail;
import com.davisys.entity.DataPayment;
import com.davisys.entity.Movie;
import com.davisys.entity.MovieTemp;
import com.davisys.entity.Payment;
import com.davisys.entity.Room;
import com.davisys.entity.Seat;
import com.davisys.entity.Showtime;
import com.davisys.entity.Users;
import com.davisys.service.QrCodeGeneratorService;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

@RestController
@CrossOrigin
public class MovieRescontroller {
	@Autowired
	private MovieDAO movieDAO;
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
	private QrCodeGeneratorService qrCodeGeneratorService;
	@Autowired
	HttpServletRequest request;
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	public static BookingPayment bookingPayment = new BookingPayment();

	@GetMapping("/movie/loadMovie/{showDate}")
	public ResponseEntity<List<MovieTemp>> loadMovie(@PathVariable String showDate) {
		try {
			List<Showtime> showtimes = showtimeDAO.loadTimeMovie(showDate);

			List<MovieTemp> listtemp = new ArrayList<>();
			Set<Integer> key = new HashSet<>();
			for (Showtime sh : showtimes) {
				if (!key.contains(sh.getMovie().getMovie_id())) {
					MovieTemp movieTemp = new MovieTemp();
//					List<Date> dateTime = new ArrayList<>();

//					HashMap<String, Integer>dateTime=new HashMap<>();
//					Object[] ob = new Object[2];
					List<Object[]> dateTime = new ArrayList<>();
//					System.out.println("id: "+sh.getMovie().getMovie_id());
					key.add(sh.getMovie().getMovie_id());
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
					String st_time;
					List<Showtime> showtimetemp = showtimeDAO.loadTimeMovieId(showDate, sh.getMovie().getMovie_id());
//					System.out.println(showtimetemp.size());
					if (showtimetemp.size() > 1) {
						int i = 0;
						for (Showtime showtime : showtimetemp) {
//							dateTime.put(String.valueOf(showtime.getStart_time()), showtime.getShowtime_id());
							dateTime.add(new Object[] { String.valueOf(st_time = sdf.format(showtime.getStart_time())),
									showtime.getShowtime_id() });
						}

					} else if (showtimetemp.size() == 1) {
						st_time = sdf.format(sh.getStart_time());

						dateTime.add(new Object[] { String.valueOf(st_time), sh.getShowtime_id() });

					}

					movieTemp.setMovie_id(sh.getMovie().getMovie_id());
					movieTemp.setTile_movie(sh.getMovie().getTitle_movie());
					movieTemp.setPoster(sh.getMovie().getPoster());
					movieTemp.setDuration(sh.getMovie().getDuration());
					movieTemp.setShow_date(sh.getShowdate());
					movieTemp.setStart_time(dateTime);
					movieTemp.setShowtime_id(sh.getShowtime_id());
					listtemp.add(movieTemp);
				}

			}
			System.out.println("key: " + key);

			return ResponseEntity.status(200).body(listtemp);
		} catch (Exception e) {
			System.out.println("Error loadMovie: " + e);
			throw e;
		}
	}

	@PostMapping("/movie/loadData")
	public ResponseEntity<DataBookingDetail> loadDataBooking(HttpServletRequest request,
			@RequestBody DataBooking dataBooking) {
		try {
			
			DataBookingDetail detail = new DataBookingDetail();
			String email = jwtTokenUtil.getEmailFromHeader(request);
//			String email =jwtTokenUtil.getEmailFromHeaderText(dataBooking.getUserEmail());
			Users user = userDAO.findEmailUser(email);
			int userId = user.getUserid();
			Movie movie = movieDAO.findIdMovie(dataBooking.getIdMovie());
			Showtime showtime = showtimeDAO.findIdShowtime(dataBooking.getIdShowTime());
			detail.setUserid(userId);
			detail.setMovie(movie);
			detail.setShowtime(showtime);
			detail.setBookings(bookingDAO.findBookingShowtime(showtime.getShowtime_id()));
			detail.setSeats(seatDAO.findAll());
			List<Booking> booking = bookingDAO.findBookingShowtime(showtime.getShowtime_id());
			return ResponseEntity.status(200).body(detail);
		} catch (Exception e) {
			System.out.println("error loadDataBooking: " + e);
			throw e;
		}
	}

	@PostMapping(value = "/movie/booking")
	public ResponseEntity<DataPayment> booking(HttpServletRequest request, @RequestBody DataBooking dataBooking)
			throws Exception {
		try {
			DataPayment dataPayment = new DataPayment();
			String email = jwtTokenUtil.getEmailFromHeader(request);
			Users userFind = userDAO.findEmailUser(email);
			int userId = userFind.getUserid();
			Showtime showtime = showtimeDAO.findIdShowtime(dataBooking.getIdShowTime());
			Users user = userDAO.findIdUsers(userId);
			dataPayment.setShowtime(showtime);
			dataPayment.setFull_name(user.getFull_name());
			dataPayment.setEmail(user.getEmail());

			int width = 200; // Adjust the desired width of the QR code
			int height = 200; // Adjust the desired height of the QR code
			Date date = new Date();
			String data = "{\"userid\": " + userId + "}";
//			qrCodeGeneratorService.generateQrCodeImage(data, width, height);
			dataPayment.setQrCodeGeneratorService(qrCodeGeneratorService.generateQrCodeImage(data, width, height));
			return ResponseEntity.status(200).body(dataPayment);
		} catch (Exception e) {
			System.out.println("error booking: " + e);
			throw e;
		}
	}

	@PostMapping("/movie/payment")
	public ResponseEntity<List<String>> submidOrder( HttpServletRequest request,@RequestBody BookingPayment bp) {
		try {
			String email = jwtTokenUtil.getEmailFromHeader(request);
			Users user = userDAO.findEmailUser(email);
			List<String> listString = new ArrayList<>();
			bookingPayment.setMoney(bp.getMoney());
			bookingPayment.setSeat(bp.getSeat());
			bookingPayment.setShowtimeId(bp.getShowtimeId());
			bookingPayment.setUserEmail(email);
			bookingPayment.setQrCode(bp.getQrCode());

//			System.out.println("error booking: " + amount);
			String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//			String baseUrl = "http://localhost:4200";
//			System.out.println(baseUrl);
			String vnpayUrl = createOrder(Integer.valueOf(bp.getMoney()), "Thanh toán hóa đơn xem phim", baseUrl);
			listString.add(vnpayUrl);
//			System.out.println("error : " + vnpayUrl);
			return ResponseEntity.status(200).body(listString);
		} catch (Exception e) {
			System.out.println("error booking: " + e);
			throw e;
		}

	}

	public String createOrder(int total, String orderInfor, String urlReturn) {
		String vnp_Version = "2.1.0";
		String vnp_Command = "pay";
		String vnp_TxnRef = VNPAYConfig.getRandomNumber(8);
		String vnp_IpAddr = "127.0.0.1";
		String vnp_TmnCode = VNPAYConfig.vnp_TmnCode;
		String orderType = "order-type";

		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_Version", vnp_Version);
		vnp_Params.put("vnp_Command", vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(total * 100));
		vnp_Params.put("vnp_CurrCode", "VND");

		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.put("vnp_OrderInfo", orderInfor);
		vnp_Params.put("vnp_OrderType", orderType);

		String locate = "vn";
		vnp_Params.put("vnp_Locale", locate);

		urlReturn += VNPAYConfig.vnp_Returnurl;
		vnp_Params.put("vnp_ReturnUrl", urlReturn);
		vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		cld.add(Calendar.MINUTE, 15);
		String vnp_ExpireDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

		List fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) vnp_Params.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
//Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				try {
					hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
					// Build query
					query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
					query.append('=');
					query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnp_SecureHash = VNPAYConfig.hmacSHA512(VNPAYConfig.vnp_HashSecret, hashData.toString());
		queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
		String paymentUrl = VNPAYConfig.vnp_PayUrl + "?" + queryUrl;
		return paymentUrl;
	}

}
