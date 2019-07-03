package de.jotwerk;

public class Util {
	
	public static String toJson(final String src) {
		StringBuilder result = new StringBuilder();
		for (char c : src.toCharArray()) {
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
