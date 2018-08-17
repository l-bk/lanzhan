<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>欢迎页面</title>
	<meta name="decorator" content="default"/>
	
	<style type="text/css">
		body{
			background: #f1f1f1;
		}
		.content{
			padding: 20px;
		}
		.content h3{
			padding: 10px;
		}
		.link-panel{
			display:table;
			width:100%;
			padding: 20px 0px;
			border-top: 1px solid #ccc;
		}
		.link-panel a{
			float: left;
			color: #000;
			margin: 0 10px;
			padding: 17px 5px;
			width:100px;
			border: 1px solid #ccc;
			border-radius: 5px;
			text-align: center;
			text-decoration: none;
			cursor: pointer;
		}
		.link-panel a:hover{
			background: #f8f8f8;
			color: #333;
		}
		.link-panel a i{
			font-size: 40px;
			padding-bottom:20px;
		}
		.link-panel a span{
			padding-top: 10px;
		}
	</style>
</head>
<body>
	
	<div class="content">
		<h3>欢迎登陆亚太网管理后台</h3>
		<div class="link-panel">
			<a href="/tyjsmgr/cms/article/form?category.name=&id=&category.id=">
				<i class="icon-edit"></i><br/><span>添加文章</span>
			</a>
			<a href="/tyjsmgr/cms/article/list?delFlag=">
				<i class="icon-print"></i><br/><span>文章管理</span>
			</a>
			<a href="/tyjsmgr/cms/article/posiarticlelist">
				<i class=icon-thumbs-up></i><br/><span>推荐文章管理</span>
			</a>
			<a href="/tyjsmgr/cms/stats/article">
				<i class="icon-bar-chart"></i><br/><span>流量统计</span>
			</a>
			<a href="/tyjsmgr/cms/ads">
				<i class="icon-columns"></i><br/><span>广告管理</span>
			</a>
			<a href="/tyjsmgr/cms/category/">
				<i class="icon-tasks"></i><br/><span>栏目管理</span>
			</a>
			<a href="/tyjsmgr/sys/user/info">
				<i class="icon-user"></i><br/><span>个人中心</span>
			</a>
		</div>
	</div>
</body>
</html>