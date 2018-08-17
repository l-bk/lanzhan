package com.modules.sys.copy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	public static void main(String[] args) {
		// String str =
		// "2016年04月26日 16:17　来源：<a href='http://www.chinanews.com/' style='color:#666;font-weight:normal;'>中国新闻网</a> <img src=\"http://i3.chinanews.com/2011/news/images/1.png\" /><a  href=\"#zw_cyhd\" target=\"_self\">参与互动</a>";
		// String reg = "来源.(?:<a.*?>)(.*?)</";
		// Pattern pattern = Pattern.compile(reg);
		// Matcher mth = pattern.matcher(str);
		// while(mth.find()){
		// System.out.println(mth.group(1));
		// }
		int i = 1002;
		int j = 10;
		int sp = i % j > 0 ? (i / j) + 1 : i / j;
		System.out.println(sp);
	}
}
