<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文章管理</title>
	<meta name="decorator" content="default"/>
			<link type="text/css" rel="Stylesheet" href="/static/kindeditor/WordPaster/css/WordPaster.css"/>
    <link type="text/css" rel="Stylesheet" href="/static/kindeditor/WordPaster/js/skygqbox.1.3.css" />
	<link type="text/css" rel="stylesheet" href="/static/kindeditor/themes/simple/simple.css" />
	<script type="text/javascript" charset="utf-8" src="/static/kindeditor/kindeditor-min.js"></script>
	<script type="text/javascript" charset="utf-8" src="/static/kindeditor/lang/zh_CN.js"></script>
    <script type="text/javascript" src="/static/kindeditor/WordPaster/js/WordPaster.js" charset="utf-8"></script>
    <script type="text/javascript" src="/static/kindeditor/WordPaster/js/skygqbox.1.3.js" charset="utf-8"></script>
	
	<script type="text/javascript">
		
		$(document).ready(function() {
			
			hideRadio();
            if($("#link").val()){
                $('#linkBody').show();
                $('#url').attr("checked", true);
            }
			$("#title").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
                    if ($("#categoryId").val()==""){
                        $("#categoryName").focus();
                        top.$.jBox.tip('请选择归属栏目','warning');
                    }else if (CKEDITOR.instances.content.getData()=="" && $("#link").val().trim()==""){
                        top.$.jBox.tip('请填写正文','warning');
                    }else{
                        loading('正在提交，请稍等...');
                        form.submit();
                    }
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
			
			$(":radio").click(function(){
				checkRadio($(this));
			});
			
		});
	function hideRadio(){
			$(":radio:checked").each(function(){
				checkRadio($(this));
			});
		}
		function checkRadio(ele){
			var v = $(ele).val();
			var n = $(ele).attr("name");
			if(n == "istime"){
				if (v == 0) {
					$(ele).parent().next().slideUp();
				} else {
					$(ele).parent().next().slideDown();
				}
			}else{
				if (v == 1) {
					$(ele).parent().next().slideUp();
				} else {
					$(ele).parent().next().slideDown();
				}
			}
		}
	function choosecategory(id,name){
		$("#categoryId").val(id);
		$("#categoryName").val(name);
	}
	function chooseposition(id,name){
		$("#posid").val(id);
		$("#posiname").val(name);
	}
	function choosemsgsource(id,name){
		$("#msgid").val(id);
		$("#msgname").val(name);
	}
	
	</script>
	<style type="text/css">
		.controls .att{
			float:left;
			display: block;
			cursor: pointer;
			margin: 3px 3px 0 3px;
		}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
	
		<li>
		<a href="${ctx}/cms/attention/list?type=${type}">
		<c:choose>
		<c:when test="${type=='0'}">关注栏目列表</c:when>
		<c:when test="${type=='1'}">关注推荐位列表</c:when>
		<c:when test="${type=='2'}">关注信息源列表</c:when>
		</c:choose>
		</a>
		</li>
		<li class="active"><a href="${ctx}/cms/attention/form?type=${type}">
		<c:choose>
		<c:when test="${type=='0'}">关注栏目添加</c:when>
		<c:when test="${type=='1'}">关注推荐位添加</c:when>
		<c:when test="${type=='2'}">关注信息源添加</c:when>
		</c:choose>
		</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="attention" action="${ctx}/cms/attention/save" method="post" class="form-horizontal">
		<form:hidden path="aid"/>
		<form:hidden path="type"/>
		<sys:message content="${message}"/>
		<c:choose>
		<c:when test="${type=='0'}">
		<div class="control-group">
			<label class="control-label">已关注的栏目:</label>
			<div class="controls">
				<c:forEach var="c" items="${attList}">
				<span onclick="choseattention('${c.category.id}','${c.category.name}');">${c.category.name}</span>
				</c:forEach>
				<!--  <span class="help-inline">多个关键字，用空格分隔。</span>-->
			</div>
		</div>
		</c:when>
		<c:when test="${type=='1'}">
		<div class="control-group">
			<label class="control-label">已关注的推荐位:</label>
			<div class="controls">
				<c:forEach var="c" items="${attList}">
					<span onclick="choseattention('${c.position.posid}','${c.position.description}');">${c.position.description}</span>
				</c:forEach>
				<!--  <span class="help-inline">多个关键字，用空格分隔。</span>-->
			</div>
		</div>
		</c:when>
		<c:when test="${type=='2'}">
			<div class="control-group">
				<label class="control-label">已关注的信息源:</label>
				<div class="controls">
					<c:forEach var="c" items="${attList}">
						<span onclick="choseattention('${c.msgsource.sid}','${c.msgsource.name}');">${c.msgsource.name}</span>
					</c:forEach>
					<!--  <span class="help-inline">多个关键字，用空格分隔。</span>-->
				</div>
			</div>
		</c:when>
		</c:choose>

		<c:choose>
		<c:when test="${type=='0'}">
		<div class="control-group">
			<label class="control-label">添加关注的栏目:</label>
			<div class="controls">
                <sys:treeselect id="category" name="category.id" value="${attention.category.id}" labelName="category.name" labelValue="${attention.category.name}"
					title="栏目" url="/cms/category/treeData" module="attention" selectScopeModule="true" notAllowSelectRoot="false" notAllowSelectParent="true" cssClass="required"/>&nbsp;
               
			</div>
		</div>
		</c:when>

		<c:when test="${type=='1'}">
		<div class="control-group">
			<label class="control-label">添加关注的推荐位:</label>
			<div class="controls">
				<c:forEach var="p" items="${posilist}" varStatus="pi">
					<a class="att" onclick="chooseposition('${p.posid}','${p.description}');">${p.description}</a> <c:if test="${fn:length(posilist)-1 != pi.index}"><span class="att">|</span></c:if>
				</c:forEach>
				<!--  <span class="help-inline">多个关键字，用空格分隔。</span>-->
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">添加目标</label>
			<div class="controls">
				<input type="hidden" name="position.posid" id="posid"/>
				<input type="text" name="position.description" id="posiname" class="input-mini required"/>
				<!--  <span class="help-inline">多个关键字，用空格分隔。</span>-->
			</div>
		</div>
		</c:when>
		<c:when test="${type=='2'}">
		<div class="control-group">
			<label class="control-label">添加关注的信息源:</label>
			<div class="controls">
				<c:forEach var="m" items="${msglist}" varStatus="mi">
					<a class="att" onclick="choosemsgsource('${m.sid}','${m.name}');">${m.name}</a> <c:if test="${fn:length(msglist)-1 != mi.index}"><span class="att">|</span></c:if>
				</c:forEach>
				<!--  <span class="help-inline">多个关键字，用空格分隔。</span>-->
			</div>
			
		</div>

		<div class="control-group">
			<label class="control-label">添加目标</label>
			<div class="controls">
				<input type="hidden" name="msgsource.sid" id="msgid"/>
				<input type="text" name="msgsource.name" id="msgname" class="input-mini required"/>
				<!--  <span class="help-inline">多个关键字，用空格分隔。</span>-->
			</div>
			</div>
		</c:when>
		</c:choose>
		
		
		
		
		
		
		<div style="height:100px"></div>
		<div class="form-actions" style="position:fixed;width:100%;bottom:0">
			<shiro:hasPermission name="cms:article:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="发 布"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>