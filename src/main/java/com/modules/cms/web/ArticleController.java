package com.modules.cms.web;

import com.common.config.Global;
import com.common.mapper.JsonMapper;
import com.common.persistence.Page;
import com.common.utils.DateUtils;
import com.common.utils.Encodes;
import com.common.utils.StringUtils;
import com.common.web.BaseController;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.modules.attention.entity.Attention;
import com.modules.attention.service.AttentionService;
import com.modules.cms.dao.ArticleDao;
import com.modules.cms.entity.Article;
import com.modules.cms.entity.ArticleData;
import com.modules.cms.entity.Category;
import com.modules.cms.entity.CmsUpdatePage;
import com.modules.cms.entity.Site;
import com.modules.cms.service.ArticleDataService;
import com.modules.cms.service.ArticleService;
import com.modules.cms.service.CategoryService;
import com.modules.cms.service.CmsUpdatePageService;
import com.modules.cms.service.FileTplService;
import com.modules.cms.service.SiteService;
import com.modules.cms.utils.CmsUtils;
import com.modules.cms.utils.TplUtils;
import com.modules.copy.web.RunMobileStatic;
import com.modules.copy.web.RunStaticPage;
import com.modules.msgsource.entity.Msgsource;
import com.modules.position.dao.PositionArticleDao;
import com.modules.position.dao.PositionDao;
import com.modules.position.entity.Position;
import com.modules.position.entity.PositionArticle;
import com.modules.sensitive.entity.Sensitive;
import com.modules.sensitive.service.SensitiveService;
import com.modules.sys.copy.CopyUtil;
import com.modules.sys.entity.Role;
import com.modules.sys.entity.User;
import com.modules.sys.service.SystemService;
import com.modules.sys.utils.UserUtils;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({"${adminPath}/cms/article"})
public class ArticleController extends BaseController
{
  private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ArticleController.class);

  @Autowired
  private ArticleService articleService;

  @Autowired
  private ArticleDataService articleDataService;

  @Autowired
  private CategoryService categoryService;

  @Autowired
  private FileTplService fileTplService;

  @Autowired
  private SiteService siteService;

  @Autowired
  private PositionDao positionDao;

  @Autowired
  private SensitiveService sensitiveService;

  @Autowired
  private AttentionService attentionService;

  @Autowired
  private RunStaticPage runStatic;

  @Autowired
  private ArticleDao articleDao;

  @Autowired
  private CmsUpdatePageService updatePageService;

  @Autowired
  private RunMobileStatic mobileStatic;

  @Autowired
  private SystemService systemService;

  @Autowired
  private PositionArticleDao positionArticleDao;

  @Value("${webpath}")
  protected String webpath;

  @ModelAttribute
  public Article get(@RequestParam(required=false) String id) { if (StringUtils.isNotBlank(id)) {
      if (this.articleService.get(id) != null) {
        return (Article)this.articleService.get(id);
      }
      return new Article();
    }

    return new Article(); }

  @RequiresPermissions({"cms:article:view"})
  @RequestMapping({"list", ""})
  public String list(Article article, HttpServletRequest request, HttpServletResponse response, Model model, String flag)
  {
    String pageNo = request.getParameter("pageNo");

    this.log.info("flag=" + flag);
    boolean checkrole = false;
    for (int i = 0; i < UserUtils.getUser().getRoleList().size(); i++) {
      if (((Role)UserUtils.getUser().getRoleList().get(i)).getId().equals("1")) {
        checkrole = true;
      }
    }
    if ((!checkrole) && 
      (!"y".equals(flag))) {
      User currentUser = UserUtils.getUser();
      article.setCreateBy(currentUser);
      this.log.info(article.getCreateBy().getId());
    }

    this.log.info("managerid:" + article.getCreateBy());
    if (article.getCreateBy() == null) {
      article.setCreateBy(UserUtils.getUser());
    }

    Page page = this.articleService.findPage(new Page(request, response), article, true);
    this.log.info("栏目文章===" + page.getList().size());
    model.addAttribute("page", page);
    model.addAttribute("usersmap", getUserMap(UserUtils.getUser()));

    return "modules/cms/articleList";
  }

  @RequiresPermissions({"cms:article:view"})
  @RequestMapping({"form"})
  public String form(Article article, String pageNo, Model model) {
    CmsUtils.addViewConfigAttribute(model, article.getCategory());
    if ((article.getCategory() != null) && (StringUtils.isNotBlank(article.getCategory().getId()))) {
      List list = this.categoryService.findByParentId(article.getCategory().getId(), 
        Site.getCurrentSiteId());

      if (list.size() > 0)
        article.setCategory(null);
      else {
        article.setCategory((Category)this.categoryService.get(article.getCategory().getId()));
      }
    }

    Attention attention = new Attention();
    attention.setType("1");
    attention.setCreateBy(UserUtils.getUser());
    List attlist = this.attentionService.findList(attention);

    String posid = article.getPosid();
    String[] ps = posid.split(",");
    List posids = new ArrayList();
    if ((ps != null) && (ps.length > 0)) {
      for (String s : ps) {
        posids.add(s);
      }
    }

    Attention attention2 = new Attention();
    User user2 = UserUtils.getUser();
    attention2.setCreateBy(user2);
    attention2.setType("0");
    List categorylist = this.attentionService.findList(attention2);

    article.setArticleData((ArticleData)this.articleDataService.get(article.getId()));

    model.addAttribute("contentViewList", getTplContent());
    model.addAttribute("article_DEFAULT_TEMPLATE", "frontViewArticle");
    if (article.getArticleData() != null) {
      String replaceContent = article.getArticleData().getContent().replace("<hr style=\"page-break-after:always;\" class=\"ke-pagebreak\" />", "_ueditor_page_break_tag_");

      article.getArticleData().setContent(replaceContent);
    }
    model.addAttribute("article", article);
    model.addAttribute("attlist", attlist);
    model.addAttribute("categorylist", categorylist);
    model.addAttribute("posid", posids);
    model.addAttribute("listType", "list");
    model.addAttribute("pageNo", pageNo);
    return "modules/cms/articleForm";
  }
  @RequiresPermissions({"cms:article:edit"})
  @RequestMapping({"save"})
  public String save(Article article, String listType, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    if (!beanValidator(model, article, new Class[0])) {
      return form(article, request.getParameter("pageNo"), model);
    }
    if ((article.getImage().indexOf("http") > -1) || (article.getImage().indexOf("https") > -1)) {
      this.logger.info("is web img");
      try {
        article.setImage(CopyUtil.downimg(article.getImage(), "/imgdown").replace(this.webpath, ""));
      }
      catch (IOException e) {
        e.printStackTrace();
        this.logger.info("down false");
      }
    } else {
      this.logger.info("not web img");
    }
    this.logger.info("替换keyworks");
    article.setKeywords(article.getKeywords().replace(" ", ","));

    this.logger.info("生成缩略图");
    CopyUtil.toMobileImg(request, article.getImage());
    if (article.getArticleData().getContent() != null) {
      List<Sensitive> sensitivelist = this.sensitiveService.findList(new Sensitive());
      this.log.info("校验敏感词");
      this.log.info("栏目为：" + article.getCategory().getId());
      for (Sensitive sensitive : sensitivelist) {
        this.log.info("校验敏感词===" + article.getArticleData().getContent().indexOf(sensitive.getValue()));
        if (article.getArticleData().getContent().indexOf(sensitive.getValue()) > -1) {
          addMessage(redirectAttributes, new String[] { "保存文章'" + 
            StringUtils.abbr(article
            .getTitle(), 50) + "'失败，文章内容存在敏感词" });
          return form(article, request.getParameter("pageNo"), model);
        }
      }

      this.logger.info("begin down web img");
      String replaceContent = article.getArticleData().getContent().replace("_ueditor_page_break_tag_", "<hr style=\"page-break-after:always;\" class=\"ke-pagebreak\" />");

      String escapeHtmlStr = CopyUtil.checkImgforWord(Encodes.unescapeHtml(replaceContent), null, this.webpath);
      article.getArticleData().setContent(Encodes.escapeHtml(escapeHtmlStr));
      this.logger.info("begin down convert mobile web img");
      CopyUtil.WordtoMobileImg(StringEscapeUtils.unescapeHtml4(article.getArticleData().getContent()), request);
    }

    if (article.getIstime().equals("1")) {
      article.setDelFlag("2");
    }
    this.log.info("posid=====" + article.getPosid());

    this.log.info("设置默认模板版");
    Category category = (Category)this.categoryService.get(article.getCategory().getId());
    article.setCustomContentView(category.getCustomContentView());

    this.articleService.save(article);

    Thread thread = new StaticPageThread(this.runStatic, this.mobileStatic, article, this.updatePageService);
    System.out.println("--------------------------------------------------------------------------------articleId :"+article.getId());
    thread.start();
    

    addMessage(redirectAttributes, new String[] { "保存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功" });

    return "redirect:" + this.adminPath + "/cms/article/" + listType + "?repage&pageNo=" + request.getParameter("pageNo");
  }

  @RequiresPermissions({"cms:article:edit"})
  @RequestMapping({"delete"})
  public String delete(Article article, String categoryId, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes, String id) {
    if (!UserUtils.getSubject().isPermitted("cms:article:audit")) {
      addMessage(redirectAttributes, new String[] { "你没有删除或发布权限" });
    }
    Article article2 = get(id);
    article2.setArticleData((ArticleData)this.articleDataService.get(article2.getId()));
    if ((isRe != null) && (isRe.booleanValue()))
      article2.setDelFlag("0");
    else {
      article2.setDelFlag("1");
    }
    this.articleService.save(article2);

    Thread thread = new StaticPageThread(this.runStatic, this.mobileStatic, article2, this.updatePageService);
    thread.start();

    addMessage(redirectAttributes, new String[] { ((isRe != null) && (isRe.booleanValue()) ? "发布" : "删除") + "文章成功" });
    return "redirect:" + this.adminPath + "/cms/article/?repage&category.id=" + (categoryId != null ? categoryId : "");
  }

  @RequiresPermissions({"cms:article:view"})
  @RequestMapping({"selectList"})
  public String selectList(Article article, HttpServletRequest request, HttpServletResponse response, Model model, String flag) {
    list(article, request, response, model, flag);
    return "modules/cms/articleSelectList";
  }
  @RequiresPermissions({"cms:article:view"})
  @ResponseBody
  @RequestMapping({"findByIds"})
  public String findByIds(String ids) {
    List list = this.articleService.findByIds(ids);
    return JsonMapper.nonDefaultMapper().toJson(list);
  }

  private List<String> getTplContent()
  {
    List tplList = this.fileTplService
      .getNameListByPrefix(
      ((Site)this.siteService
      .get(Site.getCurrentSiteId())).getSolutionPath());
    tplList = TplUtils.tplTrim(tplList, "frontViewArticle", "", new String[0]);
    return tplList;
  }

  @RequiresPermissions({"cms:article:view"})
  @RequestMapping({"posiarticlelist"})
  public String posiarticlelist(PositionArticle positionArticle, HttpServletRequest request, HttpServletResponse response, Model model) {
    this.log.info("into posiarticle");
    if (positionArticle.getArticle() == null)
      positionArticle.setArticle(new Article());
    else {
      this.log.info("into posiarticle2===" + positionArticle.getArticle().getDelFlag());
    }
    if (positionArticle.getArticle().getMsgsource() == null) {
      positionArticle.getArticle().setMsgsource(new Msgsource());
    }

    this.log.info("查询的posid为========" + positionArticle.getPosid());
    User user = UserUtils.getUser();
    Attention attention = new Attention();
    attention.setType("0");
    if (StringUtils.isNotBlank(user.getId())) {
      attention.setCreateBy(user);
    }
    Attention attention2 = new Attention();
    attention2.setType("1");
    if (StringUtils.isNotBlank(user.getId())) {
      attention2.setCreateBy(user);
    }
    List attentionList = this.attentionService.findList(attention);
    List attentionList2 = this.attentionService.findList(attention2);

    if (positionArticle.getArticle().getCreateBy() != null) {
      this.log.info("article.createby.name==" + positionArticle.getArticle().getCreateBy().getName());
    }
    this.log.info("managerid:" + positionArticle.getArticle().getCreateBy());

    Page page = this.articleService.findposiarticlePage2(new Page(request, response), positionArticle, true);

    this.log.info("Article.getcategroy==" + positionArticle.getArticle().getCategory().getName());
    Category category = new Category();
    if (positionArticle.getArticle().getCategory() != null) {
      category = positionArticle.getArticle().getCategory();
    }
    Position position = new Position();

    if ((positionArticle.getPosid() != null) && (!positionArticle.getPosid().trim().equals(""))) {
      this.log.info("into posid");

      if ((positionArticle.getPosition() != null) && (positionArticle.getPosition().getDescription() != null) && 
        (positionArticle
        .getPosition().getPosid() != null)) {
        position.setDescription(positionArticle.getPosition().getDescription());
        position.setPosid(positionArticle.getPosition().getPosid());
      }
    }

    String delflag = "";
    if (positionArticle.getArticle() != null) {
      System.out.println("查询状态为:" + positionArticle.getArticle().getDelFlag());
    }
    System.out.println("category=====" + category.getId());
    System.out.println("category=====" + category.getName());
    model.addAttribute("page", page);
    model.addAttribute("position", position);
    model.addAttribute("category", category);
    model.addAttribute("attentionList", attentionList);
    model.addAttribute("attentionList2", attentionList2);
    model.addAttribute("usersmap", getUserMap(UserUtils.getUser()));
    this.log.info("positionArticle.getArticle().getDelFlag()===" + positionArticle.getArticle().getDelFlag());
    model.addAttribute("delFlag", positionArticle.getArticle().getDelFlag());
    this.log.info("into posiarticle end");
    return "modules/cms/posiarticleList";
  }
  @RequiresPermissions({"cms:article:edit"})
  @RequestMapping({"posisave"})
  public String posisave(Article article, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    if (!beanValidator(model, article, new Class[0])) {
      return form(article, request.getParameter("pageNo"), model);
    }
    if (article.getIstime().equals("1")) {
      article.setDelFlag("2");
    }
    if ((article.getImage().indexOf("http") > -1) || (article.getImage().indexOf("https") > -1)) {
      this.logger.info("is web img");
      try {
        article.setImage(CopyUtil.downimg(article.getImage(), "/imgdown").replace(this.webpath, ""));
      }
      catch (IOException e) {
        e.printStackTrace();
        this.logger.info("down false");
      }
    } else {
      this.logger.info("not web img");
    }
    this.logger.info("替换keyworks");
    article.setKeywords(article.getKeywords().replace(" ", ","));

    this.logger.info("生成缩略图");
    CopyUtil.toMobileImg(request, article.getImage());
    if (article.getArticleData().getContent() != null) {
      List<Sensitive> sensitivelist = this.sensitiveService.findList(new Sensitive());
      this.log.info("校验敏感词");
      this.log.info("栏目为：" + article.getCategory().getId());
      for (Sensitive sensitive : sensitivelist) {
        this.log.info("校验敏感词===" + article.getArticleData().getContent().indexOf(sensitive.getValue()));
        if (article.getArticleData().getContent().indexOf(sensitive.getValue()) > -1) {
          addMessage(redirectAttributes, new String[] { "保存文章'" + 
            StringUtils.abbr(article
            .getTitle(), 50) + "'失败，文章内容存在敏感词" });
          return form(article, request.getParameter("pageNo"), model);
        }
      }

      this.logger.info("begin down web img");
      article.getArticleData()
        .setContent(Encodes.escapeHtml(CopyUtil.checkImgforWord(
        Encodes.unescapeHtml(article
        .getArticleData().getContent().replace("_ueditor_page_break_tag_", "<hr style=\"page-break-after:always;\" class=\"ke-pagebreak\" />")), null, this.webpath)));

      this.logger.info("begin down convert mobile web img");
      CopyUtil.WordtoMobileImg(StringEscapeUtils.unescapeHtml4(article.getArticleData().getContent()), request);
    }
    if (article.getIstime().equals("1")) {
      article.setDelFlag("2");
    }

    this.articleService.save(article);
    addMessage(redirectAttributes, new String[] { "保存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功" });
    String categoryId = article.getCategory() != null ? article.getCategory().getId() : null;

    return "redirect:" + this.adminPath + "/cms/article/posiarticlelist?repage";
  }

  @RequiresPermissions({"cms:article:view"})
  @RequestMapping({"posiform"})
  public String posiform(Article article, Model model) {
    CmsUtils.addViewConfigAttribute(model, article.getCategory());
    if ((article.getCategory() != null) && (StringUtils.isNotBlank(article.getCategory().getId()))) {
      List list = this.categoryService.findByParentId(article.getCategory().getId(), 
        Site.getCurrentSiteId());

      if (list.size() > 0)
        article.setCategory(null);
      else {
        article.setCategory((Category)this.categoryService.get(article.getCategory().getId()));
      }
    }

    Attention attention = new Attention();
    attention.setType("1");
    attention.setCreateBy(UserUtils.getUser());
    List attlist = this.attentionService.findList(attention);

    String posid = article.getPosid();
    String[] ps = posid.split(",");
    List posids = new ArrayList();
    if ((ps != null) && (ps.length > 0)) {
      for (String s : ps) {
        posids.add(s);
      }
    }

    Attention attention2 = new Attention();
    User user2 = UserUtils.getUser();
    attention2.setCreateBy(user2);
    attention2.setType("0");
    List categorylist = this.attentionService.findList(attention2);

    article.setArticleData((ArticleData)this.articleDataService.get(article.getId()));

    model.addAttribute("contentViewList", getTplContent());
    model.addAttribute("article_DEFAULT_TEMPLATE", "frontViewArticle");
    model.addAttribute("article", article);
    model.addAttribute("attlist", attlist);
    model.addAttribute("categorylist", categorylist);
    model.addAttribute("posid", posids);
    model.addAttribute("listType", "posiarticlelist");
    return "modules/cms/posiarticleForm"; } 
  @RequiresUser
  @ResponseBody
  @RequestMapping({"treeData"})
  public List<Map<String, Object>> treeData(String module, @RequestParam(required=false) String extId, HttpServletResponse response) { response.setContentType("application/json; charset=UTF-8");
    List mapList = Lists.newArrayList();
    Position position = new Position();

    List list = this.positionDao.findAlltwo(position);
    this.log.info("推荐位==" + list.size());
    for (int i = 0; i < list.size(); i++) {
      Position e = (Position)list.get(i);
      if ((extId == null) || ("".equals(extId))) {
        Map map = Maps.newHashMap();
        map.put("id", e.getPosid());

        map.put("pId", Integer.valueOf(0));
        map.put("name", e.getDescription());
        map.put("module", "article");
        mapList.add(map);
      }
    }

    return mapList; } 
  @RequiresUser
  @ResponseBody
  @RequestMapping({"treeData2"})
  public List<Map<String, Object>> treeData2(String module, @RequestParam(required=false) String extId, HttpServletResponse response) { response.setContentType("application/json; charset=UTF-8");
    List mapList = Lists.newArrayList();
    Attention attention = new Attention();
    attention.setType("1");
    List list = this.attentionService.findList(attention);
    this.log.info("关注的推荐==" + list.size());
    for (int i = 0; i < list.size(); i++) {
      Attention e = (Attention)list.get(i);
      if ((extId == null) || ("".equals(extId))) {
        Map map = Maps.newHashMap();
        map.put("id", e.getPosition().getPosid());

        map.put("pId", Integer.valueOf(0));
        map.put("name", e.getPosition().getDescription());
        map.put("module", "article");
        mapList.add(map);
      }
    }

    return mapList;
  }

  @RequestMapping({"upSort"})
  public String upSort(String id, String posid, String beforeid, String afterid, String delFlag, RedirectAttributes redirectAttributes)
  {
    this.log.info("要升序的id===" + id);
    this.log.info("前一条的id===" + beforeid + "=====");

    String flag = delFlag;
    if (Global.isDemoMode().booleanValue()) {
      addMessage(redirectAttributes, new String[] { "演示模式，不允许操作！" });
      return "redirect:" + this.adminPath + "/cms/article/posiarticlelist?repage";
    }
    if ((beforeid != null) && (beforeid.trim() != "")) {
      PositionArticle positionArticle = new PositionArticle();
      Category category = new Category();
      Article article = new Article();

      article.setDelFlag(delFlag);
      article.setCategory(category);
      positionArticle.setPosid(posid);
      positionArticle.setArticleId(id);
      positionArticle.setArticle(article);

      List list1 = this.positionArticleDao.findAll2(positionArticle);
      positionArticle.setArticleId(beforeid);
      List list2 = this.positionArticleDao.findAll2(positionArticle);
      if ((list1 != null) && (list1.size() > 0) && (list2 != null) && (list2.size() > 0)) {
        PositionArticle pa1 = (PositionArticle)list1.get(0);
        PositionArticle pa2 = (PositionArticle)list2.get(0);
        this.log.info("需要的升序的文章的number===" + pa1.getNumber());
        this.log.info("前面的文章的number===" + pa2.getNumber());
        this.log.info("需要的升序的文章的number===" + pa1.getArticle().getTitle());
        this.log.info("前面的文章的number===" + pa2.getArticle().getTitle());
        Integer number = pa1.getNumber();
        pa1.setNumber(pa2.getNumber());
        pa2.setNumber(number);
        try
        {
          this.positionArticleDao.update(pa1);
          this.positionArticleDao.update(pa2);
          Article article2 = get(id);
          Thread thread = new StaticPageThread(this.runStatic, this.mobileStatic, article2, this.updatePageService);
          thread.start();
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
        addMessage(redirectAttributes, new String[] { "已经是最顶位置了!" });
        return "redirect:" + this.adminPath + "/cms/article/posiarticlelist?repage&posid=" + posid + "&article.delFlag=" + flag;
      }

    }

    addMessage(redirectAttributes, new String[] { "保存文章排序成功!" });
    return "redirect:" + this.adminPath + "/cms/article/posiarticlelist?repage&posid=" + posid + "&article.delFlag=" + flag;
  }

  @RequestMapping({"downSort"})
  public String downSort(String id, String posid, String beforeid, String afterid, String delFlag, RedirectAttributes redirectAttributes)
  {
    String flag = delFlag;
    if (Global.isDemoMode().booleanValue()) {
      addMessage(redirectAttributes, new String[] { "演示模式，不允许操作！" });
      return "redirect:" + this.adminPath + "/cms/article/posiarticlelist?repage";
    }
    if ((afterid != null) && (afterid.trim() != "")) {
      PositionArticle positionArticle = new PositionArticle();
      Category category = new Category();
      Article article = new Article();

      article.setDelFlag(delFlag);
      article.setCategory(category);
      positionArticle.setPosid(posid);
      positionArticle.setArticleId(id);
      positionArticle.setArticle(article);
      List list1 = this.positionArticleDao.findAll2(positionArticle);
      positionArticle.setArticleId(afterid);
      List list2 = this.positionArticleDao.findAll2(positionArticle);
      this.log.info("list1.size==" + list1.size());
      if ((list1 != null) && (list1.size() > 0) && (list2 != null) && (list2.size() > 0))
      {
        PositionArticle pa1 = (PositionArticle)list1.get(0);
        PositionArticle pa2 = (PositionArticle)list2.get(0);
        this.log.info("需要的降序的文章的number===" + pa1.getNumber());
        this.log.info("后面的文章的number===" + pa2.getNumber());

        Integer number = pa1.getNumber();
        pa1.setNumber(pa2.getNumber());
        pa2.setNumber(number);
        try
        {
          this.positionArticleDao.update(pa1);
          this.positionArticleDao.update(pa2);
          Article article2 = get(id);
          Thread thread = new StaticPageThread(this.runStatic, this.mobileStatic, article2, this.updatePageService);
          thread.start();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } else {
      addMessage(redirectAttributes, new String[] { "已经是最低位置了!" });
      return "redirect:" + this.adminPath + "/cms/article/posiarticlelist?repage&posid=" + posid + "&article.delFlag=" + flag;
    }

    this.log.info("article.delflag===" + flag);
    addMessage(redirectAttributes, new String[] { "保存文章排序成功!" });

    return "redirect:" + this.adminPath + "/cms/article/posiarticlelist?repage&posid=" + posid + "&article.delFlag=" + flag;
  }

  public Map<String, String> getUserMap(User user)
  {
    Map userMap = new LinkedHashMap();
    if (user != null) {
      User queryuser = new User();
      user.setOffice(user.getOffice());
      List<User> users = this.systemService.findUser(queryuser);
      userMap.put("", "全部");
      for (User u : users)
      {
        if (!user.getLoginName().equals(u.getLoginName())) {
          userMap.put(u.getId(), u.getName());
        }
      }
    }

    return userMap;
  }

  @RequestMapping({"updateNumber"})
  public String updateNumber(PositionArticle positionArticle, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request)
  {
    this.log.info("into updateNumber");
    this.log.info("positionArticle.posid===" + positionArticle.getPosid());
    if (Global.isDemoMode().booleanValue()) {
      addMessage(redirectAttributes, new String[] { "演示模式，不允许操作！" });
      return "redirect:" + this.adminPath + "/cms/article/posiarticlelist?repage";
    }
    if ((positionArticle.getIds() != null) && (positionArticle.getIds().length > 0)) {
      System.out.println("positionArticle.ids====" + positionArticle.getIds().length);
      for (String sid : positionArticle.getIds()) {
        PositionArticle pa1 = (PositionArticle)this.positionArticleDao.get(sid);

        this.articleService.PositionArticleForNumber(pa1);
      }
    }

    addMessage(redirectAttributes, new String[] { "发布文章成功!" });
    return "redirect:" + this.adminPath + "/cms/article/posiarticlelist?repage";
  }

  @RequestMapping({"stick"})
  public String stick(Article article, Model model) {
    return "modules/cms/stickForm";
  }

  @RequiresPermissions({"cms:article:view"})
  @RequestMapping({"updateWeightdate"})
  public String updateWeightdate(String id, String weightDate, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    Article article = this.articleService.findArticalById(id);
    System.out.println(article.toString());
    Date date = DateUtils.parseDate(weightDate);
    article.setWeightDate(date);

    int flag = this.articleService.updateArticle(article);
    System.out.println(flag);
    addMessage(redirectAttributes, new String[] { "修改置顶时间成功" });
    return "redirect:" + this.adminPath + "/cms/article/posiarticlelist?repage";
  }

  @RequestMapping({"staticArticle"})
  public String staticArticle(Article article, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    article = (Article)this.articleDao.get(article.getId());
    article.setIsStatis("0");
    article.setIsMobileStatis("0");
    this.articleDao.update(article);
    this.runStatic.manualStatic(article.getId());
    this.mobileStatic.manualStatic(article.getId());
    return "redirect:" + this.adminPath + "/cms/article/list";
  }

  @RequestMapping({"listStatic"})
  public String listStatic(Article article, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    CmsUpdatePage updatePage = new CmsUpdatePage();
    updatePage.setId("3");
    updatePage.setType("4");
    updatePage.setCategoryId("0");
    updatePage.setIsUpdate("1");
    updatePage.setIsMobileUpdate("1");

    this.runStatic.listPublic(updatePage);
    this.mobileStatic.listPublic(updatePage);
    return "redirect:" + this.adminPath + "/cms/article/list?repage";
  }

  @RequestMapping({"articleStatic"})
  public String articleStatic(Article article, RedirectAttributes redirectAttributes, HttpServletRequest request) {
    this.articleService.updateNoneStatic();
    return "redirect:" + this.adminPath + "/cms/article/list?repage";
  }

  @RequestMapping({"manualStatic"})
  public void manualStatic(String id) {
    this.mobileStatic.manualStatic(id);
  }

  @RequestMapping({"testSort"})
  public void testSort() {
    this.runStatic.articlePublic("1");
  }

  @RequestMapping({"manualStaticIndex"})
  public void manualStaticIndex() {
    CmsUpdatePage updatePage = this.updatePageService.get("2");
    this.mobileStatic.indexPublic(updatePage);
  }
}