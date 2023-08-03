package com.davisys.controller.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.davisys.dao.MovieDAO;
import com.davisys.dao.ShowtimeDAO;
import com.davisys.entity.Movie;
import com.davisys.entity.MovieTemp;
import com.davisys.entity.Showtime;

@RestController
@CrossOrigin
public class ListMovieRescontroller {
	@Autowired
	private MovieDAO movieDAO;
	@Autowired
	private ShowtimeDAO showtimeDAO;

	@GetMapping("/movie/loadMovie/{showDate}")
	public ResponseEntity<List<MovieTemp>> loadMovie(@PathVariable String showDate) {
		try {
			List<Showtime> showtimes = showtimeDAO.loadTimeMovie(showDate);

			List<MovieTemp> listtemp = new ArrayList<>();
			Set<Integer> key = new HashSet<>();
			for (Showtime sh : showtimes) {
				if (!key.contains(sh.getMovie().getMovie_id())) {
					MovieTemp movieTemp = new MovieTemp();
//					List<Date> dateTime = new ArrayList<>();

//					HashMap<String, Integer>dateTime=new HashMap<>();
					Object[] ob = new Object[2];
					List<Object[]> dateTime = new ArrayList<>();
//					System.out.println("id: "+sh.getMovie().getMovie_id());
					key.add(sh.getMovie().getMovie_id());
					List<Showtime> showtimetemp = showtimeDAO.loadTimeMovieId(showDate, sh.getMovie().getMovie_id());
					System.out.println(showtimetemp.size());
					if (showtimetemp.size() > 1) {
						int i = 0;
						for (Showtime showtime : showtimetemp) {
//							dateTime.put(String.valueOf(showtime.getStart_time()), showtime.getShowtime_id());
							dateTime.add(new Object[] { String.valueOf(showtime.getStart_time()),
									showtime.getShowtime_id() });
						}

					} else if (showtimetemp.size() == 1) {
						dateTime.add(new Object[] { String.valueOf(sh.getStart_time()),
								sh.getShowtime_id() });
					}
					movieTemp.setTile_movie(sh.getMovie().getTitle_movie());
					movieTemp.setPoster(sh.getMovie().getPoster());
					movieTemp.setDuration(sh.getMovie().getDuration());
					movieTemp.setShow_date(sh.getShowdate());
					movieTemp.setStart_time(dateTime);
					movieTemp.setShowtime_id(sh.getShowtime_id());
					listtemp.add(movieTemp);
				}

			}
			System.out.println("key: " + key);


			return ResponseEntity.status(200).body(listtemp);
		} catch (Exception e) {
			System.out.println("Error loadMovie: " + e);
			throw e;
		}
	}

}
