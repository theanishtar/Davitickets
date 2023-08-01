package com.davisys.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.davisys.dao.MovieDAO;
import com.davisys.entity.Movie;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MovieController {
	@Autowired
	private MovieDAO movieDAO;
	@Autowired
	HttpServletRequest request;
	@Autowired
	ServletContext app;

	String editMovie = "add";

	String update = "";
	String create = "";
	String imgDavi = "https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/davi.png?alt=media&token=c3f7e0b9-b0a4-481f-8929-a47e02c4ed21";
	@GetMapping("/movie")
	public String movie(Model m) {
		editMovie = "add";
		m.addAttribute("editMovie", editMovie);
		m.addAttribute("activem", "active");
		Movie movie = new Movie();
		if (create == "true") {
			m.addAttribute("alertCreate", "true");
		}
		if (create == "false") {
			m.addAttribute("alertCreate", "false");
		}
		create = "";

		movie.setPoster(imgDavi);
		m.addAttribute("formmv", movie);
		return "admin/formmovie";
	}

	@GetMapping("/tablesmovie")
	public String tablesmovie(Model m) {
		loadMovieData(m);
		m.addAttribute("activem", "active");
		return "admin/tablesmovie";
	}

	@PostMapping("/clearMovie")
	public String clearMovie(Model m) {
		return "redirect:/movie";
	}

	@RequestMapping("/editMovie/{movie_id}")
	public String editMovie(Model m, @PathVariable("movie_id") int movie_id) {
		Movie movie = movieDAO.findIdMovie(movie_id);
		List<Movie> listMvNotUse = movieDAO.getMovieNotUse();

		editMovie = "edit";
		m.addAttribute("editMovie", editMovie);
		m.addAttribute("formmv", movie);
		m.addAttribute("listMvNotUse", listMvNotUse);
		if (update == "true") {
			m.addAttribute("alertUpdate", "true");
		}
		if (update == "false") {
			m.addAttribute("alertUpdate", "false");
		}
		update = "";
		return "admin/formmovie";
	}

	@PostMapping("/createMovie")
	public String createMovie(Model m, @RequestParam("file") MultipartFile file) {
		Movie mv = new Movie();
		create = "true";
		String poster = request.getParameter("avatar");
		String title_movie = request.getParameter("title_movie");
		String duration = request.getParameter("duration");
		String release_date = request.getParameter("release_date");
		String genre = request.getParameter("genre");
		String rating = request.getParameter("rating");
		String film_director = request.getParameter("film_director");
		String description_movie = request.getParameter("description_movie");
		if (Float.valueOf(rating) > 5) {
			create = "false";
			return "redirect:/movie";
		} else {
			String pattern = "yyyy-MM-dd";

			DateFormat dateFormat = new SimpleDateFormat(pattern);
			try {
				Date date = dateFormat.parse(release_date);
				mv.setRelease_date(date);
			} catch (Exception e) {
				create = "false";
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
			return "redirect:/tablesmovie";
		}
	}

	@PostMapping("/updateMovie/{movie_id}")
	public String updateMovie(Model m, @PathVariable("movie_id") int movie_id,
			@RequestParam("file") MultipartFile file) {
		try {
			Movie movie = movieDAO.findIdMovie(movie_id);
			update = "true";
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
				update = "false";
				return "redirect:/editMovie/{movie_id}";
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
					e.printStackTrace();
				}
				movieDAO.saveAndFlush(movie);
			}
			return "redirect:/editMovie/{movie_id}";
		} catch (Exception e) {
			System.out.println("errr: " + e);
			update = "false";
			throw e;
		}

	}

	@PostMapping("/deleteMovie/{movie_id}")
	public String deleteMovie(Model m, @PathVariable("movie_id") int movie_id) {
		Movie movie = movieDAO.findIdMovie(movie_id);
		movieDAO.delete(movie);
		return "redirect:/tablesmovie";
	}

	public void loadMovieData(Model model) {
		List<Movie> listMovie = movieDAO.findAll();
		model.addAttribute("listMovie", listMovie);
	}
}