<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="description" content="${site.description}">
<meta name="keywords" content="${site.keywords}">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="format-detection" content="telephone=no">
<title>悦读 · 不在乎多,有你所需就够 </title>
<link rel="stylesheet" type="text/css" href="/css/normalize.css">
<link rel="stylesheet" type="text/css" href="/css/index_list.css">
<link rel="stylesheet" type="text/css" href="/css/flickerplate.css">
<script type="text/javascript" src="/static/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="/js/modernizr-custom-v2.7.1.min.js"></script>
<script type="text/javascript" src="/js/jquery-finger-v0.1.0.min.js"></script>
<script type="text/javascript" src="/js/flickerplate.min.js"></script>
<script src="/js/jquery.lazyload.js" type="text/javascript"></script>
<!-- img懒加载 -->
<script type="text/javascript" charset="utf-8">
	$(function() {
		$("img.lazy").lazyload({
			/* effect : "fadeIn" */
		});
	});
	$(function(){
		$('.topic-gallery-container').flicker({
			dot_alignment: 'right',
			block_text: false
		});
	});
</script>
<style type="text/css">
	.nav a {
		width: 20%;
	}
</style>
</head>

<body>
	<header>
		<!--头部-->
		<!-- <h1 class="top">
			<a href="/app" class="logo logo1"> <cite>悦读</cite>
			</a>
		</h1> -->
		<!--logo-->
		<div class="nav-container">
			<c:import url="/WEB-INF/views/modules/cms/front/include/nav_app.jsp"/>
		</div>
	</header>

	<div class="topic-gallery-container">
		<ul>
			<c:forEach var="t" items="${fnc:getPositionList('','7',3,'')}">
				<a href="${t.url}" title="${not empty t.shorttile?t.shorttile:t.title}" target="_bank">
					<li data-background="${t.image}">
						<div class="flick-title">${not empty t.shorttile?t.shorttile:t.title}</div>
					</li>
				</a>
			</c:forEach>
		</ul>
	</div>

	<%-- <c:forEach var="t" items="${fnc:getPositionList('','8',3,'')}" varStatus="i">
		<div class="posi-content">
			<a href="${t.url}" title="${not empty t.shorttile?t.shorttile:t.title}" target="_bank">
				<div class="img-panel"> 
					<c:if test="${not empty t.image}">
						<img class="lazy" data-original="${t.image}" src="/js/grey.jpg" width="120" height="80">
					</c:if>
				</div>
				<div class="text-panel">
					<div class="content-title">
						${not empty t.shorttile?t.shorttile:t.title}
					</div>
					<div class="content-source">
						来源：${!empty t.msgsource.name?t.msgsource.name:'互联网'}
						<span class="time"><fmt:formatDate value="${t.createDate}" pattern="MM-dd" /></span>
					</div>
				</div>
			</a>
		</div>
	</c:forEach> --%>
	
	<c:forEach var="t" items="${fnc:getPositionList('','10',3,'')}" varStatus="i">
		<div class="posi-content">
			<a href="${t.url}" title="${not empty t.shorttile?t.shorttile:t.title}" target="_bank">
				<div class="img-panel"> 
					<c:if test="${not empty t.image}">
						<img class="lazy" data-original="${t.image}" src="/js/grey.jpg" width="120" height="80">
					</c:if>
				</div>
				<div class="text-panel">
					<div class="content-title">
						${not empty t.shorttile?t.shorttile:t.title}
					</div>
					<div class="content-source">
						来源：${!empty t.msgsource.name?t.msgsource.name:'互联网'}
						<span class="time"><fmt:formatDate value="${t.createDate}" pattern="MM-dd" /></span>
					</div>
				</div>
			</a>
		</div>
	</c:forEach>

	<c:forEach items="${fnc:getMoreImgPositionList('','9',1,'')}" var="t" begin="0" end="0">
		<div class="pic3-panel">
			<a href="${t.url}" title="${not empty t.shorttile?t.shorttile:t.title}" target="_bank">
				<div class="pic3-title">${not empty t.shorttile?t.shorttile:t.title}</div>
				<div class="img3-panel">
					<c:forEach items="${t.images}" var="i" begin="0" end="2">
						<div class="img-box">
							<div class="picbox">
								<c:if test="${not empty i}">
									<img data-original="${i}" src="/js/grey.jpg" class="lazy img">
								</c:if>
							</div>
						</div>
					</c:forEach>
				</div>
				<div class="content-source">
					来源：${!empty t.msgsource.name?t.msgsource.name:'互联网'}
					<span class="time"><fmt:formatDate value="${t.createDate}" pattern="MM-dd" /></span>
				</div>
			</a>
		</div>
	</c:forEach>

	


	<div id="J_NewsList"></div>

	<div id="J_NewsListTips" class="list-tips">正在加载...</div>

	<footer>
		<div class="footer">
			<!-- <a href="http://m.apdnews.com/introduction/aboutus/"
				class="about-apd">关于悦读</a> -->
			<p>©版权所有 盗版必究</p>
		</div>
	</footer>
	<script type="text/javascript">
		$(function(){
			$(window).on("scroll", function() {
				if ($(this).scrollTop() >= 88) {
					$("#J_Nav").addClass("fixed");
				} else {
					$("#J_Nav").removeClass("fixed");
				}
			});
			
			var curUrl = window.location.pathname;
			$('#J_Nav a').each(function(){
				var href = $(this).attr('href');
				if(curUrl.indexOf(href) > -1){
					$(this).css("color","#cf3a3c");
				}
			});
			
			resetImg();
			$(window).resize(function(){
				resetImg();
			});
		});
			
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
		var index = 4;

		 fetchNewsList(1);
		fetchNewsList(2); 
		 fetchNewsList(3);
		fetchNewsList(4); 
		function fetchNewsList(indexPara) {
			var reqIndex
			if(indexPara!=undefined)
			{
				reqIndex = indexPara;
				}else{
					reqIndex = index;
					}
			newsListLoading = true;
			$.ajax({
				data: {
					"type": "10",
					"type2": "9",
					"index": reqIndex,
					"size": 4,
					"size2": 1,
					"officSize": 1,
					noCache: Math.random()
				},
				url: '/getJSONPosArticleList',
				timeout: 20000,
				dataType: "json",
				async:false,
				success: function(d) {
					var data1 = d.data1;
					var data2 = d.data2;
					if ($(data1).size() > 0) {
						
						$.each(data1, function(i, v) {
							var html = '<div class="posi-content"><a href="' + v.url + '" title="' +(v.shorttile!=''&&v.shorttile!=null?v.shorttile:v.title) + '"><div class="img-panel">';
							if(v.image != undefined && v.image != null && v.image != ''){
								html += '<img class="lazy" data-original="' + v.image + '" src="/js/grey.jpg" width="120" height="80" alt="' + (v.shorttile!=''&&v.shorttile!=null?v.shorttile:v.title) + '">';
							}
							html += '</div><div class="text-panel"><div class="content-title">' + (v.shorttile!=''&&v.shorttile!=null?v.shorttile:v.title) + '</div><div class="content-source">'+
					'来源：'+((v['msgsource']!=null)?v.msgsource.name:'互联网')+'<span class="time">'+ (v.createDate).substring(5,10) +'</span></div></div></a></div>'
							$("#J_NewsList").append(html);
						});
						if ($(data2).size() > 0) {
							$.each(data2, function(i, v) {
								var html = '<div class="pic3-panel"><a href="' + v.url + '" title="' + (v.shorttile!=''&&v.shorttile!=null?v.shorttile:v.title) + '" target="_bank"><div class="pic3-title">' + (v.shorttile!=''&&v.shorttile!=null?v.shorttile:v.title) + '</div><div class="img3-panel">';
								if(v.images != null){
									for(var i = 0; i < 3; i++){
										if(v.images[i] != undefined && v.images[i] != null && v.images[i] != ''){
											html += '<div class="img-box"><div class="picbox"><img data-original="' + v.images[i] + '" src="/js/grey.jpg" class="lazy img"></div></div>';
										}
									}
								}
								html += '</div><div class="content-source">来源：'+((v['msgsource']!=null)?v.msgsource.name:'互联网')+'<span class="time">'+ (v.createDate).substring(5,10) +'</span></div></a></div>';
								$("#J_NewsList").append(html);
							});
						}
						index++;
					} else {
						$("#J_NewsListTips").text("已经是最后一页")
					}
					resetAutoGetNewsList();
					resetImg();
					setTimeout(function(){
						$('img.lazy').lazyload({
							/* effect : "fadeIn" */
						});
					}, 200);
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
		
		function resetImg(){
			var width = $('.pic3-panel img').width();
			$('.pic3-panel img').css("height", width/3*2);
		}
	</script>
</body>
</html>
