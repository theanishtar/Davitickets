package com.davisys.controller;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.davisys.dao.MovieDAO;
import com.davisys.entity.Movie;
import com.davisys.service.SessionService;

import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
@RolesAllowed("ROLE_ADMIN")
public class MovieController {
	@Autowired
	private MovieDAO movieDAO;
	@Autowired
	HttpServletRequest request;
	@Autowired
	ServletContext app;
	@Autowired
	SessionService sessionService;

	String editORcreate = "create";

	String alert = "";

	String imgDavi = "https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/davi.png?alt=media&token=c3f7e0b9-b0a4-481f-8929-a47e02c4ed21";

	@GetMapping("/tablesmovie")
	public String tablesmovie(Model m) {
		loadMovieData(m);
		m.addAttribute("alert", alert);
		alert = "";
		m.addAttribute("activem", "active");
		return "admin/tablesmovie";
	}

	public void loadMovieData(Model model) {
		List<Movie> listMovie = movieDAO.findAll();
		model.addAttribute("listMovie", listMovie);
	}

	@GetMapping("/movie")
	public String movie(Model m) {
		editORcreate = "create";
		m.addAttribute("editORcreate", editORcreate);
		Movie movie = new Movie();
		movie.setPoster(imgDavi);
		m.addAttribute("alert", alert);
		alert = "";
		m.addAttribute("formmv", movie);
		m.addAttribute("activem", "active");
		return "admin/formmovie";
	}

	@PostMapping("/clearMovie")
	public String clearMovie(Model m) {
		return "redirect:/movie";
	}

	@RequestMapping("/editMovie/{movie_id}")
	public String editMovie(Model m, @PathVariable("movie_id") int movie_id) {
		editORcreate = "edit";
		m.addAttribute("editORcreate", editORcreate);
		Movie movie = movieDAO.findIdMovie(movie_id);
		List<Movie> listMvNotUse = movieDAO.getMovieNotUse();
		m.addAttribute("formmv", movie);
		m.addAttribute("listMvNotUse", listMvNotUse);
		m.addAttribute("alert", alert);
		alert = "";
		return "admin/formmovie";
	}
	
	
//	@PostMapping("/validation/validate")
//	public String save(Model model, @Validated @ModelAttribute("movie") Movie form, Errors errors ) {
//		if(errors.hasErrors()) {
//			model.addAttribute("message", "Vui lòng sửa các lỗi sau: ");
//			return "admin/formmovie";
//		}
//		return "admin/formmovie";
//	}
//	
	
	
	@PostMapping("/createMovie")
	public String createMovie(Model m, @RequestParam("file") MultipartFile file) {
		try {
			alert = "createSuccess";
			Movie mv = new Movie();
			String poster = request.getParameter("avatar");
			String title_movie = request.getParameter("title_movie");
			String duration = request.getParameter("duration");
			String release_date = request.getParameter("release_date");
			String genre = request.getParameter("genre");
			String rating = request.getParameter("rating");
			String film_director = request.getParameter("film_director");
			String description_movie = request.getParameter("description_movie");
			if (Float.valueOf(rating) > 5) {
				alert = "createFail";
				return "redirect:/admin/movie";
			} else {
				String pattern = "yyyy-MM-dd";

				DateFormat dateFormat = new SimpleDateFormat(pattern);
				try {
					Date date = dateFormat.parse(release_date);
					mv.setRelease_date(date);
				} catch (Exception e) {
					alert = "createFail";
					e.printStackTrace();
				}

				mv.setTitle_movie(title_movie);
				mv.setDuration(Integer.valueOf(duration));
				mv.setGenre(genre);
				mv.setRating(Float.valueOf(rating));
				mv.setFilm_director(film_director);
				mv.setDescription_movie(description_movie);
				if (!file.isEmpty()) {
					mv.setPoster(poster);
				} else {
					mv.setPoster(imgDavi);
				}
				movieDAO.saveAndFlush(mv);
			}
		} catch (Exception e) {
			alert = "createFail";
		}
		return "redirect:/admin/tablesmovie";
	}

