package com.davisys.controller;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.davisys.dao.MovieDAO;
import com.davisys.entity.Movie;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

@Controller
public class ImportFileExcel {
	@Autowired
	MovieDAO moiveDao;
	
	String alert = "";

//	@PostMapping("/importExcel")
//	public String importE(@RequestParam("file") MultipartFile file) {
//		try {
//			String fileName = file.getOriginalFilename();
//			InputStream input = file.getInputStream();
//			if (!fileName.contains(".xlsx")) {
//				System.out.println("khong phai file excel");
//			} else {
//				XSSFWorkbook wb = new XSSFWorkbook(input);
//				XSSFSheet sheet = wb.getSheetAt(0); 
//				Iterator<Row> rowIter = sheet.iterator();
//				int i = 0;
//				while (rowIter.hasNext()) {
//					Row row = rowIter.next();
//					Iterator<Cell> cellIter = row.iterator();
//					if (i != 0) {
//						Movie movie = new Movie();
//						while (cellIter.hasNext()) {
//							Cell cell = cellIter.next();
//							switch (cell.getColumnIndex()) {
//							case 0:
//								movie.setPoster(String.valueOf(cell));
//								break;
//							case 1:
//								movie.setTitle_movie(String.valueOf(cell));
//								break;
//							case 2:
//								String test = String.valueOf(cell);
//								String dur = test.substring(0, test.lastIndexOf("."));
//								movie.setDuration(Integer.parseInt(dur));
//								break;
//							case 3:
//								String pattern = "dd-MM-yyyy";
//
//								DateFormat dateFormat = new SimpleDateFormat(pattern);
//								try {
//									Date date = dateFormat.parse(String.valueOf(cell));
//									movie.setRelease_date(date);
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//								break;
//							case 4:
//								movie.setGenre(String.valueOf(cell));
//								break;
//							case 5:
//								movie.setRating(Float.valueOf(String.valueOf(cell)));
//								break;
//							case 6:
//								movie.setFilm_director(String.valueOf(cell));
//								break;
//							case 7:
//								movie.setDescription_movie(String.valueOf(cell));
//								break;
//							}
//
//						}
//						moiveDao.saveAndFlush(movie);
//					}
//					i++;
//				}
//				input.close();
//				System.out.println(fileName);
//			}
//		} catch (Exception ex) {
//			System.out.println(ex);
//		}
//		return "redirect:/tablesmovie";
//	}
}
