package com.davisys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorControllerHandle implements ErrorController {
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping("/error")
	public String handleError() {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		System.out.println(status);
		if (status != null) {
			Integer statuscode = Integer.valueOf(status.toString());
			if (statuscode == HttpStatus.NOT_FOUND.value()) {
				return "error";
			}
			if (statuscode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return "error";
			}
		}
		return "error";
	}

//	public String getErrorPath() {
//		return "error404";
//	}
	
	/*
	@RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        System.out.println(status);
        if(status != null) {
            int statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "errorpages/error-403";
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "errorpages/error-500";
            }
        }
        System.out.println("k loi");
        return "error404";
    }
	*/
}