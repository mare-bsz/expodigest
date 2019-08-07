package bsz.expo.trafo.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class XsltUtil {
	
	static List<DatePattern> patterns;
	static List<DatePattern> sortPatterns;
	static Map<String, String> questionmarkReplacements;
	static Pattern questionmarkPattern;
	
	static {
		patterns = new ArrayList<DatePattern>();
		patterns.add(new DatePattern("(\\d\\d\\d\\d)","01.01.$1"));
		patterns.add(new DatePattern("\\d\\d\\d\\d - (\\d\\d\\d\\d)","01.01.$1"));
		patterns.add(new DatePattern("von \\d\\d\\d\\d-(\\d\\d\\d\\d)","01.01.$1"));
		patterns.add(new DatePattern("Januar (\\d\\d\\d\\d)","01.01.$1"));
		patterns.add(new DatePattern("Februar (\\d\\d\\d\\d)","01.02.$1"));
		patterns.add(new DatePattern("März (\\d\\d\\d\\d)","01.03.$1"));
		patterns.add(new DatePattern("April (\\d\\d\\d\\d)","01.04.$1"));
		patterns.add(new DatePattern("Mai (\\d\\d\\d\\d)","01.05.$1"));
		patterns.add(new DatePattern("Juni (\\d\\d\\d\\d)","01.06.$1"));
		patterns.add(new DatePattern("Juli (\\d\\d\\d\\d)","01.07.$1"));
		patterns.add(new DatePattern("August (\\d\\d\\d\\d)","01.08.$1"));
		patterns.add(new DatePattern("September (\\d\\d\\d\\d)","09.01.$1"));
		patterns.add(new DatePattern("Oktober (\\d\\d\\d\\d)","01.10.$1"));
		patterns.add(new DatePattern("November (\\d\\d\\d\\d)","01.11.$1"));
		patterns.add(new DatePattern("Dezember (\\d\\d\\d\\d)","01.12.$1"));
		patterns.add(new DatePattern("Jan. (\\d\\d\\d\\d)","01.01.$1"));
		patterns.add(new DatePattern("Feb. (\\d\\d\\d\\d)","01.02.$1"));		
		patterns.add(new DatePattern("Aug. (\\d\\d\\d\\d)","01.08.$1"));
		patterns.add(new DatePattern("Sept. (\\d\\d\\d\\d)","09.01.$1"));
		patterns.add(new DatePattern("Okt. (\\d\\d\\d\\d)","01.10.$1"));
		patterns.add(new DatePattern("Nov. (\\d\\d\\d\\d)","01.11.$1"));
		patterns.add(new DatePattern("Dez. (\\d\\d\\d\\d)","01.12.$1"));
		patterns.add(new DatePattern("unbekannt","01.01.1900"));
		patterns.add(new DatePattern("unbek.","01.01.1900"));		
		patterns.add(new DatePattern("Anfg.(\\d\\d\\d\\d)er","01.01.$1"));
		patterns.add(new DatePattern("um (\\d\\d\\d\\d)-\\d\\d","01.01.$1"));
		patterns.add(new DatePattern("um (\\d\\d\\d\\d)","01.01.$1"));		
		patterns.add(new DatePattern("vor (\\d\\d\\d\\d)","01.01.$1"));
		patterns.add(new DatePattern("nach (\\d\\d\\d\\d)","01.01.$1"));
		patterns.add(new DatePattern("etwa (\\d\\d\\d\\d)","01.01.$1"));
		patterns.add(new DatePattern("Anfang (\\d\\d\\d\\d)er Jahre","01.01.$1"));
		patterns.add(new DatePattern("\\[um (\\d\\d\\d\\d)\\]","01.01.$1"));	
		patterns.add(new DatePattern("vielleicht (\\d\\d\\d\\d)","01.01.$1"));
		patterns.add(new DatePattern("vermutlich (\\d\\d\\d\\d)","01.01.$1"));
		patterns.add(new DatePattern("Herbst (\\d\\d\\d\\d)","01.09.$1"));
		patterns.add(new DatePattern("Frühjahr (\\d\\d\\d\\d)","01.03.$1"));
		patterns.add(new DatePattern("Frühling (\\d\\d\\d\\d)","01.03.$1"));
		patterns.add(new DatePattern("Sommer (\\d\\d\\d\\d)","01.06.$1"));
		patterns.add(new DatePattern("Winter (\\d\\d\\d\\d)","01.12.$1"));
		patterns.add(new DatePattern("(\\d\\d)/(\\d\\d\\d\\d)","01.$1.$2"));
		patterns.add(new DatePattern("(\\d\\d)\\.(\\d\\d\\d\\d)","01.$1.$2"));
		patterns.add(new DatePattern("(\\d\\d\\d\\d)/\\d\\d\\d\\d","01.01.$1"));
		patterns.add(new DatePattern("(\\d\\d\\d\\d)/\\d\\d","01.01.$1"));
		
		sortPatterns = new ArrayList<DatePattern>();
		sortPatterns.add(new DatePattern("(\\d\\d\\d\\d)","$1"));
		sortPatterns.add(new DatePattern("(\\d\\d)/(\\d\\d\\d\\d)","$2-$1"));
		sortPatterns.add(new DatePattern("(\\d\\d)\\.(\\d\\d)\\.(\\d\\d\\d\\d)","$3-$2-$1"));
		sortPatterns.add(new DatePattern("(\\d\\d)\\.(\\d\\d\\d\\d)","$2-$1"));
		
		questionmarkReplacements = new HashMap<String, String>();
		questionmarkReplacements.put(" ¿ ", " - "); 
		questionmarkReplacements.put(" ¿", " \""); 
		questionmarkReplacements.put("¿ ", "\" ");
		questionmarkReplacements.put("¿,", "\",");
		questionmarkReplacements.put("¿.", "\".");
		questionmarkReplacements.put(".¿", ".\"");
		questionmarkReplacements.put("(¿", "(¿");
		questionmarkReplacements.put("n¿y", "n'y");
		questionmarkReplacements.put("¿)", "\")");
		questionmarkReplacements.put("!¿", "!\"");
		questionmarkReplacements.put("\"¿¿¿\"", "……………");
		questionmarkReplacements.put("Denkzettel¿", "Denkzettel\"");
		questionmarkReplacements.put("[¿]", "[...]");
		questionmarkReplacements.put("¿-", "\"-");
		questionmarkReplacements.put(">¿", ">\"");
		questionmarkReplacements.put("¿:", "\":");
		questionmarkReplacements.put("í¿á", "ížá");	
		
		String patternString = "%(" + StringUtils.join(questionmarkReplacements.keySet(), "|") + ")%";
		questionmarkPattern = Pattern.compile(patternString);
		
	}
	
	public static String normalizeDate(String datum) {
		try {
			datum = datum.trim().replace("(", "").replace(")","");
			if (! datum.isEmpty()) {								
				for(DatePattern pat : patterns) {
					if (pat.match(datum)) {
						return pat.getResult();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Normalize Date: " + datum + " :: " + e.getMessage());			
		}
		return "";
	}
	
	public static String sortableDate(String datum) {
		try {
			if (! datum.isEmpty()) {
				int pos = datum.indexOf(";");			
				datum = (pos > 0)?datum.substring(0, pos):datum;
				for(DatePattern pat : sortPatterns) {
					if (pat.match(datum)) {
						return pat.getResult();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Sortable Date: " + datum + " :: " + e.getMessage());
		}
		return "";
	}
	
	public static String timestamp() {
		Calendar date = Calendar.getInstance();
		return date.get(Calendar.YEAR) + "-" + date.get(Calendar.MONTH) + "-" + date.get(Calendar.DAY_OF_MONTH) + "-" + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
	}	
		
	public static String replaceReverseQuestionmark(String src) {		
		
		Matcher matcher = questionmarkPattern.matcher(src);
	
		StringBuffer sb = new StringBuffer();
		while(matcher.find()) {
		    matcher.appendReplacement(sb, questionmarkReplacements.get(matcher.group(1)));
		}
		matcher.appendTail(sb);
	
		return sb.toString();
	}

}


