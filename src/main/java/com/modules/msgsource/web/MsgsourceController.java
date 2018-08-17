package com.modules.msgsource.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.common.persistence.Page;
import com.common.utils.StringUtils;
import com.common.web.BaseController;
import com.google.zxing.aztec.decoder.Decoder;
import com.modules.msgsource.entity.Msgsource;
import com.modules.msgsource.service.MsgsourceService;
import com.modules.sensitive.entity.Sensitive;
import com.modules.sensitive.service.SensitiveService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "${adminPath}/cms/msgsource")
public class MsgsourceController extends BaseController {

	private Logger log = Logger.getLogger(MsgsourceController.class);
	@Autowired
	private MsgsourceService msgsourceService;

	@ModelAttribute
	public Msgsource get(@RequestParam(required = false) String sid) {
		Msgsource msgsource = new Msgsource();

		if (StringUtils.isNotBlank(sid)) {
			msgsource.setSid(Integer.valueOf(sid));
			return msgsourceService.get(msgsource);
		} else {
			return new Msgsource();
		}
	}

	@RequestMapping(value = { "list", "" })
	public String list(Msgsource msgsource, String title, HttpServletRequest request, 
			HttpServletResponse response, Model model) {
		if(!StringUtils.isBlank(title)) {
			try {
				title = URLDecoder.decode(title, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		msgsource.setName(title);
		Page<Msgsource> page = msgsourceService.findPage(new Page<Msgsource>(
				request, response), msgsource, true);

		model.addAttribute("page", page);
		model.addAttribute("title", title);
		return "modules/cms/msgsourceList";
	}

	@RequestMapping(value = "form")
	public String form(Msgsource msgsource, Model model) {

		model.addAttribute("msgsource", msgsource);
		return "modules/cms/msgsourceForm";
	}

	@RequestMapping(value = "save")
	public String save(Msgsource msgsource, Model model,
			RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, msgsource)) {
			return form(msgsource, model);
		}

		if (msgsource.getSid() != null && msgsource.getSid().SIZE > 0) {
			msgsourceService.save(msgsource);
			addMessage(redirectAttributes, "保存信息源成功");
		} else {
			Msgsource msgsource2 = msgsourceService.getName(msgsource);
			if (msgsource2 != null) {
				addMessage(redirectAttributes, "信息源已存在");
			} else {
				msgsourceService.save(msgsource);
				addMessage(redirectAttributes, "保存信息源成功");
			}
		}

		return "redirect:" + adminPath + "/cms/msgsource";
	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Map<String, Object> delete(String sid, String title, @RequestBody String data, Model model,
			RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
		// JSONObject object = JSONObject.fromObject(data);
		title = data.substring(data.indexOf("=") + 1);
		title = URLDecoder.decode(title, "utf-8");
		// title = object.getString("title");
		Msgsource msgsource = new Msgsource();
		msgsource.setSid(Integer.valueOf(sid));
		msgsourceService.delete(msgsource);
		addMessage(redirectAttributes, "删除信息源成功");
		// redirectAttributes.addAttribute("title", title);
		// return "redirect:" + adminPath + "/cms/msgsource/list?repage"/* + (StringUtils.isBlank(title)? "" : "&title=" + title)*/;
		Map<String, Object> map= new HashMap<String, Object>();
		map.put("title", title);
		return map;
	}
	// 批量删除
	@RequestMapping(value = "multiDelete")
	public String multiDelete(String s_id, Model model, RedirectAttributes redirectAttributes) {
		if(!StringUtils.isBlank(s_id)) {
			String [] ids = s_id.split("\\|");
			Msgsource msgsource = new Msgsource();
			for (String id : ids) {
				msgsource.setSid(Integer.valueOf(id));
				msgsourceService.delete(msgsource);
			}
			addMessage(redirectAttributes, "成功删除信息源" + String.valueOf(ids.length) + "条");
		}
		return "redirect:" + adminPath + "/cms/msgsource/list?repage";
	}
}
