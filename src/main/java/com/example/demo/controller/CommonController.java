package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/common")
public class CommonController {
	
	@PostMapping("/getCreatePage/{pageKey}")
	public @ResponseBody String getCreatePage(@PathVariable String pageKey){
		return pageKey;		
	}

}
