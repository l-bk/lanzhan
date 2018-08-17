<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>推荐位管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 3});
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
		<li class="active"><a href="${ctx}/cms/position/">推荐位列表</a></li>
		<shiro:hasPermission name="cms:category:edit"><li><a href="${ctx}/cms/position/form">推荐位添加</a></li></shiro:hasPermission>
	</ul>
	<sys:message content="${message}"/>
	<form id="listForm" method="post">
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<tr><th>推荐位名称</th><th>是否启用</th><th>位置</th><th style="text-align:center;">最后修改时间</th><th>操作</th></tr>
			<c:forEach items="${list}" var="tpl">
				<tr id="${tpl.posid}" pId="0">
					<td><a href="${ctx}/cms/position/form?id=${tpl.posid}">${tpl.description}</a></td>
					<td><c:if test="${tpl.stat=='1'}">是</c:if>
						<c:if test="${tpl.stat=='0'}">否</c:if>
					</td>
					<td>
						<c:if test="${tpl.type=='0'}">头条焦点图</c:if>
	                    <c:if test="${tpl.type=='1'}">头条top热榜</c:if>
	                    <c:if test="${tpl.type=='2'}">头条多张缩略图</c:if>
	                    <c:if test="${tpl.type=='3'}">头条单张缩略图</c:if>
	                    <c:if test="${tpl.type=='4'}">内页精彩图片</c:if>
	                    <c:if test="${tpl.type=='5'}">内页推荐阅读</c:if>
	                    <c:if test="${tpl.type=='6'}">内页(多缩略图)</c:if>
	                    <c:if test="${tpl.type=='7'}">头条焦点图(测试)</c:if>
	                    <c:if test="${tpl.type=='8'}">头条top热榜(测试)</c:if>
	                    <c:if test="${tpl.type=='9'}">头条多张缩略图(测试)</c:if>
	                    <c:if test="${tpl.type=='10'}">头条单张缩略图(测试)</c:if>
	                    <c:if test="${tpl.type=='11'}">内页精彩图片(测试)</c:if>
	                    <c:if test="${tpl.type=='12'}">内页推荐阅读(测试)</c:if>
	                    <c:if test="${tpl.type=='13'}">内页(多缩略图)(测试)</c:if>
	                    <c:if test="${tpl.type=='14'}">内页(女性内一推荐阅读)(测试)</c:if>
	                    <c:if test="${tpl.type=='15'}">内页(女性内二 多缩略图)(测试)</c:if>
					</td>
					<td style="text-align:center;">
						<fmt:formatDate value="${tpl.medifydate}" type="both"/>
					</td>
					
					<td>
						
						<shiro:hasPermission name="cms:category:edit">
							<a href="${ctx}/cms/position/form?posid=${tpl.posid}">修改</a>
							<a href="${ctx}/cms/position/delete?posid=${tpl.posid}" onclick="return confirmx('要删除该推荐位吗？', this.href)">删除</a>
							
						</shiro:hasPermission>
					</td>
				</tr>
			</c:forEach>
		</table>
		<!-- <shiro:hasPermission name="cms:category:edit"><div class="form-actions pagination-left">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
		</div></shiro:hasPermission> -->
	</form>
</body>
</html>