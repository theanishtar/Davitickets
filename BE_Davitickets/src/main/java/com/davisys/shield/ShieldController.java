package com.davisys.shield;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shield")
public class ShieldController {

	@GetMapping("/get")
	public String getAll() {
//		Shield s = new Shield("dang@poly", "cfg88g", 0, 0);
//		ShieldDAO dao = new ShieldDAO();
//		dao.create(s);
		
		return "";
	}
}
