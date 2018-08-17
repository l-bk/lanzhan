<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>站点管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/templatefile/list?show=${param.show}">模板列表</a></li>

		<li><a href="${ctx}/cms/templatefile/form?show=${param.show}">模板添加</a></li>

	</ul>

	<sys:message content="${message}" />
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>名称</th>
				<th>文件名</th>
				<th>修改时间</th>
				<th>备注</th>
				<shiro:hasPermission name="cms:site:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="t">
				<tr>
					<td>${t.description}</td>
					<td>${t.templatefilename}</td>
					<td>${t.modifydate}</td>
					<td>${t.notes}</td>
					<shiro:hasPermission name="cms:site:edit">
						<td>
						<a href="http://www.gzlanzhan.cn${t.templatepath}" target="_blank">预览</a> 
						<a href="${ctx}/cms/templatefile/form?cid=${t.cid}&show=${param.show}">修改</a> 
						<%-- <a href="${ctx}/cms/templatefile/updateArticle?show=${param.show}&templatefilename=${t.templatefilename}">更新模板</a> 
						<c:set var="defultfront"
								value="frontComment.jsp,frontGuestbook.jsp,frontIndex.jsp,frontList.jsp,frontListCategory.jsp,frontMap.jsp,frontSearch.jsp,frontViewArticle.jsp" />
							<c:set var="check" value="true" /> <c:forEach var="tf" items="${fn:split(defultfront,',')}">
								<c:if test="${t.templatefilename==tf }">
									<c:set var="check" value="false" />
								</c:if>
							</c:forEach> <c:if test="${check==true}">
								<a href="${ctx}/cms/templatefile/delete?cid=${t.cid}&show=${param.show}" onclick="return confirmx('确认要删除该模板吗？', this.href)">删除</a>
							</c:if></td> --%>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>