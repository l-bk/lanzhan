	<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>广告管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#reContent").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/ads/">广告列表</a></li>
		<li class="active"><a href="${ctx}/cms/ads/form?id=${ads.aid}">添加广告<%-- <shiro:hasPermission name="cms:guestbook:edit">${guestbook.delFlag eq '2'?'审核':'查看'}</shiro:hasPermission><shiro:lacksPermission name="cms:guestbook:edit">查看</shiro:lacksPermission> --%></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="ads" action="${ctx}/cms/ads/save" method="post" class="form-horizontal">
		<form:hidden path="aid"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">广告标题:</label>
			<div class="controls">
				<form:input path="adname" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">广告标签:</label>
			<div class="controls">
				<form:input path="tagname" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">广告类型:</label>
			<div class="controls">
				<form:select path="typeid" class="required">
					<c:forEach var='i' items="${fns:getDictList('cms_ads_type')}">
						<form:option value="${i.value}">${i.label}</form:option>
					</c:forEach>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">广告代码位置:</label>
			<div class="controls">
				<form:select path="advPosid" class="required">
					<c:forEach var='i' items="${fns:getDictList('cms_ads_position')}">
						<form:option value="${i.value}">${i.label}</form:option>
					</c:forEach>
				</form:select>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">广告创建时间:</label>
			<div class="controls">
				<fmt:parseDate value="${ads.starttime}" pattern="yyyy-MM-dd HH:mm:ss" var="t"/>
				<input id="starttime" type="text" name="starttime" maxlength="20" class="input-medium Wdate" value='<fmt:formatDate value="${t}" pattern="yyyy-MM-dd HH:mm:ss"/>'
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">广告结束时间:</label>
			<div class="controls">
				<fmt:parseDate value="${ads.endtime}" pattern="yyyy-MM-dd HH:mm:ss" var="t"/>
				<input id="endtime" type="text" name="endtime" maxlength="20" class="input-medium Wdate" value='<fmt:formatDate value="${t}" pattern="yyyy-MM-dd HH:mm:ss"/>'
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>	
					
					
		<div class="control-group">
			<label class="control-label">广告脚本:</label>
			<div class="controls">
				<form:textarea id="content" htmlEscape="false" path="normbody" rows="4" maxlength="200" class="input-xxlarge"/>
				
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">发布时间:</label>
			<div class="controls">
				<input id="createDate" name="starttime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${ads.starttime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div> --%>
		<%-- <div class="control-group">
			<label class="control-label">发布时间:</label>
			<div class="controls">
				<input id="createDate" name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div> --%>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>