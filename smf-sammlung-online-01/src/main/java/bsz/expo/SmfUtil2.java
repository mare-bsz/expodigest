package bsz.expo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmfUtil2 {
	
	private static Pattern ansetzung = Pattern.compile("(.*) \\<.+\\>");
	
	public static String clean(final String src) {
		Matcher matcher = ansetzung.matcher(src);		
		if (matcher.matches()) {
			return toJson(matcher.group(1));
		} else {
			return toJson(src);
		}		
	}
	
	public static String toJson(final String src) {
		StringBuilder result = new StringBuilder();
		for (char c : src.trim().toCharArray()) {
			switch (c) {
				case '"': result.append("\\\""); break;
				case '\\': result.append("\\\\"); break;
				case '\n': result.append("\\n"); break;
				case '/' : result.append("\\/"); break;
				case '\b' : result.append("\\b"); break;
				case '\f' :	result.append("\\f"); break;		
				case '\r' : result.append("\\r"); break;
				case '\t' : result.append("\\t"); break;
				default: result.append(c);
			}			
		}
		return result.toString();		
	}

}
