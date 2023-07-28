package com.davisys.service;

import org.springframework.stereotype.Service;

import com.davisys.dao.MovieDAO;
import com.davisys.entity.Movie;

@Service
public class MovieServiceImp implements MovieService {
private MovieDAO movieDao;
public MovieServiceImp(MovieDAO movieDAO) {
	super();
	this.movieDao=movieDAO;
}
@Override
public Movie saveMovie(Movie movie) {
	return movieDao.save(movie);
}
}
