package com.modules.cms.web.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.modules.collect.entity.CollectContext;

@Controller
@RequestMapping(value = "${frontPath}/home")
public class HomeController {

	@RequestMapping("/showHomePage")
	public String showHomePage(Model model) {

		CollectContext cc = new CollectContext();
		Map<String, String> user = new HashMap<String, String>();
		Map<String, String> user2 = new HashMap<String, String>();
		List list = new ArrayList();
		user.put("name", "张志坚");
		user.put("sex", "11");
		user.put("feng", "3333");

		user2.put("name", "利好吧");
		user2.put("sex", "22");
		user2.put("feng", "程序员");

		cc.setId("111");
		cc.setLang("en");
		cc.setDescription("你很好");
		list.add(cc);

		model.addAttribute("list", list);
		model.addAttribute("user", user);
		model.addAttribute("name", "spring-mvc");
		return "thymeleaf/home";
	}

	@ResponseBody
	@RequestMapping("/showHomePage2")
	public List showHomePage2(Model model) {

		CollectContext cc = new CollectContext();
		Map<String, String> user = new HashMap<String, String>();
		Map<String, String> user2 = new HashMap<String, String>();
		List list = new ArrayList();
		user.put("name", "张志坚");
		user.put("sex", "11");
		user.put("feng", "3333");

		user2.put("name", "利好吧");
		user2.put("sex", "22");
		user2.put("feng", "程序员");

		cc.setId("111");
		cc.setLang("en");
		cc.setDescription("你很好");
		list.add(cc);

		return list;
	}

}