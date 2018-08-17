<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/front/include/taglib.jsp"%><!DOCTYPE HTML>

<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="initial-scale=1.0, maximum-scale=2.0, minimum-scale=1.0, user-scalable=yes, width=device-width">
<title>${article.title}_${article.category.name}_${site.name }</title>
<meta name="description" content="${article.description}">
<meta name="keywords" content="${article.keywords }">
<meta name="robots" content="all">
<link rel="stylesheet" href="/css/tansuo.css" />
<link rel="alternate" media="only screen and(max-width:640px)" />
<!--1111-->
</head>
<body>
	<div class="header-bg">
		<div class="header">
			<a class="logo" href="#"><img width="149" height="42"
				src="/images/logo1.png" /></a>
			<ul class="nav">
				<li><a href="/zy">养生中医</a></li>
				<li><a href="/news">养生新知</a></li>
				<li><a href="/bj">养生保健</a></li>
				<li><a href="/photo">养生图说</a></li>
			</ul>
		</div>
	</div>
	<div class="wrap clearfix">
		<div class="blank20"></div>
		<div class="article_detail">
			<div class="breadcrumb">
				<!-- <a href="#" target="_blank">首页</a> &gt; <a href="#" target="_blank">国际</a>
				&gt; <span>正文</span> -->
				<a href='/'>首页</a>><a href='/tansuo'>${category.name}</a>> 正文
			</div>
			<div class="blank20"></div>
			<div class="title">
				<h1>${article.title}</h1>
				<div class="subtitle clearfix">
					<span><fmt:formatDate value="${article.createDate}"
							pattern="yyyy-MM-dd HH:mm:ss" /></span>&nbsp;&nbsp; <span
						class="profile_avatar">作者：${article.createBy}</span>&nbsp;&nbsp;
					来源：<span><a rel="nofollow" style="color: #808080"
						href="http://www.88ysg.com" target="_blank">88养生堂</a></span>
				</div>
			</div>
			<div class="blank20"></div>
			<div class="arc_text ">
				<%-- <p style="text-align: center;">
					<a href="#"><img src="${article.image }"
						title="${article.title }" alt="${article.title }" /></a>
				</p> --%>
				<p>${article.articleData.content }</p>
			</div>
			<div class="blank20"></div>
			<div class="blank20"></div>
			<c:if test="${page.last>=2}">
				<div class="page-box">
					<div class="dede_pages">
						<ul class="pagelist">
							<li><a>共${page.count }页: </a></li>
							<li><a href="${article.id}.shtml">首页</a></li>
							<c:if test="${page.index>1}">
								<li><a href="${article.id}_${page.index-1}.shtml">上一页</a></li>
							</c:if>
							<c:forEach var="t" begin="1" end="${page.last}">
								<li <c:if test="${t==page.index}">class="thisclass"</c:if>><a
									href="${article.id}_${t}.shtml">${t}</a></li>
							</c:forEach>
							<c:if test="${page.index<page.last}">
								<li><a href="${article.id}_${page.index+1}.shtml">下一页</a></li>
							</c:if>
							<li><a href="${article.id}_${page.last}.shtml">尾页</a></li>
						</ul>
					</div>
				</div>
			</c:if>
			<div class="blank20"></div>
			<div class="extend-read">
				<h1>延伸阅读</h1>
				<ul>
					<c:forEach var="t"
						items="${fnc:getRondomList('1470964716658',6,'')}" varStatus="i">
						<li><a target="_blank" href="${t.url }" max-length="36">${t.title }</a></li>
					</c:forEach>
					<!-- <li>安徽艺考生夜间训练 嘴咬筷子深蹲夹纸</li>
					<li>安徽艺考生夜间训练 嘴咬筷子深蹲夹纸</li>
					<li>安徽艺考生夜间训练 嘴咬筷子深蹲夹纸</li>
					<li>安徽艺考生夜间训练 嘴咬筷子深蹲夹纸</li>
					<li>安徽艺考生夜间训练 嘴咬筷子深蹲夹纸</li> -->
				</ul>
			</div>
			<div class="blank20"></div>
		</div>
		<div class="blank20"></div>
		<div class="g_imgs">
			<h1>天天有奇闻</h1>
			<div class="left allFemale">
				<div class="inner">
					<c:forEach var="t" items="${fnc:getPositionList('','25',12,'')}"
						varStatus="i">
						<a class="item" target="_blank"
							alt="${!empty t.shorttile?t.shorttile:t.title}" href="${t.url }"
							title="${!empty t.shorttile?t.shorttile:t.title}"><img
							src="${t.image }" width="130" height="100"
							style="display: block;"><span class="t">${fns:abbr(!empty t.shorttile?t.shorttile:t.title, 23)}</span></a>
						<!--<p>${fns:abbr(!empty t.shorttile?t.shorttile:t.title, 23)}</p></a>-->
					</c:forEach>
					<!-- <a class="item" href="#" title="老版西游记雷人亮点" target="_blank"><img
						class="img lazy"
						original="http://www.xiwuji.com/d/file/jingcaitupian/2016-01-04/a0c100d571509f582e382b1f4d50fead.jpg"
						width="130" height="100" alt="老版西游记雷人亮点"
						src="/d/file/jingcaitupian/2016-01-04/a0c100d571509f582e382b1f4d50fead.jpg"
						style="display: block;"><span class="t" title="老版西游记雷人亮点">老版西游记雷人亮点</span></a>
					<a class="item" href="#" title="餐馆遇让人惊魂的一幕" target="_blank"><img
						class="img lazy"
						original="http://www.xiwuji.com/d/file/jingcaitupian/2016-03-09/dba0d4bb8ee9e592d4fa3a4ed885ae76.jpg"
						width="130" height="100" alt="餐馆遇让人惊魂的一幕"
						src="/d/file/jingcaitupian/2016-03-09/dba0d4bb8ee9e592d4fa3a4ed885ae76.jpg"
						style="display: block;"><span class="t" title="餐馆遇让人惊魂的一幕">餐馆里上演的惊魂记</span></a>
					<a class="item" href="#" title="镜头下的中国性工作者" target="_blank"><img
						class="img lazy"
						original="http://www.xiwuji.com/d/file/shehuituku/2016-01-07/e12030b66a263c6f8e2a43c2fa460cb3.jpg"
						width="130" height="100" alt="镜头下的中国性工作者"
						src="/d/file/shehuituku/2016-01-07/e12030b66a263c6f8e2a43c2fa460cb3.jpg"
						style="display: block;"><span class="t" title="镜头下的中国性工作者">镜头下的中国性工作</span></a>
					<a class="item" href="#" title="无法解释的5个诡异事件" target="_blank"><img
						class="img lazy"
						original="http://www.xiwuji.com/d/file/jingcaitupian/2016-01-05/small14519861228568961451986555.jpg"
						width="130" height="100" alt="无法解释的5个诡异事件"
						src="/d/file/jingcaitupian/2016-01-05/small14519861228568961451986555.jpg"
						style="display: block;"><span class="t" title="无法解释的5个诡异事件">无法解释的诡异事件</span></a>
					<a class="item" href="#" title="中国黑社会老大排行榜" target="_blank"><img
						class="img lazy"
						original="http://www.xiwuji.com/d/file/jingcaitupian/2016-01-05/small14519843774518561452036558.jpg"
						width="130" height="100" alt="中国黑社会老大排行榜"
						src="/d/file/jingcaitupian/2016-01-05/small14519843774518561452036558.jpg"
						style="display: block;"><span class="t" title="中国黑社会老大排行榜">中国黑社会排行榜</span></a>
			
			
					<a class="item" href="#" title="中国各省打架排行榜" target="_blank"><img
						class="img lazy"
						original="http://www.xiwuji.com/d/file/jingcaitupian/2016-03-19/37f3d87420857ad90c33e983834c128d.jpg"
						width="130" height="100" alt="中国各省打架排行榜"
						src="/d/file/jingcaitupian/2016-03-19/37f3d87420857ad90c33e983834c128d.jpg"
						style="display: block;"><span class="t" title="中国各省打架排行榜">中国各省打架排行榜</span></a>
					<a class="item" href="#" title="老版西游记雷人亮点" target="_blank"><img
						class="img lazy"
						original="http://www.xiwuji.com/d/file/jingcaitupian/2016-01-04/a0c100d571509f582e382b1f4d50fead.jpg"
						width="130" height="100" alt="老版西游记雷人亮点"
						src="/d/file/jingcaitupian/2016-01-04/a0c100d571509f582e382b1f4d50fead.jpg"
						style="display: block;"><span class="t" title="老版西游记雷人亮点">老版西游记雷人亮点</span></a>
					<a class="item" href="#" title="餐馆遇让人惊魂的一幕" target="_blank"><img
						class="img lazy"
						original="http://www.xiwuji.com/d/file/jingcaitupian/2016-03-09/dba0d4bb8ee9e592d4fa3a4ed885ae76.jpg"
						width="130" height="100" alt="餐馆遇让人惊魂的一幕"
						src="/d/file/jingcaitupian/2016-03-09/dba0d4bb8ee9e592d4fa3a4ed885ae76.jpg"
						style="display: block;"><span class="t" title="餐馆遇让人惊魂的一幕">餐馆里上演的惊魂记</span></a>
					<a class="item" href="#" title="镜头下的中国性工作者" target="_blank"><img
						class="img lazy"
						original="http://www.xiwuji.com/d/file/shehuituku/2016-01-07/e12030b66a263c6f8e2a43c2fa460cb3.jpg"
						width="130" height="100" alt="镜头下的中国性工作者"
						src="/d/file/shehuituku/2016-01-07/e12030b66a263c6f8e2a43c2fa460cb3.jpg"
						style="display: block;"><span class="t" title="镜头下的中国性工作者">镜头下的中国性工作</span></a>
					<a class="item" href="#" title="无法解释的5个诡异事件" target="_blank"><img
						class="img lazy"
						original="http://www.xiwuji.com/d/file/jingcaitupian/2016-01-05/small14519861228568961451986555.jpg"
						width="130" height="100" alt="无法解释的5个诡异事件"
						src="/d/file/jingcaitupian/2016-01-05/small14519861228568961451986555.jpg"
						style="display: block;"><span class="t" title="无法解释的5个诡异事件">无法解释的诡异事件</span></a>
					<a class="item" href="#" title="中国黑社会老大排行榜" target="_blank"><img
						class="img lazy"
						original="http://www.xiwuji.com/d/file/jingcaitupian/2016-01-05/small14519843774518561452036558.jpg"
						width="130" height="100" alt="中国黑社会老大排行榜"
						src="/d/file/jingcaitupian/2016-01-05/small14519843774518561452036558.jpg"
						style="display: block;"><span class="t" title="中国黑社会老大排行榜">中国黑社会排行榜</span></a> -->
				</div>
			</div>
			<div class="left female">
				<div class="inner">
					<!--<c:forEach var="t" items="${fnc:getPositionList('','35',2,'')}"
						varStatus="i">
						<a class="item" target="_blank"
							alt="${!empty t.shorttile?t.shorttile:t.title}" href="${t.url }"
							title="${!empty t.shorttile?t.shorttile:t.title}"><img
							src="${t.image }" width="130" height="100"
							style="display: block;"><span class="t">${fns:abbr(!empty t.shorttile?t.shorttile:t.title, 23)}</span></a> -->
						<!--<p>${fns:abbr(!empty t.shorttile?t.shorttile:t.title, 23)}</p></a>-->
					<!-- </c:forEach> -->
					<a class="item" href="http://www.88ysg.com/sc/1470641934589.shtml?hmsr=xiao&hmpl=%E5%8D%AB%E8%A3%A4%E7%A1%AC%E5%B9%BF&hmcu=%E7%A1%AC%E5%B9%BF%E7%89%88&hmkw=%E5%8D%AB%E8%A3%A4&hmci=" title="如何让女友更爱你" target="_blank"><img
						class="lazy"
						original=""
						width="130" height="100" alt="如何让女友更爱你"
						src="/attached/image/20160816/1471314472338047548.png"
						style="display: block;"><span class="t" title="如何让女友更爱你">如何让女友更爱你</span></a>
