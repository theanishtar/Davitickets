package com.davisys.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.davisys.auth.AuthenticationRequest;
import com.davisys.auth.AuthenticationResponse;
import com.davisys.auth.OAuthenticationRequest;
import com.davisys.auth.OAuthenticationResponse;
import com.davisys.entity.Roles;
import com.davisys.entity.Users;
import com.davisys.reponsitory.RoleCustomRepo;
import com.davisys.reponsitory.UsersReponsitory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class AuthenticationService {
	private final UsersReponsitory usersReponsitory;

	@Autowired
	private final AuthenticationManager authenticationManager;
	private final RoleCustomRepo roleCustomRepo;
	private final JwtService jwtService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	// Bean PasswordEncoder trả về bởi BCryptPasswordEncoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public boolean isTokenValid(String username, String password) {
		try {
			System.out.println("Valid Token In4: " + username + " pass: " + password);
			// Create a new UsernamePasswordAuthenticationToken with the provided username
			// and password
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
					password);

			// Perform authentication using AuthenticationManager
			Authentication authenticationResult = authenticationManager.authenticate(authenticationToken);

			// If the authentication was successful, the token is considered valid
			// You can also get the authenticated principal using
			// authenticationResult.getPrincipal()
			return authenticationResult.isAuthenticated();
		} catch (Exception e) {
			// If authentication failed, the token is considered invalid
			return false;
		}
	}

	public AuthenticationResponse authenticationResponse(AuthenticationRequest authenticationRequest) {
		try {
			Users user = usersReponsitory.findByEmail(authenticationRequest.getEmail()).orElseThrow();
			if(!user.isAccount_status()) return null;
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					authenticationRequest.getEmail(), authenticationRequest.getPassword());

			System.out.println(user.getFull_name());
			List<Roles> role = null;
			if (user != null) {
				role = roleCustomRepo.getRole(user);
				/*
				 * System.out.println("Details list role:");
				 * role.stream().forEach(System.out::println);
				 */
			}
			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

			Set<Roles> set = new HashSet<>();
//			List<Roles>set = new ArrayList<>();
			role.stream().forEach(c -> set.add(new Roles(c.getName())));
			user.setRoles(set);

			set.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getName())));
			System.out.println("=====+++++++++++++++========");

			authenticationManager.authenticate(token);
			System.out.println("=============");

			var jwtToken = jwtService.generateToken(user, authorities);
			var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);

			return AuthenticationResponse.builder().token(jwtToken).refreshToken(jwtRefreshToken)
					.name(user.getFull_name()).roles(authorities).build();
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public AuthenticationResponse authenticationResponse(OAuthenticationRequest oAuthenticationRequest) {

		try {
			Users user = usersReponsitory.findByEmail(oAuthenticationRequest.getEmail()).orElseThrow();
			
			System.out.println(user.getEmail()+"-------------------");

			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getEmail(),
					user.getPassword());

			System.out.println(user.getFull_name());
			List<Roles> role = null;
			if (user != null) {
				role = roleCustomRepo.getRole(user);
				/*
				 * System.out.println("Details list role:");
				 * role.stream().forEach(System.out::println);
				 */
			}
			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

			Set<Roles> set = new HashSet<>();
//			List<Roles>set = new ArrayList<>();
			role.stream().forEach(c -> set.add(new Roles(c.getName())));
			user.setRoles(set);

			set.stream().forEach(i -> authorities.add(new SimpleGrantedAuthority(i.getName())));
			System.out.println("=====+++++++++++++++========");

			authenticationManager.authenticate(token);
			System.out.println("=============");

			var jwtToken = jwtService.generateToken(user, authorities);
			var jwtRefreshToken = jwtService.generateRefreshToken(user, authorities);

			return AuthenticationResponse.builder().token(jwtToken).refreshToken(jwtRefreshToken)
					.name(user.getFull_name()).roles(authorities).build();
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}
