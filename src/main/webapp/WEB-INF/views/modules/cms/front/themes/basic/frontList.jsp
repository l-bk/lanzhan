<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%><!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="description" content="${site.description}">
<meta name="keywords" content="${site.keywords}">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="format-detection" content="telephone=no">
<title>悦读·${category.name}</title>
<link rel="stylesheet" href="/css/normalize.css">
<link rel="stylesheet" href="/css/index_list.css">
<script type="text/javascript" src="/static/jquery/jquery-1.9.1.min.js"></script>
<script src="/js/jquery.lazyload.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		$("img.lazy").lazyload({
			/* effect : "fadeIn" */
		});
	});
</script>
</head>

<body>
	<header>
		<!--头部-->
		<h1 class="top">
			<a href="/" class="back"></a>
			<a href="/${category.nameEn}" class="logo logo${category.id}">
				<cite>悦读·${category.name}</cite>
			</a>
			<a class="menu"></a>
		</h1>
		<!--logo-->
		<div class="nav-container" style="display: none">
			<c:import url="/WEB-INF/views/modules/cms/front/include/nav.jsp"/>
		</div>
	</header>
	<c:forEach items="${page.list}" var="t" varStatus="i" begin="1">
		<div class="posi-content">
			<a href="${t.url}" title="${t.title}" target="_bank">
				<div class="img-panel">
					<c:if test="${not empty t.image}">
						<img class="lazy" data-original="${t.image}" src="/js/grey.jpg" width="120" height="80" alt="${!empty t.shorttile?t.shorttile:t.title}">
					</c:if>
				</div>
				<div class="text-panel">
					<div class="content-title">
						${!empty t.shorttile?t.shorttile:t.title}
					</div>
					<div class="content-source">来源：${!empty t.msgsource.name?t.msgsource.name:'互联网'}</div>
				</div>
			</a>
		</div>
	</c:forEach>
	<div id="J_NewsList"></div>
	<div class="list-tips">${page.count!=0?'加载更多':'无相关的文章'}</div>

	<footer>
		<div class="footer">
			<!-- <a href="http://m.apdnews.com/introduction/aboutus/"
				class="about-apd">关于悦读</a> -->
			<p>©版权所有 盗版必究</p>
		</div>
	</footer>
	<script type="text/javascript">
		var $J_Nav = $("#J_Nav");
		$(window).on('scroll', function() {
			if ($(this).scrollTop() >= 88) {
				$('.nav-container').show();
				$J_Nav.addClass("fixed");
			} else {
				$J_Nav.removeClass("fixed");
				$('.nav-container').hide();
			}
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
						$.each(d.list, function(i, v) {
							var html = '<div class="posi-content"><a href="' + v.url + '" title="' + (v.shorttile!=''&&v.shorttile!=null?v.shorttile:v.title)+ '" target="_bank"><div class="img-panel">';
							if(v.image != undefined && v.image != null && v.image != ''){
								html += '<img class="lazy" data-original="' + v.image + '" src="/js/grey.jpg" width="120" height="80" alt="' + (v.shorttile!=''&&v.shorttile!=null?v.shorttile:v.title) + '">';
							}
							html += '</div><div class="text-panel"><div class="content-title">' + (v.shorttile!=''&&v.shorttile!=null?v.shorttile:v.title) + '</div><div class="content-source">来源：'+((v['msgsource']!=null)?v.msgsource.name:'互联网')+'</div></div></a></div>';
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
