package com.modules.sys.web;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping(value = "${adminPath}/sys/welcome")
public class WelcomeController {
	
	
	@RequestMapping(value = {"list",""})
	public String index() {
		return "modules/sys/welcome";
	}
}
