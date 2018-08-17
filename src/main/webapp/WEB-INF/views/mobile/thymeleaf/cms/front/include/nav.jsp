<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<div id="J_Nav" class="nav">
	<a href="/" target="_bank">头条</a>
	<c:forEach items="${fnc:getMainNavList('1')}" var="t" varStatus="i">
		<a href="${t.url}" target="_bank">${t.name}</a>
	</c:forEach>
</div>