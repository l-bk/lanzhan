<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%><!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="description" content="${article.description}">
<meta name="keywords" content="${article.keywords}">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="format-detection" content="telephone=no">
<title>${article.title}_${article.category.name}_${site.name }</title>
<link rel="stylesheet" href="${ctx}/css/main.css">
<script type="text/javascript" src="${ctx}/static/jquery/jquery-1.9.1.min.js"></script>
<script src="${ctx}/js/jquery.lazyload.js" type="text/javascript"></script>
<!-- img懒加载 -->
<script type="text/javascript" charset="utf-8">
	$(function() {
		//获取文章动态加载的img标签,并加入lazyload
		var list = document.getElementById("contentID").getElementsByTagName("img");
		for (var i = 0; i < list.length; i++) {
			var imgvalue = list[i].src;
			list[i].setAttribute('data-original', imgvalue);
			list[i].setAttribute('src', '/js/grey.jpg');
		}
		$("img").lazyload({
			effect : "fadeIn"
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
		<script type="text/javascript">
			/*wap顶部横幅*/
			var cpro_id = "u2642134";
		</script>
		<script src="http://cpro.baidustatic.com/cpro/ui/cm.js" type="text/javascript"></script>
		<div class="header">
			<a class="goback" onclick="history.go(-1);"><img width="22" height="22" src="${ctx}/images/goback.jpg" /></a> <a href="${ctx}/sh"
				class="logo logo${article.category.id}"><img width="120" height="30" src="${ctx}/images/logo.jpg" /></a>
		</div>
		<!-- <script type="text/javascript">
			if (isWeiXin()) {
				$('#shareB').show();
			} else {

			}

			// 判断是否通过微信打开网页
			function isWeiXin() {
				var ua = window.navigator.userAgent.toLowerCase();
				if (ua.match(/MicroMessenger/i) == 'micromessenger') {
					return true;
				} else {
					return false;
				}
			}
		</script> -->
	</header>
	<article class="article">
		<!--文章-->
		<h1 class="art-title">${article.title }</h1>
		<div class="art-info">
			<span>时间：<fmt:formatDate value="${article.createDate}" pattern="yyyy-MM-dd HH:mm:ss" /></span><span>来源：<a
				href="${!empty article.msgsource.url?article.msgsource.url:article.acquisitionSource}">${!empty article.msgsource.name?article.msgsource.name:'互联网'}</a></span><span
				style="display: none;">点击：${article.hits} 次</span>
		</div>

		<!--广告位：PC标题下  -->
		<c:forEach var='i' items="${fnc:getAdsList('title_bottom','2')}">
			${fns:unescapeHtml(i.normbody)}
		</c:forEach>

		<div class="article-content">${fnc:WordtoMobileImg(pageContext.request,article.articleData.content)}</div>

		<!--广告位：PC翻页上  -->
<script type="text/javascript">
var cpro_id="u2641938";
(window["cproStyleApi"] = window["cproStyleApi"] || {})[cpro_id]={at:"3",hn:"1",wn:"3",imgRatio:"1.7",scale:"20.6",pat:"6",tn:"template_inlay_all_mobile_lu_native",rss1:"#FFFFFF",adp:"1",ptt:"0",titFF:"%E5%BE%AE%E8%BD%AF%E9%9B%85%E9%BB%91",titFS:"14",rss2:"#000000",titSU:"0",ptbg:"70",ptp:"0"}
</script>
<script src="http://cpro.baidustatic.com/cpro/ui/cm.js" type="text/javascript"></script>
		<%-- <!--广告位：PC翻页上  -->
		<c:forEach var='i' items="${fnc:getAdsList('page_top','2')}">
			${fns:unescapeHtml(i.normbody)}
		</c:forEach> --%>

		<c:if test="${page.last>=2}">
			<div class="page-box">
				<div class="dede_pages">
					<ul class="pagelist">
						<li><a>共${page.count}页 </a></li>
						<li><a href="${article.id}${fns:getUrlSuffix()}">首页</a></li>
						<c:if test="${page.index>1}">
							<li><a href="${article.id}_${page.index-1}${fns:getUrlSuffix()}">上一页</a></li>
						</c:if>
						<c:forEach var="t" begin="1" end="${page.last}">
							<li <c:if test="${t==page.index}">class="thisclass"</c:if>><a href="${article.id}_${t}${fns:getUrlSuffix()}">${t}</a></li>
						</c:forEach>
						<c:if test="${page.index<page.last}">
							<li><a href="${article.id}_${page.index+1}${fns:getUrlSuffix()}">下一页</a></li>
						</c:if>
						<li><a href="${article.id}_${page.last}${fns:getUrlSuffix()}">尾页</a></li>
					</ul>
				</div>
			</div>
		</c:if>
		<section>
			<!--百度分享-->
			<!-- <div class="share">
				<a href="javascript:void(0);" class="share-btn" id="shareBtn"></a> <span>
					<div class="bdsharebuttonbox">
						<a href="#" class="bds_more" data-cmd="more"></a><a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a><a href="#"
							class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a><a href="#" class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博"></a><a href="#"
							class="bds_renren" data-cmd="renren" title="分享到人人网"></a><a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a>
					</div> <script>
						window._bd_share_config = {
							"common" : {
								"bdSnsKey" : {},
								"bdText" : "",
								"bdMini" : "2",
								"bdMiniList" : false,
								"bdPic" : "",
								"bdStyle" : "0",
								"bdSize" : "24"
							},
							"share" : {},
							"image" : {
								"viewList" : [ "qzone", "tsina", "tqq", "renren", "weixin" ],
								"viewText" : "分享到：",
								"viewSize" : "16"
							},
							"selectShare" : {
								"bdContainerClass" : null,
								"bdSelectMiniList" : [ "qzone", "tsina", "tqq", "renren", "weixin" ]
							}
						};
						with (document)
							0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='
									+ ~(-new Date() / 36e5)];
					</script>
				</span>
			</div> -->
			<!-- <script type="text/javascript" src="js/zepto.min.js"></script>
			<script type="text/javascript">
				$('#shareBtn').click(function() {
					var $next = $(this).next();
					if ($next.hasClass('act')) {
						$next.removeClass('act');
					} else {
						$next.addClass('act');
					}
				});
			</script> -->
		</section>
		<!--./百度分享-->
		<section>
			<!--相关新闻-->
			<div class="relate-news">
				<h2>推荐阅读</h2>
				<c:forEach items="${fnc:getPositionList('','14',4,'')}" var="t" varStatus="i">
					<div class="posi-content">
						<a class="img-panel" href="${t.url}" title="${not empty t.shorttile?t.shorttile:t.title}" target="_bank"> <img src="${t.image}"
							width="120" height="80" alt="${not empty t.shorttile?t.shorttile:t.title}">
						</a>
						<div class="text-panel">
							<a href="${t.url}" title="${not empty t.shorttile?t.shorttile:t.title}"></a>
							<div class="content-title">
								<a href="${t.url}" title="${not empty t.shorttile?t.shorttile:t.title}">${not empty t.shorttile?t.shorttile:t.title}</a>
							</div>
							<div class="content-source">来源：${!empty t.msgsource.name?t.msgsource.name:'互联网'}</div>
						</div>
					</div>
				</c:forEach>

			</div>
		</section>
	</article>
	<!--./文章-->
	<!--./相关新闻-->
	<section class="extend-module">
		<ul class="picture_moudule clearfix" data-sudaclick="tab1_content">
			<c:forEach items="${fnc:getMoreImgPositionList('','15',4,'')}" var="t">
				<li><a href="${t.url}"><div class="pic-wrap">
							<img src="${t.image}" alt="${not empty t.shorttile?t.shorttile:t.title}">
							<p>${not empty t.shorttile?t.shorttile:t.title}</p>
						</div></a></li>
			</c:forEach>
		</ul>
	</section>


	<%-- <!--广告位：PC精彩图片  -->
	<c:forEach var='i' items="${fnc:getAdsList('photo_one','2')}">
			${fns:unescapeHtml(i.normbody)}
	</c:forEach>

	<!--广告位：PC最底部  -->
	<c:forEach var='i' items="${fnc:getAdsList('bottom','2')}">
			${fns:unescapeHtml(i.normbody)}
	</c:forEach> --%>

	<!-- 广告位 ［wap精彩图片］-->
	<script type="text/javascript">
		var cpro_id = "u2642140";
		(window["cproStyleApi"] = window["cproStyleApi"] || {})[cpro_id] = {
			at : "3",
			hn : "2",
			wn : "2",
			imgRatio : "1.7",
			scale : "20.14",
			pat : "6",
			tn : "template_inlay_all_mobile_lu_native",
			rss1 : "#FFFFFF",
			adp : "1",
			ptt : "0",
			titFF : "%E5%BE%AE%E8%BD%AF%E9%9B%85%E9%BB%91",
			titFS : "14",
			rss2 : "#000000",
			titSU : "0",
			ptbg : "70",
			ptp : "0"
		}
	</script>
	<script src="http://cpro.baidustatic.com/cpro/ui/cm.js" type="text/javascript"></script>


	<!-- 广告位 ［wap最底部］-->
	<script type="text/javascript">
		/*wap悬浮*/
		var cpro_id = "u2642122";
	</script>
	<script src="http://cpro.baidustatic.com/cpro/ui/cm.js" type="text/javascript"></script>

<!-- 	<script type="text/javascript">
		/*wap度宝*/
		var cpro_id = "u2642130";
	</script>
	<script src="http://cpro.baidustatic.com/cpro/ui/cm.js" type="text/javascript"></script>

	<script type="text/javascript">
		/*wap智能*/
		var cpro_id = "u2642128";
	</script>
	<script src="http://cpro.baidustatic.com/cpro/ui/cm.js" type="text/javascript"></script> -->


	<script>
		var _hmt = _hmt || [];
		(function() {
			var hm = document.createElement("script");
			hm.src = "//hm.baidu.com/hm.js?89826c7b92a7c6bcde1b6f6248becfe8";
			var s = document.getElementsByTagName("script")[0];
			s.parentNode.insertBefore(hm, s);
		})();
	</script>

	<footer>
		<div class="footer">
			<!-- <a href="http://m.apdnews.com/introduction/aboutus/"
				class="about-apd">关于悦读</a> -->
			<p>©版权所有 盗版必究</p>
		</div>
	</footer>

	<script type="text/javascript">
		var curUrl = window.location.pathname;
		$('#J_Nav a').each(function() {
			var href = $(this).attr('href');
			if (curUrl.indexOf(href) > -1 && href != "/") {
				$(this).css("color", "#3E98F0");
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
	</script>
</body>
</html>
