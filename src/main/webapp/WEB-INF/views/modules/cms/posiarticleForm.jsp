<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>文章管理</title>
<meta name="decorator" content="default" />
<link type="text/css" rel="Stylesheet"
	href="/static/kindeditor/WordPaster/css/WordPaster.css" />
<link type="text/css" rel="Stylesheet"
	href="/static/kindeditor/WordPaster/js/skygqbox.1.3.css" />
<link type="text/css" rel="stylesheet"
	href="/static/kindeditor/themes/simple/simple.css" />
<link type="text/css" rel="Stylesheet"
	href="/static/kindeditor/themes/default/default.css" />
<script type="text/javascript" charset="utf-8"
	src="/static/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript" charset="utf-8"
	src="/static/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript"
	src="/static/kindeditor/WordPaster/js/WordPaster.js" charset="utf-8"></script>
<script type="text/javascript"
	src="/static/kindeditor/WordPaster/js/skygqbox.1.3.js" charset="utf-8"></script>

<script type="text/javascript">
	var posid = ${posid};
	$(document)
			.ready(
					function() {
						$(document).on("keyup", "[data-act='innum']",
								function() {
									var v = $(this).val();
									var target = $(this).data("target");
									var length = $(this).data("length");

									if (v.length <= length) {
										$(target).html(v.length);
									} else {
										$(this).val(v.substring(0, length))
										$(target).html(length);
									}
								});
						$("input[name='posid']").each(function() {
							for (var i = 0; i < posid.length; i++) {
								if ($(this).val() == posid[i]) {
									$(this).attr("checked", "checked");
								}
							}
						});
						hideRadio();
						if ($("#link").val()) {
							$('#linkBody').show();
							$('#url').attr("checked", true);
						}
						$("#title").focus();
						$("#inputForm")
								.validate(
										{
											submitHandler : function(form) {
												if ($("#categoryId").val() == "") {
													$("#categoryName").focus();
													top.$.jBox.tip('请选择归属栏目',
															'warning');
												} else if (UE.getEditor(
														'content').getContent() == ""
														&& $("#link").val()
																.trim() == "") {
													top.$.jBox.tip('请填写正文',
															'warning');
												} else {
													loading('正在提交，请稍等...');
													form.submit();
												}
											},
											errorContainer : "#messageBox",
											errorPlacement : function(error,
													element) {
												$("#messageBox").text(
														"输入有误，请先更正。");
												if (element.is(":checkbox")
														|| element.is(":radio")
														|| element
																.parent()
																.is(
																		".input-append")) {
													error.appendTo(element
															.parent().parent());
												} else {
													error.insertAfter(element);
												}
											}
										});

						$(":radio").click(function() {
							checkRadio($(this));
						});

					});
	function hideRadio() {
		$(":radio:checked").each(function() {
			checkRadio($(this));
		});
	}
	function checkRadio(ele) {
		var v = $(ele).val();
		var n = $(ele).attr("name");
		if (n == "istime") {
			if (v == 0) {
				$(ele).parent().next().slideUp();
			} else {
				$(ele).parent().next().slideDown();
			}
		}
	}
	function choseattention(id, name) {
		$("#categoryId").val(id);
		$("#categoryName").val(name);
	}
	KindEditor.ready(function(K) {
		var editor2 = K.editor({
			allowFileManager : true,
			cssPath : '/static/kindeditor/plugins/code/prettify.css',
			uploadJson : '/static/kindeditor/jsp/upload_json.jsp',
			fileManagerJson : '/static/kindeditor/jsp/file_manager_json.jsp',
			allowFileManager : true,
		});

		K('#inserimage').click(
				function() {
					editor2.loadPlugin('image', function() {
						editor2.plugin.imageDialog({
							imageUrl : K('#url1').val(),
							clickFn : function(url, title, width, height,
									border, align) {
								$('#smallimage').attr("src", url);
								$("#smallimage").show();
								$('#image').val(url);
								/* alert(url); */
								editor2.hideDialog();
							}
						});
					});
				});
	});
