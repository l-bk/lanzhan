<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%><!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="description" content="${site.description}">
<meta name="keywords" content="${site.keywords}">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="format-detection" content="telephone=no">
<title>美容美妆 - 悦读 女性</title>
<link rel="stylesheet" href="${ctx}/css/main.css">
<script type="text/javascript" src="${ctx}/static/jquery/jquery-1.9.1.min.js"></script>
<script src="${ctx}/js/jquery.lazyload.js" type="text/javascript"></script>
</head>
<script type="text/javascript">
$(document).ready(function(){
	$("#mz").addClass("active");
	});

</script>

<body>
	<div class="header">
		<a class="logo" href="${ctx}/sh"><img width="120" height="30" src="${ctx}/images/logo.jpg" /></a>
	</div>
	<div class="nav-container">
		<c:import url="/WEB-INF/views/modules/cms/front/include/nav_nx.jsp" />
	</div>
	<!-- <ul class="nav">
		<li><a href="#">美容达人</a></li>
		<li class="active"><a href="#">美容美妆</a></li>
		<li><a href="#">撩汉宝典</a></li>
	</ul> -->
	<div class="column-panel">
		<c:forEach items="${page.list}" var="t" varStatus="i" begin="0">
		<div class="w_all backff bor_botcc mtop10">
			<div class="in_listtu">
				<a href="${t.url}"><img src="${t.image}"
					alt=""></a>
			</div>
			<div class="txtcenter ptop10">
				—— <span class="bor_zj_3bt_e"><a href="${t.url}">YUEDU</a></span><span class="color_red bor_zj_3bt_c"><a href="${t.url}">美容</a></span>——
			</div>
			<div class="txtcenter in_listwz">
				<a href="${t.url}">${t.title}</a>
			</div>
			<div class="txtcenter in_listdate"><fmt:formatDate value="${t.createDate}" pattern="MM-dd" /></div>
		</div>
		</c:forEach>
		<!-- <div class="w_all backff bor_botcc mtop10">
			<div class="in_listtu">
				<a href="http://m.rayli.com.cn/picture/16972"><img src="http://uploads.rayli.com.cn/2016/0422/thumb_640_480_1461314967410.jpg"
					alt=""></a>
			</div>
			<div class="txtcenter ptop10">
				—— <span class="bor_zj_3bt_e"><a href="/category/3">YUEDU</a></span><span class="color_red bor_zj_3bt_c"><a href="/category/3">美容</a></span>——
			</div>
			<div class="txtcenter in_listwz">
				<a href="http://m.rayli.com.cn/picture/16972">荷叶边 把女神高圆圆和拿少女心允儿都下</a>
			</div>
			<div class="txtcenter in_listdate">2016-04-22</div>
		</div>
		<div class="w_all backff bor_botcc mtop10">
			<div class="in_listtu">
				<a href="http://m.rayli.com.cn/picture/16972"><img src="http://uploads.rayli.com.cn/2016/0422/thumb_640_480_1461314967410.jpg"
					alt=""></a>
			</div>
			<div class="txtcenter ptop10">
				—— <span class="bor_zj_3bt_e"><a href="/category/3">YUEDU</a></span><span class="color_red bor_zj_3bt_c"><a href="/category/3">美容</a></span>——
			</div>
			<div class="txtcenter in_listwz">
				<a href="http://m.rayli.com.cn/picture/16972">荷叶边 把女神高圆圆和拿少女心允儿都下</a>
			</div>
			<div class="txtcenter in_listdate">2016-04-22</div>
		</div>
		<div class="w_all backff bor_botcc mtop10">
			<div class="in_listtu">
				<a href="http://m.rayli.com.cn/picture/16972"><img src="http://uploads.rayli.com.cn/2016/0422/thumb_640_480_1461314967410.jpg"
					alt=""></a>
			</div>
			<div class="txtcenter ptop10">
				—— <span class="bor_zj_3bt_e"><a href="/category/3">YUEDU</a></span><span class="color_red bor_zj_3bt_c"><a href="/category/3">美容</a></span>——
			</div>
			<div class="txtcenter in_listwz">
				<a href="http://m.rayli.com.cn/picture/16972">荷叶边 把女神高圆圆和拿少女心允儿都下</a>
			</div>
			<div class="txtcenter in_listdate">2016-04-22</div>
		</div>
		<div class="w_all backff bor_botcc mtop10">
			<div class="in_listtu">
				<a href="http://m.rayli.com.cn/picture/16972"><img src="http://uploads.rayli.com.cn/2016/0422/thumb_640_480_1461314967410.jpg"
					alt=""></a>
			</div>
			<div class="txtcenter ptop10">
				—— <span class="bor_zj_3bt_e"><a href="/category/3">YUEDU</a></span><span class="color_red bor_zj_3bt_c"><a href="/category/3">美容</a></span>——
			</div>
			<div class="txtcenter in_listwz">
				<a href="http://m.rayli.com.cn/picture/16972">荷叶边 把女神高圆圆和拿少女心允儿都下</a>
			</div>
			<div class="txtcenter in_listdate">2016-04-22</div>
		</div>
		<div class="w_all backff bor_botcc mtop10">
			<div class="in_listtu">
				<a href="http://m.rayli.com.cn/picture/16972"><img src="http://uploads.rayli.com.cn/2016/0422/thumb_640_480_1461314967410.jpg"
					alt=""></a>
			</div>
			<div class="txtcenter ptop10">
				—— <span class="bor_zj_3bt_e"><a href="/category/3">YUEDU</a></span><span class="color_red bor_zj_3bt_c"><a href="/category/3">美容</a></span>——
			</div>
			<div class="txtcenter in_listwz">
				<a href="http://m.rayli.com.cn/picture/16972">荷叶边 把女神高圆圆和拿少女心允儿都下</a>
			</div>
			<div class="txtcenter in_listdate">2016-04-22</div>
		</div> -->
		<div id="J_NewsList"></div>
	</div>
	<div class="more-btn list-tips" >${page.count!=0?'加载更多':'无相关的文章'}</div>
	<div class="footer">
		<!-- <a href="#" class="about-apd">关于悦读</a> -->
		<p>©版权所有 盗版必究</p>
	</div>
	
	<script type="text/javascript">
		var $J_Nav = $("#J_Nav");
		$(window).on('scroll', function() {
		/* 	if ($(this).scrollTop() >= 88) {
				$('.nav-container').show();
				$J_Nav.addClass("fixed");
			} else {
				$J_Nav.removeClass("fixed");
				$('.nav-container').hide();
			} */
		});
		
		var curUrl = window.location.pathname;
		$('#J_Nav a').each(function(){
			var href = $(this).attr('href');
			if(curUrl.indexOf(href) > -1 && href != "/"){
				$(this).css("color","#cf3a3c");
			}
		});
		
		$('.menu').click(function(){
			var ele = $('.nav-container');
			var flag = ele.css('display');
			if(flag == "none"){
				ele.show();
			}
			else{
				ele.hide();
			}
		});
		
		//滚动加载
		var $body = $("body");
		var allowAutoGetNewsList = true;
		var newsListLoading = false;
		var loadingCondition;
		$(document).bind(
			"scroll",
			function() {
				if (allowAutoGetNewsList && !newsListLoading && $body.scrollTop() > loadingCondition) {
					fetchNewsList();
				}
			});
		var index = 2;

		function fetchNewsList() {
			newsListLoading = true;
			$.ajax({
				data: {
					"categoryId": "${category.id}",
					"pageNo": index,
					"pageSize": 8,
					noCache: Math.random()
				},
				url: '/getJSONArticleList',
				timeout: 20000,
				dataType: "json",
				success: function(d) {
					if(d.last>=index){
						$.each(d.list, function(i, t) {
							var html = 		'<div class="w_all backff bor_botcc mtop10">'+
							'<div class="in_listtu">'+
							'<a href="'+t.url +'"><img src="'+t.image+'"'+
							'	alt=""></a>'+
						'</div>'+
						'<div class="txtcenter ptop10">'+
						'	—— <span class="bor_zj_3bt_e"><a href="'+t.url+'">YUEDU</a></span><span class="color_red bor_zj_3bt_c"><a href="'+t.url+'">美容</a></span>——'+
						'</div>'+
						'<div class="txtcenter in_listwz">'+
						'	<a href="'+t.url+'">'+t.title+'</a>'+
						'</div>'+
					'	<div class="txtcenter in_listdate">'+ (t.createDate).substring(5,10)+'</div>'+
					'</div>';
						/* 	var html = '<div class="posi-content"><a href="' + v.url + '" title="' + (v.shorttile!=''&&v.shorttile!=null?v.shorttile:v.title)+ '" target="_bank"><div class="img-panel">';
							if(v.image != undefined && v.image != null && v.image != ''){
								html += '<img class="lazy" data-original="' + v.image + '" src="/js/grey.jpg" width="120" height="80" alt="' + (v.shorttile!=''&&v.shorttile!=null?v.shorttile:v.title) + '">';
							}
							html += '</div><div class="text-panel"><div class="content-title">' + (v.shorttile!=''&&v.shorttile!=null?v.shorttile:v.title) + '</div><div class="content-source">来源：'+((v['msgsource']!=null)?v.msgsource.name:'互联网')+'</div></div></a></div>';
							 */
							$("#J_NewsList")
								.append(html);
						});
						index++;
					}else{
						$(".list-tips").text("已经是最后一页")	
					}
					resetAutoGetNewsList();
					$("img.lazy").lazyload({
						/* effect : "fadeIn" */
					});
				},
				error: function() {
					autoGetNewsListError();
				}
			});
		}

		function resetAutoGetNewsList() {
			loadingCondition = parseInt(($body.height() - $(window).height()) * 2 / 3);
			newsListLoading = false;
		}
		resetAutoGetNewsList();

		function autoGetNewsListError() {
			newsListLoading = false;
			resetAutoGetNewsList();
		}
	</script>
</body>
</html>