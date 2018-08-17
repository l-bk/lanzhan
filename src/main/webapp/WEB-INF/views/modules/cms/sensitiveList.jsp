<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文章管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function viewComment(href){
			top.$.jBox.open('iframe:'+href,'查看评论',$(top.document).width()-220,$(top.document).height()-120,{
				buttons:{"关闭":true},
				loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
					$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
					$("body", h.find("iframe").contents()).css("margin","10px");
				}
			});
			return false;
		}
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
		<li class="active"><a href="${ctx}/cms/sensitive/">敏感词列表</a></li>
		<li><a href="${ctx}/cms/sensitive/form">敏感词添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="sensitive" action="${ctx}/cms/sensitive/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>标题：</label><input tyep="text" name="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		
		<th>敏感词</th>
		<th>发布者</th><th>更新时间</th><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="sensitive">
			<tr>
				<td><a href="${ctx}/cms/sensitive/form?sid=${sensitive.sid}" >${sensitive.value}</a></td>
				
				<td>${sensitive.user.name}</td>
				<td><fmt:formatDate value="${sensitive.updateDate}" type="both"/></td>
				<td>
					
					
						
	    				<a href="${ctx}/cms/sensitive/form?sid=${sensitive.sid}">修改</a>
	    				<a href="${ctx}/cms/sensitive/delete?sid=${sensitive.sid}" onclick="return confirmx('确认要删除该敏感词吗？', this.href)">删除</a>
					
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>