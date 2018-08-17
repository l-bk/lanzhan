/*
	版权所有 2009-2015 荆门泽优软件有限公司 保留所有权利。
	邮箱:1085617561@qq.com
	描述:Word图片上传控件
	此文件实现上传后自动关闭上传窗口的功能
*/
KindEditor.lang({
	wordpaster: '粘贴图片或自动上传Word图片'
	,netpaster:'自动上传远程服务器图片'
});

//系统错误
var WordPasterError = {
	"0": "连接服务器错误"
	, "1": "发送数据错误"
	, "2": "接收数据错误"
	, "3": "本地文件不存在"
	, "4": "不能读取本地文件"
	, "5": "公司未授权"
	, "6": "未设置IP"
	, "7": "域名未授权"
	, "8": "设置回调函数失败"
	, "9": "文件大小超出限制"
	, "10": "下载网络文件失败"
};
	
//配置信息
var WordPasterConfig = {
	"EncodeType"		    : "GB2312"
	, "Company"			    : "荆门泽优软件有限公司"
	, "Version"			    : "1,4,101,31253"
	, "License"			    : ""
	, "Debug"			    : false//调试模式
	, "LogFile"			    : "C:\\WordPaster.log"//日志文件路径
	, "PasteWordType"	    : ""	//粘贴WORD的图片格式。JPG/PNG/GIF/BMP,推荐使用JPG，防止BMP图片太大。
	, "PasteImageType"	    : ""	//粘贴文件，剪帖板的图片格式，为空表示本地图片格式。JPG/PNG/GIF/BMP
	, "JpgQuality"		    : 100	//JPG质量。0~100
	, "ThumbWidth"		    : 0		//缩略图宽度。0表示不使用缩略图
	, "ThumbHeight"		    : 0		//缩略图高度。0表示不使用缩略图
	, "Debug"			    : false	//是否打开日志功能
	, "LogFile"			    : "c:\\log.txt"//日志文件路径。
	, "AppPath"			    : ""
	, "IcoError"            : "http://www.ncmem.com/products/word-imagepaster/kindeditor4x/WordPaster/error.png"
    , "IcoUploader"         : "http://www.ncmem.com/products/word-imagepaster/kindeditor4x/WordPaster/upload.gif"
	, "PostUrl"			    : "http://www.ncmem.com/products/word-imagepaster/fckeditor2461/asp.net/upload.aspx"
    //x86
	, "ClsidImagePaster"    : "DC4A6931-3570-44b4-842D-C13EE637BBC1"
	, "ClsidUploader"	    : "26CFEDE7-789D-4077-B49D-C7F1D7150B93"
	, "ClsidParser"		    : "1832A49E-09D5-470E-AA4B-BE8F28034218"
	, "CabPath"			    : "http://www.ncmem.com/download/WordPaster/WordPaster.cab"
	//x64
	, "ClsidImagePaster64"	: "04496806-5926-4682-89E6-95ADAA3DF6CE"
	, "ClsidUploader64"		: "E98D2D8B-61F0-44c4-87C6-830D309A874B"
	, "ClsidParser64"		: "9446FA60-55DB-44e9-8FA3-CA485441526A"
	, "CabPath64"			: "http://www.ncmem.com/download/WordPaster/WordPaster64.cab"
	//Firefox
	, "MimeType"	        : "application/npWordPaster"
	, "XpiPath"		        : "http://www.ncmem.com/download/WordPaster/WordPaster.xpi"
	//Chrome
	, "CrxName"		        : "npWordPaster"
	, "MimeTypeChr"	        : "application/npWordPaster"
	, "CrxPath"		        : "http://www.ncmem.com/download/WordPaster/WordPaster.crx"
	, "ExePath"		        : "http://www.ncmem.com/download/WordPaster/WordPaster.exe"
};

var WordPasterActiveX = {
	"Uploader"		: "Xproer.ImageFileUploader"
	, "ImagePaster"	: "Xproer.ImagePaster"	//
	, "WordParser"	: "Xproer.WordParser"
	//x64
	, "Uploader64"		: "Xproer.ImageFileUploader64"
	, "ImagePaster64"	: "Xproer.ImagePaster64"
	, "WordParser64"	: "Xproer.WordParser64"
};

