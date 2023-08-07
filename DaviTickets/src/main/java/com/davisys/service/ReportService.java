package com.davisys.service;

import java.io.IOException;
import java.util.List;
import java.text.SimpleDateFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.davisys.dao.MovieDAO;
import com.davisys.entity.Movie;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

@Service
public class ReportService {

	@Autowired
	MovieDAO movieDao;

	public void generateExcel(HttpServletResponse response) throws Exception {
try {
	List<Movie> mv = movieDao.findAll();
	HSSFWorkbook workbook = new HSSFWorkbook();
	HSSFSheet sheet = workbook.createSheet("Danh sách phim");
	HSSFRow row = sheet.createRow(0);
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	row.createCell(0).setCellValue("Tiêu đề");
	row.createCell(1).setCellValue("Ngày ra mắt");
	row.createCell(2).setCellValue("Thời gian");
	row.createCell(3).setCellValue("Thể loại");
	row.createCell(4).setCellValue("Hình ảnh");
	row.createCell(5).setCellValue("Đánh giá");
	row.createCell(6).setCellValue("Đạo diễn");
	row.createCell(7).setCellValue("Mô tả");

	int dataRowIndex = 1;
	for (Movie movies : mv) {

		HSSFRow dataRow = sheet.createRow(dataRowIndex);
		dataRow.createCell(0).setCellValue(movies.getTitle_movie());
		dataRow.createCell(1).setCellValue(sdf.format(movies.getRelease_date()));
		dataRow.createCell(2).setCellValue(movies.getDuration());
		dataRow.createCell(3).setCellValue(movies.getGenre());
		dataRow.createCell(4).setCellValue(movies.getPoster());
		dataRow.createCell(5).setCellValue(movies.getRating());
		dataRow.createCell(6).setCellValue(movies.getFilm_director());
		dataRow.createCell(7).setCellValue(movies.getDescription_movie());
		dataRowIndex++;
	}
	ServletOutputStream ops = response.getOutputStream();
	workbook.write(ops);
	workbook.close();
	ops.close();
} catch (Exception e) {
throw e;
}
		
	}
}
