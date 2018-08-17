(function(e) {
    function t(e) {
        return (10 > e ? "0": "") + e
    }
    function n(e, n) {
        e = ("" + e).length < 13 ? 1e3 * e: e;
        var a, i, o, r, s = (new Date).getTime(),
            c = (s - e) / 1e3;
        o = Math.round(c / 86400),
            i = Math.round(c / 3600),
            a = Math.round(c / 60),
            r = Math.round(c);
        var d = !1;
        if ("boolean" == typeof n && (d = n, n = null), d) {
            var l = new Date;
            return l.setTime(e),
            l.getFullYear() + "-" + t(l.getMonth() + 1) + "-" + t(l.getDate()) + " " + t(l.getHours()) + ":" + t(l.getMinutes())
        }
        if (0 > c) return "1分钟前";
        if (o > 0 && 4 > o) return o + "天前";
        if (0 >= o && i > 0) return i + "小时前";
        if (0 >= i && a > 0) return a + "分钟前";
        if (0 >= a && r > 0) return r + "秒钟前";
        if (0 === r) return "刚刚";
        var l = new Date;
        l.setTime(e);
        var u = l.getFullYear() + "-" + t(l.getMonth() + 1) + "-" + t(l.getDate()),
            p = t(l.getHours()) + ":" + t(l.getMinutes());
        return null != n && 1 === n ? u: u + " " + p
    }
    function a(t, n) {
        setTimeout(function() {
                var a = e('<link rel="' + (F() ? "alternate ": "") + 'stylesheet" />');
                e("head").append(a),
                    a.attr("href", [t + (t.indexOf("?") >= 0 ? "": "?") + e.param(n), (new Date).getTime()].join("&")),
                    setTimeout(function() {
                            a.remove()
                        },
                        5e3)
            },
            0)
    }
    function i(t, n) {
        var a = !1,
            i = function() {
                a || (a = !0, n.success())
            };
        setTimeout(i, 500),
            e.ajax({
                type: "POST",
                url: t,
                dataType: "json",
                data: n.params,
                success: i
            })
    }
    function o(t, n) {
        var i, o = R("fr"),
            r = R("from"),
            s = C() ? 1 : 2,
            c = "0",
            d = "http://log.news.baidu.com/v.gif";
        o = o ? o: "",
            r = r ? r: "",
            !o && r ? o = "844c" === r ? "zizhu": "jisuhome": o || r || (o = "zizhu"),
        0 === tt.MODE_NOIMAGE.showImage && (c = "1"),
            i = e.extend({
                    pid: 107,
                    wise: 1,
                    from: "iphone" === window.showType ? "webapp": "lite",
                    fr: o,
                    soe: s,
                    m_ni: c
                },
                t),
            n ? a(d, i) : setTimeout(function() {
                    a(d, i)
                },
                100)
    }
    function r(e, t) {
        var n = e.params;
        if (/^chosen.*/.test(n.class) && 15 === n.pos) var a = "/news?tn=bdapilog&t=feed_ads";
        else var a = "/news?tn=bdapilog&t=mob_ads&n=" + n.class + "_" + n.pos;
        t ? i(a, e) : setTimeout(function() {
                i(a, e)
            },
            200)
    }
    function s(t, n, i) {
        var o = "http://smtlog.news.baidu.com/sn/api/webapplog",
            r = {
                id: t,
                from: "news_webapp",
                pd: "webapp",
                os: e.os.iphone ? "iphone": "android",
                baiduid: l("BAIDUID"),
                data: JSON.stringify(n)
            },
            s = f();
        s && (r.cuid = s),
            i ? a(o, r) : setTimeout(function() {
                    a(o, r)
                },
                100)
    }
    function c(e) {
        return new RegExp('^[^\\x00-\\x20\\x7f\\(\\)<>@,;:\\\\\\"\\[\\]\\?=\\{\\}\\/\\u0080-\\uffff]+$').test(e)
    }
    function d(e) {
        if (c(e)) {
            var t = new RegExp("(^| )" + e + "=([^;]*)(;|$)"),
                n = t.exec(document.cookie);
            if (n) return n[2] || null
        }
        return null
    }
    function l(e) {
        var t = d(e);
        return "string" == typeof t ? t = decodeURIComponent(t) : null
    }
    function u(e, t, n) {
        if (c(e)) {
            n = n || {};
            var a = n.expires;
            "number" == typeof n.expires && (a = new Date, a.setTime(a.getTime() + n.expires)),
                document.cookie = e + "=" + t + (n.path ? "; path=" + n.path: "") + (a ? "; expires=" + a.toGMTString() : "") + (n.domain ? "; domain=" + n.domain: "") + (n.secure ? "; secure": "")
        }
    }
    function p(e, t, n) {
        u(e, encodeURIComponent(t), n)
    }
    function h(e, t) {
        t = t || {},
            t.expires = new Date(0),
            u(e, "", t)
    }
    function f() {
        var t = e.localStorage("NEWS_CUID") && e.localStorage("NEWS_CUID").cuid,
            n = null,
            a = l("cuid") || l("BAIDUCUID") || "";
        return /baiduboxapp\/[^\/]+\/[^\/]+\/[^\/]+\/([^\/]+)\//.test(window.navigator.userAgent) && (n = RegExp.$1),
        t || n || a || ""
    }
    function g(t) {
        e.ajax({
            type: "get",
            url: "http://127.0.0.1:7777/getcuid?callback=?",
            dataType: "jsonp",
            callback: "getCuid",
            success: t
        })
    }
    function m(e, t, n) {
        try {
            window.localStorage.setItem(e, t)
        } catch(a) {
            p(e, t, n)
        }
    }
    function w(e) {
        try {
            return window.localStorage.getItem(e)
        } catch(t) {
            return l(e)
        }
    }
    function v(e) {
        try {
            window.localStorage.removeItem(e)
        } catch(t) {
            h(e)
        }
    }
    function b() {
        var e = "_webappandroid_storage_test",
            t = "ok";
        return m(e, t),
            t == w(e) ? (v(e), !0) : !1
    }
    function y(e, t) {
        try {
            return window.localStorage.setItem(e, t),
                !0
        } catch(n) {
            return ! 1
        }
    }
    function x(e) {
        try {
            return window.localStorage.getItem(e)
        } catch(t) {
            return "@@--not_from_localstorage--@@"
        }
    }
    function _(e) {
        try {
            return window.localStorage.removeItem(e),
                !0
        } catch(t) {
            return ! 1
        }
    }
    function k() {
        var e = "_webappandroid_localstorage_test",
            t = "ok";
        return y(e, t) && t == x(e) ? (_(e), !0) : !1
    }
    function S(e, t, n) {
        return "translate" + (rocket.has3d ? "3d": "") + "(" + e + "px, " + t + "px" + (rocket.has3d ? ", " + n + "px)": ")")
    }
    function T(t, n, a, i) {
        if (0 === a) return t != n && (t && e(t).hide(), setTimeout(function() {
                n && e(n).show()
            },
            0)),
            void(i && i());
        n = e(n),
            t = e(t);
        var o = document.documentElement.clientWidth;
        t.css({
            "-webkit-transition-property": "-webkit-transform",
            "-webkit-transform": S(0, 0, 0),
            "-webkit-transition-duration": "0ms",
            "-webkit-transition-timing-function": "ease-out",
            "-webkit-transition-delay": "initial"
        }),
            n.css({
                "-webkit-transition-property": "-webkit-transform",
                "-webkit-transform": S((1 === a ? "": "-") + o, 0, 0),
                "-webkit-transition-duration": "0ms",
                "-webkit-transition-timing-function": "ease-out",
                "-webkit-transition-delay": "initial",
                display: "block"
            }),
            setTimeout(function() {
                    function e() {
                        t.css({
                            display: "none",
                            "-webkit-transform": S(0, 0, 0),
                            "-webkit-transition-duration": "0ms"
                        }),
                            n.css({
                                display: "block",
                                "-webkit-transform": S(0, 0, 0),
                                "-webkit-transition-duration": "0ms"
                            })
                    }
                    n.css({
                        "-webkit-transform": S(0, 0, 0),
                        "-webkit-transition-duration": "350ms"
                    }),
                        t.css({
                            "-webkit-transform": S((1 === a ? "-": "") + o, 0, 0),
                            "-webkit-transition-duration": "350ms"
                        }),
                        setTimeout(function() {
                                setTimeout(function() {
                                        e(),
                                        i && i()
                                    },
                                    0)
                            },
                            400)
                },
                0)
    }
    function C() {
        return /Android.*baidubrowser/.test(navigator.userAgent)
    }
    function I(e) {
        return e ? ["#page", "2", "" == e.url ? "emptyurl:" + e.nid: encodeURIComponent(e.url), encodeURIComponent(e.title), encodeURIComponent(e.site || "-"), e.ts, e.nid].join("/") : ""
    }
    function A(e) {
        return e = e.replace(/<([ac-z]|\w{2,})/gi, "&lt;$1"),
            e.replace(/([ac-z]|\w{2,})>/gi, "$1&gt;")
    }
    function N(e, t, n, a) {
        var i = Math.abs(e - n),
            o = Math.abs(t - a),
            r = Math.sqrt(Math.pow(i, 2) + Math.pow(o, 2)),
            s = o / r,
            c = Math.acos(s),
            d = 180 / (Math.PI / c);
        return d
    }
    function R(e) {
        return (document.location.search.match(new RegExp("(?:^\\?|&)" + e + "=(.*?)(?=&|$)")) || ["", null])[1] || (document.location.pathname.match(new RegExp(e + "=(.*?)(?=\\/)")) || ["", null])[1]
    }
    function D() {
        e.os.ios && window.devicePixelRatio >= 2 && e.browser.version >= 5 && e("head").append(e('<link rel="apple-touch-startup-image" href="http://ns13.bdstatic.com/static/news/webapp/img/startup_640_920.jpg" />'))
    }
    function P(t, n, a, i) {
        var o = !1,
            r = "http://127.0.0.1:6259/",
            s = r + t + "?" + n;
        e.ajax({
            url: s,
            dataType: "jsonp",
            success: function(e) {
                o = !0,
                    0 == e.error ? "function" == typeof a && a(e) : "function" == typeof i && i()
            },
            error: function() {
                o || (o = !0, "function" == typeof i && i())
            }
        });
        var c = setTimeout(function() {
                o || (o = !0, "function" == typeof i && i(), c && (clearTimeout(c), c = null))
            },
            2e3)
    }
    function E(e, t) {
        P("getpackageinfo", "packagename=com.baidu.news", e, t)
    }
    function L(e, t) {
        P("sendintent", "intent=" + encodeURIComponent(e), null,
            function() {
                Z() && (t = tt.conf.getWXAppStoreURL()),
                t && (location.href = t)
            })
    }
    function B(t, n, a, i) {
        e(t).off("click"),
            e(t).on("click",
                function() {
                    var o = e(t),
                        r = "#Intent;launchFlags=0x10000000;component=com.baidu.gamebox/.SplashActivity;end",
                        s = n || o.data("act"),
                        c = a || o.data("app"),
                        d = i || o.data("url");
                    return c ? (L(r, c), void(s && _ss({
                        act: s
                    }))) : void window.open(d)
                })
    }
    function O(t, n, a, i) {
        e(t).off("click"),
            e(t).on("click",
                function() {
                    var o = e(t),
                        r = n || o.data("act"),
                        s = a || o.data("app"),
                        c = i || o.data("url"),
                        d = "#Intent;action=com.baidu.news.detail;launchFlags=0x10000000;component=com.baidu.news/.ui.NewsDetailActivity;S.topic=搜索;S.news_url=" + encodeURIComponent(c) + ";i.news_from=15;S.nid=1;end";
                    return s ? (L(d, s), void(r && _ss({
                        act: r
                    }))) : void window.open(c)
                })
    }
    function $(e) {
        var t = "#Intent;action=android.intent.action.MAIN;launchFlags=0x10000000;component=com.baidu.news/.ui.IndexActivity";
        P("sendintent", "intent=" + encodeURIComponent(t), null,
            function() {
                e && (location.href = e)
            })
    }
    function q(t, n, a) {
        var t = e(t);
        src = t.data("href"),
            act = t.data("act"),
            o({
                act: act
            }),
        Z() && (src = tt.conf.getWXAppStoreURL()),
            a ? window.open(src) : setTimeout(function() {
                    location.href = src
                },
                300),
        n && n.stopPropagation()
    }
    function U() {
        var e, t = l("BAIDULOC"),
            n = {};
        return t ? (e = t.split("_"), 5 == e.length ? (n.mcx = e[0], n.mcy = e[1], n.citycode = e[3], n.ts = e[4], n) : null) : null
    }
    function G() {
        return "undefined" != typeof window.showType && "iphone" == window.showType ? !0 : !1
    }
    function M() {
        return /MQQBrowser/i.test(navigator.userAgent)
    }
    function z() {
        return /UCBrowser/i.test(navigator.userAgent)
    }
    function F() {
        return /baidubrowser/i.test(navigator.userAgent)
    }
    function H() {
        return /baiduboxapp/i.test(navigator.userAgent)
    }
    function Z() {
        return /micromessenger/i.test(navigator.userAgent)
    }
    function W(t) {
        e("#wrapper").append(t)
    }
    function j(t) {
        e("head").append('<style type="text/css">' + t + "</style>")
    }
    function J(e) {
        var t = this;
        if (void 0 != t._imgBase64) return void e(t._imgBase64);
        var n = new Image;
        n.onerror = function() {
            t._imgBase64 = !1,
                e(t._imgBase64)
        },
            n.onload = function() {
                t._imgBase64 = 1 == n.width,
                    e(t._imgBase64)
            },
            n.src = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAACGFjVEwAAAABAAAAAcMq2TYAAAANSURBVAiZY2BgYPgPAAEEAQB9ssjfAAAAGmZjVEwAAAAAAAAAAQAAAAEAAAAAAAAAAAD6A+gBAbNU+2sAAAARZmRBVAAAAAEImWNgYGBgAAAABQAB6MzFdgAAAABJRU5ErkJggg=="
    }
    function Q() {}
    function Y(e, t) {
        if ("undefined" != typeof e.naturalWidth) t({
            w: e.naturalWidth,
            h: e.naturalHeight,
            t: !0
        });
        else {
            var n = new Image;
            n.src = e.src,
                n.onload = function() {
                    t({
                        w: n.width,
                        h: n.height,
                        t: !1
                    })
                }
        }
    }
    function V(e, t) {
        return t ? e.replace(/([A-Z])/g,
            function(e, t) {
                return "-" + t.toLowerCase()
            }) : e.replace(/([a-z])-([a-z])/g,
            function(e, t, n) {
                return t + n.toUpperCase()
            }).replace(/^-/, "")
    }
    function X(e, t) {
        var n = window._testEl = window._testEl || document.createElement("div");
        e = V(e, !1);
        var a = void 0 != n.style[e];
        return null == t ? a ? e: null: (a && (n.style[e] = t), null != n.style[e])
    }
    function K() {
        if (Z() && G) var t = e("body"),
            n = e('<iframe src="http://nsclick.baidu.com/v.gif?pid=107&wise=1&from=topic&fr=&soe=2&m_ni=0&act=hackweixin" style="display:none;"></iframe>').on("load",
                function() {
                    setTimeout(function() {
                            n.off("load").remove()
                        },
                        0)
                }).appendTo(t)
    }
    function et() {
        var e = document.referrer;
        return e && /wappass.baidu.com\/passport/i.test(e) ? !0 : !1
    }
    window.webappandroid = window.webappandroid || {};
    var tt = window.webappandroid;
    helper = tt.helper = tt.helper || {};
    var nt = function(e, t, n) {
        var a = document.createElement("script");
        a.onload = a.onreadystatechange = function() {
            var e = a.readyState;
            if ("undefined" == typeof e || "loaded" === e || "complete" === e) try {
                n ? t && t.apply(n) : t && t()
            } finally {
                a.onload = a.onreadystatechange = null,
                    a = null
            }
        },
            a.type = "text/javascript",
            a.charset = "utf-8",
            a.src = e,
            (document.head || document.documentElement).appendChild(a)
    };
    e.extend(helper, {
        getFormatedDate: n,
        storageSet: m,
        storageGet: w,
        storageRemove: v,
        storageTest: b,
        cookieSet: p,
        cookieGet: l,
        cookieRemove: h,
        cuidGet: f,
        localStorageSet: y,
        localStorageGet: x,
        localStorageRemove: _,
        localStorageTest: k,
        slideAnimate: T,
        checkSoe: C,
        getCarouselImageUrl: I,
        filterHTMLTag: A,
        getAngle: N,
        queryParam: R,
        fixStartupImage: D,
        invokeApp: L,
        setupInvokeGame: B,
        setupInvokeApp: O,
        checkNewsClient: E,
        openNewsClient: $,
        clickHandler: q,
        sendSMTStat: s,
        sendNSClickStat: o,
        sendFAStat: r,
        getLocInfoFromCookie: U,
        isIOS: G,
        isUC: z,
        isQQ: M,
        isBD: F,
        isBaiduBox: H,
        isWeiXin: Z,
        addHTML: W,
        addCSS: j,
        getCuid: g,
        moniter: Q,
        getImgNaturalDimensions: Y,
        isCanImgBase64: J,
        domCssConvert: V,
        probableTestCss: X,
        hackWeixinForIOS: K,
        isFromPassport: et,
        loadScript: nt
    }),
        window._ch = tt.helper.clickHandler,
        window._ss = tt.helper.sendNSClickStat,
        window._sf = tt.helper.sendFAStat,
        window._smt = tt.helper.sendSMTStat
}) (Zepto);

