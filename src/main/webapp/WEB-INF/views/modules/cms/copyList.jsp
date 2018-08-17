<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>数据采集管理</title>
<meta name="decorator" content="default" />
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
		$("#treeTable").treeTable({
			expandLevel : 3
		});
	});
	function updateSort() {
		loading('正在提交，请稍等...');
		$("#listForm").attr("action", "${ctx}/cms/category/updateSort");
		$("#listForm").submit();
	}
</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/copy/">采集站点列表</a></li>
		<shiro:hasPermission name="cms:category:edit"><li><a href="${ctx}/cms/copy/form">站点添加</a></li></shiro:hasPermission>
	</ul>
	<sys:message content="${message}" />
	<form id="listForm" method="post">
		<table id="treeTable"
			class="table table-striped table-bordered table-condensed">
			<tr>
				<th>采集站点名称</th>
				<th>采集站点一级域名</th>
				<th>采集网站的编码</th>
				<th>文章页的编码</th>
				<th>是否直接入库</th>
				<th style="text-align:center;">定时采集时间</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${list}" var="tpl">
				<tr id="${tpl.cid}" pId="0">
					<td><a href="${ctx}/cms/copy/form?cid=${tpl.cid}">${tpl.description}</a></td>
					<td>${tpl.url}</td>
					<td>${tpl.lang}</td>
					<td>${tpl.contentLang}</td>
					<td>${tpl.ischeckflag}</td>
					<td>${tpl.beginTime}</td>
					
					<td><a
						href="${pageContext.request.contextPath}${fns:getFrontPath()}/list-${tpl.id}${fns:getUrlSuffix()}"
						target="_blank">访问</a> <shiro:hasPermission
							name="cms:category:edit">
							<a href="${ctx}/cms/copy/form?cid=${tpl.cid}">修改</a>
							<a href="${ctx}/cms/category/delete?id=${tpl.id}"
								onclick="return confirmx('要删除该栏目及所有子栏目项吗？', this.href)">删除</a>
							<a href="${ctx}/cms/copynode/list?parentid=${tpl.cid}">采集规则</a>
						</shiro:hasPermission></td>
				</tr>
			</c:forEach>
		</table>
		<shiro:hasPermission name="cms:category:edit">
			<div class="form-actions pagination-left">
				<input id="btnSubmit" class="btn btn-primary" type="button"
					value="保存排序" onclick="updateSort();" />
			</div>
		</shiro:hasPermission>
	</form>
</body>
</html>