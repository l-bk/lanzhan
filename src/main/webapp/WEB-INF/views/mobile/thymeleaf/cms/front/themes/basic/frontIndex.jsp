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
<title>腾云军事</title>
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
	$(function() {
		$('.topic-gallery-container').flicker({
			dot_alignment : 'right',
			block_text : false
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
			<a href="/" class="logo logo1"> <cite>腾云军事</cite>
			</a>
		</h1>
		<!--logo-->
		<div class="nav-container">
			<c:import url="/WEB-INF/views/modules/cms/front/include/nav.jsp" />
		</div>
	</header>

	<div class="topic-gallery-container">
		<ul>
			<c:forEach var="t" items="${fnc:getPositionList('','0',3,'')}">
				<a href="${t.url}"
					title="${not empty t.shorttile?t.shorttile:t.title}" target="_bank">
					<li data-background="${t.image}">
						<div class="flick-title">${not empty t.shorttile?t.shorttile:t.title}</div>
				</li>
				</a>
			</c:forEach>
		</ul>
	</div>
	

	<c:forEach var="t" items="${fnc:getPositionList('','3',3,'')}"
		varStatus="i">
		<div class="posi-content">
			<a href="${t.url}"
				title="${not empty t.shorttile?t.shorttile:t.title}" target="_bank">
				<div class="img-panel">
					<c:if test="${not empty t.image}">
						<img class="lazy" data-original="${t.image}" src="/js/grey2.png"
							width="120" height="80">
					</c:if>
				</div>
				<div class="text-panel">
					<div class="content-title">${not empty t.shorttile?t.shorttile:t.title}
					</div>
					<div class="content-source">
						来源：${!empty t.msgsource.name?t.msgsource.name:'互联网'} <span
							class="time"><fmt:formatDate value="${t.createDate}"
								pattern="MM-dd" /></span>
					</div>
				</div>
			</a>
		</div>
	</c:forEach>
	
	<c:forEach items="${fnc:getMoreImgPositionList('','2',1,'')}" var="t"
		begin="0" end="0">
		<div class="pic3-panel">
			<a href="${t.url}"
				title="${not empty t.shorttile?t.shorttile:t.title}" target="_bank">
				<div class="pic3-title">${not empty t.shorttile?t.shorttile:t.title}</div>
				<div class="img3-panel">
					<c:forEach items="${t.images}" var="i" begin="0" end="2">
						<div class="img-box">
							<div class="picbox">
								<c:if test="${not empty i}">
									<img data-original="${i}" src="/js/grey2.png" class="lazy img">
								</c:if>
							</div>
						</div>
					</c:forEach>
				</div>
				<div class="content-source">
					来源：${!empty t.msgsource.name?t.msgsource.name:'互联网'} <span
						class="time"><fmt:formatDate value="${t.createDate}"
							pattern="MM-dd" /></span>
				</div>
			</a>
		</div>
	</c:forEach>


	<%-- <c:forEach var="t" items="${fnc:getPositionList('','3',4,'')}"
		varStatus="i">
		<div class="posi-content">
			<a href="${t.url}"
				title="${not empty t.shorttile?t.shorttile:t.title}" target="_bank">
				<div class="img-panel">
					<c:if test="${not empty t.image}">
						<img class="lazy" data-original="${t.image}" src="/js/grey2.png"
							width="120" height="80">
					</c:if>
				</div>
				<div class="text-panel">
					<div class="content-title">${not empty t.shorttile?t.shorttile:t.title}
					</div>
					<div class="content-source">
						来源：${!empty t.msgsource.name?t.msgsource.name:'互联网'} <span
							class="time"><fmt:formatDate value="${t.createDate}"
								pattern="MM-dd" /></span>
					</div>
				</div>
			</a>
		</div>
	</c:forEach> --%>


	<div id="J_NewsList"></div>

	<div id="J_NewsListTips" class="list-tips">正在加载...</div>

	<footer>
		<div class="footer" >
			<p style="font-size: 16px;">腾云军事 www.60mil.com</p>
			<p>
				<a href="http://www.miitbeian.gov.cn/state/outPortal/loginPortal.action;jsessionid=rpDyXRXVH0Bv883FlqX2Q214zhrRD80pfzclPFPLLjKxLF51S5GT!1219073593">粤ICP备15002994号-1</a>
			</p>
			<p>本站文章来源于网络 仅作为展示之用 版权归原作者所有</p>
		</div>
	</footer>
	<script type="text/javascript">
		$(function() {
			$(window).on("scroll", function() {
				if ($(this).scrollTop() >= 88) {
					$("#J_Nav").addClass("fixed");
				} else {
					$("#J_Nav").removeClass("fixed");
				}
			});

			var curUrl = window.location.pathname;
			$('#J_Nav a').each(function() {
				var href = $(this).attr('href');
				if (curUrl.indexOf(href) > -1) {
					$(this).css({"color": "#1A5A9A","font-weight":"bold"});
				}
			});

			resetImg();
			$(window).resize(function() {
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
					if (allowAutoGetNewsList && !newsListLoading
							&& $body.scrollTop() > loadingCondition) {
						fetchNewsList();
					}
				});
		var index = 1;

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
			$
					.ajax({
						data : {
							"type" : "3",
							"type2" : "2",
							"index" : reqIndex,
							"size" : 4,
							"size2" : 1,
							"officSize": 1,
							noCache : Math.random()
						},
						url : '/getJSONPosArticleList',
						timeout : 20000,
						dataType : "json",
						async:false,
						success : function(d) {
							var data1 = d.data1;
							var data2 = d.data2;
							if ($(data1).size() > 0) {
								
								$
										.each(
												data1,
												function(i, v) {
													var html = '<div class="posi-content"><a href="'
															+ v.url
															+ '" title="'
															+ (v.shorttile != ''
																	&& v.shorttile != null ? v.shorttile
																	: v.title)
															+ '"><div class="img-panel">';
													if (v.image != undefined
															&& v.image != null
															&& v.image != '') {
														html += '<img class="lazy" data-original="'
																+ v.image
																+ '" src="/js/grey2.png" width="120" height="80" alt="'
																+ (v.shorttile != ''
																		&& v.shorttile != null ? v.shorttile
																		: v.title)
																+ '">';
													}
													html += '</div><div class="text-panel"><div class="content-title">'
															+ (v.shorttile != ''
																	&& v.shorttile != null ? v.shorttile
																	: v.title)
															+ '</div><div class="content-source">'
															+ '来源：'
															+ ((v['msgsource'] != null) ? v.msgsource.name
																	: '互联网')
															+ '<span class="time">'
															+ (v.createDate)
																	.substring(
																			5,
																			10)
															+ '</span></div></div></a></div>'
													$("#J_NewsList").append(
															html);
												});
								if ($(data2).size() > 0) {
									$
											.each(
													data2,
													function(i, v) {
														var html = '<div class="pic3-panel"><a href="'
																+ v.url
																+ '" title="'
																+ (v.shorttile != ''
																		&& v.shorttile != null ? v.shorttile
																		: v.title)
																+ '" target="_bank"><div class="pic3-title">'
																+ (v.shorttile != ''
																		&& v.shorttile != null ? v.shorttile
																		: v.title)
																+ '</div><div class="img3-panel">';
														if (v.images != null) {
															for (var i = 0; i < 3; i++) {
																if (v.images[i] != undefined
																		&& v.images[i] != null
																		&& v.images[i] != '') {
																	html += '<div class="img-box"><div class="picbox"><img data-original="' + v.images[i] + '" src="/js/grey2.png" class="lazy img"></div></div>';
																}
															}
														}
														html += '</div><div class="content-source">来源：'
																+ ((v['msgsource'] != null) ? v.msgsource.name
																		: '互联网')
																+ '<span class="time">'
																+ (v.createDate)
																		.substring(
																				5,
																				10)
																+ '</span></div></a></div>';
														$("#J_NewsList")
																.append(html);
													});
								}
								index++;
							} else {
								$("#J_NewsListTips").text("已经是最后一页")
							}
							resetAutoGetNewsList();
							resetImg();
							setTimeout(function() {
								$('img.lazy').lazyload({
								/* effect : "fadeIn" */
								});
							}, 200);
						},
						error : function() {
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

		function resetImg() {
			var width = $('.pic3-panel img').width();
			$('.pic3-panel img').css("height", width / 3 * 2);
		}
	</script>
</body>
</html>
