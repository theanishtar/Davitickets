package com.davisys.controller.rest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.auth.AuthenticationRequest;
import com.davisys.auth.AuthenticationResponse;
import com.davisys.auth.OAuthenticationRequest;
import com.davisys.dao.UserDAO;
import com.davisys.encrypt.AES;
import com.davisys.entity.Users;
import com.davisys.reponsitory.RoleCustomRepo;
import com.davisys.reponsitory.UsersReponsitory;
import com.davisys.service.AuthenticationService;
import com.davisys.service.JwtService;
import com.davisys.shield.Shield;
import com.davisys.shield.ShieldDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin("*")
public class Login {
	@Autowired
	UsersReponsitory usersReponsitory;

	@Autowired
	UserDAO dao;
	
	@Autowired
	AuthenticationService authenticationService;
	
	@Autowired
	AES aes;
	

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    JwtService jwtService;
    
    @Autowired
	HttpServletRequest httpServletRequest;

	private final RoleCustomRepo roleCustomRepo = new RoleCustomRepo();
	
	private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // Lấy địa chỉ IPv4 cho localhost
                try {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ipAddress = inetAddress.getHostAddress();
                } catch (UnknownHostException e) {
                    // Xử lý lỗi nếu cần
                }
            }
        }
        return ipAddress;
    }
	
	public int doLogin(String email,HttpServletRequest req, AuthenticationRequest authReq) {
		Users user = usersReponsitory.findByEmail(authReq.getEmail()).get();
		if(user != null && passwordEncoder.matches(authReq.getPassword(), user.getPassword())) {
			// Login hợp lệ
			if(!user.isAccount_status()) {
				// Bị khóa ở server
				return -1;
			}
			return 0;
		} else {
			Date currentDate = new Date();
			ShieldDAO dao = new ShieldDAO();
			// Không hợp lệ
			Shield shiledErr = new Shield(authReq.getEmail(), getClientIp(req), currentDate.getTime(), 1);
			if(dao.findByEmail(authReq.getEmail()) == null)
				dao.create(shiledErr);
			else {
				Shield shie = dao.findByEmail(authReq.getEmail());
				shie.setCountErr(shie.getCountErr()+1);
				dao.update(dao.getKeyByEmail(authReq.getEmail()), shie);
			}
			return -2;
		}
	}
	
    public int pushToShield(HttpServletRequest req, AuthenticationRequest authReq) {
		/*	STATUS
		 * 		200 login success -> 0
		 * 		301 account fail -> -2
		 * 		302 account is banked (server)-> return -1
		 * 		303 account is locked (spam)  -> return time lock 
		 */
		// Kiểm tra trạng thái ở firebase
		Date currentDate = new Date();
		ShieldDAO dao = new ShieldDAO();
		//Shield s = dao.findByKey();

		// Lấy Shield trên DB Firebase
		Shield shieldFind = dao.findByEmail(authReq.getEmail());
		if (shieldFind == null) {
			return doLogin(authReq.getEmail(), req, authReq);
		} else {
			// Có trên firebase -> check trạng thái bị khóa
			if(shieldFind.getIp().equalsIgnoreCase(getClientIp(req))) {
				// Kiểm tra có trùng IP hay không
				if(shieldFind.getCountErr() >= 5) {
					// Nếu Login sai 5 lần -> khóa trong 3p
					// Tính thời gian đã trôi qua giữa hai thời điểm, tính bằng mili giây
			        long timeDifferenceInMillis = currentDate.getTime() - shieldFind.getTime();
			        
			        // Chuyển đổi sang đơn vị phút
			        long minutesPassed = timeDifferenceInMillis / (60 * 1000);
			        
			        /*
			         * Khóa theo lũy tiến: 2p/5l login sai -> khóa 4p khi login sai 10lần
			         */
			        if(minutesPassed <= shieldFind.getCountErr()/3) {
			        	// 3p chưa hết hạn -> không thể Login
			        	return shieldFind.getCountErr()/3;
			        } else {
			        	int passedLock = doLogin(authReq.getEmail(), req, authReq);
			        	return passedLock;
			        }
				}
				// tăng số lần đăng nhập sai và thời gian
//				Shield shieldCheck = dao.findByEmail(authReq.getEmail());
//				String key = dao.getKeyByEmail(authReq.getEmail());
//				shieldCheck.setTime(currentDate.getTime());
//				shieldCheck.setCountErr(shieldCheck.getCountErr()+1);
//				dao.update(key, shieldCheck);
			}
			return doLogin(authReq.getEmail(), req, authReq);
		}
		// Shield shield = new Shield(authReq.getEmail(), req.getRemoteAddr(),
		// currentDate.getTime(), 0);
	}

	@PostMapping("/oauth/login")
	public ResponseEntity<AuthenticationResponse> authLog(@RequestBody AuthenticationRequest authenticationRequest) {
		
		int shieldLogin = pushToShield(httpServletRequest, authenticationRequest);
		if(shieldLogin != 0) {
			if(shieldLogin == -1) {
				AuthenticationResponse authRes = new AuthenticationResponse(); 
				authRes.setToken(String.valueOf(shieldLogin));
				authRes.setName("Tài khoản của bạn đã bị khóa, hãy liên hệ với Email: Davitickets@gmail.com để được mở khóa!");
				return ResponseEntity.status(302).body(authRes);
			} 
			if(shieldLogin > 0) {
				AuthenticationResponse authRes = new AuthenticationResponse(); 
				authRes.setToken(String.valueOf(shieldLogin));
				authRes.setName("Tài khoản của bạn sẽ được mở sau "+String.valueOf(shieldLogin)+" phút!");
				return ResponseEntity.status(303).body(authRes);
			}
			if(shieldLogin == -2){
				return ResponseEntity.status(301).body(null);
			}
		}
		if(shieldLogin == 0) {
			ShieldDAO dao = new ShieldDAO();
			String key = dao.getKeyByEmail(authenticationRequest.getEmail());
			dao.delete(key);
		}
		
		return ResponseEntity.ok(authenticationService.authenticationResponse(authenticationRequest));
	}
	
	//LOGIN TỰ ĐỘNG VỚI GG API
	@PostMapping("/oauth/login/authenticated")
	public ResponseEntity<AuthenticationResponse> authorization(@RequestBody String data){
		String encryptData;
		System.out.println(data);
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode usgc = mapper.readTree(data);
			encryptData = aes.DecryptAESfinal(usgc.get("data").asText());

			String email = encryptData;
			//String password = encryptData.substring(encryptData.indexOf(":")+1);
			
			System.out.println("EMAIL: "+email);
			//System.out.println("PASS: "+password);
			
			// check user
			Users userCheck = dao.findEmailUser(email);
			if(userCheck != null) {
				String[] role = userCheck.getAuth();
				Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
				
				for(String s:role) {
					authorities.add(new SimpleGrantedAuthority("ROLE_"+s));
				}

				//Set<Roles> set = new HashSet<>();
				//role.stream().forEach(c -> set.add(new Roles(c.getName())));
				//userCheck.setRoles(set);
				//set.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getName())));
				
				OAuthenticationRequest authReq = new OAuthenticationRequest(email);
				String token = jwtService.generateToken(userCheck, authorities);
				return ResponseEntity.ok(new AuthenticationResponse(userCheck.getFull_name(), authorities, token, null));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(301).body(null);
		}
		return ResponseEntity.status(301).body(null);
	}
}
