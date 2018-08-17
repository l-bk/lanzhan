<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<%@ attribute name="category" type="com.modules.cms.entity.Category"
	required="true" description="栏目对象"%>
<a href='/'>首页</a>>${category.name}> 正文
