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
	<li ><a href="${ctx}/cms/copy/list">返回</a></li>
		<li class="active"><a href="${ctx}/cms/copynode/">采集规则列表</a></li>
		<shiro:hasPermission name="cms:category:edit"><li><a href="${ctx}/cms/copynode/form?parentid=${param.parentid}">规则添加</a></li></shiro:hasPermission>
		<li><a href="${ctx}/cms/copy/">采集站点列表</a></li>
	</ul>
	<sys:message content="${message}" />
	<form id="listForm" method="post">
		<table id="treeTable"
			class="table table-striped table-bordered table-condensed">
			<tr>
				<th>采集点名称</th>
				<th>采集点地址</th>
				<th>采集条数</th>
				<th>所属栏目</th>
				<!-- <th style="text-align:center;">是否采集分页</th> -->
				<th>操作</th>
			</tr>
			<c:forEach items="${list}" var="tpl">
				<tr id="${tpl.cid}" pId="0">
					<td><a href="${ctx}/cms/category/form?id=${tpl.id}">${tpl.description}</a></td>
					<td>${tpl.copyUrl}</td>
					<td>${tpl.copyCount}</td>
				
					<td>${tpl.categoryId}</td>
					<!-- <td>${tpl.isSplitPage}</td> -->
					
					<td><a
						href="${pageContext.request.contextPath}${fns:getFrontPath()}/list-${tpl.id}${fns:getUrlSuffix()}"
						target="_blank">访问</a> <shiro:hasPermission
							name="cms:category:edit">
							<a href="${ctx}/cms/copynode/form?cid=${tpl.cid}">修改</a>
							<a href="${ctx}/cms/copynode/delete?id=${tpl.cid}"
								onclick="return confirmx('要删除该采集规则？', this.href)">删除</a>
							<a href="${ctx}/cms/copynode/collect?parentid=${tpl.parentid}&cid=${tpl.cid}">开始采集</a>
							<a href="${ctx}/copy/contextNodeDefineFilter/list?nodeDefineId=${tpl.cid}&parentid=${tpl.parentid}">采集过滤</a>
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