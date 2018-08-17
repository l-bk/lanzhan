<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<%@ attribute name="category" type="com.modules.cms.entity.Category"
	required="true" description="栏目对象"%>
<%@ attribute name="pageSize" type="java.lang.Integer" required="false"
	description="页面大小"%>
<c:forEach
	items="${fnc:getArticleList(category.site.id, category.id, not empty pageSize?pageSize:8, 'posid:3, orderBy: \"hits desc\"')}"
	var="article" varStatus="i">
	<li><a href="${ctx}/view-${article.category.id}-${article.id}${urlSuffix}">
							<span class="tag">${i.index+1}</span>
								${fns:abbr(article.title,28)}
							</a></li>
	
</c:forEach>