<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="replace" type="java.lang.String" required="true" description="需要替换的textarea编号"%>
<%@ attribute name="uploadPath" type="java.lang.String" required="false" description="文件上传路径，路径后自动添加年份。若不指定，则编辑器不可上传文件"%>
<%@ attribute name="height" type="java.lang.String" required="false" description="编辑器高度"%>
<script type="text/javascript" src="/static/ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="/static/ueditor/ueditor.all.js"></script>
<script type="text/javascript">
	var ue = UE.getEditor('${replace}');
</script>