<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%><!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="description" content="${article.description}">
<meta name="keywords" content="${article.keywords}">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="format-detection" content="telephone=no">
<title>${article.title}_${article.category.name}_${site.name }</title>
<link rel="stylesheet" href="/css/normalize.css">
<link rel="stylesheet" href="/css/article.css">
<script type="text/javascript" src="/static/jquery/jquery-1.9.1.min.js"></script>
<script src="/js/jquery.lazyload.js" type="text/javascript"></script>
<!-- img懒加载 -->
<script type="text/javascript" charset="utf-8">
	$(function() {
		//获取文章动态加载的img标签,并加入lazyload
		var list=document.getElementById("contentID").getElementsByTagName("img");
		for (var i=0; i<list.length;i++){
			var imgvalue = list[i].src; 
			list[i].setAttribute('data-original',imgvalue);
			list[i].setAttribute('src','/js/grey2.png');				
		}
		$("img").lazyload({
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
		<!--<script type="text/javascript"> 
		     /*wap顶部横幅*/ 
		     var cpro_id = "u2642134";
		</script>
		<script src="http://cpro.baidustatic.com/cpro/ui/cm.js" type="text/javascript"></script>-->
		<h1 class="top">
			<a class="aback" onclick="history.go(-1);"></a>
			<a href="/${article.category.nameEn}" class="logo logo${article.category.id}">
				<cite>腾云军事·${article.category.name}</cite>
			</a>
			<a class="menu"></a>
		</h1>
		<!--logo-->
		<div class="nav-container" style="display: none">
			<c:import url="/WEB-INF/views/modules/cms/front/include/nav.jsp"/>
		</div>
		<script type="text/javascript">
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
		</script>
	</header>
	<article class="article">
		<!--文章-->
		<h1 class="art-title">${article.title }</h1>
		<div class="art-info">
			<span>时间：<fmt:formatDate value="${article.createDate}"
					pattern="yyyy-MM-dd HH:mm:ss" /></span><span>来源：<a
				href="${!empty article.msgsource.url?article.msgsource.url:article.acquisitionSource}">${!empty article.msgsource.name?article.msgsource.name:'互联网'}</a></span><span
				style="display:none;">点击：${article.hits} 次</span>
		</div>
		
		<!--广告位：PC标题下  -->
 		<c:forEach var='i' items="${fnc:getAdsList('title_bottom','2')}">
			${fns:unescapeHtml(i.normbody)}
		</c:forEach>		
		
		<div id="contentID" class="article-content">
			${fnc:WordtoMobileImg(pageContext.request,article.articleData.content)}
		</div>

		</article>
		
		<!--广告位：PC翻页上  -->
<!--<script type="text/javascript">
var cpro_id="u2641938";
(window["cproStyleApi"] = window["cproStyleApi"] || {})[cpro_id]={at:"3",hn:"1",wn:"3",imgRatio:"1.7",scale:"20.6",pat:"6",tn:"template_inlay_all_mobile_lu_native",rss1:"#FFFFFF",adp:"1",ptt:"0",titFF:"%E5%BE%AE%E8%BD%AF%E9%9B%85%E9%BB%91",titFS:"14",rss2:"#000000",titSU:"0",ptbg:"70",ptp:"0"}
</script>
<script src="http://cpro.baidustatic.com/cpro/ui/cm.js" type="text/javascript"></script>-->

		<article class="article">
		
		<c:forEach var='i' items="${fnc:getAdsList('page_top','2')}">
			${fns:unescapeHtml(i.normbody)}
		</c:forEach>
		
		<c:if test="${page.last>=2}">
			<div class="page-box">
				<div class="dede_pages">
					<ul class="pagelist">
						<li><a>共${page.count}页 </a></li>
						<li><a href="${article.id}${fns:getUrlSuffix()}">首页</a></li>
						<c:if test="${page.index>1}">
							<li><a
								href="${article.id}_${page.index-1}${fns:getUrlSuffix()}">上一页</a></li>
						</c:if>
						<c:forEach var="t" begin="1" end="${page.last}">
							<li <c:if test="${t==page.index}">class="thisclass"</c:if>><a
								href="${article.id}_${t}${fns:getUrlSuffix()}">${t}</a></li>
						</c:forEach>
						<c:if test="${page.index<page.last}">
							<li><a href="${article.id}_${page.index+1}${fns:getUrlSuffix()}">下一页</a></li>
						</c:if>
						<li><a href="${article.id}_${page.last}${fns:getUrlSuffix()}">尾页</a></li>
					</ul>
				</div>
			</div>
		</c:if>
		
		<!--广告位：PC翻页下  -->
		<c:forEach var='i' items="${fnc:getAdsList('page_bottom','2')}">
			${fns:unescapeHtml(i.normbody)}
		</c:forEach>	
		
		<section>
			
			<!--百度分享-->
			<div class="share">
				<a href="javascript:void(0);" class="share-btn" id="shareBtn"></a> <span>
					<div class="bdsharebuttonbox">
						<a href="#" class="bds_more" data-cmd="more"></a><a href="#"
							class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a><a
							href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a><a
							href="#" class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博"></a><a
							href="#" class="bds_renren" data-cmd="renren" title="分享到人人网"></a><a
							href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a>
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
							"selectShare" : {
								"bdContainerClass" : null,
								"bdSelectMiniList" : [ "qzone", "tsina", "tqq",
										"renren", "weixin" ]
							}
						};
						with (document)
							0[(getElementsByTagName('head')[0] || body)
									.appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='
									+ ~(-new Date() / 36e5)];
					</script>
				</span>
			</div>
			<script type="text/javascript">
				$('#shareBtn').click(function() {
					var $next = $(this).next();
					if ($next.hasClass('act')) {
						$next.removeClass('act');
					} else {
						$next.addClass('act');
					}
				});
				$(function(){
					resetImg();
					$(window).resize(function(){
						resetImg();
					});
				});
				
				function resetImg(){
					var width = $('.img').width();
					$('.img').css("height", width/3*2);
				}
			</script>
		</section>
		<!--./百度分享-->
		<section>
			<!--相关新闻-->
			<div class="relate-news">
				<h2>推荐阅读</h2>
				<aside class="recommend_moudule j_relevent_box" data-sudaclick="recommend_news">
					<c:forEach items="${fnc:getPositionList('','5',2,'')}" var="t" varStatus="i">
						<a href="${t.url}" title="${not empty t.shorttile?t.shorttile:t.title}" target="_bank">
							<dl class="clearfix">
								<dt class="j_art_lazy">
									<img data-original="${t.image}" width="120" height="80" src="/js/grey2.png" />
								</dt>
								<dd>
									<h3 class="title">
										${not empty t.shorttile?t.shorttile:t.title}
									</h3>
									<div class="mark_count">
										<span>
											${!empty t.msgsource.name?t.msgsource.name:'互联网'}
										</span>
										<time>
											<fmt:formatDate value="${t.createDate}" pattern="MM-dd" />
										</time>
									</div>
								</dd>
							</dl>
						</a>
					</c:forEach>
					
		<!--广告位：PC推荐阅读一  -->
		<c:forEach var='i' items="${fnc:getAdsList('read_one','2')}">
			${fns:unescapeHtml(i.normbody)}
		</c:forEach>			
							
					<c:forEach items="${fnc:getMoreImgPositionList('','6',1,'')}" var="t" begin="0" end="0">
						<a href="${t.url}" title="${not empty t.shorttile?t.shorttile:t.title}" target="_bank">
							<dl class="three-img clearfix">
								<dd>
									<h3 class="title">
										${not empty t.shorttile?t.shorttile:t.title}
									</h3>
								</dd>
								<div class="img3-panel">
									<c:forEach items="${t.images}" var="i" begin="0" end="2">
										<div class="img-box">
											<div class="picbox">
												<img data-original="${i}" src="/js/grey2.png" class="img" />
											</div>
										</div>
									</c:forEach>
								</div>
								<div class="mark_count">
									<span>
										${!empty t.msgsource.name?t.msgsource.name:'互联网'}
									</span>
									<time>
										<fmt:formatDate value="${t.createDate}" pattern="MM-dd" />
									</time>
								</div>
							</dl>
						</a>
					</c:forEach>
					
		<!--广告位：PC推荐阅读二  -->
		<c:forEach var='i' items="${fnc:getAdsList('read_two','2')}">
			${fns:unescapeHtml(i.normbody)}
		</c:forEach>			 
								
					<c:forEach items="${fnc:getPositionList('','5',5,'')}" var="t" begin="2" end="5">
						<a href="${t.url}" title="${not empty t.shorttile?t.shorttile:t.title}" target="_bank">
							<dl class="clearfix">
								<dt class="j_art_lazy">
									<img data-original="${t.image}" width="120" height="80" src="/js/grey2.png" />
								</dt>
								<dd>
									<h3 class="title">
										${not empty t.shorttile?t.shorttile:t.title}
									</h3>
									<div class="mark_count">
										<span>
											${!empty t.msgsource.name?t.msgsource.name:'互联网'}
										</span>
										<time>
											<fmt:formatDate value="${t.createDate}" pattern="MM-dd" />
										</time>
									</div>
								</dd>
							</dl>
						</a>
					</c:forEach>
				</aside>
			</div>						
		</section>

		<!--广告位：PC推荐阅读三  -->
		<c:forEach var='i' items="${fnc:getAdsList('read_three','2')}">
			${fns:unescapeHtml(i.normbody)}
		</c:forEach>
				
		<section class="extend-module">
			<div class="relate-news" style="margin-bottom: 0;">
				<h2>精彩图片</h2>
				<ul class="picture_moudule clearfix" data-sudaclick="tab1_content" style="padding-bottom: 0;">
					<c:forEach items="${fnc:getPositionList('','4',4,'')}" var="t"
						varStatus="i">
						<li><a href="${t.url}">
								<div class="pic-wrap">
									<img data-original="${t.image}" src="/js/grey2.png" alt="${not empty t.shorttile?t.shorttile:t.title}">
									<p>${not empty t.shorttile?t.shorttile:t.title}</p>
								</div>
						</a></li>
					</c:forEach>
				</ul>
			</div>
		</section>
	</article>
	<!--./文章-->
	
	<!--广告位：PC精彩图片  -->
	<c:forEach var='i' items="${fnc:getAdsList('photo_one','2')}">
			${fns:unescapeHtml(i.normbody)}
	</c:forEach>	
	
	<!--广告位：PC最底部  -->
	<c:forEach var='i' items="${fnc:getAdsList('bottom','2')}">
			${fns:unescapeHtml(i.normbody)}
	</c:forEach>
		
	<!-- 广告位 ［wap精彩图片］-->
<!--<script type="text/javascript">
var cpro_id="u2642140";
(window["cproStyleApi"] = window["cproStyleApi"] || {})[cpro_id]={at:"3",hn:"2",wn:"2",imgRatio:"1.7",scale:"20.14",pat:"6",tn:"template_inlay_all_mobile_lu_native",rss1:"#FFFFFF",adp:"1",ptt:"0",titFF:"%E5%BE%AE%E8%BD%AF%E9%9B%85%E9%BB%91",titFS:"14",rss2:"#000000",titSU:"0",ptbg:"70",ptp:"0"}
</script>
<script src="http://cpro.baidustatic.com/cpro/ui/cm.js" type="text/javascript"></script>-->


<!-- 广告位 ［wap最底部］-->		
<!--<script type="text/javascript">
    /*wap悬浮*/
    var cpro_id = "u2642122";
</script>
<script src="http://cpro.baidustatic.com/cpro/ui/cm.js" type="text/javascript"></script>-->

<!--<script type="text/javascript"> 
     /*wap度宝*/ 
     var cpro_id = "u2642130";
</script>
<script src="http://cpro.baidustatic.com/cpro/ui/cm.js" type="text/javascript"></script>

<script type="text/javascript"> 
     /*wap智能*/ 
     var cpro_id = "u2642128";
</script>
<script src="http://cpro.baidustatic.com/cpro/ui/cm.js" type="text/javascript"></script>-->


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
		<div class="footer" >
			<p style="font-size: 16px;">腾云军事 www.60mil.com</p>
			<p>
				<a href="http://www.miitbeian.gov.cn/state/outPortal/loginPortal.action;jsessionid=rpDyXRXVH0Bv883FlqX2Q214zhrRD80pfzclPFPLLjKxLF51S5GT!1219073593">粤ICP备15002994号-1</a>
			</p>
			<p>本站文章来源于网络 仅作为展示之用 版权归原作者所有</p>
		</div>
	</footer>
	
	<script type="text/javascript">		
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
	</script>
</body>
</html>