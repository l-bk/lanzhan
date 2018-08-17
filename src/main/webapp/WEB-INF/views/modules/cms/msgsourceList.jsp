<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>信息源管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript" src="/js/jquery-1.7.js"></script>
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
		function quanxuan(obj)
		{
			console.log($(obj).attr("checked"));
			if($(obj).attr("checked")=="checked")
			{
				$(".each_checkbox").attr("checked","checked");
			}
			else
			{
				$(".each_checkbox").removeAttr("checked");
			}
		}
		function del_checked1()
		{
			var con=confirm("确定删除吗?");
			if(con==true)
			{
				var del_id="";
				$(".each_checkbox").each(function()
				{
					if($(this).attr("checked")=="checked")
					{
						//console.log($(this).attr("id"))
						del_id += $(this).attr("id")+"|";
					}
				})
				window.location.href="${ctx}/cms/msgsource/multiDelete?s_id="+del_id;
			}
		}
		function kengdie(sid)
		{
			var title1=$(".input-small").val();
			var con=confirm("确认删除吗？");
			if(con==true)
			{
				var obj={'title':title1};
				console.log(obj);
				$.ajax({
					type:"POST",
					async:true, 	
					data:obj, 
					dataType:"json",
					json:"callback",
					url:"${ctx}/cms/msgsource/delete?sid="+sid,		
					contentType:"application/json;charset=utf-8",
					success:fun,		
					error:function(XMLHttpRequest,textStatus,errorThrown)
					{
						console.log("异常");
					}
				});
				function fun(json)
				{
					console.log(json.title);
					$(".input-small").val(json.title);
					$("#searchForm").submit();
				}
			}
			else{}
		}
	</script>
	<script type="text/javascript">
		$(function()
		{
			$(".input-small").val('${title}');
		})
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cms/msgsource/">信息源列表</a></li>
		<li><a href="${ctx}/cms/msgsource/form">信息源添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="msgsource" action="${ctx}/cms/msgsource/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>标题：</label><input tyep="text" name="title" htmlEscape="false" maxlength="50" class="input-small"/>&nbsp;
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>&nbsp;&nbsp;
		<input type="button" onclick="del_checked1()" id="del_checked" style="position:absolute;right:5px;" class="btn btn-primary" value="删除" />
		
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr>
		<th><input type="checkbox" name="quanxuan" class="quanxuan" onclick="quanxuan(this)"/></th>
		<th>信息源名称</th><th>信息源URL</th>
		<th>发布者</th><th>更新时间</th><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="msgsource">
			<tr>
				<td><input type="checkbox" class="each_checkbox" id="${msgsource.sid}"/></td>
				<td><a href="${ctx}/cms/msgsource/form?sid=${msgsource.sid}" >${msgsource.name}</a></td>
				<td>${msgsource.url}</td>
				<td>${msgsource.user.name}</td>
				<td><fmt:formatDate value="${msgsource.updateDate}" type="both"/></td>
				<td>
					
					
						
	    				<a href="${ctx}/cms/msgsource/form?sid=${msgsource.sid}">修改</a>
	    				<a style="cursor:pointer" onclick='kengdie(${msgsource.sid});'>删除</a>
					
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>