<a class="item" href="http://www.88ysg.com/sc/1470641934589.shtml?hmsr=xiao&hmpl=%E5%8D%AB%E8%A3%A4%E7%A1%AC%E5%B9%BF&hmcu=%E7%A1%AC%E5%B9%BF%E7%89%88&hmkw=%E5%8D%AB%E8%A3%A4&hmci=" title="如何让女友更爱你" target="_blank"><img
						class="lazy"
						original=""
						width="130" height="100" alt="如何让女友更爱你"
						src="/attached/image/20160816/1471314472338047548.png"
						style="display: block;"><span class="t" title="如何让女友更爱你">如何让女友更爱你</span></a>
				</div>
			</div>
		</div>
		<div class="blank20"></div>
	</div>
	<!--js-->
	<script type="text/javascript"
		src="${ctx}/static/jquery/jquery-1.9.1.min.js"></script>
	<script type="text/javascript">
		/******首页焦点图******/
		var indx = 1;
		var looper = 3500;
		var myTimer;
		$(document).ready(function(){
			if($('#thscrll') && $('#thscrll img').length > 0) {
				$('#thscrll').css({"padding-bottom":"15px"}); }

			if($("#fcimg li").length >1){
				$("#fcimg").after( $('<div></div><ul id="fcnum"></ul>'));
				for(i=1;i<=$("#fcimg li").length;i++){
					if(i==1) $("#fcnum").append($("<li class=\"crn\"> </li>"));
					else $("#fcnum").append($("<li> </li>"));
				}
				clearInterval(myTimer);
				myTimer = setInterval('showFImg("#fcimg li","#fcnum li","crn")', looper);
				$("#fcnum li").click(function(){
					indx  =  $("#fcnum li").index(this);
					showFImg("#fcimg li","#fcnum li","crn");
					try{
						clearInterval(myTimer);
						myTimer = setInterval('showFImg("#fcimg li","#fcnum li","crn")', looper);
					}catch(e){}
					return false;
				});
				$("#fcimg").hover(function(){
					if(myTimer){ clearInterval(myTimer); }
				},function(){
					clearInterval(myTimer);
					myTimer = setInterval('showFImg("#fcimg li","#fcnum li","crn")', looper);
				});
			}
		});

		function showFImg(il,nl,cs){
			if($(il).length >1){
				crobj = $(il).eq(indx);
				$(il).not(crobj).hide();
				$(nl).removeClass(cs)
				$(nl).eq(indx).addClass(cs);
				crobj.stop(true,true).fadeIn('slow');
				indx = (++indx) % ($(il).length);
			}
		}
		</script>

	<script type="text/javascript">
			(function(e) {
			    e.fn.cxSlide = function(t) {
			        if (!this.length) return;
			        t = e.extend({},
			        e.cxSlide.defaults, t);
			        var n = this,
			        r = {};
			        r.fn = {};
			        var i;
			        r.box = n.find(".focus-box"),
			        r.list = r.box.find(".list"),
			        r.items = r.list.find("li"),
			        r.itemSum = r.items.length;
			        if (r.itemSum <= 1) return;
			        r.numList = n.find(".focus-btn"),
			        r.numBtns = r.numList.find("li"),
			        r.plusBtn = n.find(".plus"),
			        r.minusBtn = n.find(".minus"),
			        r.boxWidth = r.box.width(),
			        r.boxHeight = r.box.height(),
			        r.s = 0;
			        if (t.btn && !r.numList.length) {
			            i = "";
			            for (var s = 1; s <= r.itemSum; s++) i += "<li class='b_" + s + "'>" + s + "</li>";
			            r.numList = e("<ul></ul>", {
			                "class": "focus-btn",
			                html: i
			            }).appendTo(n),
			            r.numBtns = r.numList.find("li")
			        }
			        t.plus && !r.plusBtn.length && (r.plusBtn = e("<div></div>", {
			            "class": "plus"
			        }).appendTo(n)),
			        t.minus && !r.minusBtn.length && (r.minusBtn = e("<div></div>", {
			            "class": "minus"
			        }).appendTo(n)),
			        r.fn.on = function() {
			            if (!t.auto) return;
			            r.fn.off(),
			            r.run = setTimeout(function() {
			                r.fn.goto()
			            },
			            t.time)
			        },
			        r.fn.off = function() {
			            typeof r.run != "undefined" && clearTimeout(r.run)
			        },
			        r.fn.checkBtn = function(e) {
			            r.numList.length && r.numBtns.eq(e).addClass("selected").siblings("li").removeClass("selected")
			        },
			        r.fn.goto = function(e) {
			            r.fn.off();
			            var n = typeof e == "undefined" ? r.s + 1 : parseInt(e, 10),
			            i = r.s,
			            s = r.itemSum - 1;
			            if (n == r.s) {
			                r.fn.on();
			                return
			            }
			            n > s ? n = 0 : n < 0 && (n = s),
			            r.fn.checkBtn(n);
			            var o;
			            switch (t.type) {
			            case "x":
			                o = r.boxWidth * n,
			                n == 0 && i == s ? (r.items.eq(0).css({
			                    left: r.boxWidth * r.itemSum
			                }), o = r.boxWidth * r.itemSum) : i == 0 && (r.items.eq(0).css({
			                    left: ""
			                }), r.box.scrollLeft(0)),
			                r.box.stop(!0, !1).animate({
			                    scrollLeft: o
			                },
			                t.speed);
			                break;
			            case "y":
			                o = r.boxHeight * n,
			                n == 0 && i == s ? (r.items.eq(0).css({
			                    top: r.boxHeight * r.itemSum
			                }), o = r.boxHeight * r.itemSum) : i == 0 && (r.items.eq(0).css({
			                    top: ""
			                }), r.box.scrollTop(0)),
			                r.box.stop(!0, !1).animate({
			                    scrollTop: o
			                },
			                t.speed);
			                break;
			            case "fade":
			                r.items.css({
			                    display:
			                    "none",
			                    position: "absolute",
			                    top: 0,
			                    left: 0,
			                    zIndex: ""
			                }),
			                r.items.eq(i).css({
			                    display: "",
			                    zIndex: 1
			                }),
			                r.items.eq(n).css({
			                    zIndex: 2
			                }).fadeIn(t.speed);
			                break;
			            case "toggle":
			                r.items.eq(n).show().siblings("li").hide()
			            }
			            r.s = n,
			            r.box.queue(function() {
			                r.fn.on(),
			                r.box.dequeue()
			            })
			        },
			        r.box.hover(function() {
			            r.fn.off()
			        },
			        function() {
			            r.fn.on()
			        }),
			        t.btn && r.numList.delegate("li", t.events,
			        function() {
			            r.fn.goto(r.numBtns.index(e(this)))
			        }),
			        t.plus && r.plusBtn.bind(t.events,
			        function() {
			            r.fn.goto()
			        }),
			        t.minus && r.minusBtn.bind(t.events,
			        function() {
			            r.fn.goto(r.s - 1)
			        }),
			        r.fn.checkBtn(t.start),
			        r.fn.goto(t.start)
			    },
			    e.cxSlide = {
			        defaults: {
			            events: "click",
			            type: "x",
			            start: 0,
			            speed: 800,
			            time: 3e3,
			            auto: !0,
			            btn: !0,
			            plus: !1,
			            minus: !1
			        }
			    }
			})(jQuery);
		</script>
	<script>
			$("#slide_x").cxSlide({plus:true,minus:true});
		</script>
</body>
</html>
<script type="text/javascript">
    /*探索双对联*/
    var cpro_id = "u2736990";
</script>
<script type="text/javascript" src="http://cpro.baidustatic.com/cpro/ui/f.js"></script>
<script type="text/javascript">
    /*探索顶搜*/
    var cpro_psid = "u2736995";
</script>
<script type="text/javascript" src="http://su.bdimg.com/static/dspui/js/f.js"></script>