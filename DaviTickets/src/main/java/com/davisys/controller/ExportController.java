package com.davisys.controller;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.davisys.service.ReportService;

@Controller
@RolesAllowed("ROLE_ADMIN")
public class ExportController {

	@Autowired
	private ReportService reportService;
	
	@GetMapping("/testexport")
	public String test() throws Exception {
		return "testexport";
	}

	@GetMapping("/export")
	public String ExportReport(HttpServletResponse response) throws Exception {
		response.setContentType("application/octet-stream");

		String headerKey = "Content-Disposition";
		String headerValue = "attachment;filename=Movie.xls";

		response.setHeader(headerKey, headerValue);

		reportService.generateExcel(response);

		return "admin/tablesmovie";
	}
	
	
}
