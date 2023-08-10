package com.davisys.service;

import com.davisys.entity.Roles;
import com.davisys.entity.Users;

public interface UsersService {
	Users saveUser(Users user);
	Roles saveRole(Roles role);
	void addToUser(String userName, String roleName);
}
