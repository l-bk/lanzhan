<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>选择文章</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("input[name=id]").each(function(){
				var articleSelect = null;
				if (top.mainFrame.cmsMainFrame){
					articleSelect = top.mainFrame.cmsMainFrame.articleSelect;
				}else{
					articleSelect = top.mainFrame.articleSelect;
				}
				for (var i=0; i<articleSelect.length; i++){
					if (articleSelect[i][0]==$(this).val()){
						this.checked = true;
					}
				}
				$(this).click(function(){
					var id = $(this).val(), title = $(this).attr("title");
					if (top.mainFrame.cmsMainFrame){
						top.mainFrame.cmsMainFrame.articleSelectAddOrDel(id, title);
					}else{
						top.mainFrame.articleSelectAddOrDel(id, title);
					}
				});
			});
		});
		function view(href){
			top.$.jBox.open('iframe:'+href,'查看文章',$(top.document).width()-220,$(top.document).height()-120,{
				buttons:{"关闭":true},
				loaded:function(h){
					$(".jbox-content", top.document).css("overflow-y","hidden");
					$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
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
	<div style="margin:10px;">
	<form:form id="searchForm" modelAttribute="articleTemp" action="${ctx}/cms/articleTemp/selectList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>栏目：</label><sys:treeselect id="category" name="category.id" value="${articleTemp.category.id}" labelName="category.name" labelValue="${articleTemp.category.name}"
					title="栏目" url="/cms/category/treeData" module="articleTemp" cssClass="input-small"/>
		<label>标题：</label><form:input path="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
	</form:form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th style="text-align:center;">选择</th><th>栏目</th><th>标题</th><th>权重</th><th>点击数</th><th>发布者</th><th>更新时间</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="articleTemp">
			<tr>
				<td style="text-align:center;"><input type="checkbox" name="id" value="${articleTemp.id}" title="${fns:abbr(articleTemp.title,40)}" /></td>
				<td><a href="javascript:" onclick="$('#categoryId').val('${articleTemp.category.id}');$('#categoryName').val('${articleTemp.category.name}');$('#searchForm').submit();return false;">${articleTemp.category.name}</a></td>
				<td><a href="${ctx}/cms/articleTemp/form?id=${articleTemp.id}" title="${articleTemp.title}" onclick="return view(this.href);">${fns:abbr(articleTemp.title,40)}</a></td>
				<td>${articleTemp.weight}</td>
				<td>${articleTemp.hits}</td>
				<td>${articleTemp.createBy.name}</td>
				<td><fmt:formatDate value="${articleTemp.updateDate}" type="both"/></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	</div>
</body>
</html>