//package com.davisys.controller;
//import java.util.Base64;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.SessionAttributes;
//
//import com.davisys.dao.UserDAO;
//import com.davisys.entity.Account;
//import com.davisys.entity.LoginData;
//import com.davisys.entity.SessionUser;
//import com.davisys.entity.StatusLogin;
//import com.davisys.entity.UserGoogleCloud;
//import com.davisys.entity.Users;
//import com.davisys.utils.SessionUtils;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//import jakarta.websocket.Session;
//
//import com.davisys.encrypt.DES;
//@RestController
//@CrossOrigin("*")
//public class Login {
//
//	/*
//	 * @Autowired HttpSession session;
//	 */
//
//	
//	@Autowired
//	private UserDAO userDAO;
//	
//	@Autowired
//	SessionUtils sessionUtils;
//	
//	@ModelAttribute("userSession")
//    public SessionUser setUpUserSession() {
//        return new SessionUser();
//    }
//
//	
//	@PostMapping("/rest/login")
//	public ResponseEntity<StatusLogin> login(HttpServletRequest request, @RequestBody Account account) {
//		StatusLogin statusLogin = new StatusLogin();
//		try {
//			Users user = userDAO.findEmaiAndPhonelUser(account.getEmail());
//			HttpSession session = request.getSession();
//			session.setAttribute("currentUser", user);
//			System.out.println("SESSIONID CURRENT USER: "+session.getId());
//			System.out.println(user.getUserid());
//			session.setAttribute("currentUserId", user.getUserid());
//
//
//			if (user != null && user.getUser_password().equals(account.getUser_password())) {
////				statusLogin.setStatus("successfull");
//				if (user.getUser_role() == true) {
//					statusLogin.setRole(true);
//				} else {
//					statusLogin.setRole(false);
//				}
//				statusLogin.setUser(user);
//				return ResponseEntity.status(200).body(statusLogin);
//			} else {
//				return ResponseEntity.status(401).body(null);
//			}
//		} catch (Exception e) {
//			System.out.println("error: " + e);
//			throw e;
//		}
//	}
//	
//	@PostMapping("/login")
//	public ResponseEntity<StatusLogin> loginAuth(HttpServletRequest request, @RequestBody LoginData account) {
//		StatusLogin statusLogin = new StatusLogin();
//		try {
//			Users user = userDAO.findEmaiAndPhonelUser(account.getEmail());
//			System.out.println("UFind: "+user.getUser_password().equals(account.getPassword()));
//
//			if (user != null) {
////				statusLogin.setStatus("successfull");
//				if (user.getUser_role() == true) {
//					statusLogin.setRole(true);
//				} else {
//					statusLogin.setRole(false);
//				}
//				statusLogin.setUser(user);
//				return ResponseEntity.status(200).body(statusLogin);
//			} else {
//				System.out.println("NOT FOUND USER");
//				return ResponseEntity.status(401).body(null);
//			}
//		} catch (Exception e) {
//			System.out.println("error: " + e);
//			throw e;
//		}
//	}
//	
//	@PostMapping("/login/oauth/authenticated")
//	public ResponseEntity<LoginData> authorization(@RequestBody String data){
//		//System.out.println(data);
//		String encryptData;
//		String a = "";
//		
//		try {
//			ObjectMapper mapper = new ObjectMapper();
//			// UserGoogleCloud userGoogleCloud = mapper.readValue(payload,
//			// UserGoogleCloud.class);
//			JsonNode usgc = mapper.readTree(data);
//			//System.out.println("data geted: "+usgc.get("data").asText());
//			encryptData = DES.des_decrypt(usgc.get("data").asText());
//			//System.out.println("encryptData: "+encryptData);
//
//			a = encryptData.substring(encryptData.indexOf(":")+1);
//			String email = encryptData.substring(0, encryptData.indexOf(":"));
//			String password = encryptData.substring(encryptData.indexOf(":")+1);
//			
//			System.out.println("EMAIL: "+email);
//			System.out.println("PASS: "+password);
//			
//			LoginData u = new LoginData(email, password);
//			return ResponseEntity.status(200).body(u);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//if(encryptData.substring(encryptData.indexOf(":")))
//		
//		return ResponseEntity.status(3001).body(null);
//	}
//
//	@PostMapping("/rest/loginWithGG")
//	public ResponseEntity<UserGoogleCloud> loginWithGG(HttpServletRequest req, HttpSession session) {
//		try {
//			// System.out.println("s: "+req.getParameter("credential"));
//			StatusLogin statusLogin = new StatusLogin();
//			String token = req.getParameter("credential");
//			String[] chunks = token.split("\\.");
//
//			Base64.Decoder decoder = Base64.getUrlDecoder();
//
//			String header = new String(decoder.decode(chunks[0]));
//			String payload = new String(decoder.decode(chunks[1]));
//
//			// UserGoogleCloud ugc = new Gson().fromJson(payload, UserGoogleCloud.class);
//
//			ObjectMapper mapper = new ObjectMapper();
//			// UserGoogleCloud userGoogleCloud = mapper.readValue(payload,
//			// UserGoogleCloud.class);
//			JsonNode usgc = mapper.readTree(payload);
//
//			System.out.println(">> Name : " + usgc.get("email").asText());
//			/*
//			 * usgc.iterator().forEachRemaining(u -> {
//			 * System.out.println(">> Name : "+u.get(0).asText()); });
//			 */
//
//			Users checkUser = userDAO.findEmailUser(usgc.get("email").asText());
//			Users user = new Users();
//			if (checkUser == null) {
//				user.setFull_name(usgc.get("name").asText());
//				user.setGender("Nam");
//				user.setUser_password(usgc.get("sub").asText());
//				user.setPhone(null);
//				user.setEmail(usgc.get("email").asText());
//				user.setProfile_picture(usgc.get("picture").asText());
//				user.setAccount_status(true);
//				user.setProcessed_by(true);
//				user.setUser_birtday(null);
//				user.setGg_id(usgc.get("sub").asText());
//				user.setUser_role(false);
//				userDAO.save(user);
//
//				session.setAttribute("isUserLoggedIn", user);
//				statusLogin.setRole(user.getUser_role());
//				statusLogin.setSesionId(session.getId());
//				statusLogin.setUser(user);
//			} else {
//				session.setAttribute("isUserLoggedIn", checkUser);
//				statusLogin.setRole(checkUser.getUser_role());
//				statusLogin.setSesionId(session.getId());
//				statusLogin.setUser(checkUser);
//			}
//
////			return ResponseEntity.status(200).body(statusLogin);
//			return ResponseEntity.status(200).body(null);
//
//		} catch (Exception e) {
//			System.out.println("error: " + e);
//			return ResponseEntity.status(200).body(null);
//		}
//
//	}
//
//	@GetMapping("/rest/getAccount/{email}")
//	public ResponseEntity<Users> getAccountGGCloud(@PathVariable String email) {
//
//		Users checkUser = userDAO.findEmailUser(email);
//		System.out.println("First S: " + checkUser.getEmail());
//		if (checkUser != null) {
//			return ResponseEntity.status(200).body(checkUser);
//		}
//		return ResponseEntity.status(404).body(null);
//	}
//	
//	@GetMapping("/rest/getAccounts")
//	public ResponseEntity<List<Users>> getAll() {
//
//		List<Users> list = userDAO.findAll();
//		return ResponseEntity.status(200).body(list);
//	}
//
//}