	@PostMapping("/updateMovie/{movie_id}")
	public String updateMovie(Model m, @PathVariable("movie_id") int movie_id,
			@RequestParam("file") MultipartFile file) {
		try {
			alert = "updateSuccess";
			Movie movie = movieDAO.findIdMovie(movie_id);
			String title_movie = request.getParameter("title_movie");
			String poster = request.getParameter("avatar");
			String duration = request.getParameter("duration");
			String release_date = request.getParameter("release_date");
			String genre = request.getParameter("genre");
			String rating = request.getParameter("rating");
			String film_director = request.getParameter("film_director");
			String description_movie = request.getParameter("description_movie");
			String pattern = "yyyy-MM-dd";
			if (Float.valueOf(rating) > 5) {
				alert = "updateFail";
				return "redirect:/admin/editMovie/{movie_id}";
			} else {
				movie.setMovie_id(movie_id);
				movie.setTitle_movie(title_movie);
				movie.setDuration(Integer.valueOf(duration));
				movie.setGenre(genre);
				movie.setRating(Float.valueOf(rating));
				movie.setFilm_director(film_director);
				movie.setDescription_movie(description_movie);
				if (!file.isEmpty()) {
					movie.setPoster(poster);
				} else {
					movie.setPoster(movie.getPoster());
				}

				DateFormat dateFormat = new SimpleDateFormat(pattern);
				try {
					Date date = dateFormat.parse(release_date);
					movie.setRelease_date(date);
				} catch (Exception e) {
					alert = "updateFail";
					e.printStackTrace();
				}
				movieDAO.saveAndFlush(movie);
			}
			return "redirect:/admin/editMovie/{movie_id}";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			alert = "updateFail";
			throw e;
		}

	}

	@PostMapping("/deleteMovie/{movie_id}")
	public String deleteMovie(Model m, @PathVariable("movie_id") int movie_id) {
		try {
			alert = "deleteSuccess";
			Movie movie = movieDAO.findIdMovie(movie_id);
			movieDAO.delete(movie);
		} catch (Exception e) {
			System.out.println("errr: " + e);
			alert = "deleteFail";
		}
		return "redirect:/admin/tablesmovie";
	}
	
	@PostMapping("/importExcel")
	public String importE(@RequestParam("file") MultipartFile file) {
		try {
			alert = "importSuccess";
			String fileName = file.getOriginalFilename();
			InputStream input = file.getInputStream();
			if (!fileName.contains(".xlsx")) {
				alert = "importFail";
			} else {
				XSSFWorkbook wb = new XSSFWorkbook(input);
				XSSFSheet sheet = wb.getSheetAt(0); 
				Iterator<Row> rowIter = sheet.iterator();
				int i = 0;
				while (rowIter.hasNext()) {
					Row row = rowIter.next();
					Iterator<Cell> cellIter = row.iterator();
					if (i != 0) {
						Movie movie = new Movie();
						while (cellIter.hasNext()) {
							Cell cell = cellIter.next();
							switch (cell.getColumnIndex()) {
							case 0:
								movie.setPoster(String.valueOf(cell));
								break;
							case 1:
								movie.setTitle_movie(String.valueOf(cell));
								break;
							case 2:
								String test = String.valueOf(cell);
								String dur = test.substring(0, test.lastIndexOf("."));
								movie.setDuration(Integer.parseInt(dur));
								break;
							case 3:
								String pattern = "dd-MM-yyyy";

								DateFormat dateFormat = new SimpleDateFormat(pattern);
								try {
									Date date = dateFormat.parse(String.valueOf(cell));
									movie.setRelease_date(date);
								} catch (Exception e) {
									e.printStackTrace();
								}
								break;
							case 4:
								movie.setGenre(String.valueOf(cell));
								break;
							case 5:
								movie.setRating(Float.valueOf(String.valueOf(cell)));
								break;
							case 6:
								movie.setFilm_director(String.valueOf(cell));
								break;
							case 7:
								movie.setDescription_movie(String.valueOf(cell));
								break;
							}

						}
						movieDAO.saveAndFlush(movie);
					}
					i++;
				}
				input.close();
				System.out.println(fileName);
			}
		} catch (Exception ex) {
			alert = "importSuccess";
			System.out.println(ex);
		}
		return "redirect:/admin/tablesmovie";
	}

}