</script>
<style type="text/css">
.controls .att {
	float: left;
	display: block;
	cursor: pointer;
	margin: 3px 3px 0 3px;
}

.atten-span:hover {
	color: #8DB6CD;
}

.cls-exp, .cls-exp .title {
	padding: 5px 0;
}

.controls label {
	display: inline-block;
	width: 200px
}
</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a
			href="${ctx}/cms/article/?category.id=${article.category.id}">文章列表</a></li>
		<li class="active"><a
			href="<c:url value='${fns:getAdminPath()}/cms/article/form?id=${article.id}&category.id=${article.category.id}'><c:param name='category.name' value='${article.category.name}'/></c:url>">文章<shiro:hasPermission
					name="cms:article:edit">${not empty article.id?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="cms:article:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="article"
		action="${ctx}/cms/article/save?listType=posiarticlelist" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}" />
		<div class="control-group">
			<label class="control-label">关注的栏目:</label>
			<div class="controls">
				<c:forEach var="c" items="${categorylist}" varStatus="i">
					<a class="att"
						onclick="choseattention('${c.category.id}','${c.category.name}');">${c.category.name}</a>
					<c:if test="${fn:length(attlist)-1 != i.index}">
						<span class="att">|</span>
					</c:if>
				</c:forEach>
				<!--  <span class="help-inline">多个关键字，用空格分隔。</span>-->
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属栏目:</label>
			<div class="controls">
				<sys:treeselect id="category" name="category.id"
					value="${article.category.id}" labelName="category.name"
					labelValue="${article.category.name}" title="栏目"
					url="/cms/category/treeData" module="article"
					selectScopeModule="true" notAllowSelectRoot="false"
					notAllowSelectParent="true" cssClass="required" />
				&nbsp; <span> <input id="url" type="checkbox"
					onclick="if(this.checked){$('#linkBody').show()}else{$('#linkBody').hide()}$('#link').val()"><label
					for="url">外部链接</label>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">长标题:</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="200"
					class="input-xxlarge required" style="text-align: left;"
					data-act="innum" data-target="#titleNum" data-length="25" />
				<span id="titleNum">0</span>/25
				<!-- &nbsp;<label>颜色:</label>
				<form:select path="color" class="input-mini">
					<form:option value="" label="默认"/>
					<form:options items="${fns:getDictList('color')}" itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select> -->
			</div>
		</div>
		<div id="linkBody" class="control-group" style="display:none">
			<label class="control-label">外部链接:</label>
			<div class="controls">
				<form:input path="link" htmlEscape="false" maxlength="200"
					class="input-xlarge" />
				<span class="help-inline">绝对或相对地址。</span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">短标题:</label>
			<div class="controls">
				<form:input path="shorttile" htmlEscape="false" maxlength="200"
					class="input-xlarge" data-act="innum" data-target="#shorttileNum"
					data-length="25" />
				<span id="shorttileNum">0</span>/25
				<!--  <span class="help-inline">多个关键字，用空格分隔。</span>-->
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">关键字:</label>
			<div class="controls">
				<form:input path="keywords" htmlEscape="false" maxlength="200"
					class="input-xlarge" data-act="innum" data-target="#keywordsNum"
					data-length="25" />
				<span id="keywordsNum">0</span>/25
				<!--  <span class="help-inline">多个关键字，用空格分隔。</span>-->
			</div>
		</div>

		
		<div class="control-group">
			<label class="control-label">来源:</label>
			<div class="controls">
				<sys:formtreeselect id="msgid" name="msgsource.sid"
					value="${article.msgid}" labelName="msgsource.name"
					labelValue="${article.msgsource.name}" title="栏目"
					url="/cms/category/formTreeData" module="article"
					notAllowSelectRoot="false" cssClass="input-small" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">关注的推荐位:</label>
			<div class="controls">
				<c:forEach var="t" items="${attlist}">
					<label> <form:checkbox path="posid"
							value="${t.position.posid}" />${t.position.description}
					</label>
				</c:forEach>

			</div>
		</div>
		<!-- <div class="control-group">
			<label class="control-label">过期时间:</label>
			<div class="controls">
				<form:hidden path="weight" />
				<input id="weightDate" name="weightDate" type="text"
					readonly="readonly" maxlength="20" class="input-small Wdate"
					value="<fmt:formatDate value="${article.weightDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});" />
				<span class="help-inline">数值越大排序越靠前，过期时间可为空，过期后取消置顶。</span>
			</div>
		</div> -->

		<div class="control-group">
			<label class="control-label">缩略图:</label>
			<div class="controls">
				<input type="hidden" id="image" name="image"
					value="${article.imageSrc}" /> <img src="${article.imageSrc}"
					id="smallimage"
					<c:if test="${empty article.imageSrc}"> stype="display:none"</c:if>
					width="50px" height="40px"> <input type="button"
					id="inserimage" value="选择文件" />
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">摘要:</label>
			<div class="controls">
				<form:textarea path="description" htmlEscape="false" rows="4"
					maxlength="200" class="input-xxlarge" style="width:800px"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">正文:</label>
		</div>
		<div class="control-group">
			<form:textarea id="content" htmlEscape="true"
				path="articleData.content" rows="4" maxlength="200"
				stlye="width:95%" />
			<sys:ueditor replace="content" uploadPath="/uploads" />

		</div>

		<div class="control-group">
			<label class="control-label">相关文章:</label>
			<div class="controls">
				<form:hidden id="articleDataRelation" path="articleData.relation"
					htmlEscape="false" maxlength="200" class="input-xlarge" />
				<ol id="articleSelectList"></ol>
				<a id="relationButton" href="javascript:" class="btn">添加相关</a>
				<script type="text/javascript">
					var articleSelect = [];
					function articleSelectAddOrDel(id, title) {
						var isExtents = false, index = 0;
						for (var i = 0; i < articleSelect.length; i++) {
							if (articleSelect[i][0] == id) {
								isExtents = true;
								index = i;
							}
						}
						if (isExtents) {
							articleSelect.splice(index, 1);
						} else {
							articleSelect.push([ id, title ]);
						}
						articleSelectRefresh();
					}
					function articleSelectRefresh() {
						$("#articleDataRelation").val("");
						$("#articleSelectList").children().remove();
						for (var i = 0; i < articleSelect.length; i++) {
							$("#articleSelectList")
									.append(
											"<li>"
													+ articleSelect[i][1]
													+ "&nbsp;&nbsp;<a href=\"javascript:\" onclick=\"articleSelectAddOrDel('"
													+ articleSelect[i][0]
													+ "','"
													+ articleSelect[i][1]
													+ "');\">×</a></li>");
							$("#articleDataRelation").val(
									$("#articleDataRelation").val()
											+ articleSelect[i][0] + ",");
						}
					}
					$.getJSON("${ctx}/cms/article/findByIds", {
						ids : $("#articleDataRelation").val()
					}, function(data) {
						for (var i = 0; i < data.length; i++) {
							articleSelect.push([ data[i][1], data[i][2] ]);
						}
						articleSelectRefresh();
					});
					$("#relationButton")
							.click(
									function() {
										top.$.jBox
												.open(
														"iframe:${ctx}/cms/article/selectList?pageSize=8",
														"添加相关",
														$(top.document).width() - 220,
														$(top.document)
																.height() - 180,
														{
															buttons : {
																"确定" : true
															},
															loaded : function(h) {
																$(
																		".jbox-content",
																		top.document)
																		.css(
																				"overflow-y",
																				"hidden");
															}
														});
									});
				</script>
			</div>
		</div>
		<div class="control-group" style="display: none">
			<label class="control-label">是否允许评论:</label>
			<div class="controls" style="margin-top:2px;">
				<form:radiobuttons path="articleData.allowComment"
					items="${fns:getDictList('yes_no')}" itemLabel="label"
					itemValue="value" htmlEscape="false" />

			</div>
		</div>
		<div class="control-group">
			<label class="control-label" style="margin-top:8px">是否定时发布:</label>
			<div class="controls">
				<div class="cls-exp">
					<form:radiobutton path="istime" value="1" />
					是&nbsp;
					<form:radiobutton path="istime" value="0" />
					否
					<!-- <label style="margin-left: 100px">发布时间:</label>
					 <input id="createDate" name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
						value="<fmt:formatDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> --!>
				</div>
				<div class="cls-exp">
					<div class="title">
						<label>定时发布时间:</label>
					</div>
					<div class="cls-exp">
						<fmt:parseDate value="${article.timing}"
							pattern="yyyy-MM-dd HH:mm:ss" var="t" />
						<input id="timing" type="text" name="timing" maxlength="20"
							class="input-medium Wdate"
							value='<fmt:formatDate value="${t}" pattern="yyyy-MM-dd HH:mm:ss"/>'
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" />
						<input id="createDate" name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
						value="<fmt:formatDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
						onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</div>
				</div>
			</div>

		</div>
		<shiro:hasPermission name="cms:article:audit">
			<div class="control-group">
				<label class="control-label">发布状态:</label>
				<div class="controls" style="margin-top:2px;">
					<form:radiobuttons path="delFlag"
						items="${fns:getDictList('cms_del_flag')}" itemLabel="label"
						itemValue="value" htmlEscape="false" class="required" />
					<span class="help-inline"></span>
				</div>
			</div>
		</shiro:hasPermission>
		<shiro:hasPermission name="cms:category:edit">
			<div class="control-group" style="display: none">
				<label class="control-label">自定义内容视图:</label>
				<div class="controls">
					<form:select path="customContentView" class="input-medium">
						<form:option value="" label="默认视图" />
						<c:forEach var="t" items="${contentViewList}">
							<form:option value="${t}">${fns:getDictLabel(t,'temp_name','')}</form:option>
						</c:forEach>

					</form:select>
					<span class="help-inline">自定义内容视图名称必须以"${article_DEFAULT_TEMPLATE}"开始</span>
				</div>
			</div>
			<div class="control-group" style="display: none">
				<label class="control-label">自定义视图参数:</label>
				<div class="controls">
					<form:input path="viewConfig" htmlEscape="true" />
					<span class="help-inline">视图参数例如: {count:2,
						title_show:"yes"}</span>
				</div>
			</div>
		</shiro:hasPermission>
		<c:if test="${not empty article.id}">
			<div class="control-group" style="display: none">
				<label class="control-label">查看评论:</label>
				<div class="controls">
					<input id="btnComment" class="btn" type="button" value="查看评论"
						onclick="viewComment('${ctx}/cms/comment/?module=article&contentId=${article.id}&status=0')" />
					<script type="text/javascript">
						function viewComment(href) {
							top.$.jBox
									.open(
											'iframe:' + href,
											'查看评论',
											$(top.document).width() - 220,
											$(top.document).height() - 180,
											{
												buttons : {
													"关闭" : true
												},
												loaded : function(h) {
													$(".jbox-content",
															top.document).css(
															"overflow-y",
															"hidden");
													$(
															".nav,.form-actions,[class=btn]",
															h.find("iframe")
																	.contents())
															.hide();
													$(
															"body",
															h.find("iframe")
																	.contents())
															.css("margin",
																	"10px");
												}
											});
							return false;
						}
					</script>
				</div>
			</div>
		</c:if>
		<div style="height:100px"></div>
		<div class="form-actions" style="position:fixed;width:100%;bottom:0">
			<shiro:hasPermission name="cms:article:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit"
					value="发 布" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回"
				onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>