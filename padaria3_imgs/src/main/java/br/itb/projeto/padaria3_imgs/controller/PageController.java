package br.itb.projeto.padaria3_imgs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class PageController {

	@GetMapping("/index")
	public String index() {
		return "/index";
	}
	
}
