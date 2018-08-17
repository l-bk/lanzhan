<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>文章管理</title>
<meta name="decorator" content="default" />
<script>
	$(function()
	{
		$(".position_a").each(function()
		{
			console.log($(this).text());
			console.log("${positionArticle.position.description}");
			if($(this).text()=="${positionArticle.position.description}")
			{
				console.log("123123123123");
				$(this).click();
			}
		})
	})
</script>
<script type="text/javascript">
		$(function(){
			$(document).on('click', '[data-act="time"]', function(){
				$("#beginDate").val($(this).data('begindate'));
				$("#endDate").val($(this).data('enddate'));
				$("#searchForm").submit();
			});
 			$(document).on('click', '#seniorSubmit', function(){
				console.log($("#flag").val());
				//$("#flag").val("y");
				$("#searchForm").submit();
			});
			$(document).on('click', '#btnSubmit', function(){
				console.log($("#flag").val());
				//$("#createBy").val("${(fns:getUser()).id}");
				$("#searchForm").submit();
			}); 
			var postions=$("a[class='position_a']");
        	postions.on('click',function(){
        		// alert(222);
        		$(this).addClass('active').siblings().removeClass('active');
        	});
        	var categorys=$("a[class='category_a']");
        	categorys.on('click', function(){
        		// alert(111);
    			$(this).addClass('active').siblings().removeClass('active');
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
        function selectCategory(categoryid,categoryname){
        	$("#categoryId").val(categoryid);
        	$("#categoryName").val(categoryname);
        	console.log($("#categoryId").val());
   //      	var categorys=$("a[class='category_a']");
   //      	categorys.on('click', function(){
   //      		alert(111);
   //  			categorys.css('background-color', 'white');
   // 				$(this).css('background-color', 'yellow');
			// });
        }
        function selectPosition(posid,posname){
        	$("#posidId").val(posid);
        	$("#posidName").val(posname);
        	console.log($("#posidId").val());
       //  	var postions=$("a[class='position_a']");
       //  	postions.on('click',function(){
       //  		alert(222);
       //  		postions.css('background-color', 'white');
   				// $(this).css('background-color', 'yellow');
       //  	});
        }
        function doSelect(e)
{
	for (iIndex=0; iIndex<e.form.elements.length; iIndex++)
	{
		var es = e.form.elements[iIndex];
		if ((es.name == "ids")&&(!es.disabled))
		{
			es.checked = e.checked;
		}
	}	
	return true;
}
       function gogogo(path,id) {
    	   var v = document.getElementById(id).value;	
    	   var path1 = path + v;
    	   document.getElementById(id+"a").href=path1;
       }
	</script>
<style type="text/css">
.search-condition {
	margin: 10px 0;
}

.search-condition a {
	cursor: pointer;
	padding: 2px 5px;
}

.search-condition .active {
	background: #157ab5;
	color: #fff;
	border-radius: 5px;
}

.category_a {
	
}

.category_b {
	background-color: #000;
}

.position_a {
	
}

.position_b {
	background-color: #000;
}
</style>

</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a
			href="${ctx}/cms/article/posiarticlelist?category.id=${article.category.id}">文章列表</a></li>
		<shiro:hasPermission name="cms:article:edit">
			<li><a
				href="<c:url value='${fns:getAdminPath()}/cms/article/posiform?id=${article.id}&category.id=${article.category.id}'><c:param name='category.name' value='${article.category.name}'/></c:url>">文章添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="positionArticle"
		action="${ctx}/cms/article/posiarticlelist" method="post"
		class="breadcrumb form-search">
		<div class="search-condition">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden"
				value="${page.pageSize}" /> <input id="flag" name="flag"
				type="hidden" value="n" /> 
				<input id="positionId" name="posidId" type="hidden" value="${positionArticle.posid}" />
				<input id="positionName" name="posidName" type="hidden" value="${positionArticle.position.description}" />
				<label>推荐位：</label>
			<sys:positreeselect id="posid" name="posid" value="${positionArticle.posid}"
				labelName="position.description"
				labelValue="${positionArticle.position.description}" title="栏目"
				url="/cms/article/treeData" module="article"
				notAllowSelectRoot="false" cssClass="input-small" />

			<label>标题：</label>
			<form:input path="article.title" htmlEscape="false" maxlength="50"
				class="input-small" />
			<label>短标题：</label>
			<form:input path="article.shorttile" htmlEscape="false" maxlength="50"
				class="input-small" />
			&nbsp; <input id="btnSubmit" class="btn btn-primary" type="submit"
				value="查询" />&nbsp;&nbsp; <label>状态：</label>
			<form:radiobuttons onclick="$('#searchForm').submit();"
				path="article.delFlag" items="${fns:getDictList('cms_del_flag')}"
				itemLabel="label" itemValue="value" htmlEscape="false" />
		</div>
		<div class="search-condition">
			<label>栏&nbsp;&nbsp;&nbsp;&nbsp;目：</label>
			<sys:treeselect2 id="category" name="article.category.id"
				value="${category.id}" labelName="article.category.name"
				labelValue="${category.name}" title="栏目"
				url="/cms/category/treeData" module="article"
				notAllowSelectRoot="false" cssClass="input-small" />
			<label>管理员：</label>
			<form:select name="select_name" id="createBy" path="article.createBy"
				class="input-medium">

				<form:options items="${usersmap}" htmlEscape="false" />&nbsp;
				<form:option value="${(fns:getUser()).id}" label="">${(fns:getUser()).name}</form:option>
			</form:select>

		</div>
		<div class="search-condition">
			<label>关注栏目: </label>
			<c:forEach var="c" items="${attentionList}" varStatus="i">
				<a id="category_a${c.category.id}" class="category_a"
					onclick="selectCategory(${c.category.id},'${c.category.name}');">${c.category.name}</a>
				<c:if test="${i.index < fn:length(attentionList)-1}">|</c:if>
			</c:forEach>
		</div>
		<div class="search-condition">
			<label>关注推荐位: </label>
			<c:forEach var="p" items="${attentionList2}" varStatus="i">
				<a id="position_a${p.position.posid}" class="position_a"
					onclick="selectPosition('${p.position.posid}','${p.position.description}');">${p.position.description}</a>
				<c:if test="${i.index < fn:length(attentionList2)-1}">|</c:if>
			</c:forEach>
		</div>
		<div class="search-condition">
			<label>定置时间: </label> <a data-act="time"
				data-begindate="${fnc:getDefinedTime(0, 9999, 9999, 9999)}"
				data-enddate="${fnc:getDefinedTime(0, 0, 0, 0)}">当天</a> | <a
				data-act="time"
				data-begindate="${fnc:getDefinedTime(-1, 9999, 9999, 9999)}"
				data-enddate="${fnc:getDefinedTime(0, 9999, 9999, 9999)}">昨天</a> | <a
				data-act="time"
				data-begindate="${fnc:getDefinedTime(-3, 9999, 9999, 9999)}"
				data-enddate="${fnc:getDefinedTime(0, 9999, 9999, 9999)}">最近三天</a> <label>
			</label> <input id="beginDate" name="article.beginDate" type="text"
				readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${article.beginDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
			- <input id="endDate" name="article.endDate" type="text"
				readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${article.endDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
			<input id="seniorSubmit" class="btn btn-primary" type="button"
				value="高级查询" /> <span class="help-inline">查询当前用户发布的文章</span>
		</div>
	</form:form>
	<form:form id="upForm" modelAttribute="positionArticle" name="upForm"
		action="${ctx}/cms/article/updateNumber" method="post"
		class="breadcrumb form-search">
		<sys:message content="${message}" />
		<table id="contentTable"
			class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th><INPUT TYPE="checkbox" NAME="cbox"
						onclick="doSelect(this)"></th>
					<th>推荐位</th>
					<th>栏目</th>
					<th>标题</th>
					<th>发布状态</th>
					<th>点击数</th>
					<th>发布者</th>
					<th>发布时间</th>
					<th>置顶时间</th>
					<th>排序</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${page.list}" var="article" varStatus="i">
					<tr>
						<td><INPUT TYPE="checkbox" NAME="ids" value="${article.sid}"></td>
						<td><a href="javascript:"
							onclick="$('#posidId').val('${article.posid}');$('#posidName').val('${article.position.description}');$('#searchForm').submit();return false;">${article.position.description}</a></td>
						<td><a href="javascript:"
							onclick="$('#categoryId').val('${article.article.category.id}');$('#categoryName').val('${article.article.category.name}');$('#searchForm').submit();return false;">${article.article.category.name}</a></td>
						<td><a
							href="${ctx}/cms/article/posiform?id=${article.article.id}"
							title="${article.article.title}">${fns:abbr(article.article.title,40)}</a></td>
						<td><c:choose>
								<c:when test="${article.article.delFlag=='0'}">
					已人工发布
					</c:when>
								<c:when test="${article.article.delFlag=='1'}">
					已撤稿
					</c:when>
								<c:when test="${article.article.delFlag=='2'}">
					未发布
					</c:when>
								<c:when test="${article.article.delFlag=='3'}">
					已自动发布
					</c:when>
							</c:choose></td>
						<td>${article.article.hits}</td>
						<td>${article.article.user.name}</td>
						<td><fmt:formatDate value="${article.article.createDate}"
								type="both" /></td>
						<td><input id="${article.article.id }" name="weightDate" type="text"
							class="Wdate" readonly="readonly" maxlength="20"
							value="<fmt:formatDate value="${article.article.weightDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" /> 
							<a href="#" id="${article.article.id }a"  onclick="gogogo('${ctx}/cms/article/updateWeightdate?id=${article.article.id}&weightDate=','${article.article.id}');">确定修改</a>
						</td>
						<td><a
							href="${ctx}/cms/article/upSort?id=${article.article.id}&posid=${article.posid}&beforeid=${page.list[i.index-1].article.id}&afterid=${page.list[i.index+1].article.id}&delFlag=${delFlag}">升序</a>&nbsp;
							<a
							href="${ctx}/cms/article/downSort?id=${article.article.id}&posid=${article.posid}&beforeid=${page.list[i.index-1].article.id}&afterid=${page.list[i.index+1].article.id}&delFlag=${delFlag}">降序</a></td>
						<td><a href="${article.article.url}" target="_blank">访问</a> <shiro:hasPermission
								name="cms:article:edit">
								<c:if test="${article.article.category.allowComment eq '1'}">
									<shiro:hasPermission name="cms:comment:view">
										<a
											href="${ctx}/cms/comment/?module=article&contentId=${article.article.id}&delFlag=2"
											onclick="return viewComment(this.href);">评论</a>
									</shiro:hasPermission>
								</c:if>
								<a href="${ctx}/cms/article/posiform?id=${article.article.id}">修改</a>
								<shiro:hasPermission name="cms:article:audit">
									<a
										href="${ctx}/cms/article/delete?id=${article.article.id}${article.article.delFlag ne 0&&article.article.delFlag ne 0?'&isRe=true':''}&categoryId=${article.article.category.id}"
										onclick="return confirmx('确认要${article.article.delFlag ne 0&&article.article.delFlag ne 0?'发布':'撤稿'}该文章吗？', this.href)">
										<c:choose>
											<c:when
												test="${article.article.delFlag==0||article.article.delFlag==3}">
									撤稿
									</c:when>
											<c:when test="${article.article.delFlag==2}">
									发布
									</c:when>
											<c:when test="${article.article.delFlag==1}">
									反撤稿
									</c:when>
										</c:choose>

									</a>
								</shiro:hasPermission>
							</shiro:hasPermission></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form:form>
	<div class="pagination">
		${page} <input class="btn btn-primary" style="float: right"
			type="button" name="smb" id="smb"
			onclick="$('#upForm').submit();return false;" value="发布到前台" />
	</div>
	<div></div>
</body>
</html>