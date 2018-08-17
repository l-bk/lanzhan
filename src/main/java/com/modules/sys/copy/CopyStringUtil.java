package com.modules.sys.copy;

public class CopyStringUtil {
	private String str = "";

	private String replacereg = "";

	public CopyStringUtil(String str) {
		this.str = str;
		replaceFetch(str);
	}

	public String getReplacereg() {
		if (replacereg != null && replacereg.length() > 0) {
			return replacereg;
		} else {
			return "";
		}
	}

	public void setReplacereg(String replacereg) {
		this.replacereg = replacereg;
	}

	private void replaceFetch(String str) {
		boolean c1 = false;
		boolean c2 = false;
		boolean c3 = false;
		boolean c4 = false;
		boolean c5 = false;
		boolean c6 = false;
		boolean c7 = false;
		// System.out.println(str.indexOf("[通配]") > -1);
		if (str != null && str.length() > -1) {
			if (str.indexOf("[通配]") > -1) {
				str = str.replace("[通配]", ".*?");
			} else {
				c1 = true;
			}
			if (str.indexOf("[空格]") > -1) {
				str = str.replace("[空格]", ".*?");
			} else {
				c2 = true;
			}
			if (str.indexOf("[换行或连接]") > -1) {
				str = str.replace("[换行或连接]", ".*?");
			} else {
				c3 = true;
			}
			if (str.indexOf("[URl内容]") > -1) {
				str = str.replace("[URl内容]", "(.*?)");
			} else {
				c4 = true;
			}
			if (str.indexOf("[内容]") > -1) {
				str = str.replace("[内容]", "(.*?)");

			} else {
				c5 = true;
			}
			if (str.indexOf("[数字]") > -1) {
				str = str.replace("[数字]", "[\\d]*");
			} else {
				c6 = true;
			}
			if (str.indexOf("[数字或字母]") > -1) {
				str = str.replace("[数字或字母]", "[a-zA-Z0-9]*");
			} else {
				c6 = true;
			}
			if (str.indexOf("[字母]") > -1) {
				str = str.replace("[字母]", "[a-zA-Z]*");
			} else {
				c7 = true;
			}

			boolean check = c1 && c2 && c3 && c4 && c5 && c6 && c7;
			// System.out.println(check);
			if (!check) {
				replaceFetch(str);
			} else {
				setReplacereg(str);
			}
			// System.out.println("替换后str=" + str);

		}
	}
}