/*
	上传对象管理器
	关联HTML元素：
		文件上传列表：FilePostLister
		文件上传列表项模板：UploaderTemplate
		文件上传列表分隔线：FilePostLine
*/
function WordPasterManager(mgr)
{
	this.Manager = mgr;//粘贴管理器
	this.ActiveX = mgr.ActiveX;
	this.Config = mgr.Config;
	this.Fields = mgr.Fields;
	this.Browser = mgr.Browser;
	this.BrowserVer = mgr.BrowserVer;
	this.OpenDialog = mgr.OpenUploadFileDialog;
	this.CloseDialog = mgr.CloseUploadFileDialog;
	this.InsertHtml = mgr.InsertHtml;
	this.WordParser = mgr.ctlWordParser;
	this.WordParser.setObj(this);//
	
	this.EditorContent = ""; //编辑器内容。当图片上传完后需要更新此变量值
	this.Editor = null; //编辑器
	this.CurrentUploader = null; //当前上传项。
	this.filesPanel = mgr.filesPanel; //上传列表层
	this.fileItem = mgr.fileItem; //上传项信息层模板
	this.line = mgr.line; //上传列表分割线
	this.UploaderList = new Object(); //上传项列表
	//已上传图片列表
	//模型：LocalUrl:ServerUrl
	this.UploadCompleteImage = new Array();
	this.UploaderListCount = 0; //上传项总数
	this.UnUploaderIdList = new Array(); //未上传项ID列表
	this.UploaderPool = null;//IE上传对象缓存
	this.UploaderPoolFF = -1;//firefox上传对象缓存

	//photo regex
	this.ImgRegx = /<img width=\"\d+\" height=\"\d+\" alt=\"\S*\" src=\"file:\/\/\/(\S+)\"\ \/>/gi;
	this.ImgRegx2 = /<img src=\"file:\/\/\/(\S+)\" width=\d+ height=\d+>/gi;
	this.ShapeStyleRegexs = {
		wh: /(?:width: )(\S+)pt; (?:height: )(\S+)pt/i
	, w: /(?:width: )(\S+)pt;/i
	};
	this.StyleSrcRegx = /style=\"(.*)\"(?:.*)src=\"file:\/\/\/(\S+)\"/;
	this.imgSrcRegex = /(?:src=\"file:\/\/\/)(\S+)\"/;
	//<v:shape 样式提供给img标签使用
	this.styleRegx = /style=\"(.*?)\"/;
	this.ImgRegex = {
		word: /<v:imagedata o:title=\"\S*\" src=\"file:\/\/\/(\S+)\"><\/v:imagedata>/gi
		, word2: /src=\"file:\/\/\/(\S+)\"><\/v:imagedata>/gi
		, app: /<v:imagedata src=\"file:\/\/\/(\S+)\" o:href=\"(\S+)\"><\/v:imagedata>/gi
		, web: /<v:imagedata o:href=\"(\S+)\" src=\"file:\/\/\/(\S+)\"><\/v:imagedata>/gi
		, loc: /<v:imagedata src=\"file:\/\/\/(\S+)\" o:title=\"\S*\"><\/v:imagedata>/gi
		, wps: /<img width=\"\d+\" height=\"\d+\" alt=\"\S*\" src=\"file:\/\/\/(\S+)\" \/>/gi
		, wps2: /<img src=\"file:\/\/\/(\S+)\" width=\d+ height=\d+>/gi
		, net:/<img[^>]+\w+="[^"]+"[^>]*>/gi
	};

	//检查是否存在本地相同图片
	this.CompleteExist = function(localPath)
	{
		for (var i = 0, l = this.UploadCompleteImage.length ; i < l ; ++i)
		{
			if (this.UploadCompleteImage[i].localPath == localPath)
			{
				return true;
			}
		}
		return false;
	};

	//添加到已完成列表
	this.CompleteAdd = function(localPathStr, serverPathStr)
	{
		this.UploadCompleteImage.push({ localPath: localPathStr, serverPath: serverPathStr });
	};

	//清空已完成列表，关闭图片上传对话框时调用
	this.CompleteClear = function()
	{
		this.UploadCompleteImage.length = 0;
	};

	//获取已完成的图片地址
	this.CompleteGet = function(localPath)
	{
		for (var i = 0, l = this.UploadCompleteImage.length; i < l; ++i)
		{
			if (this.UploadCompleteImage[i].localPath == localPath)
			{
				return this.UploadCompleteImage[i].serverPath;
			}
		}
		return "";
	};
	
	//检查剪帖板中是否包含WORD图片
	this.HasWordImages = function(data)
	{
		for (regName in this.ImgRegex)
		{
			var imgs = data.match(this.ImgRegex[regName]);
			if (null != imgs)
			{
				return true;
			}
		}
		return false;
	};
	
	//粘贴Word
	this.PasteWord = function ()
	{
		this.EditorContent = this.WordParser.GetWord();
		//有图片
		if (this.WordParser.GetImages())
		{
			this.OpenDialog();
			this.PostFirst();
		} //没有图片
		else
		{
			this.InsertHtml(this.EditorContent);
		}
	};

	//粘贴Excel
	this.PasteExcel = function ()
	{
		this.EditorContent = this.WordParser.GetExcel();
		//有图片
		if (this.WordParser.GetImages())
		{
			this.OpenDialog();
			this.PostFirst();
		} //没有图片
		else
		{
			this.InsertHtml(this.EditorContent);
		}
	};

	//匹配形状
	this.MatchShape = function(data)
	{
		this.EditorContent = data;
		this.WordParser.Parse(this.EditorContent);
		//wps
		this.MatchImgs(this.EditorContent);
		this.MatchImgs2(this.EditorContent);
		this.OpenDialog();
		this.PostFirst();
	};

	//配置img标签
	//<img width="553" height="217" alt="" src="file:///C:/Users/ADMINI~1/AppData/Local/Temp/ksohtml/wps_clip_image-12942.png" />
	this.MatchImgs = function(html)
	{
		var imgStyle = /width=\"(\d+)\" height=\"(\d+)\" alt=\"\S*\" src=\"file:\/\/\/(\S+)\"/;
		var imgs = html.match(this.ImgRegx);
		if (null != imgs)
		{
			for (var i = 0, l = imgs.length; i < l; ++i)
			{
				var style = imgStyle.exec(imgs[i]);
				if (style)
				{
					this.AddFile(RegExp.$3, imgs[i], RegExp.$1, RegExp.$1);
				}
			}
		}
	};

	//WPS标签
	//<IMG src="file:///C:/DOCUME~1/ADMINI~1/LOCALS~1/Temp/ksohtml/wps_clip_image-25006.png" width=301 height=849>
	this.MatchImgs2 = function(html)
	{
		var imgStyle = /src=\"file:\/\/\/(\S+)\" width=(\d+) height=(\d+)/;
		var imgs = html.match(this.ImgRegx2);
		if (null != imgs)
		{
			for (var i = 0, l = imgs.length; i < l; ++i)
			{
				var style = imgStyle.exec(imgs[i]);
				if (style)
				{
					this.AddFile(RegExp.$1, imgs[i], RegExp.$2, RegExp.$3);
				}
			}
		}
	};

	//判断是否远程文件
	this.IsSvrImg = function (src)
	{
		var rex = /(http|https):\/\//i;
		var domainLoc = this.Config["PostUrl"].replace(rex, "");
		var index = domainLoc.indexOf("/");
		domainLoc = domainLoc.substr(0, index);
		src = src.replace(rex,"");
		index = src.indexOf("/");
		var domainSvr = src.substr(0, index);
		return domainLoc != domainSvr;
	};

	//匹配网络图片
	this.MatchNetImg = function (data)
	{
		this.EditorContent = data;
		var hasSvrImg = false;
		var regSrc = /src="([^"]+)"/;
		var mc = data.match(this.ImgRegex.net);
		if (null != mc)
		{
			for (var i = 0, l = mc.length; i < l; ++i)
			{
				regSrc.exec(mc[i]);
				var url = RegExp.$1;
				if (this.IsSvrImg(url))
				{
					hasSvrImg = true;
					this.AddNetFile(mc[i], url);
				}
			}
			if (hasSvrImg)
			{
				this.OpenDialog();
				this.PostFirst();
			}
		}
	};

	/*
		解析形状项
		参数：
			shapeTag 匹配成功的HTML字符串
			shapeStyle 形状样式字符串。"WIDTH: 600pt; HEIGHT: 450pt" id=_x0000_i1025 alt="" type="#_x0000_t75"
			imageSrc 图片本地地址。
	*/
	this.MatchShapeItem = function(shapeTag, shapeStyle, imageSrc)
	{
		var width = 0;
		var height = 0;
		var style = this.ShapeStyleRegexs.wh.exec(shapeStyle);
		//匹配高度和宽度
		if (style)
		{
			width = Math.round(RegExp.$1 * 1.3333);
			height = Math.round(RegExp.$2 * 1.3333);
			height = isNaN(height) ? 0 : height;
		} //只匹配宽度，由控件自动计算比例
		else
		{
			style = this.ShapeStyleRegexs.w.exec(shapeStyle);
			if (style)
			{
				width = Math.round(RegExp.$1 * 1.3333);
			}
		}
		this.AddFile(imageSrc, shapeTag, width, height);
	};
	
	//只匹配图片
	this.MatchImage = function()
	{
		var urlReg = /(?:src=\"file:\/\/\/)(\S+)(?:\")/;
		var data = this.Editor.GetClipboardHTML();
		this.EditorContent = data; //保存到图片上传管理器中

		for (regName in this.ImgRegex)
		{
			var imgs = data.match(this.ImgRegex[regName]);
			if (null != imgs)
			{
				var len = imgs.length;
				var url = "";

				for (var i = 0; i < len; i++)
				{
					url = urlReg.exec(imgs[i]);
					if (url)
					{
						this.AddFile(RegExp.$1, imgs[i]);
					}
				}
			}
		}
		this.Dialog.show();
		this.PostFirst();
	};
	
	//开始上传编辑器中所有的WORD图片
	this.UploadImages = function()
	{
		var html = this.Editor.GetClipboardHTML();
		//测试代码。
		this.MatchShape(html);
	};
	//传送当前队列的第一个文件
	this.PostFirst = function()
	{
		if (this.UnUploaderIdList.length > 0)
		{
			var index = this.UnUploaderIdList.shift();
			this.UploaderList[index].Post();
		}
	};
	
	/*
	验证文件名是否存在
	参数:
		fileName:文件名称
	*/
	this.Exist = function(fileName)
	{
		for (var a in this.UploaderList)
		{
			if (this.UploaderList[a].LocalFile == fileName)
			{
				return true;
			}
		}
		return false;
	};

	/*
	根据ID删除上传任务
	参数:
	[0] FileID
	*/
	this.Delete = function()
	{
		var obj = this.UploaderList[arguments[0]];
		if (null == obj) return;

		var tbID = "item" + obj.FileID;
		var item = document.getElementById(tbID);
		if (item) document.removeChild(item); //删除
	};

	/*
		添加到上传列表
		参数
			index 上传对象唯一标识
			uploaderObj 上传对象
	*/
	this.AppendToUploaderList = function(index, uploaderObj)
	{
		this.UploaderList[index] = uploaderObj;
		++this.UploaderListCount;
	};

	/*
		添加到上传列表层
		参数
			fid 文件ID
			div 上传信息层对象
			obj 上传对象
	*/
	this.AppendToListDiv = function(fid, div, obj)
	{
		var line = this.line.clone(true); //分隔线
		line.attr("id", "FilePostLine" + fid)
            .css("display", "block");
		obj.Separator = line;

		this.filesPanel.append(div);
		this.filesPanel.append(line);
	};

	/*
		更新编辑器中的图片标签
		参数:
			0 原始图片标签
			1 新的图片地址
	*/
	this.ReplaceEditorImgTag = function(srcOld,srcNew,w,h)
	{
		var img = "<img src=\"";
		img += srcNew;
		img += "\"";

		if (w == "0" || h == "0")
		{ }
		else
		{
		    img += ' width="' + w + '" height="' + h + '"';
		}

		//有style属性
		var style = this.styleRegx.exec(srcOld);
		if (style)
		{
			img += " style=\"" + RegExp.$1 + "\" ";
		}
		img += "/>";
		
		this.EditorContent = this.EditorContent.replace(srcOld, img);
		//this.Editor.SetData(this.EditorContent);
	};

	/*
		更新编辑器内容。
		在所有图片上传完后调用。
		在上传图片出现错误时调用。
	*/
	this.UpdateContent = function ()
	{
		this.InsertHtml(this.EditorContent);
	};

	//更新网络图片
	this.UpdateContentNetImg = function ()
	{ 
		this.Editor.html(this.EditorContent);
	};
	
	// This function will be called from the PasteFromWord dialog (fck_paste.html)
	// Input: oNode a DOM node that contains the raw paste from the clipboard
	// bIgnoreFont, bRemoveStyles booleans according to the values set in the dialog
	// Output: the cleaned string
	this.CleanWord = function(data, bIgnoreFont, bRemoveStyles)
	{
		var html = data;

		html = html.replace(/<o:p>\s*<\/o:p>/g, '');
		html = html.replace(/<o:p>[\s\S]*?<\/o:p>/g, '&nbsp;');

		// Remove mso-xxx styles.
		html = html.replace(/\s*mso-[^:]+:[^;"]+;?/gi, '');

		// Remove margin styles.
		html = html.replace(/\s*MARGIN: 0cm 0cm 0pt\s*;/gi, '');
		html = html.replace(/\s*MARGIN: 0cm 0cm 0pt\s*"/gi, "\"");

		html = html.replace(/\s*TEXT-INDENT: 0cm\s*;/gi, '');
		html = html.replace(/\s*TEXT-INDENT: 0cm\s*"/gi, "\"");

		html = html.replace(/\s*TEXT-ALIGN: [^\s;]+;?"/gi, "\"");

		html = html.replace(/\s*PAGE-BREAK-BEFORE: [^\s;]+;?"/gi, "\"");

		html = html.replace(/\s*FONT-VARIANT: [^\s;]+;?"/gi, "\"");

		html = html.replace(/\s*tab-stops:[^;"]*;?/gi, '');
		html = html.replace(/\s*tab-stops:[^"]*/gi, '');

		// Remove FONT face attributes.
		if (bIgnoreFont)
		{
			html = html.replace(/\s*face="[^"]*"/gi, '');
			html = html.replace(/\s*face=[^ >]*/gi, '');

			html = html.replace(/\s*FONT-FAMILY:[^;"]*;?/gi, '');
		}

		// Remove Class attributes
		html = html.replace(/<(\w[^>]*) class=([^ |>]*)([^>]*)/gi, "<$1$3");

		// Remove styles.
		if (bRemoveStyles)
			html = html.replace(/<(\w[^>]*) style="([^\"]*)"([^>]*)/gi, "<$1$3");

		// Remove style, meta and link tags
		html = html.replace(/<STYLE[^>]*>[\s\S]*?<\/STYLE[^>]*>/gi, '');
		html = html.replace(/<(?:META|LINK)[^>]*>\s*/gi, '');

		// Remove empty styles.
		html = html.replace(/\s*style="\s*"/gi, '');

		html = html.replace(/<SPAN\s*[^>]*>\s*&nbsp;\s*<\/SPAN>/gi, '&nbsp;');

		html = html.replace(/<SPAN\s*[^>]*><\/SPAN>/gi, '');

		// Remove Lang attributes
		html = html.replace(/<(\w[^>]*) lang=([^ |>]*)([^>]*)/gi, "<$1$3");

		html = html.replace(/<SPAN\s*>([\s\S]*?)<\/SPAN>/gi, '$1');

		html = html.replace(/<FONT\s*>([\s\S]*?)<\/FONT>/gi, '$1');

		// Remove XML elements and declarations
		html = html.replace(/<\\?\?xml[^>]*>/gi, '');

		// Remove w: tags with contents.
		html = html.replace(/<w:[^>]*>[\s\S]*?<\/w:[^>]*>/gi, '');

		// Remove Tags with XML namespace declarations: <o:p><\/o:p>
		html = html.replace(/<\/?\w+:[^>]*>/gi, '');

		// Remove comments [SF BUG-1481861].
		html = html.replace(/<\!--[\s\S]*?-->/g, '');

		html = html.replace(/<(U|I|STRIKE)>&nbsp;<\/\1>/g, '&nbsp;');

		html = html.replace(/<H\d>\s*<\/H\d>/gi, '');

		// Remove "display:none" tags.fix bug:可能会替换img标签
		//html = html.replace(/<(\w+)[^>]*\sstyle="[^"]*DISPLAY\s?:\s?none[\s\S]*?<\/\1>/ig, '');

		// Remove language tags
		html = html.replace(/<(\w[^>]*) language=([^ |>]*)([^>]*)/gi, "<$1$3");

		// Remove onmouseover and onmouseout events (from MS Word comments effect)
		html = html.replace(/<(\w[^>]*) onmouseover="([^\"]*)"([^>]*)/gi, "<$1$3");
		html = html.replace(/<(\w[^>]*) onmouseout="([^\"]*)"([^>]*)/gi, "<$1$3");

		if (false)
		{
			// The original <Hn> tag send from Word is something like this: <Hn style="margin-top:0px;margin-bottom:0px">
			html = html.replace(/<H(\d)([^>]*)>/gi, '<h$1>');

			// Word likes to insert extra <font> tags, when using MSIE. (Wierd).
			html = html.replace(/<(H\d)><FONT[^>]*>([\s\S]*?)<\/FONT><\/\1>/gi, '<$1>$2<\/$1>');
			html = html.replace(/<(H\d)><EM>([\s\S]*?)<\/EM><\/\1>/gi, '<$1>$2<\/$1>');
		}
		else
		{
			html = html.replace(/<H1([^>]*)>/gi, '<div$1><b><font size="6">');
			html = html.replace(/<H2([^>]*)>/gi, '<div$1><b><font size="5">');
			html = html.replace(/<H3([^>]*)>/gi, '<div$1><b><font size="4">');
			html = html.replace(/<H4([^>]*)>/gi, '<div$1><b><font size="3">');
			html = html.replace(/<H5([^>]*)>/gi, '<div$1><b><font size="2">');
			html = html.replace(/<H6([^>]*)>/gi, '<div$1><b><font size="1">');

			html = html.replace(/<\/H\d>/gi, '<\/font><\/b><\/div>');

			// Transform <P> to <DIV>
			var re = new RegExp('(<P)([^>]*>[\\s\\S]*?)(<\/P>)', 'gi'); // Different because of a IE 5.0 error
			html = html.replace(re, '<div$2<\/div>');

			// Remove empty tags (three times, just to be sure).
			// This also removes any empty anchor
			html = html.replace(/<([^\s>]+)(\s[^>]*)?>\s*<\/\1>/g, '');
			html = html.replace(/<([^\s>]+)(\s[^>]*)?>\s*<\/\1>/g, '');
			html = html.replace(/<([^\s>]+)(\s[^>]*)?>\s*<\/\1>/g, '');
		}
		//<span style="font-family: 宋体; font-size: 12pt"><img alt="" src="/upload/2011-12-03_1119250937.jpg" /></span>
		html = html.replace(/<span[^>]*>(<img.+\/>)<\/span>/gi, '$1');
		//<div style="margin-top: 0pt; margin-bottom: 0pt"><img alt="" src="/upload/2012-01-08_2246370625.jpg" /></div>
		html = html.replace(/<div[^>]*>(<img.+\/>)<\/div>/gi, '$1<br/>');
		//<a name="OLE_LINK2"><span><img/><img/></a>
		html = html.replace(/<a[^>]*>((<span>)?(<img.+\/>).*)<\/a>/gi, '$3');
		return html;
	};
}

/*
	添加图片
	参数：
		obj
		alt
		width
		height
		src
		img
*/
function WordParser_AddImage(obj,alt,width,height,src,img)
{ 
	obj.AddFile(src, img, width, height);
}

/*
	添加一个文件到上传队列
	参数:
		[0] 文件名称
		[1] imgTag图片标记
		width 图片宽度
		heigth 图片高度
	返回值:
	文件上传对象
*/
WordPasterManager.prototype.AddFile = function(imgPath,imgHtml,width,height)
{
	//本地文件名称不存在
	//if (!this.Exist(arguments[0]))
	{
		var fileNameArray = imgPath.split("\\");
		var fileName = fileNameArray[fileNameArray.length - 1];
		var fid = this.UploaderListCount;
		this.UnUploaderIdList.push(fid); //添加到未上传列表
		
		var upFile = new FileUploader(fid, imgPath, this,width,height);
		var ui = this.fileItem.clone();
		ui.css("display", "block")
			.attr("id", "item" + fid);

		var objFileName = ui.find('div[name="fname"]');
		var divMsg		= ui.find('div[name="msg"]');
		var aBtn		= ui.find('a[name="btn"]');
		var divPercent	= ui.find('div[name="percent"]');
		var divProcess	= ui.find('div[name="process"]');

		objFileName.text(fileName)
			.attr("title", arguments[0]);
		upFile.pProcess = divProcess;
		upFile.pMsg = divMsg;
		upFile.pMsg.text("");
		upFile.pButton = aBtn;
		aBtn.attr("fid", fid)
			.attr("domid", "item" + fid)
			.attr("lineid", "FilePostLine" + fid)
			.click(function ()
			{
			    switch ($(this).text())
			    {
			        case "暂停":
			        case "停止":
			            upFile.Stop();
			            break;
			        case "取消":
			            { upFile.Remove(); }
			            break;
			        case "续传":
			        case "重试":
			            upFile.Post();
			            break;
			    }
			});
		upFile.pPercent = divPercent;
		upFile.pPercent.text("0%");
		upFile.ImageTag = imgHtml; //图片标记
		upFile.InfDiv = ui;//上传信息DIV

		//添加到上传列表层
		this.AppendToListDiv(fid, ui,upFile);

		//添加到上传列表
		this.AppendToUploaderList(fid, upFile);
		upFile.Ready(); //准备
	}
}

/*
	添加本地文件
	参数:
		imgPath	本地图片文件路径
	返回值:
		文件上传对象
*/
WordPasterManager.prototype.AddLocalFile = function(imgPath)
{
	//本地文件名称不存在
	//if (!this.Exist(arguments[0]))
	{
		var fileNameArray = imgPath.split("\\");
		var fileName = fileNameArray[fileNameArray.length - 1];
		var fid = this.UploaderListCount;
		this.UnUploaderIdList.push(fid); //添加到未上传列表

		var upFile = new FileUploader(fid, imgPath, this, 0, 0);
		upFile.ATL.SetImageType(fileName.split(".")[1]);
		upFile.PostLocalFile = true; //标识是本地文件
		var ui = this.fileItem.clone(); //JQuery Object
		ui.css("display", "block")
			.attr("id", "item" + fid);
		var divLeft = ui.children().eq(0);
		var divRight = ui.children().eq(1);
		var objFileName = divLeft.children().eq(0);
		objFileName.text(fileName);
		objFileName.attr("title", arguments[0]);
		upFile.pProcess = divLeft.children().eq(1).children().eq(0);
		upFile.pMsg = divLeft.children().eq(2);
		upFile.pMsg.text("");
		upFile.pButton = divRight.children().eq(0);
		upFile.pButton.attr("fid", fid)
			.attr("domid", "item" + fid)
			.attr("lineid", "FilePostLine" + fid)
			.click(function ()
			{
			    switch ($(this).text())
			    {
			        case "暂停":
			        case "停止":
			            upFile.Stop();
			            break;
			        case "取消":
			            { upFile.Remove(); }
			            break;
			        case "续传":
			        case "重试":
			            upFile.Post();
			            break;
			    }
			});
		upFile.pPercent = divRight.children().eq(1);
		upFile.pPercent.text("0%");
		upFile.ImageTag = ""; //图片标记
		upFile.Manager = this;
		upFile.InfDiv = ui; //上传信息DIV

		//添加到上传列表层
		this.AppendToListDiv(fid, ui, upFile);

		//添加到上传列表
		this.AppendToUploaderList(fid, upFile);
		upFile.Ready(); //准备
	}
}

/*
	添加网络文件
	参数:
		tag 标签值
		imgUrl	网络图片地址
	返回值:
		文件上传对象
*/
WordPasterManager.prototype.AddNetFile = function (tag,imgUrl)
{
	//本地文件名称不存在
	//if (!this.Exist(arguments[0]))
	{
		var arrUrl = imgUrl.split("/");
		var fileName = arrUrl[arrUrl.length - 1];
		var fid = this.UploaderListCount;
		this.UnUploaderIdList.push(fid); //添加到未上传列表

		var upFile = new FileUploader(fid, imgUrl, this, 0, 0);
		upFile.ATL.SetImageType(fileName.split(".")[1]);
		upFile.PostNetFile = true; //标识是网络文件
		var ui = this.fileItem.clone(); //JQuery Object
		ui.css("display", "block")
			.attr("id", "item" + fid);

		var fname       = ui.find('div[name="fname"]');
		var divMsg		= ui.find('div[name="msg"]');
		var aBtn		= ui.find('a[name="btn"]');
		var divPercent	= ui.find('div[name="percent"]');
		var divProcess	= ui.find('div[name="process"]');

		fname.text(fileName)
			.attr("title", arguments[0]);
		upFile.pProcess = divProcess;
		upFile.pMsg = divMsg;
		upFile.pMsg.text("");
		upFile.pButton = aBtn
		upFile.pButton.attr("fid", fid)
			.attr("domid", "item" + fid)
			.attr("lineid", "FilePostLine" + fid)
			.click(function(){
				switch ($(this).text()) 
				{
					case "暂停":
					case "停止":
						upFile.Stop();
						break;
					case "取消":
						{upFile.Remove();}
						break;
					case "续传":
					case "重试":
						upFile.Post();
						break;
				}
			});
		upFile.pPercent = divPercent;
		upFile.pPercent.text("0%");
		upFile.ImageTag = tag; //图片标记
		upFile.Manager = this;
		upFile.InfDiv = ui; //上传信息DIV

		//添加到上传列表层
		this.AppendToListDiv(fid, ui, upFile);

		//添加到上传列表
		this.AppendToUploaderList(fid, upFile);
		upFile.Ready(); //准备
	}
}

var FileUploaderState = {
	Ready: 0,
	Posting: 1,
	Stop: 2,
	Error: 3,
	GetNewID: 4,
	Complete: 5,
	WaitContinueUpload: 6,
	None: 7,
	Waiting: 8
};

/*
	文件上传对象
	参数：
		fileID 文件唯一标识
		filePath 包含完整路径的本地文件名称。D:\Soft\QQ.exe
		width 图片宽度
		height 图片高度
	属性：
		pMsg		显示上传信息，进度信息
		pProcess	进度条
		pPercent	显示百分比文字
		pButton		按钮按钮
		LocalFile	本地文件路径。D:\Soft\QQ.exe
		ImageTag	图片标记。
		InfDiv		上传信息层。
		Separator	分隔线
*/
function FileUploader(fileID,filePath,mgr,width,height)
{
	var _this = this;
	this.Editor = mgr.Editor;
	this.Manager = mgr;
	this.Config = mgr.Config;
	this.ActiveX = mgr.ActiveX;
	this.Fields = mgr.Fields;
	this.Browser = mgr.Browser;
	this.InsertHtml = mgr.InsertHtml;
	this.UploaderPool = mgr.UploaderPool;
	this.UploaderPoolFF = mgr.UploaderPoolFF;

	this.PostLocalFile = false;//是否上传本地文件
	this.PostNetFile = false; //是否是网络图片
	this.imgW = width;
	this.imgH = height;
	this.LocaFile = filePath; //网络图片需要使用
	this.State = FileUploaderState.None;
	this.LocalFile = filePath;
	this.FileID = fileID;
	this.ImageTag = ""; //图片标记，在图片上传完后需要替换此标记
	
    //FileUploader Control
	this.FileUploaderIE =
    {
        "Com": null
        , "ImageType": _this.Config["PasteWordType"]
		, "FileID": 0
    	//methods
		, "Init": function()
		{
		    if (mgr.UploaderPool == null)
		    {
		        mgr.UploaderPool = new ActiveXObject(_this.ActiveX["Uploader"]);
		    }
		    this.Com = mgr.UploaderPool;
			this.Com.Object = _this;
			this.Com.LocalFile = _this.LocalFile;
			this.Com.FileID = this.FileID;
			this.Com.Width = _this.imgW;
			this.Com.Height = _this.imgH;
			this.Com.EncodeType = _this.Config["EncodeType"];
			this.Com.ImageType = this.ImageType;
			this.Com.JpgQuality = _this.Config["JpgQuality"];
			this.Com.License = _this.Config["License"];
			this.Com.Company = _this.Config["Company"];
			this.Com.PostUrl = _this.Config["PostUrl"];
			this.Com.Debug = _this.Config["Debug"];
			this.Com.LogFile = _this.Config["LogFile"];
			this.Com.SetThumbSize(_this.Config["ThumbWidth"], _this.Config["ThumbHeight"]);

			this.Com.OnComplete = HttpUploader_Complete;
			this.Com.OnPost = HttpUploader_Process;
			this.Com.OnError = HttpUploader_Error;
			this.Com.OnConnected = HttpUploader_Connected;

			_this.ResetFields();
		}
		, "Dispose": function () {
			//回收资源，重复复用
			_this.UploaderPool = this.Com;
			//this.Com.Dispose();
		}
		, "Post": function(){this.Com.Post();}
		, "Stop": function(){this.Com.Stop();}
		, "AddField": function(fn, fv){this.Com.AddField(fn, fv);}
		, "SetImageType": function(tp){this.ImageType = tp;}
		, "GetResponse": function(){return this.Com.Response;}
		, "GetLogData": function(){return this.Com.LogData;}
    };
    this.FileUploaderFF = 
    {
        "Com": null
        , "ImageType": _this.Config["PasteWordType"]
		,"SignID":"0"
		//methods
		,"Init":function()
		{
			this.Com = _this.Browser.GetPlugin();
			if (-1 == mgr.UploaderPoolFF)
			{
			    mgr.UploaderPoolFF = this.Com.FileUploader_AddFile();
			}
			this.SignID = mgr.UploaderPoolFF;
			
			this.Com.FileUploader_SetObject(this.SignID,_this);
			this.Com.FileUploader_SetLocalFile(this.SignID,_this.LocalFile);
			this.Com.FileUploader_SetLicense(this.SignID,_this.Config["License"]);
			this.Com.FileUploader_SetCompany(this.SignID,_this.Config["Company"]);
			this.Com.FileUploader_SetPostUrl(this.SignID,_this.Config["PostUrl"]);
			this.Com.FileUploader_SetEncodeType(this.SignID,_this.Config["EncodeType"]);
			this.Com.FileUploader_SetImageType(this.SignID,this.ImageType);
			this.Com.FileUploader_SetJpgQuality(this.SignID,_this.Config["JpgQuality"]);
			this.Com.FileUploader_SetWidth(this.SignID,_this.imgW);
			this.Com.FileUploader_SetHeight(this.SignID,_this.imgH);
			this.Com.FileUploader_SetThumbSize(this.SignID,_this.Config["ThumbWidth"], _this.Config["ThumbHeight"]);

			this.Com.FileUploader_SetOnComplete(this.SignID, HttpUploader_Complete);
			this.Com.FileUploader_SetOnConnected(this.SignID, HttpUploader_Connected);
			this.Com.FileUploader_SetOnError(this.SignID, HttpUploader_Error);
			this.Com.FileUploader_SetOnPost(this.SignID, HttpUploader_Process);

			_this.ResetFields();
		}
		, "Dispose": function () {
			//回收资源，重复复用。
			_this.UploaderPoolFF = this.SignID;
			//this.Com.FileUploader_DelFile(this.SignID);//资源复用，这一句可以取消
		}
		,"Post":function()
		{
			this.Com.FileUploader_Post(this.SignID);
		}
		,"Stop":function()
		{
			this.Com.FileUploader_Stop(this.SignID);
		}
		,"AddField":function(fn,fv) {
			this.Com.FileUploader_AddField(this.SignID,fn,fv.toString());
		}
		,"SetImageType":function(tp)
		{
			this.ImageType = tp;
		}
		,"GetResponse" : function()
		{
			return this.Com.FileUploader_GetResponse(this.SignID);
		}
    };
    _this.ATL = _this.FileUploaderIE;
    if (mgr.BrowserVer == BrowserVersion.Firefox
		|| mgr.BrowserVer == BrowserVersion.Chrome)
    {
    	_this.ATL = _this.FileUploaderFF;
    }

    this.ResetFields = function () {
    	//添加附加字段
    	var pname;
    	for (pname in _this.Fields) {
    		this.ATL.AddField(pname, _this.Fields[pname]);
    	}
    };

	//方法-准备
	this.Ready = function()
	{
		//this.pButton.style.display = "none";
		_this.pMsg.text("正在上传队列中等待...");
		_this.State = FileUploaderState.Ready;
	};

	//方法-开始传输
	this.Post = function()
	{
		//存在相同图片项
		if (_this.ExistSameImage()) return;
		
		_this.pButton.css("display","");
		_this.pButton.text("停止");
		_this.State = FileUploaderState.Posting;
		_this.ATL.Init();
		_this.ATL.Post();
	};

	//存在相同项，本地图片地址相同
	this.ExistSameImage = function()
	{
		//已上传列表中存在相同项
		if (this.Manager.CompleteExist(this.LocalFile))
		{
			var url = this.Manager.CompleteGet(this.LocalFile);
			_this.pButton.text("");
			_this.pProcess.css("width","100%");
			_this.pPercent.text("100%");
			_this.pMsg.text("上传完成");
			_this.State = FileUploaderState.Complete;

			//从上传列表中清除
			_this.Remove();
			//更新编辑器中的图片标签
			_this.ReplaceEditorImgTag(url);
			_this.PostNext();
			return true;
		}
		return false;
	};

	//从上传列表中删除
	this.Remove = function()
	{
		//删除信息层
		_this.InfDiv.remove();
		//删除分隔线
		_this.Separator.remove();
		//清空本地文件名称
		_this.LocalFile = "";
	};

	//方法-启动下一个传输
	this.PostNext = function()
	{
		if (_this.Manager.UnUploaderIdList.length > 0)
		{
			var index = _this.Manager.UnUploaderIdList.shift();
			var obj = _this.Manager.UploaderList[index];

			//空闲状态
			if (FileUploaderState.Ready == obj.State)
			{
				obj.Post();
			}
		} //所有文件上传完毕
		else
		{
			//上传网络图片
			if (_this.PostNetFile)
			{
				_this.Manager.UpdateContentNetImg();//更新内容
			} //上传Word图片时才替换内容
			else if (!_this.PostLocalFile)
			{
				_this.Manager.UpdateContent();
			}
			_this.Manager.CloseDialog();
		}
	};
	
	//停止传输
	this.Stop = function()
	{
		//var obj = _this.Manager.UploaderList[fid];
		this.ATL.Stop();
		this.State = FileUploaderState.Stop;
		this.pButton.text("重试");
	};

	//替换编辑器中的图片标签
	this.ReplaceEditorImgTag = function(imgSrc)
	{
	    _this.Manager.ReplaceEditorImgTag(this.ImageTag, imgSrc, _this.imgW, _this.imgH);
	};

	//本地图片文件上传完毕
	this.LocalFileComplete = function (imgSrc)
	{
		var img = '<img src="' + imgSrc + '"/>';
		_this.InsertHtml(img);
	};

	//网络图片上传完毕
	this.NetFileComplete = function (src)
	{
		var regSrc = new RegExp(_this.LocaFile, "g");
		//将编辑器中所有网络图片地址替换为服务器图片地址。
		_this.Manager.EditorContent = _this.Manager.EditorContent.replace(regSrc, src);
	};

	//文件上传完毕
	this.FilePostComplete = function(imgSrc)
	{
		//上传的本地文件
		if (_this.PostLocalFile)
		{
			_this.LocalFileComplete(imgSrc);
		} //网络图片上传完毕
		else if(_this.PostNetFile)
		{
			_this.NetFileComplete(imgSrc);
		}//上传的Word中的图片
		else
		{
			_this.ReplaceEditorImgTag(imgSrc);
		}
	};
}

//连接成功
function HttpUploader_Connected(obj)
{
	obj.pMsg.text("服务器连接成功");
}

//传输完毕
function HttpUploader_Complete(obj)
{
	obj.pButton.text("");
	obj.pProcess.css("width","100%");
	obj.pPercent.text("100%");
	obj.pMsg.text("上传完成");
	obj.State = FileUploaderState.Complete;

	//添加到已上传列表
	obj.Manager.CompleteAdd(obj.LocalFile, obj.ATL.GetResponse());
	//从上传列表中清除
	obj.Remove();
	//更新编辑器中的图片标签
	obj.FilePostComplete(obj.ATL.GetResponse());
	obj.PostNext();
}

//传输进度
function HttpUploader_Process(obj, speed, postedLen, percent, time)
{
	var msg = "已上传:" + postedLen + " 速度:" + speed + " 剩余时间:" + time;
	obj.pMsg.text(msg);
	obj.pPercent.text(percent);
	obj.pProcess.css("width",percent);
}

//传输错误
function HttpUploader_Error(obj, err)
{
	if (obj.Config["Debug"])
	{
		alert(obj.ATL.GetLogData());
	}
	obj.pMsg.text( WordPasterError[err]);
	obj.pButton.text("重试");
	obj.State = FileUploaderState.Error;
	obj.PostNext(); //继续传输下一个
}

var ImagePasterState = {
	Posting: 0
	, Stop: 1
	, Error: 2
	, Complete: 3
	, None: 4
};

/*
	图片粘贴器。负责从剪帖板中获取图片数据，并自动上传到WEB服务器中。
	参数:
	[0] 显示粘贴信息的层
	属性
	InfoDiv 提示信息层，用来显示图片粘贴器的处理进度信息。
	关联HTML元素：
		粘贴信息层：PasterMessager
*/
function ImagePasterManager(mgr)
{
	this.Manager = mgr;
	this.Config = mgr.Config;
	this.Fields = mgr.Fields;
	this.ActiveX = mgr.ActiveX;
	//图片粘贴窗口
	this.OpenDialog = mgr.OpenPasteFileDialog;
	this.CloseDialog = mgr.ClosePasteFileDialog;
	this.InsertHtml = mgr.InsertHtml;
	this.Com = mgr.ctlImagePaster;
	this.Com.setObj(this);

	this.InfoDiv 	= mgr.imgPasterDlg; //提示信息层
	this.InfoIco 	= mgr.imgIco;
	this.Message 	= mgr.imgMsg;
	this.Progress 	= mgr.imgPercent;
	this.State 	= ImagePasterState.None;
	
	this.IsBitmap = function()
	{
		return this.Com.IsBitmap();
	};

	this.IsFile = function()
	{
		return this.Com.IsFile();
	};
	
	//是否正在上传
	this.IsPosting = function()
	{
		return this.State == ImagePasterState.Posting;
	};

	//粘贴
	this.Paste = function()
	{
		//清空附加字段
		this.Com.ClearFields();
		var item = null, fields = this.Fields;
		for (item in fields)
		{
			this.Com.AddField(item, fields[item]);
		}

		this.OpenDialog();//显示图片上传窗口
		this.Com.Paste();
	};

	//粘贴多张图片
	this.PasteFiles = function()
	{
		this.Manager.OpenUploadFileDialog();
		var files = this.Com.GetFiles();
		for (var i = 0, l = files.length; i<l; ++i)
		{
			this.Manager.WordPaster.AddLocalFile(files.getItem(i));
		}
		this.Manager.WordPaster.PostFirst();
	};
}

//服务器连接成功
function ImagePaster_Connected(obj)
{
	obj.InfoDiv.css("display","block");
	obj.Progress.text("10%");
	obj.State = ImagePasterState.Posting;
}

//图片上传完毕
function ImagePaster_Complete(obj)
{
	obj.Progress.text("100%");
	obj.Message.text("上传完成");
	obj.State = ImagePasterState.Complete;
	obj.CloseDialog(); //显示图片上传窗口

	//插入到编辑器
	var img = "<img src=\"";
	img += obj.Com.GetResponse();
	img += "\" />";
	// Insert as plain text.
	obj.InsertHtml(img);
}

/*
事件-传输中....
参数:
obj JS对象
speed	传输速度
length 已传输长度。1Byte,1KB,1MB,1GB
percent 上传百分比
time 剩余时间
*/
function ImagePaster_OnProcess(obj, speed, length, percent, time)
{
	obj.Progress.text(percent);
}

//事件-传输停止。暂时未使用
function ImagePaster_Stop(obj)
{
	obj.Com.Stop();
	obj.State = ImagePasterState.Stop;
}
/*
事件-传输错误
参数:
obj JS对象
err 错误码
*/
function ImagePaster_OnError(obj, ert)
{
	if (obj.Config["Debug"])
	{
		alert(obj.Com.GetLogData());
	}
	obj.Message.text(WordPasterError[ert]);
	obj.InfoIco.src = obj.Config["IcoError"];
	obj.Progress.text("");
	obj.State = ImagePasterState.Error;
}

//浏览器版本
var BrowserVersion = {
	IE: 0
	, IE64: 1
	, Firefox: 2
	, Chrome: 3
};
//粘贴管理器
function PasterManager()
{
	var _this = this;
	this.Editor = null;
	this.Domain = "http://" + document.location.host + "/";
	this.Fields = {}; //符加信息
	this.UploadDialogCreated = false;
	this.PasteDialogCreated = false;
	this.imgPasterDlg = null;//jquery obj
	this.imgUploaderDlg = null;//jquery obj
	this.imgIco = null;//jquery obj
	this.imgMsg = null;//jquery obj
	this.imgPer = null;//jquery obj
	this.ffPaster = null;
	this.iePaster = null;
	this.ieParser = null;
	this.filesPanel = null;//jquery obj
	this.fileItem = null;//jquery obj
	this.line = null;//jquery obj
	this.BrowserVer = BrowserVersion.IE;
	//配置信息
	this.Config = WordPasterConfig;
	this.ActiveX = WordPasterActiveX;
	
	//FireFox浏览器信息管理对象
	this.BrowserFF = {
		"GetHtml": function()
		{
			var html = '<embed type="' + _this.Config["MimeType"] + '" pluginspage="' + _this.Config["XpiPath"] + '" width="1" height="1" id="objWordPaster"/>';
			return html;
		}
		, "GetPlugin": function()
		{
			return _this.ffPaster;
		}
	    , "NeedUpdate": function ()
	    {
	        var obj = this.GetPlugin();
	        var ver = "0";
	        try
	        {
	            ver = obj.Version;
	        }
	        catch (e) { }
	        return ver == _this.Config["Version"];
	    }//检查插件是否已安装
         , "Check": function () {
         	var mimetype = navigator.mimeTypes;
         	if (typeof mimetype == "object" && mimetype.length) {
         		for (var i = 0; i < mimetype.length; i++) {
         			if (mimetype[i].type == _this.Config["MimeType"].toLowerCase()) {
         				return mimetype[i].enabledPlugin;
         			}
         		}
         	}
         	else {
         		mimetype = [_this.Config["MimeType"]];
         	}
         	if (mimetype) {
         		return mimetype.enabledPlugin;
         	}
         	return false;
         } //安装插件
		, "Setup": function()
		{
			var xpi = new Object();
			xpi["Calendar"] = _this.Config["XpiPath"];
			InstallTrigger.install(xpi, function(name, result) { });
		}
		, "Init": function() { }
	};
	//Chrome浏览器信息管理对象
	this.BrowserChrome =
	{
		"GetHtml": function()
		{
			var html = '<embed type="' + _this.Config["MimeTypeChr"] + '" pluginspage="' + _this.Config["XpiPath"] + '" width="1" height="1" id="objWordPaster"/>';
			return html;
		}
		, "GetPlugin": function()
		{
			return _this.ffPaster;
		} //检查插件是否需要更新
        , "NeedUpdate": function ()
        {
            var obj = this.GetPlugin();
            var ver = "0";
            try
            {
                ver = obj.Version;
            }
            catch (e)
            { }
            return ver == _this.Config["Version"];
        }//检查插件是否已安装
		, "Check": function()
		{
			for (var i = 0, l = navigator.plugins.length; i < l; i++)
			{
				if (navigator.plugins[i].name == _this.Config["CrxName"])
				{
					return true;
				}
			}
			return false;
		} //安装插件
		, "Setup": function()
		{
			document.write('<iframe style="display:none;" src="' + _this.Config["XpiPath"] + '"></iframe>');
		}
		, "Init": function()
		{ }
	};
	//IE浏览器信息管理对象
	this.BrowserIE = {
		"GetHtml": function()
		{
			/*ActiveX的静态加载方式，如果在框架页面中使用此控件，推荐使用静态加截方式。
			<div style="display: none">
			<object id="Paster" classid="clsid:1832A49E-09D5-470E-AA4B-BE8F28034218" codebase="http://www.ncmem.com/products/screencapture/demo/ScreenCapture.cab#version=1,6,26,54978" width="1" height="1"></object>
			</div>
			*/
			var acx = '<div style="display: none">';
			//Word图片粘贴
			acx += ' <object id="objImagePaster" classid="clsid:' + _this.Config["ClsidImagePaster"] + '"';
			acx += ' codebase="' + _this.Config["CabPath"] + '#version='+_this.Config["Version"]+'"';
			acx += ' width="1" height="1" ></object>';
			//Word图片上传控件
			acx += ' <object id="objFileUploader" classid="clsid:' + _this.Config["ClsidUploader"] + '"';
			acx += ' codebase="' + _this.Config["CabPath"] + '#version='+_this.Config["Version"]+'"';
			acx += ' width="1" height="1" ></object>';
			//Word解析组件
			acx += ' <object id="objWordParser" classid="clsid:' + _this.Config["ClsidParser"] + '"';
			acx += ' codebase="' + _this.Config["CabPath"] + '#version='+_this.Config["Version"]+'"';
			acx += ' width="1" height="1" ></object>';
			acx += '</div>';
			return acx;
		}
		, "GetPlugin": function()
		{
			return _this.iePaster;
		}
	    , "NeedUpdate": function ()
	    {
	        var obj = this.GetPlugin();
	        var ver = "0";
	        try
	        {
	            ver = obj.Version;
	        }
	        catch (e) { }
	        return ver == _this.Config["Version"];
	    }//检查插件是否已安装
		, "Check": function()
		{
			try
			{
			    var com = new ActiveXObject(_this.ActiveX["Uploader"]);
				return true;
			}
			catch (e) { return false; }
		}
		, "Init": function()
		{ }
	};
	this.Browser = this.BrowserIE;
	//WordParser Control
	this.WordParserIE =
	{
		"Com": null
		, "Object": null//调用者设置
        , "setObj": function (obj) { if (!_this.Browser.Check()) return; this.Com.Object = obj; }
		, "Init": function()
		{
		    if (!_this.Browser.Check()) return;
			this.Com = _this.ieParser;
			//this.Com.Object = this.Object;
			this.Com.AddWordImg = WordParser_AddImage;
		}
		, "Parse": function() { }
		, "IsWord": function()
		{
			return this.Com.IsWord();
		}
		, "IsExcel": function ()
		{
			return this.Com.IsExcel();
		}
		, "GetWord": function()
		{
			return this.Com.GetWord();
		}
		, "GetImages": function()
		{
			return this.Com.GetImages();
		}
		, "GetExcel": function()
		{
			return this.Com.GetExcel();
		}
	};
	this.WordParserFF =
	{
		"Com": null
		, "Object": null//调用者设置
        , "setObj": function (obj) { this.Com.WordParser_Object = obj; }
		, "Init": function()
		{
		    if (!_this.Browser.Check()) return;
			this.Com = _this.ffPaster;
			this.Com.Debug = _this.Config["Debug"];
			this.Com.LogFile = _this.Config["LogFile"];
			//this.Com.WordParser_Object = this.Object;
			this.Com.WordParser_AddWordImg = WordParser_AddImage;
		}
		, "Parse": function() { }
		, "IsWord": function()
		{
			return this.Com.WordParser_IsWord();
		}
		, "IsExcel": function ()
		{
			return this.Com.WordParser_IsExcel();
		}
		, "GetWord": function()
		{
			return this.Com.WordParser_GetWord();
		}
		, "GetImages": function()
		{
			return this.Com.WordParser_GetImages();
		}
		, "GetExcel": function()
		{
			return this.Com.WordParser_GetExcel();
		}
	};
	this.ctlWordParser = this.WordParserIE;
    //ImagePaster Control
	this.ImagePasterIE = {
		"Com": null
    	, "Object": null//调用者设置
        , "setObj": function (obj) { if (!_this.Browser.Check()) return; this.Com.Object = obj; }//bug:为什么在这里设置无效？
    	, "Init": function()
    	{
    	    if (!_this.Browser.Check()) return;
    		this.Com = _this.iePaster;
    		//this.Com.Object = this.Object;
    		this.Com.ImageType = _this.Config["PasteImageType"];
    		this.Com.License = _this.Config["License"];
    		this.Com.Company = _this.Config["Company"];
    		this.Com.PostUrl = _this.Config["PostUrl"];
    		this.Com.EncodeType = _this.Config["EncodeType"];
    		this.Com.Debug = _this.Config["Debug"];
    		this.Com.LogFile = _this.Config["LogFile"];
    		this.Com.SetThumbSize(_this.Config["ThumbWidth"], _this.Config["ThumbHeight"]);
    		//event
    		this.Com.OnConnected 	= ImagePaster_Connected;
    		this.Com.OnComplete 	= ImagePaster_Complete;
    		this.Com.OnPost 		= ImagePaster_OnProcess;
    		this.Com.OnError 		= ImagePaster_OnError;
    	}
    	, "ClearFields": function() { this.Com.ClearFields(); }
    	, "AddField": function(fn, fv) { this.Com.AddField(fn, fv); }
    	, "Stop": function() { this.Com.Stop(); }
    	, "Paste": function() { this.Com.Paste(); }
    	, "IsBitmap": function() { return this.Com.IsBitmap(); }
    	, "IsFile": function() { return this.Com.IsFile(); }
    	, "GetFiles": function()
    	{
    		var arr = new Array();
    		var files = this.Com.GetFiles();
    		for (var i = files.lbound(1); i <= files.ubound(1); i++)
    		{
    			arr.push(files.getItem(i));
    		}
    		return arr;
    	}
    	, "GetResponse": function() { return this.Com.Response; }
    	, "GetLogData":function(){return this.Com.LogData;}
	};
	this.ImagePasterFF = {
		"Com": null
    	, "Object": null//调用者设置
        , "setObj": function (obj) { this.Com.ImgPst_Object = obj; }
    	, "Init": function ()
    	{
    	    if (!_this.Browser.Check()) return;
    		this.Com = _this.ffPaster;
    		//this.Com.ImgPst_Object = this.Object;
    		this.Com.ImgPst_License = _this.Config["License"];
    		this.Com.ImgPst_Company = _this.Config["Company"];
    		this.Com.ImgPst_PostUrl = _this.Config["PostUrl"];
    		this.Com.ImgPst_EncodeType = _this.Config["EncodeType"];
    		this.Com.ImgPst_ImageType = _this.Config["PasteImageType"];
    		this.Com.ImgPst_SetThumbSize(_this.Config["ThumbWidth"], _this.Config["ThumbHeight"]);
    		//event
    		this.Com.ImgPst_OnComplete  = ImagePaster_Complete;
    		this.Com.ImgPst_OnConnected = ImagePaster_Connected;
    		this.Com.ImgPst_OnPost 		= ImagePaster_OnProcess;
    		this.Com.ImgPst_OnError 	= ImagePaster_OnError;
    	}
    	, "ClearFields": function() { this.Com.ImgPst_ClearFields(); }
    	, "AddField": function(fn, fv) { this.Com.ImgPst_AddField(fn, fv); }
    	, "Stop": function() { this.Com.ImgPst_Stop(); }
    	, "Paste": function() { this.Com.ImgPst_Paste(); }
    	, "IsBitmap": function() { return this.Com.ImgPst_IsBitmap(); }
    	, "IsFile": function() { return this.Com.ImgPst_IsFile(); }
    	, "GetFiles": function() { return this.Com.ImgPst_GetFiles(); }
    	, "GetResponse": function() { return this.Com.ImgPst_Response; }
	};
	this.ctlImagePaster = this.ImagePasterIE;
    
	var browserName = navigator.userAgent.toLowerCase();
	this.ie = browserName.indexOf("msie") > 0;
	//IE11
	this.ie = this.ie ? this.ie : browserName.search(/(msie\s|trident.*rv:)([\w.]+)/)!=-1;
	this.firefox = browserName.indexOf("firefox") > 0;
	this.chrome = browserName.indexOf("chrome") > 0;

	if ( this.ie )
	{
		//Win64
		if (window.navigator.platform == "Win64")
		{
			_this.BrowserVer = BrowserVersion.IE64;
			_this.Config["ClsidImagePaster"] = this.Config["ClsidImagePaster64"];
			_this.Config["ClsidUploader"] = this.Config["ClsidUploader64"];
			_this.Config["ClsidParser"] = this.Config["ClsidParser64"];
			_this.Config["CabPath"] = this.Config["CabPath64"];
			//ActiveX
			_this.ActiveX["Uploader"] = this.ActiveX["Uploader64"];
			_this.ActiveX["ImagePaster"] = this.ActiveX["ImagePaster64"];
			_this.ActiveX["WordParser"] = this.ActiveX["WordParser64"];
		}
	} //Firefox
	else if ( this.firefox )
	{
		_this.BrowserVer = BrowserVersion.Firefox;
		_this.Browser = this.BrowserFF;
		_this.ctlImagePaster = _this.ImagePasterFF;
		_this.ctlWordParser = _this.WordParserFF;
		if (!_this.Browser.Check()) {_this.Browser.Setup();}
	} //chrome
	else if ( this.chrome )
	{
		_this.BrowserVer = BrowserVersion.Chrome;
		_this.Config["XpiPath"] = _this.Config["CrxPath"];
		_this.Config["MimeType"] = _this.Config["MimeTypeChr"];
		_this.Browser = this.BrowserChrome;
		_this.ctlImagePaster = _this.ImagePasterFF;
		_this.ctlWordParser = _this.WordParserFF;
		if (!_this.Browser.Check()) {_this.Browser.Setup();}
    }
	
	//初始化路径
	this.InitPath = function()
	{
		this.Config["AppPath"] = this.Config["AppPath"].replace("/", "");
		if (this.Config["AppPath"].length > 0)
		{
			//http://www.ncmem.com/AppPath/
			this.Domain += this.Config["AppPath"] + "/";
		}
		this.Config["CabPath"] = this.Domain + this.Config["CabPath"];
		this.Config["PostUrl"] = this.Domain + this.Config["PostUrl"];
	};
	
	//加载控件及HTML元素
	this.GetHtml = function()
	{
		//Word图片粘贴
		var acx = "";
		/*
			静态加截控件代码，在复杂WEB系统中或者框架页面中请静态方式加截Word解析组件(Xproer.WordParser)。
			<object id="objWordParser" classid="clsid:1832A49E-09D5-470E-AA4B-BE8F28034218"	width="1" height="1" ></object>
		*/
		acx += '<embed name="ffPaster" type="' + this.Config["MimeType"] + '" pluginspage="' + this.Config["XpiPath"] + '" width="1" height="1" id="objWordPaster"/>';
	    //Word图片粘贴
		acx += ' <object name="iePaster" classid="clsid:' + this.Config["ClsidImagePaster"] + '"';
		acx += ' codebase="' + this.Config["CabPath"] + '#version=' + this.Config["Version"] + '"';
		acx += ' width="1" height="1" ></object>';
	    //Word图片上传控件
		acx += ' <object name="ieUploader" classid="clsid:' + this.Config["ClsidUploader"] + '"';
		acx += ' codebase="' + this.Config["CabPath"] + '#version=' + this.Config["Version"] + '"';
		acx += ' width="1" height="1" ></object>';
	    //Word解析组件
		acx += ' <object name="ieParser" classid="clsid:' + this.Config["ClsidParser"] + '"';
		acx += ' codebase="' + this.Config["CabPath"] + '#version=' + this.Config["Version"] + '"';
		acx += ' width="1" height="1" ></object>';
	    //单张图片上传窗口
		acx += '<div name="imgPasterDlg" id="PasterMessager" style="display:none;">';		
		acx += '<img name="ico" id="infIco" alt="进度图标" src="' + this.Config["IcoUploader"] + '" /><span name="msg">图片上传中...</span><span name="percent">10%</span>';
		acx += '</div>';
	    //图片批量上传窗口
		acx += '<div name="filesPanel" id="FilePostLister" style="display: none;"></div>';
	    //
		acx += '<div style="display: none;">';

		//文件上传列表项模板
		acx += '<div class="UploaderItem" name="fileItem" id="UploaderTemplate">\
		            <div class="UploaderItemLeft">\
		            <div name="fname" class="FileName top-space">HttpUploader程序开发.pdf</div>\
		            <div class="ProcessBorder top-space">\
		                <div name="process" class="Process"></div>\
		            </div>\
		            <div name="msg" class="PostInf top-space">已上传:15.3MB 速度:20KB/S 剩余时间:10:02:00</div>\
		        </div>\
		        <div class="UploaderItemRight">\
		            <a name="btn" class="Btn" href="javascript:void(0)">取消</a>\
		            <div name="percent" class="ProcessNum">35%</div>\
		        </div>';
		acx += '</div>'; //template end
		//分隔线
		acx += '<div name="line" class="Line" id="FilePostLine"></div>';

		//hide div end
		acx += '</div>';
		return acx;
	};

    //加载控件及HTML元素
	this.Load = function ()
	{
	    var dom = $(document.body).append(this.GetHtml());
	    this.ffPaster   	= dom.find('embed[name="ffPaster"]').get(0);
	    this.iePaster   	= dom.find('object[name="iePaster"]').get(0);
	    this.ieParser   	= dom.find('object[name="ieParser"]').get(0);
	    this.line       	= dom.find('div[name="line"]');
	    this.fileItem   	= dom.find('div[name="fileItem"]');
	    this.filesPanel 	= dom.find('div[name="filesPanel"]');		
	    this.imgUploaderDlg = dom.find('div[name="filesPanel"]');
	    this.imgPasterDlg 	= dom.find('div[name="imgPasterDlg"]');
        this.imgIco         = this.imgPasterDlg.find('img[name="ico"]');
        this.imgMsg         = this.imgPasterDlg.find('span[name="msg"]');
        this.imgPercent     = this.imgPasterDlg.find('span[name="percent"]');

        this.init();
	};

	this.LoadTo = function (oid)
	{
	    var dom = $("#" + oid).append(this.GetHtml());
	    this.ffPaster   	= dom.find('embed[name="ffPaster"]').get(0);
	    this.iePaster   	= dom.find('object[name="iePaster"]').get(0);
	    this.ieParser   	= dom.find('object[name="ieParser"]').get(0);
	    this.line       	= dom.find('div[name="line"]');
	    this.fileItem   	= dom.find('div[name="fileItem"]');
	    this.filesPanel 	= dom.find('div[name="filesPanel"]');		
	    this.imgUploaderDlg = dom.find('div[name="filesPanel"]');
	    this.imgPasterDlg 	= dom.find('div[name="imgPasterDlg"]');
        this.imgIco         = this.imgPasterDlg.find('img[name="ico"]');
        this.imgMsg         = this.imgPasterDlg.find('span[name="msg"]');
        this.imgPercent     = this.imgPasterDlg.find('span[name="percent"]');

        this.init();
	};

    //在文档加载完毕后调用
	this.init = function ()
	{
	    $(function ()
	    {
	        _this.ctlImagePaster.Init();//
	        _this.ctlWordParser.Init();

	        _this.WordPaster = new WordPasterManager(_this);
	        _this.ImagePaster = new ImagePasterManager(_this);
	    });
	};

	//打开图片上传对话框
	this.OpenUploadFileDialog = function()
	{
	    _this.imgUploaderDlg.skygqbox();
	};
	this.CloseUploadFileDialog = function()
	{
	    $('#wrapClose').click();
	    this.CompleteClear(); //清空图片缓存
	};
	//打开粘贴图片对话框
	this.OpenPasteFileDialog = function()
	{
	    _this.imgPasterDlg.skygqbox();
	};
	this.ClosePasteFileDialog = function()
	{
	    $('#wrapClose').click();
	};
	this.InsertHtml = function (html)
	{ 
		this.Editor.insertHtml(html);
	};

	//在页面中调用
	this.SetEditor = function(edt)
	{
		this.Editor = edt;
		this.ImagePaster.Editor = edt;
		this.WordPaster.Editor = edt;
	};
	
	//单击按钮粘贴
	_this.Paste = function()
	{
	    if (!_this.Browser.Check()) return;//未安装
	    //上传WORD图片
		if (_this.ctlWordParser.IsWord())
		{
			_this.WordPaster.PasteWord();
		}//上传Excel图片
		else if (_this.ctlWordParser.IsExcel())
		{
			_this.WordPaster.PasteExcel();
		}//是图片文件
		else if (_this.ImagePaster.IsBitmap())
		{
			if (!_this.ImagePaster.IsPosting())
			{
				_this.ImagePaster.Paste();
			}
		} //上传本地硬盘文件图片
		else if (_this.ImagePaster.IsFile())
		{
			_this.ImagePaster.PasteFiles();
		}//文本
		else
		{
			_this.Editor.exec('paste');
		}
	};
	
	//粘贴Excel
	_this.PasteExcel = function()
	{
		if (_this.ctlWordParser.IsExcel())
		{
			_this.WordPaster.PasteExcel();
		}
	};

	//上传网络图片
	_this.UploadNetImg = function ()
	{
		var data = _this.Editor.html();
		_this.WordPaster.MatchNetImg(data);
	};
}