package com.davisys.controller.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.davisys.entity.Roles;
import com.davisys.entity.Users;
import com.davisys.reponsitory.RoleCustomRepo;
import com.davisys.reponsitory.UsersReponsitory;
import com.davisys.service.AuthenticationService;
import com.davisys.service.JwtService;
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
    

    private final RoleCustomRepo roleCustomRepo = new RoleCustomRepo();

	@PostMapping("/oauth/login")
	public ResponseEntity<AuthenticationResponse> authLog(@RequestBody AuthenticationRequest authenticationRequest){
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
