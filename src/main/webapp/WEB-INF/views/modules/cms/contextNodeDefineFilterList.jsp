<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>采集过滤管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
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
		<li ><a href="${ctx}/cms/copynode/list?parentid=${parentid}">返回</a></li>
		<li class="active"><a href="${ctx}/copy/contextNodeDefineFilter/">采集过滤列表</a></li>
		<shiro:hasPermission name="copy:contextNodeDefineFilter:edit"><li><a href="${ctx}/copy/contextNodeDefineFilter/form?nodeDefineId=${param.nodeDefineId }&parentid=${parentid}">采集过滤添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="contextNodeDefineFilter" action="${ctx}/copy/contextNodeDefineFilter/" method="post" class="breadcrumb form-search">
		<%-- <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/> --%>
		<%-- <ul class="ul-form">
			<li><label>过滤规则标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul> --%>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>过滤规则标题</th>
				<th style ="width: 40%">开始表达式</th>
				<th style ="width: 10%">结束表达式</th>
				<th>类型:</th>
				<th>创建时间</th>
				<shiro:hasPermission name="copy:contextNodeDefineFilter:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="contextNodeDefineFilter">
			<tr>
				<td><a href="${ctx}/copy/contextNodeDefineFilter/form?cid=${contextNodeDefineFilter.cid}&nodeDefineId=${contextNodeDefineFilter.nodeDefineId}">
					${contextNodeDefineFilter.title}
				</a></td>
				<td>
					${contextNodeDefineFilter.begin}
				</td>
				<td>
					${contextNodeDefineFilter.end}
				</td>
				<td>
				${fns:getDictLabel(contextNodeDefineFilter.type, 'context_node_filter', '无')}
				</td>
				<td>
					<fmt:formatDate value="${contextNodeDefineFilter.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="copy:contextNodeDefineFilter:edit"><td>
    				<a href="${ctx}/copy/contextNodeDefineFilter/form?cid=${contextNodeDefineFilter.cid}&nodeDefineId=${contextNodeDefineFilter.nodeDefineId}&parentid=${parentid}">修改</a>
					<a href="${ctx}/copy/contextNodeDefineFilter/delete?cid=${contextNodeDefineFilter.cid}&nodeDefineId=${contextNodeDefineFilter.nodeDefineId}&parentid=${parentid}" onclick="return confirmx('确认要删除该采集过滤吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<%-- <div class="pagination">${page}</div> --%>
</body>
</html>