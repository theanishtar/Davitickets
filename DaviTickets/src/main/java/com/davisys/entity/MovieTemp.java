package com.davisys.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieTemp {
	private String tile_movie;
	private String poster;
//	private List<Date> start_time;
	private List<Object[]> start_time;
//	private HashMap<String, Integer> start_time;
	private int duration;
	private Date show_date;
	private int showtime_id;
}
