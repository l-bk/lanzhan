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
		<li class="active"><a href="${ctx}/cms/attention/list?type=${type}">
		<c:choose>
			<c:when test="${type=='0'}">
			关注栏目列表
			</c:when>
			<c:when test="${type=='1'}">
			关注推荐位列表
			</c:when>
			<c:when test="${type=='2'}">
			关注信息源列表
			</c:when>
		</c:choose>
		</a></li>
		<li><a href="${ctx}/cms/attention/form?type=${type}">
		
		<c:choose>
			<c:when test="${type=='0'}">
			关注栏目添加
			</c:when>
			<c:when test="${type=='1'}">
			关注推荐位添加
			</c:when>
			<c:when test="${type=='2'}">
			关注信息源添加
			</c:when>
		</c:choose>
		</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="attention" action="${ctx}/cms/attention/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>标题：</label>
			<input type="hidden" name="type" value="${type}"/>
			<input tyep="text" name="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;

		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<c:if test="${type=='0'}">
		<th>栏目</th>
		</c:if>
		<c:if test="${type=='1'}">
		<th>推荐位</th>
		</c:if>
		<c:if test="${type=='2'}">
		<th>信息源</th>
		</c:if>
		<th>发布者</th><th>更新时间</th><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="attention">
			<tr>
				<td>
				<c:choose>
				<c:when test="${type=='0'}">
				<a href="${ctx}/cms/attention/form?id=${attention.aid}&type=${type}" >${attention.category.name}</a>
				</c:when>
				<c:when test="${type=='1'}">
				<a href="${ctx}/cms/attention/form?id=${attention.aid}&type=${type}" >${attention.position.description}</a>
				</c:when>
				<c:when test="${type=='2'}">
				<a href="${ctx}/cms/attention/form?id=${attention.aid}&type=${type}" >${attention.msgsource.name}</a>
				</c:when>
				</c:choose>
				</td>
				
				<td>${attention.user.name}</td>
				<td><fmt:formatDate value="${attention.updateDate}" type="both"/></td>
				<td>
					
					
						
	    				<a href="${ctx}/cms/attention/form?aid=${attention.aid}&type=${type}">修改</a>
	    				<a href="${ctx}/cms/attention/delete?aid=${attention.aid}&type=${type}" onclick="return confirmx('确认要删除该关注项吗？', this.href)">删除</a>
					
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>