<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
	<title>文章管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(function(){
			$(document).on('click', '[data-act="time"]', function(){
				$("#beginDate").val($(this).data('begindate'));
				$("#endDate").val($(this).data('enddate'));
				$("#searchForm").submit();
			});
			$(document).on('click', '#seniorSubmit', function(){
				console.log($("#flag").val());
				$("#flag").val("y");
				$("#searchForm").submit();
			});
			$(document).on('click', '#btnSubmit', function(){
				console.log($("#flag").val());
				$("#createBy").val("${(fns:getUser()).id}");
				$("#searchForm").submit();
			});
		});
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
	<style type="text/css">
		.search-condition{
			margin: 10px 0;
		}
		.search-condition a{
			cursor: pointer;
		}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/article/?category.id=${article.category.id}">文章列表</a></li>
		<shiro:hasPermission name="cms:article:edit"><li><a href="<c:url value='${fns:getAdminPath()}/cms/article/form?id=${article.id}&category.id=${article.category.id}'><c:param name='category.name' value='${article.category.name}'/></c:url>">文章添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="article" action="${ctx}/cms/article/" method="post" class="breadcrumb form-horizontal">
		<div class="search-condition">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<input id="flag" name="flag" type="hidden" value="n"/>
			<label>栏目：</label><sys:treeselect id="category" name="category.id" value="${article.category.id}" labelName="category.name" labelValue="${article.category.name}"
						title="栏目" url="/cms/category/treeData" module="article" notAllowSelectRoot="false" cssClass="input-small"/>
			<label>标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
			<label>状态：</label><form:radiobuttons onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
		</div>
		<div class="search-condition">
			<label>定置时间: </label>
			<a data-act="time" data-begindate="${fnc:getDefinedTime(0, 9999, 9999, 9999)}" data-enddate="${fnc:getDefinedTime(0, 0, 0, 0)}">当天</a> |
			<a data-act="time" data-begindate="${fnc:getDefinedTime(-1, 9999, 9999, 9999)}" data-enddate="${fnc:getDefinedTime(0, 9999, 9999, 9999)}">昨天</a> | 
			<a data-act="time" data-begindate="${fnc:getDefinedTime(-3, 9999, 9999, 9999)}" data-enddate="${fnc:getDefinedTime(0, 9999, 9999, 9999)}">最近三天</a>
			<label> </label>
			<input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${article.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			-
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${article.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			<input id="seniorSubmit" class="btn btn-primary" type="button" value="高级查询"/>
			<span class="help-inline">查询当前用户发布的文章</span>
		</div>
		<div class="search-condition">
			<label>ID查询: </label>
			<input id="id" name="id" type="text" maxlength="20" class="input-small" value=""/>
			<label>管理员: </label>
			<form:select id="createBy" path="createBy" class="input-medium">
				<form:option value="${(fns:getUser()).id}" label="">${(fns:getUser()).name}</form:option>
				<form:options items="${usersmap}" htmlEscape="false"/></form:select>
			<!-- <input id="static" class="btn btn-primary" type="button" value="栏目页静态化" style="float: right"/> -->
			<a href="${ctx}/cms/article/articleStatic" style="float: right;color:white;padding:5px 8px;background-color: #3daae9;border-radius:5px;margin-right: 10px;">文章页静态化</a>
			<a href="${ctx}/cms/article/listStatic" style="float: right;color:white;padding:5px 8px;background-color: #3daae9;border-radius:5px;margin-right: 10px;">栏目页静态化</a>&nbsp;&nbsp;&nbsp;&nbsp;
		
		 </div>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>栏目</th><th>标题</th><th>发布状态</th><th>点击数</th><th>发布者</th><th>发布时间</th><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="article">
			<tr>
				<td><a href="javascript:" onclick="$('#categoryId').val('${article.category.id}');$('#categoryName').val('${article.category.name}');$('#searchForm').submit();return false;">${article.category.name}</a></td>
				<td><a href="${ctx}/cms/article/form?id=${article.id}&pageNo=${page.pageNo}" title="${article.title}">${fns:abbr(article.title,40)}</a></td>
				<td>
				<c:choose>
					<c:when test="${article.delFlag=='0'}">
					已人工发布
					</c:when>
					<c:when test="${article.delFlag=='1'}">
					已撤稿
					</c:when>
					<c:when test="${article.delFlag=='2'}">
					未发布
					</c:when>
					<c:when test="${article.delFlag=='3'}">
					已自动发布
					</c:when>
				</c:choose>
				

				</td>
				<td>${article.hits}</td>
				<td>${article.user.name}</td>
				<td><fmt:formatDate value="${article.createDate}" type="both"/></td>
				<td>
				<%-- 	<a href="${article.url}" target="_blank">访问</a> --%>
					<a href="${article.staticUrl}" target="_blank">静态页面访问</a>
					<%-- <a href="${ctx}/cms/article/staticRequest?id=${article.id}" target="_blank">静态页面访问</a> --%>
					<a href="${ctx}/cms/article/staticArticle?id=${article.id}" target="_self">立即静态化</a>
					<shiro:hasPermission name="cms:article:edit">
						<c:if test="${article.category.allowComment eq '1'}"><shiro:hasPermission name="cms:comment:view">
							<a href="${ctx}/cms/comment/?module=article&contentId=${article.id}&delFlag=2" onclick="return viewComment(this.href);">评论</a>
						</shiro:hasPermission></c:if>
						
		    				<a href="${ctx}/cms/article/form?id=${article.id}&pageNo=${page.pageNo}">修改</a>
		    				<shiro:hasPermission name="cms:article:audit">
								<!-- <a href="${ctx}/cms/article/delete?id=${article.id}${article.delFlag ne 0?'&isRe=true':''}&categoryId=${article.category.id}" onclick="return confirmx('确认要${article.delFlag ne 0||article.delFlag ne 3?'反撤稿':'撤稿'}该文章吗？', this.href)" >${article.delFlag == 0||article.delFlag == 3?'撤稿':'反撤稿'}</a> -->

								<a href="${ctx}/cms/article/delete?id=${article.id}${article.delFlag ne 0&&article.delFlag ne 3?'&isRe=true':''}&categoryId=${article.category.id}" onclick="return confirmx('确认要${article.delFlag ne 0&&article.delFlag ne 3?'发布':'撤稿'}该文章吗？', this.href)" >
								<c:choose>
									<c:when test="${article.delFlag==0||article.delFlag==3}">
									撤稿
									</c:when>
									<c:when test="${article.delFlag==2}">
									发布
									</c:when>
									<c:when test="${article.delFlag==1}">
									反撤稿
									</c:when>
								</c:choose>
								</a>
							</shiro:hasPermission>
						
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>