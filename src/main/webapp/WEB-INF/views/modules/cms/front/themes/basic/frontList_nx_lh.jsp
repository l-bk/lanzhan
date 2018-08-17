<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%><!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="description" content="${site.description}">
<meta name="keywords" content="${site.keywords}">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="format-detection" content="telephone=no">
<title>撩汉宝典 - 悦读 女性</title>
<link rel="stylesheet" href="${ctx}/css/main.css">
<script type="text/javascript" src="${ctx}/static/jquery/jquery-1.9.1.min.js"></script>
<script src="${ctx}/js/jquery.lazyload.js" type="text/javascript"></script>
</head>
<script type="text/javascript">
	$(document).ready(function() {
		$("#lh").addClass("active");
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
		<li><a href="#">时尚达人</a></li>
		<li><a href="#">美容美妆</a></li>
		<li class="active"><a href="#">撩汉宝典</a></li>
	</ul> -->



	<div class="column-panel" style="padding-bottom: 0;">
		<c:forEach items="${page.list}" var="t" varStatus="i" begin="0">
			<section class="m_article list-item  special_section clearfix" id="BRFIOPFA00014PRF">
				<a href="${t.url}" title="${t.title}" target="_bank">
					<div class="m_article_img">
						<!-- 	<div class="m_article_mark mark_new">专题</div> -->
						<img width="120" height="90" class="lazy" src="${t.image}">
					</div>
					<div class="m_article_info">
						<div class="m_article_title">
							<span>${!empty t.shorttile?t.shorttile:t.title}</span>
						</div>
					</div>
				</a>
			</section>

		</c:forEach>

		<div id="J_NewsList"></div>
	</div>

	<div class="more-btn list-tips">${page.count!=0?'加载更多':'无相关的文章'}</div>
	<div class="footer">
		<!-- <a href="#" class="about-apd">关于悦读</a> -->
		<p>©版权所有 盗版必究</p>
	</div>

	<script type="text/javascript">
		var $J_Nav = $("#J_Nav");
		$(window).on('scroll', function() {
			/* alert($(this).scrollTop()); */
			/* if ($(this).scrollTop() >= 98) {
				$('.nav-container').show();
				$J_Nav.addClass("fixed");
			} else {
				$J_Nav.removeClass("fixed");
				$('.nav-container').hide();
			} */
		});

		var curUrl = window.location.pathname;
		$('#J_Nav a').each(function() {
			var href = $(this).attr('href');
			if (curUrl.indexOf(href) > -1 && href != "/") {
				$(this).css("color", "#cf3a3c");
			}
		});

		$('.menu').click(function() {
			var ele = $('.nav-container');
			var flag = ele.css('display');
			if (flag == "none") {
				ele.show();
			} else {
				ele.hide();
			}
		});

		//滚动加载
		var $body = $("body");
		var allowAutoGetNewsList = true;
		var newsListLoading = false;
		var loadingCondition;
		$(document).bind("scroll", function() {
			if (allowAutoGetNewsList && !newsListLoading && $body.scrollTop() > loadingCondition) {
				fetchNewsList();
			}
		});
		var index = 2;

		function fetchNewsList() {
			newsListLoading = true;
			$.ajax({
				data : {
					"categoryId" : "${category.id}",
					"pageNo" : index,
					"pageSize" : 8,
					noCache : Math.random()
				},
				url : '/getJSONArticleList',
				timeout : 20000,
				dataType : "json",
				success : function(d) {
					if (d.last >= index) {
						$.each(d.list, function(i, t) {
							var html = '<section class="m_article list-item  special_section clearfix" id="BRFIOPFA00014PRF">'
									+ '<a href="'+t.url+'" title="'+t.title+'" target="_bank">' + '<div class="m_article_img">' +
									/* '<div class="m_article_mark mark_new">专题</div>'+ */
									'<img width="120" height="90"  class="lazy"'+
									'	src="'+t.image+'">' + '</div>'
									+ '<div class="m_article_info">' + '<div class="m_article_title">' + '	<span>'
									+ (t.shorttile != '' && t.shorttile != null ? t.shorttile : t.title) + '</span>' + '</div>' + '</div>'
									+ '</a>' + '</section>';
							$("#J_NewsList").append(html);
						});
						index++;
					} else {
						$(".list-tips").text("已经是最后一页")
					}
					resetAutoGetNewsList();
					$("img.lazy").lazyload({
					/* effect : "fadeIn" */
					});
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
	</script>
</body>
</html>