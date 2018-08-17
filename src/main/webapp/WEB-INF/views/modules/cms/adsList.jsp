<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>广告位管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/ads">广告位列表</a></li>
		<%-- <shiro:hasPermission name="cms:ads:edit"> --%><li><a href="${ctx}/cms/ads/form">添加广告</a></li><%-- </shiro:hasPermission> --%>
	</ul>
	<form:form id="searchForm" modelAttribute="ads" action="${ctx}/cms/ads/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	</form:form>
	<sys:message content="${message}" />
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>广告标题</th>
				<th>类型</th>
				<th>广告位置</th>				
				<th>标签名</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="ads">
				<tr id="${ads.aid}" pId="0">
					<td><a href="${ctx}/cms/ads/form?aid=${ads.aid}">${ads.adname}</a></td>
					<td>${fns:getDictLabel(ads.typeid,'cms_ads_type','')}</td>
					<td>${fns:getDictLabel(ads.advPosid,'cms_ads_position','')}</td>
					<td>${ads.tagname}</td>
					<td>${ads.starttime}</td>
					<td>${ads.endtime}</td>
					
					<td>
						<%-- <a href="${pageContext.request.contextPath}${fns:getFrontPath()}/list-${tpl.id}${fns:getUrlSuffix()}" target="_blank">
							访问
						</a> --%>
						<%-- <shiro:hasPermission name=""> --%>
							<a href="${ctx}/cms/ads/form?aid=${ads.aid}">修改</a>
							<a href="${ctx}/cms/ads/delete?aid=${ads.aid}"
								onclick="return confirmx('要删除吗？', this.href)">删除</a>
							<%-- <a href="${ctx}/cms/copynode/form?parent.id=${tpl.id}">采集规则</a> --%>
						<%-- </shiro:hasPermission> --%>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
</body>
</html>