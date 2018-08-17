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
<title>腾云军事·${category.name}</title>
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
<style type="text/css">
	@media screen and (max-width:319px){html{font-size:29px!important}}
	@media screen and (min-width:320px){html{font-size:31px!important}}
	@media screen and (min-width:360px){html{font-size:35px!important}}
	@media screen and (min-width:375px){html{font-size:36px!important}}
	@media screen and (min-width:384px){html{font-size:37px!important}}
	@media screen and (min-width:414px){html{font-size:40px!important}}
	@media screen and (min-width:480px){html{font-size:46px!important}}
	@media screen and (min-width:550px){html{font-size:53px!important}}
	@media screen and (min-width:600px){html{font-size:58px!important}}
	@media screen and (min-width:640px){html{font-size:62px!important}}
	@media screen and (min-width:700px){html{font-size:68px!important}}
	@media screen and (min-width:750px){html{font-size:73px!important}}
	@media screen and (min-width:800px){html{font-size:78px!important}}
	@media screen and (min-width:850px){html{font-size:83px!important}}
	@media screen and (min-width:900px){html{font-size:88px!important}}
	@media screen and (min-width:950px){html{font-size:93px!important}}
	@media screen and (min-width:1000px){html{font-size:98px!important}}
	.nav a {
			font-size: 0.5rem;
	}
</style>
</head>

<body>
	<header>
		<!--头部-->
		<h1 class="top">
			<a href="/" class="back"></a>
			<a href="/${category.nameEn}" class="logo logo${category.id}">
				<cite>腾云军事·${category.name}</cite>
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
						<img class="lazy" data-original="${t.image}" src="/js/grey2.png" width="120" height="80" alt="${!empty t.shorttile?t.shorttile:t.title}">
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
			<p style="font-size: 16px;">腾云军事 www.60mil.com</p>
			<p>
				<a href="http://www.miitbeian.gov.cn/state/outPortal/loginPortal.action;jsessionid=rpDyXRXVH0Bv883FlqX2Q214zhrRD80pfzclPFPLLjKxLF51S5GT!1219073593">粤ICP备15002994号-1</a>
			</p>
			<p>本站文章来源于网络 仅作为展示之用 版权归原作者所有</p>
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
				$(this).css("color","#1a5a9a");
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
								html += '<img class="lazy" data-original="' + v.image + '" src="/js/grey2.png" width="120" height="80" alt="' + (v.shorttile!=''&&v.shorttile!=null?v.shorttile:v.title) + '">';
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
