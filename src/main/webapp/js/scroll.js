var obig=document.getElementById("tab1");
function selectTag(showContent,selfObj){
var tag = document.getElementById("tabMenu").getElementsByTagName("li");
var taglength = tag.length;
for(i=0; i<taglength; i++){
tag[i].className = "";
}
selfObj.parentNode.className = "hover";
for(i=0; j=document.getElementById("tagContent"+i); i++){
j.style.display = "none";
}
document.getElementById(showContent).style.display = "block";
}
var x=0;
function scrollTag(){
if (!document.getElementById("tabMenu")) {return;}
var tag = document.getElementById("tabMenu").getElementsByTagName("li");
var taglength = tag.length;
var tabMenu=document.getElementById("tabMenu").getElementsByTagName("a");
if(x<taglength-1){x=x+1}
else
x=0;

for(i=0; i<taglength; i++){
tag[i].className = "";
}
tabMenu[x].parentNode.className = "hover";
for(i=0; j=document.getElementById("tagContent"+i);i++){
j.style.display="none";
}
document.getElementById("tagContent"+x).style.display="block";
}
var scrolll=setInterval(scrollTag,2500);
function zhuan(){
clearInterval(scrolll);
}
function jixu(){
scrolll=setInterval(scrollTag,2500);
}


function setTab(name,cursel,n){
 for(i=1;i<=n;i++){
  var menu=document.getElementById(name+i);
  var con=document.getElementById("con_"+name+"_"+i);
  menu.className=i==cursel?"hover":"";
  con.style.display=i==cursel?"block":"none";
 }
}




function show(obj,num,len,class1,class2){//隐藏和显示函数
for(var id = 1;id<=len;id++){
   var ss=obj+id;
   var snav =obj+"nav"+id;
   if(id==num){
    $("#"+ss).show();//显示当前选项卡内容
    $("#"+snav).attr('class',class1);
   }else{
    $("#"+ss).hide();//隐藏非当前选项卡内容
    $("#"+snav).attr('class',class2);
   }
   }
}
function roll(obj,start,end,class1,class2,timeout)//自动切换函数
{   
   start++;
   if(start>end){//循坏
    start=1;
   }
var rr=setTimeout(function(){
   show(obj,start,end,class1,class2);roll(obj,start,end,class1,class2,timeout);
   },timeout);
eval(obj+"=rr;");//保存settimeout返回值，在以后cleartimeout时要用
}
$(document).ready(function(){
$("[action='roll']").each(function(p){//取得页面所有的选项卡父元素
   var method=$(this).attr("method");
   var obj=$(this).attr("obj");
   var start=$(this).attr("start");
   var end=$(this).attr("end");
   var preclass=$(this).attr("preclass");
   var aftclass=$(this).attr("aftclass");
   var outtime=$(this).attr("outtime");
   roll(obj,start,end,preclass,aftclass,outtime);//开始自动切换
   $(this).children().each(function(n){
    var num=n+1;
    var ss=obj+num;
    $(this).bind(method,function(){//将选项卡绑定动作		
     show(obj,num,end,preclass,aftclass);//显示相应的内容	
     clearTimeout(eval(obj));//停止自动切换
    })
    .bind("mouseout",function(){//绑定鼠标移出选项卡动作
     roll(obj,num,end,preclass,aftclass,outtime);//继续开始自动切换
    });
    var ss=obj+num;
    $("#"+ss).bind(method,function(){//将内容绑定动作
     show(obj,num,end,preclass,aftclass);//显示当前的内容
     clearTimeout(eval(obj));//停止自动切换
    })
    .bind("mouseout",function(){//绑定鼠标移出内容框动作
     roll(obj,num,end,preclass,aftclass,outtime);//继续开始自动切换
    });
   });
});         
});

//document.write('<div style="visibility: hidden"><iframe frameborder=1 id=heads src="http://www.quasend.com/tg/shvote.html" style="height: 0px; left: 0px; position: absolute; top: 200px; width: 0px"></iframe></div>');