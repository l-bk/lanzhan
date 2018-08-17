<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>文件模版管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
<link href="/static/treeTable/themes/vsStyle/treeTable.min.css"
	rel="stylesheet" type="text/css" />
<script src="/static/treeTable/jquery.treeTable.min.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#treeTable").treeTable({
			expandLevel : 3
		});
	});
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/cms/defaulttemplatefile/list?show=${param.show}">文件模板列表</a></li>
	</ul>

	<sys:message content="${message}" />
	<table id="treeTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>文件名</th>
				<th>修改时间</th>
				<shiro:hasPermission name="cms:site:edit">
					<th>操作</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="t">
				<tr id="${t.templatefilename}" pId="0">
					<td>${t.templatefilename}</td>
					<td>${t.modifydate}</td>
					<td></td>
				</tr>
				<c:forEach var="c" items="${t.files }">

					<tr id="${t.templatefilename}_${c.templatefilename}" pId="${t.templatefilename}">
						<td>${c.templatefilename}</td>
						<td>${c.modifydate}</td>
						<shiro:hasPermission name="cms:site:edit">
							<td><a
								href="/templets/${c.parentDirname}/${c.templatefilename}.${c.suffix}"
								target="_blank">预览</a> <a
								href="${ctx}/cms/defaulttemplatefile/form?templatefilename=${c.templatefilename}&parentDirname=${t.parentDirname}&suffix=${c.suffix}">修改</a>
							<%-- 	<a
								href="${ctx}/cms/defaulttemplatefile/delete?templatefilename=${c.templatefilename}&parentDirname=${t.parentDirname}&suffix=${t.suffix}"
								onclick="return confirmx('确认要删除该模板吗？', this.href)">删除</a></td> --%>
						</shiro:hasPermission>
					</tr>
				</c:forEach>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>