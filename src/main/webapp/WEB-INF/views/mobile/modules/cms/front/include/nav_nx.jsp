<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<%-- <div id="J_Nav" class="nav">
	<!-- <a href="/app" target="_bank">头条</a> -->
	<c:forEach items="${fnc:getChildCatagory2(site.id,'15')}" var="t" varStatus="i">
		<c:forEach items="${t.childList}" var="c">
			<a href="${c.url}" target="_bank">${c.remarks}</a>
		</c:forEach>
	</c:forEach>
</div> --%>

<ul class="nav">
	<c:forEach items="${fnc:getChildCatagory2(site.id,'15')}" var="t" varStatus="i">
		<c:forEach items="${t.childList}" var="c">
			<li id="${c.nameEn}"><a href="${c.url}" target="_bank">${c.remarks}</a></li>
		</c:forEach>
	</c:forEach>
</ul>
