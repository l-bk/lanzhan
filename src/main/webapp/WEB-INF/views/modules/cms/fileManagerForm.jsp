<%--
  Created by IntelliJ IDEA.
  User: jenwing
  Date: 16/7/17
  Time: 下午9:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>文件管理</title>
    <meta name="decorator" content="default" />
    <script type="text/javascript">
        $(document).ready(
                function() {
                    $("#name").focus();
                    $("#inputForm")
                            .validate(
                                    {
                                        submitHandler : function(form) {
                                            loading('正在提交，请稍等...');
                                            form.submit();
                                        },
                                        errorContainer : "#messageBox",
                                        errorPlacement : function(error, element) {
                                            $("#messageBox").text("输入有误，请先更正。");
                                            if (element.is(":checkbox")
                                                    || element.is(":radio")
                                                    || element.parent().is(
                                                            ".input-append")) {
                                                error.appendTo(element.parent()
                                                        .parent());
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
    <li class="active"><a href="${ctx}/cms/filemanager/list?show=${param.show}">文件列表</a></li>
    <li><a href="${ctx}/cms/filemanager/form?show=${param.show}">文件添加</a></li>
</ul>
<br />
<form:form id="inputForm" modelAttribute="template"
           action="${ctx}/cms/filemanager/save" method="post"
           class="form-horizontal">
    <form:hidden path="cid" />
    <form:hidden path="templatepath" />
    <form:hidden path="show" />

    <div class="control-group">
        <label class="control-label">文件名:</label>
        <div class="controls">
            <form:input path="templatefilename" class="input required" />
        </div>
    </div>


    <div class="control-group">
        <label class="control-label">描述:</label>
        <div class="controls">
            <form:input path="description" class="input required" />
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">序号:</label>
        <div class="controls">
            <form:input path="sortNumber" class="input required" />
        </div>
    </div>

    <div class="control-group">
        <label class="control-label">模板内容:</label>
        <div class="controls">
            <form:textarea path="context" htmlEscape="true" rows="80"
                           cssStyle="width:100%;height:460px;" class="required" />
        </div>
    </div>
    <div class="form-actions">
        <input id="btnSubmit" class="btn btn-primary" type="submit"
               value="保 存" />&nbsp; <input id="btnCancel" class="btn" type="button"
                                           value="返 回" onclick="history.go(-1)" />
    </div>
</form:form>
</body>
</html>
