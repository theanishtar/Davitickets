package com.davisys.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.davisys.dao.MovieDAO;
import com.davisys.entity.Movie;
import com.davisys.service.ReportService;

import javax.servlet.http.HttpServletResponse;

@Controller
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
