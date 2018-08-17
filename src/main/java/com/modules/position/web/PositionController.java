package com.modules.position.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.common.config.Global;
import com.common.web.BaseController;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.Site;
import com.modules.cms.service.FileTplService;
import com.modules.cms.service.SiteService;
import com.modules.cms.utils.TplUtils;
import com.modules.copy.entity.Contextdefine;
import com.modules.position.dao.PositionDao;
import com.modules.position.entity.Position;
import com.modules.position.util.PositionUtil;


@Controller
@RequestMapping(value = "${adminPath}/cms/position")
public class PositionController extends BaseController{
	@Autowired PositionDao positionDao;
	@Autowired
	private FileTplService fileTplService;
	@Autowired
	private SiteService siteService;
	
	@RequestMapping(value={"list",""})
	public String list(Position position,HttpServletRequest request,HttpServletResponse response,Model model){
		
		List <Position>list = positionDao.findAlltwo(new Position());
		model.addAttribute("list", list);
		System.out.println("into PositionController");
		
		
		return "modules/cms/positionList";
	}
	@RequestMapping(value = "form")
	public String form(Position position, Model model) {
		if(position.getPosid()!=null){
			position=positionDao.findAlltwo(position).get(0);
		}
		model.addAttribute("listViewList",
				getTplContent(Category.DEFAULT_TEMPLATE));
		model.addAttribute("category_DEFAULT_TEMPLATE",
				Category.DEFAULT_TEMPLATE);
		model.addAttribute("contentViewList",
				getTplContent(Article.DEFAULT_TEMPLATE));
		model.addAttribute("article_DEFAULT_TEMPLATE", Article.DEFAULT_TEMPLATE);
		model.addAttribute("position", position);
		PositionUtil.InitPositionDesc(positionDao);
		
		model.addAttribute("POSITIONDESC", PositionUtil.POSITIONDESC);
		return "modules/cms/positionForm";
	}
	
	private List<String> getTplContent(String prefix) {
		List<String> tplList = fileTplService.getNameListByPrefix(siteService
				.get(Site.getCurrentSiteId()).getSolutionPath());
		tplList = TplUtils.tplTrim(tplList, prefix, "");
		return tplList;
	}
	
	@RequestMapping(value = "save")
	public String save(Position position, Model model,
			RedirectAttributes redirectAttributes) {
		if (Global.isDemoMode()) {
			addMessage(redirectAttributes, "演示模式，不允许操作！");
			return "redirect:" + adminPath + "/cms/position/";
		}
		if (!beanValidator(model, position)) {
			System.out.println("数据错误");
			return form(position, model);
		}
		if (position.getPosid() != null) {
			System.out.println("posid==="+position.getPosid());
			positionDao.marge(position);
		}else{
			
			positionDao.insert(position);
		}
		addMessage(redirectAttributes,
				"保存推荐位'" + position.getDescription() + "'成功");
		return "redirect:" + adminPath + "/cms/position/";
	}
	
	@RequestMapping(value = "delete")
	public String delete(Position position,
			RedirectAttributes redirectAttributes) {
		
		if (position.getPosid()!=null) {
			positionDao.deleteByPosid(position.getPosid());
			positionDao.delposiarticle(position.getPosid());
			addMessage(redirectAttributes, "删除推荐位成功");
			PositionUtil.InitPositionDesc(positionDao);
		} else {
			addMessage(redirectAttributes, "删除推荐位失败, 不允许编号为空");
			
		}
		return "redirect:" + adminPath + "/cms/position/";
	}
}
