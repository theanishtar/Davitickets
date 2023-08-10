package com.davisys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class SessionService {
	@Autowired
	HttpSession session;

	public void set(String name, Object pass) {
		session.setAttribute(name, pass);

	}

	public <T> T get(String name) {
		return (T) session.getAttribute(name);

	}

	public void remove(String name) {
		session.removeAttribute(name);

	}
}