package com.davisys.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.davisys.entity.Roles;
import com.davisys.entity.Users;
import com.davisys.reponsitory.RolesReponsitory;
import com.davisys.reponsitory.UsersReponsitory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UsersServiceImpl implements UsersService {
	@Autowired
	private UsersReponsitory usersReponsitory;
	@Autowired
	private RolesReponsitory rolesReponsitory;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Users saveUser(Users user) {
		user.setUser_password(passwordEncoder.encode(user.getUser_password()));
		return usersReponsitory.save(user);
	}

	@Override
	public Roles saveRole(Roles role) {
		return rolesReponsitory.save(role);
	}

	@Override
	public void addToUser(String userName, String roleName) {
		Users user = usersReponsitory.findByEmail(userName).get();
		Roles role = rolesReponsitory.findByName(roleName);
		user.getRoles().add(role);

	}
	

}
