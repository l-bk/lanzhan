<%--
  Created by IntelliJ IDEA.
  User: jenwing
  Date: 16/7/17
  Time: 下午9:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>文件管理</title>
    <meta name="decorator" content="default" />
    <script type="text/javascript">
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="${ctx}/cms/filemanager/list?show=${param.show}">文件列表</a></li>

    <li><a href="${ctx}/cms/filemanager/form?show=${param.show}">文件添加</a></li>

</ul>

<sys:message content="${message}" />
<table id="contentTable"
       class="table table-striped table-bordered table-condensed">
    <thead>
    <tr>
        <th>文件名</th>
        <th>修改时间</th>
        <th>描述</th>
        <th>序号</th>
        <shiro:hasPermission name="cms:site:edit">
            <th>操作</th>
        </shiro:hasPermission>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="t">
        <tr>
            <td>${t.templatefilename}</td>
            <td>${t.modifydate}</td>
            <td>${t.description}</td>
            <td>${t.sortNumber}</td>
            <shiro:hasPermission name="cms:site:edit">
                <td>
                    <a href="${ctx}/cms/filemanager/preview?cid=${t.cid}&templatefilename=${t.templatefilename}&show=${param.show}" target="_blank">预览</a>
                    <a href="${ctx}/cms/filemanager/form?cid=${t.cid}&show=${param.show}">修改</a>
                    <%--<c:set var="defultfront" value="frontComment.jsp,frontGuestbook.jsp,frontIndex.jsp,frontList.jsp,frontListCategory.jsp,frontMap.jsp,frontSearch.jsp,frontViewArticle.jsp"/>--%>
                    <c:set var="check" value="true"/>
                    <%--<c:forEach var="tf" items="${fn:split(defultfront,',')}">--%>
                        <%--<c:if test="${t.templatefilename==tf }">--%>
                            <%--<c:set var="check" value="false"/>--%>
                        <%--</c:if>--%>
                    <%--</c:forEach>--%>
                    <c:if test="${check==true}">
                        <a
                                href="${ctx}/cms/filemanager/delete?cid=${t.cid}&show=${param.show}"
                                onclick="return confirmx('确认要删除该模板吗？', this.href)">删除</a>
                    </c:if>
                </td>
            </shiro:hasPermission>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}</div>
</body>
</html>
