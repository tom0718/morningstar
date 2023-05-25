package com.mms.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XssMyFilter {

	private static final Pattern SCRIPTS = Pattern.compile("<script([^'\"]|\"[^\"]*\"|'[^']*')*?</script>",
			Pattern.CASE_INSENSITIVE);

	public static String checkNormal(String name) {

		if (name != null && !name.equals("")) {
			name = name.replaceAll("<", "&lt;");
			name = name.replaceAll(">", "&gt;");
			name = name.replaceAll("&", "&amp;");
			name = name.replaceAll("\\", "&quot;");

			name = name.replaceAll("%00", null);
			name = name.replaceAll("\"", "&#34;");
			name = name.replaceAll("\'", "&#39;");
			name = name.replaceAll("%", "&#37;");

			name = name.replaceAll("../", "");
			name = name.replaceAll("..\\\\", "");
			name = name.replaceAll("./", "");
			name = name.replaceAll("%2F", "");

		}

		return name;

	}

	public static String checkScript(String name) {

		if (name != null && !name.equals("")) {
			Matcher m = SCRIPTS.matcher(name);
			name = m.replaceAll("");

		}
		
		return name;

	}

}
