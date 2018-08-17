package com.modules.attention.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.common.persistence.Page;
import com.common.utils.StringUtils;
import com.common.web.BaseController;
import com.modules.attention.entity.Attention;
import com.modules.attention.service.AttentionService;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.Site;
import com.modules.cms.service.CategoryService;
import com.modules.cms.utils.CmsUtils;
import com.modules.msgsource.entity.Msgsource;
import com.modules.msgsource.service.MsgsourceService;
import com.modules.position.dao.PositionDao;
import com.modules.position.entity.Position;
import com.modules.position.util.PositionUtil;
import com.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/cms/attention")
public class AttentionController extends BaseController {
	@Autowired
	private AttentionService attentionService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private PositionDao positiondao;
	@Autowired
	private MsgsourceService msgsourceService;

	@ModelAttribute
	public Attention get(@RequestParam(required = false) String aid) {
		Attention attention = new Attention();

		if (StringUtils.isNotBlank(aid)) {
			attention.setAid(Integer.valueOf(aid));
			return attentionService.get(attention);
		} else {
			return new Attention();
		}
	}

	@RequestMapping(value = { "list", "" })
	public String list(Attention attention, HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(required = false) String type, @RequestParam(required = false) String title) {
		attention.setPosition(new Position());
		System.out.println("title=" + title);
		if (title != null && title != "") {
			if ("0".equals(type)) {
				Category category = new Category();
				category.setName(title);
				attention.setCategory(category);
			}
			if ("1".equals(type)) {
				Position position = new Position();
				position.setDescription(title);
				attention.setPosition(position);
			}
			if ("2".equals(type)) {
				Msgsource msgsource = new Msgsource();
				msgsource.setName(title);
				attention.setMsgsource(msgsource);
			}
		}
		attention.setType(type);
		// boolean checkrole = false;
		// for (int i = 0; i < UserUtils.getUser().getRoleList().size(); i++) {
		// if (UserUtils.getUser().getRoleList().get(i).getId().equals("1")) {
		// checkrole = true;
		// }
		// }
		// if (!checkrole) {
		// attention.setCreateBy(UserUtils.getUser());
		// System.out.println("attention.getCreateBy().getName()=="
		// + attention.getCreateBy().getName());
		//
		// }
		attention.setCreateBy(UserUtils.getUser());
		Page<Attention> page = attentionService.findPage(new Page<Attention>(request, response), attention, true);
		System.out.println("关注的list==" + page.getList().size());
		System.out.println("type===" + attention.getType());

		model.addAttribute("page", page);
		model.addAttribute("type", attention.getType());
		return "modules/cms/attentionList";
	}

	@RequestMapping(value = "form")
	public String form(Attention attention, Model model, @RequestParam(required = false) String type) {

		if (attention.getCategory() != null && StringUtils.isNotBlank(attention.getCategory().getId())) {
			List<Category> list = categoryService.findByParentId(attention.getCategory().getId(), Site.getCurrentSiteId());

			if (list.size() > 0) {
				attention.setCategory(null);
			} else {
				attention.setCategory(categoryService.get(attention.getCategory().getId()));
			}
		}
		List<Position> posilist = positiondao.findAlltwo(new Position());
		List<Msgsource> msglist = msgsourceService.findList(new Msgsource());

		Attention attention2 = new Attention();
		attention2.setType(type);
		attention2.setCreateBy(UserUtils.getUser());
		List<Attention> attList = attentionService.findList(attention2);

		// System.out.println("type===="+type);
		model.addAttribute("type", type);
		model.addAttribute("attention", attention);
		model.addAttribute("posilist", posilist);
		model.addAttribute("msglist", msglist);
		model.addAttribute("attList", attList);
		return "modules/cms/attentionForm";
	}

	@RequestMapping(value = "save")
	public String save(Attention attention, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, attention)) {
			return form(attention, model, attention.getType());
		}
		// System.out.println("信息源id===" + attention.getMsgsource().getSid());
		if (attention.getPosition() != null) {
			attention.setPosid(attention.getPosition().getPosid());
		}
		if (attention.getMsgsource() != null) {
			attention.setMsgid(attention.getMsgsource().getSid());
		}
		if (attention.getCategory() != null) {
			attention.setCategory_id(attention.getCategory().getId());
		}
		attentionService.save(attention);
		System.out.println("type===" + attention.getType());
		addMessage(redirectAttributes, "保存关注项成功");
		return "redirect:" + adminPath + "/cms/attention/list?type=" + attention.getType();
	}

	@RequestMapping(value = "delete")
	public String delete(Attention attention, Model model, RedirectAttributes redirectAttributes) {

		attentionService.delete(attention);
		addMessage(redirectAttributes, "删除关注项成功");

		return "redirect:" + adminPath + "/cms/attention/list?type=" + attention.getType();
	}

}
