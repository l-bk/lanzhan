<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>子节点管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			hideRadio();
			$("#name").focus();
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
			if(n == "isBaseHref" || n == "isSplitPage" || n == "ishaspic" || n == "iscontextnextpage" || n == "IsMsgsource"){
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
	</script>
	<style type="text/css">
		.cls-exp, .cls-exp .title{
			padding: 5px 0;
		}
		.cls-exp label{
			font-weight: bold;
		}
		.btm-bdr{
			border-bottom:1px solid #ddd;
		}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/copynode/">子节点列表</a></li>
		<li class="active"><a href="${ctx}/cms/copynode/form?id=${contextnodedefine.cid}">子节点<shiro:hasPermission name="cms:category:edit">${not empty contextnodedefine.cid?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:category:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="contextnodedefine" action="${ctx}/cms/copynode/save" method="post" class="form-horizontal">
		<form:hidden path="cid"/>
		<form:hidden path="parentid"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">是否启用:</label>
			<div class="controls">
                <form:radiobutton path="stat" value="1"/>是 &nbsp;<form:radiobutton path="stat" value="0"/>否
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">归属栏目:</label>
			<div class="controls">
                <sys:treeselect id="category" name="categoryId" value="${contextnodedefine.categoryId}" labelName="category.name" labelValue="${contextnodedefine.category.name}"
					title="栏目" url="/cms/category/treeData" module="article" selectScopeModule="true" notAllowSelectRoot="false" notAllowSelectParent="true" cssClass="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">关注推荐位标识:</label>
			<div class="controls">
				<form:input path="posid" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">子节点名称:</label>
			<div class="controls">
				<form:input path="description" htmlEscape="false" class="input-xlarge required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">采集节点的URL:</label>
			<div class="controls">
				<form:input path="copyUrl" htmlEscape="false"  class="required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">设定采集文章列表:</label>
			<div class="controls">
				<div class="cls-exp">
					列表开始表达式:
					<form:input path="listRegBegin" htmlEscape="false"  class="input-xxlarge required"/>
				</div>
				<div class="cls-exp">
					<label style="text-align:right;font-weight:normal;width:94px">列表:</label>
					<form:input path="copyUrlReg" htmlEscape="false"  class="input-xxlarge required"/>
				</div>
				<div class="cls-exp">
					列表结束表达式:
					<form:input path="listRegEnd" htmlEscape="false"  class="input-xxlarge required"/>
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">采集条数:</label>
			<div class="controls">
				<form:input path="copyCount" htmlEscape="false"  class="required"/>
			</div>
		</div>
		<%-- <form:hidden path="isSplitPage" value="1"/> --%>
		
		<div class="control-group">
			<label class="control-label">是否规定基准url:</label>
			<div class="controls">
				<div class="cls-exp btm-bdr">
					<form:radiobutton path="isBaseHref" value="1"/>是 &nbsp;<form:radiobutton path="isBaseHref" value="0"/>否
				</div>
				<div class="cls-exp">
					<div class="title"><label>基准url的表达式</label></div>
					<div class="cls-exp">
						基准url表达式:
						<form:input path="baseHrefReg" htmlEscape="false" class="input-xxlarge"/>
					</div>
				</div>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">是否分页采集:</label>
			<div class="controls">
				<div class="cls-exp btm-bdr">
					<form:radiobutton path="isSplitPage" value="1"/>是 &nbsp;<form:radiobutton path="isSplitPage"/>否
				</div>
				<div class="cls-exp">
					<div class="title"><label>采集下一页的表达式</label></div>
					<div class="cls-exp">
						下一页表达式:
						<form:input path="splitPageNewReg" htmlEscape="false" class="input-xxlarge"/>
					</div>
				</div>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">列表是否使用默认的文章标题:</label>
			<div class="controls">
				<div class="cls-exp btm-bdr">
					<form:radiobutton path="isListSimpleTitle" value="1"/>是 &nbsp;<form:radiobutton path="isListSimpleTitle" value="0"/>否
				</div>
				
				<div class="cls-exp">
					<div class="title"><label>设定文章列表里的文章标题:</label></div>
					<div class="cls-exp">
						标题开始表达式:
						<form:input path="titleRegBegin" htmlEscape="false" class="input-xxlarge" />
					</div>
					<div class="cls-exp">
						标题结束表达式:
						<form:input path="titleRegEnd" htmlEscape="false" class="input-xxlarge" />
					</div>
				</div>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">列表是否使用默认的文章短标题:</label>
			<div class="controls">
				<div class="cls-exp btm-bdr">
					<form:radiobutton path="isContextSimpleShortTitle" value="1"/>是 &nbsp;<form:radiobutton path="isContextSimpleShortTitle" value="0"/>否
				</div>
				
				<div class="cls-exp">
					<div class="title"><label>设定文章列表里的文章短标题:</label></div>
					<div class="cls-exp">
						短标题开始表达式:
						<form:input path="contextShortTitleRegBegin" htmlEscape="false" class="input-xxlarge" />
					</div>
					<div class="cls-exp">
						短标题结束表达式:
						<form:input path="contextShortTitleRegEnd" htmlEscape="false" class="input-xxlarge" />
					</div>
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">列表页是否使用默认的meta标签:</label>
			<div class="controls">
				<div class="cls-exp btm-bdr">
					<form:radiobutton path="isListSimpleMeta" value="1"/>是 &nbsp;<form:radiobutton path="isListSimpleMeta" value="0"/>否
				</div>
				<div>
				<div class="cls-exp btm-bdr">
					<div class="title"><label>设定文章列表页里的seo关键字:</label></div>
					<div class="cls-exp">
						关键字开始表达式:
						<form:input path="listKeyorksRegBegin" htmlEscape="false" class="input-xxlarge" />
					</div>
					<div class="cls-exp">
						关键字结束表达式:
						<form:input path="listKeyorksRegEnd" htmlEscape="false" class="input-xxlarge" />
					</div>
				</div>
				<div class="cls-exp">
					<div class="title"><label>设定文章列表页里的seo描述:</label></div>
					<div class="cls-exp">
						描述开始表达式:
						<form:input path="listDescriptionRegBegin" htmlEscape="false" class="input-xxlarge" />
					</div>
					<div class="cls-exp">
						描述结束表达式:
						<form:input path="listDescriptionRegEnd" htmlEscape="false" class="input-xxlarge" />
					</div>
				</div>
				</div>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">内容页是否使用默认的文章标题:</label>
			<div class="controls">
				<div class="cls-exp btm-bdr">
					<form:radiobutton path="isContextSimpleTitle" value="1"/>是 &nbsp;<form:radiobutton path="isContextSimpleTitle" value="0"/>否
				</div>
				<div class="cls-exp">
					<div class="title"><label>设定内容页的文章标题:</label></div>
					<div class="cls-exp">
						标题开始表达式:
						<form:input path="contextTitleBegin" htmlEscape="false" class="input-xxlarge" />
					</div>
					<div class="cls-exp">
						标题结束表达式:
						<form:input path="contextTitleEnd" htmlEscape="false" class="input-xxlarge" />
					</div>
				</div>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">内容页是否使用默认的meta标签:</label>
			<div class="controls">
				<div class="cls-exp btm-bdr">
					<form:radiobutton path="isContextSimpleMeta" value="1"/>是 &nbsp;<form:radiobutton path="isContextSimpleMeta" value="0"/>否
				</div>
				<div>
				<div class="cls-exp btm-bdr">
					<div class="title"><label>设定文章内容页里的seo关键字:</label></div>
					<div class="cls-exp">
						关键字开始表达式:
						<form:input path="contextKeyorksRegBegin" htmlEscape="false" class="input-xxlarge" />
					</div>
					<div class="cls-exp">
						关键字结束表达式:
						<form:input path="contextKeyorksRegEnd" htmlEscape="false" class="input-xxlarge" />
					</div>
				</div>
				<div class="cls-exp">
					<div class="title"><label>设定文章内容页里的seo描述:</label></div>
					<div class="cls-exp">
						描述开始表达式:
						<form:input path="contextDescriptionRegBegin" htmlEscape="false" class="input-xxlarge" />
					</div>
					<div class="cls-exp">
						描述结束表达式:
						<form:input path="contextDescriptionRegEnd" htmlEscape="false" class="input-xxlarge" />
					</div>
				</div>
				</div>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">发布时间是否使用当前时间:</label>
			<div class="controls">
				<div class="cls-exp btm-bdr">
					<form:radiobutton path="isContextSimpleDate" value="1"/>是 &nbsp;<form:radiobutton path="isContextSimpleDate" value="0"/>否
				</div>
				<div>
					<div class="cls-exp">
						<div class="cls-exp">
							发布时间表达式:
							<form:input path="contextDateReg" htmlEscape="false" class="input-xxlarge" />
						</div>
						<div class="cls-exp">
							发布时间的格式:
							<form:input path="contextDateFormat" htmlEscape="false" class="input-xxlarge" />
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">是否有缩略图:</label>
			<div class="controls">
				<div class="cls-exp btm-bdr">
					<form:radiobutton path="ishaspic" value="1"/>是 &nbsp;<form:radiobutton path="ishaspic" value="0"/>否
				</div>
				<div class="cls-exp">
					<div class="title"><label>缩略图表达式</label></div>
					<div class="cls-exp">
						<form:input path="picreg" htmlEscape="false" class="input-xxlarge"/>
					</div>
				</div>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">内容页是否有下一页:</label>
			<div class="controls">
				<div class="cls-exp btm-bdr">
					<form:radiobutton path="iscontextnextpage" value="1"/>是 &nbsp;<form:radiobutton path="iscontextnextpage" value="0"/>否
				</div>
				<div class="cls-exp">
					<div class="title"><label>内容页下一页表达式</label></div>
					<div class="cls-exp">
						<form:input path="contextnextreg" htmlEscape="false" class="input-xxlarge"/>
					</div>
				</div>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">设定文章内容:</label>
			<div class="controls">
				<div class="cls-exp">
					文章内容开始表达式:
					<form:input path="mainContextRegBegin" htmlEscape="false" class="input-xxlarge" />
				</div>
				<div class="cls-exp">
					文章内容结束表达式:
					<form:input path="mainContextRegEnd" htmlEscape="false" class="input-xxlarge" />
				</div>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">是否采集信息来源:</label>
			<div class="controls">
				<div class="cls-exp btm-bdr">
					<form:radiobutton path="IsMsgsource" value="1"/>是 &nbsp;<form:radiobutton path="IsMsgsource" value="0"/>否
				</div>

				<div class="cls-exp">

					<div class="title"><label>设置信息来源:</label></div>
					<div class="cls-exp">
						信息来源开始表达式:
						<form:input path="MsgsourceBegin" htmlEscape="false" class="input-xxlarge" />
					</div>
					<div class="cls-exp">
						信息来源结束表达式:
						<form:input path="MsgsourceEnd" htmlEscape="false" class="input-xxlarge" />
					</div>
					<div class="cls-exp btm-bdr">
					<label class="control-label">是否采集信息来源:</label>
					<form:radiobutton path="IsMsgsourceCn" value="1"/>是 &nbsp;<form:radiobutton path="IsMsgsourceCn" value="0"/>否
					</div>
					<div class="cls-exp">
						来源URL表达式:
						<form:input path="MsgsourceUrlReg" htmlEscape="false" class="input-xxlarge" />
					</div>
					<div class="cls-exp">
						来源文字表达式:
						<form:input path="MsgsourceCnReg" htmlEscape="false" class="input-xxlarge" />
					</div>
					
				</div>
			</div>
		</div>
		
		
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>