<%@ page language="java" import="java.util.*" pageEncoding="GB2312"%><%@ 
	page contentType="text/html;charset=GB2312"%><%@ 
	page import = "Xproer.*" %><%@ 
	page import="org.apache.commons.lang.StringUtils" %><%@ 
	page import="org.apache.commons.fileupload.*" %><%@ 
	page import="org.apache.commons.fileupload.disk.*" %><%@ 
	page import="org.apache.commons.fileupload.servlet.*" %><%
/*	
	���¼�¼��
		2013-01-25 ȡ����SmartUpload��ʹ�ã�����commons-fileupload�������Ϊ���Է���SmartUpload���ڴ�й¶�����⡣
*/
//String path = request.getContextPath();
//String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String uname = "";// 		= request.getParameter("uid");
String upass = "";// 		= request.getParameter("fid");
 
// Check that we have a file upload request
boolean isMultipart = ServletFileUpload.isMultipartContent(request);
FileItemFactory factory = new DiskFileItemFactory();   
ServletFileUpload upload = new ServletFileUpload(factory);
//upload.setSizeMax(262144);//256KB
List files = null;
try 
{
	files = upload.parseRequest(request);
} 
catch (FileUploadException e) 
{// �����ļ��ߴ�����쳣  
    out.println("�ϴ�����:" + e.toString());
    return;
   
}

FileItem imgFile = null;
// �õ������ϴ����ļ�
Iterator fileItr = files.iterator();
// ѭ�����������ļ�
while (fileItr.hasNext()) 
{
	// �õ���ǰ�ļ�
	imgFile = (FileItem) fileItr.next();
	// ���Լ�form�ֶζ������ϴ�����ļ���(<input type="text" />��)
	if(imgFile.isFormField())
	{
		String fn = imgFile.getFieldName();
		String fv = imgFile.getString(); 
		if(fn.equals("uname")) uname = fv;
		if(fn.equals("upass")) upass = fv;
	}
	else 
	{
		break;
	}
}
Uploader up = new Uploader(pageContext,request);
up.SaveFile(imgFile);
String url = up.GetFilePathRel(); 
out.write(url);
response.setHeader("Content-Length",url.length()+"");//����Content-length��ǣ��Ա�ؼ���ȷ��ȡ���ص�ַ��
%>