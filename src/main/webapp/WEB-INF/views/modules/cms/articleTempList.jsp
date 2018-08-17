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
        
        function bPost(){
        	// $("#postForm").attr("action","${ctx}/cms/articleTemp/post");
        	// alert("into post");
        	
			if ($('input:checked').length > 0) {
  				$("#postForm").submit();
			} else {
   				alert('请勾选要发布的文章');
			}
        	
        }
        function bPostAll(){
        	$("#postForm").attr("action","${ctx}/cms/articleTemp/postAll");
        	// alert("into post");
        	$("#postForm").submit();
        }
        $(function() {  
        	$("#checkall").click(function() {  
        if ($(this).attr("checked")) {  
            $("input[name=ids]").each(function() {  
                $(this).attr("checked", true);  
            });  
        } else {  
            $("input[name=ids]").each(function() {  
                $(this).attr("checked", false);  
            });  
        }  
    }); 
        });
	</script>
</head>
<style type="text/css">
</style>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/articleTemp/?articleTemp.id=${articleTemp.category.id}">文章列表</a></li>
		<!-- <shiro:hasPermission name="cms:article:edit"><li><a href="<c:url value='${fns:getAdminPath()}/cms/articleTemp/form?id=${articleTemp.id}&category.id=${articleTemp.category.id}'><c:param name='category.name' value='${articleTemp.category.name}'/></c:url>">文章添加</a></li></shiro:hasPermission> -->
	</ul>
	<form:form id="searchForm" modelAttribute="articleTemp" action="${ctx}/cms/articleTemp/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>栏目：</label><sys:treeselect id="category" name="category.id" value="${articleTemp.category.id}" labelName="category.name" labelValue="${articleTemp.category.name}"
					title="栏目" url="/cms/category/treeData" module="articleTemp" notAllowSelectRoot="false" cssClass="input-small"/>
		<label>标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		<!-- <label>状态：</label><form:radiobuttons onclick="$('#searchForm').submit();" path="delFlag" items="${fns:getDictList('cms_del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/> -->
		<input type="button" id="btnPostAll" value="发布所有文章" onclick="confirmx('是否发布全部文章?',bPostAll)" />
		<input type="button" id="btnPost" value="发布文章" onclick="confirmx('是否发布所勾选的文章?',bPost)" />
		<a href="${ctx}/cms/article/listStatic" style="float: right;color:white;padding:5px 8px;background-color: #3daae9;border-radius:5px">栏目页静态化</a>
	</form:form>
	<form:form id="postForm" modelAttribute="articleTemp" action="${ctx}/cms/articleTemp/post" method="post">
	<sys:messageNew content="${message}" />
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th><input type="checkbox" name="checkall" id="checkall"  /></th><th>栏目</th><th>标题</th><th>权重</th><th>点击数</th><th>发布者</th><th>更新时间</th><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="articleTemp">
			<tr>
				<td><input type="checkbox" name="ids" value="${articleTemp.id}"></td>
				<td><a href="javascript:" onclick="$('#categoryId').val('${articleTemp.category.id}');$('#categoryName').val('${articleTemp.category.name}');$('#searchForm').submit();return false;">${articleTemp.category.name}</a></td>
				<td><a href="${ctx}/cms/articleTemp/form?id=${articleTemp.id}" title="${articleTemp.title}">${fns:abbr(articleTemp.title,40)}</a></td>
				<td>${articleTemp.weight}</td>
				<td>${articleTemp.hits}</td>
				<td>${articleTemp.user.name}</td>
				<td><fmt:formatDate value="${articleTemp.updateDate}" type="both"/></td>
				<td>
					<a href="${articleTemp.acquisitionSource}" target="_blank">访问文章来源</a>
					<a href="/preview${articleTemp.url}" target="_blank">访问</a>
					<shiro:hasPermission name="cms:article:edit">
						<c:if test="${articleTemp.category.allowComment eq '1'}"><shiro:hasPermission name="cms:comment:view">
							<a href="${ctx}/cms/comment/?module=articleTemp&contentId=${articleTemp.id}&delFlag=2" onclick="return viewComment(this.href);">评论</a>
						</shiro:hasPermission></c:if>
	    				<a href="${ctx}/cms/articleTemp/form?id=${articleTemp.id}">修改</a>
	    				<!-- <shiro:hasPermission name="cms:article:audit">
							<a href="${ctx}/cms/articleTemp/delete?id=${articleTemp.id}${articleTemp.delFlag ne 0?'&isRe=true':''}&categoryId=${articleTemp.category.id}" onclick="return confirmx('确认要${articleTemp.delFlag ne 0?'反撤稿':'撤稿'}该文章吗？', this.href)" >${articleTemp.delFlag ne 0?'反撤稿':'撤稿'}</a>
						</shiro:hasPermission> -->
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	</form:form>
	<div class="pagination">${page}</div>
</body>
</html>