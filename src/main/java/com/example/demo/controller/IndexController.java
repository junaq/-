package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
 
      private static final String INDEX="index.html";
      
      
      @GetMapping("/index")
      public String index(){
    	  return INDEX;
      }
	  
	
	
}
