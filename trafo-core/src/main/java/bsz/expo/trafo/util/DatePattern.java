package bsz.expo.trafo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatePattern {
	
		protected String result;
		protected String replacement;
		protected String regexp;
		Pattern pattern;
		
		public DatePattern(String regexp, String replacement) {
			this.regexp = regexp;
			this.pattern = Pattern.compile("^" + regexp + "$");
			this.replacement = replacement;
		}
				
		public String getResult() { return result; }
		
		public boolean match(String datum) {
			Matcher matcher = pattern.matcher(datum);
			if (matcher.matches()) {
				result = matcher.replaceFirst(replacement);
				return true;
			}
			return false;
		}
		
		
		
}