(function(t) {
    var e = function(e, n) {
        this.element = t(e),
            this.params = t.extend({},
                this.getDefaults(), n || {}),
            this.selector = this.params._itemSelector,
            this._listSelector = this.params._listSelector,
            this._itemSelector = this.params._itemSelector,
            this._iconSelector = this.params._iconSelector,
            this._itemContainer = this.element.find(this._listSelector),
            this._iconContainer = this.element.find(this._iconSelector),
            this._startX = 0,
            this._startY = 0,
            this._lastX = 0,
            this._isLoop = this.params.isLoop,
            this.isAutoPlay = this.params.isAutoPlay;
        var i = this.params.preWidth;
        this._listW = this.element.width() || (100 > i ? t("body").width() - 10 : i),
            this._curIndex = this.params.startIndex,
            this._items = this._itemContainer.children(this._itemSelector),
            this._len = Math.ceil(this._items.length / this.params.preCount),
            this.init("slider")
    };
    e.DEFAULTS = {
        _listSelector: ".g-list",
        _itemSelector: ".g-item",
        _iconSelector: ".g-icons",
        startIndex: 0,
        iconCurClass: "cur",
        isLoop: !0,
        isAutoPlay: !0,
        isLazyLoad: !0,
        preCount: 2,
        theme: "",
        preWidth: 310,
        onItemClick: function() {},
        onAfterChange: function() {}
    },
        e.prototype.move = function(t) {
            var e = this,
                n = this._iconContainer,
                i = e.params;
            setTimeout(function() {
                    n.find("i").eq(t).addClass(i.iconCurClass).siblings("i").removeClass(i.iconCurClass),
                        i.onAfterChange(t)
                },
                200)
        },
        e.prototype.getDefaults = function() {
            return e.DEFAULTS
        },
        e.prototype.init = function() {
            var e, n = this,
                i = n._itemContainer,
                r = n.selector,
                a = this._iconContainer,
                o = e = n._len,
                s = n.params.isLazyLoad,
                c = n.params.isLoop;
            for (n.domInit(), s && (c ? n.loopLazyLoad(n._curIndex) : n.lazyLoad(n._curIndex)); e--;) a.append("<i></i>");
            a.find("i").eq(n.params.startIndex).addClass(n.params.iconCurClass),
                o > 1 ? i.on("touchstart", r, t.proxy(n.touchstart, n)).on("touchmove", r, t.proxy(n.touchmove, n)).on("touchend", r, t.proxy(n.touchend, n)).on("click", r, t.proxy(n.itemClick, n)) : i.on("click", r, t.proxy(n.itemClick, n)),
            n.params.isAutoPlay && (setInterval(function() {
                    n.isAutoPlay && n.autoPlay()
                },
                5e3), n._itemContainer.on("webkitTransitionEnd", t.proxy(n.resetPos, n), !1))
        },
        e.prototype.domInit = function() {
            var t = this;
            if (t.element.addClass(t.params.theme), t._items.each(function(e) {
                    t._items.eq(e).css({
                        width: 1 / t.params.preCount * 100 + "%"
                    })
                }), t.params.isLoop) {
                for (var e = 0; e < t.params.preCount; e++) t._itemContainer.append(t._items.eq(e).clone());
                for (var n = 1; n <= t.params.preCount; n++) t._itemContainer.prepend(t._items.eq(t._items.length - n).clone());
                t._itemContainer.css({
                    "-webkit-transform": "translateX(" + -t._listW * (t._curIndex + 1) + "px)"
                })
            }
        },
        e.prototype.touchstart = function(t) {
            if (t.touches) {
                var e = this;
                e._startX = e._lastX = t.touches[0].clientX,
                    e._startY = e._lastY = t.touches[0].clientY,
                    e.curX = -e._listW * (e.params.isLoop ? e._curIndex + 1 : e._curIndex),
                    e.isAutoPlay = !1,
                    e.resetPos(e._curIndex)
            }
        },
        e.prototype.touchmove = function(t) {
            if (t.touches) {
                var e = this,
                    n = t.changedTouches[0].clientX,
                    i = t.changedTouches[0].clientY,
                    r = n - e._lastX,
                    a = (i - e._lastY, webappandroid.helper.getAngle(e._startX, e._startY, n, i));
                50 > a || isNaN(a) || (e._lastX = n, e._lastY = i, e.curX += r, e._itemContainer.css({
                    "-webkit-transform": "translateX(" + e.curX + "px)"
                }), t.preventDefault())
            }
        },
        e.prototype.touchend = function(t) {
            if (t.touches) {
                var e = this,
                    n = e._curIndex,
                    i = e.params.isLoop,
                    r = e._len,
                    a = t.changedTouches[0].clientX,
                    o = t.changedTouches[0].clientY,
                    s = webappandroid.helper.getAngle(e._startX, e._startY, e._lastX, e._lastY),
                    c = a - e._startX,
                    u = o - e._startY,
                    l = i ? -1 : 0,
                    d = i ? r: r - 1;
                if ((0 !== c || 0 !== u) && !(50 > s || isNaN(s))) {
                    c > 50 && n !== l && (n -= 1, e.move(n)),
                    -50 > c && n !== d && (n += 1, e.move(n));
                    var c = -e._listW * (i ? n + 1 : n);
                    e.timeout = setTimeout(function() {
                            e._itemContainer.css({
                                "-webkit-transition": "-webkit-transform 350ms ease-out",
                                "-webkit-transform": "translateX(" + c + "px)"
                            }),
                                clearTimeout(e.timeout)
                        },
                        0),
                    e.params.isLazyLoad && (i ? e.loopLazyLoad(n) : e.lazyLoad(n)),
                    !i || n != d && n != l || (n = n == d ? r - d: n == l ? r + l: n, e.move(n)),
                        e._curIndex = n,
                        t.preventDefault()
                }
            }
        },
        e.prototype.autoPlay = function() {
            var t = this,
                e = t._curIndex,
                n = t.params.isLoop,
                i = t._len,
                r = n ? -1 : 0,
                a = n ? i: i - 1;
            e += 1,
                t.move(e);
            var o = -t._listW * (n ? e + 1 : e);
            t.timeout = setTimeout(function() {
                    t._itemContainer.css({
                        "-webkit-transition": "-webkit-transform 350ms ease-out",
                        "-webkit-transform": "translateX(" + o + "px)"
                    }),
                        clearTimeout(t.timeout)
                },
                0),
            t.params.isLazyLoad && (n ? t.loopLazyLoad(e) : t.lazyLoad(e)),
            !n || e != a && e != r || (e = e == a ? i - a: e == r ? i + r: e, t.move(e)),
                t._curIndex = e
        },
        e.prototype.resetPos = function() {
            var t = this,
                e = t._curIndex;
            t.loopLazyLoad(e),
                t._itemContainer.css({
                    "-webkit-transform": "translateX(" + -t._listW * (e + 1) + "px)",
                    "-webkit-transition": "none"
                }),
            t.isAutoPlay || (t._timer && clearTimeout(t._timer), t._timer = setTimeout(function() {
                    t.isAutoPlay = !0
                },
                5e3))
        },
        e.prototype.loopLazyLoad = function(t) {
            for (var e = this,
                     n = 0,
                     i = e.params.preCount; i > n; n++) e.load(e._itemContainer.find(e._itemSelector).eq((t + 1) * i + n).find("img.lazy-load"))
        },
        e.prototype.lazyLoad = function(t) {
            for (var e = this,
                     n = 0,
                     i = e.params.preCount; i > n; n++) e.load(e._items.eq(t * i + n).find("img.lazy-load"))
        },
        e.prototype.load = function(e) {
            var n, i = t(e);
            n = t("<img />").on("load",
                function() {
                    i.replaceWith(n),
                        i.removeAttr("data-url"),
                        n.off("load")
                }).on("error",
                function() {
                    n.off("error").remove()
                }).attr("src", i.attr("data-url"))
        },
        e.prototype.itemClick = function(e) {
            var n = this;
            n.params.onItemClick(t(e.target).closest(n.selector)),
                e.preventDefault()
        },
        e.prototype.reset = function() {
            var t, e = this;
            e._listW = e._itemContainer.width(),
                t = -e._listW * (e.params.isLoop ? e._curIndex + 1 : e._curIndex),
                e._itemContainer.css({
                    "-webkit-transform": "translateX(" + t + "px)"
                })
        },
        e.prototype.destroy = function() {
            clearTimeout(this.timeout),
                this.hide().$element.off("." + this.type).removeData("bs." + this.type)
        };
    var n = t.fn.topicSlider;
    t.fn.topicSlider = function(n) {
        return this.each(function() {
            var i = t(this),
                r = i.data("bs.slider"),
                a = "object" == typeof n && n;
            r || i.data("bs.slider", r = new e(this, a)),
            "string" == typeof n && r[n]()
        })
    },
        t.fn.topicSlider.Constructor = e,
        t.fn.topicSlider.noConflict = function() {
            return t.fn.topicSlider = n,
                this
        }
})(Zepto);