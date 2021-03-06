package bsz.expo;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;

import org.apache.solr.common.SolrDocument;

public class Util {
	
	public static String toJson(final String src) {
		if (src == null || src.isEmpty()) {
			return "";
		} else {
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
	
	public static String toXML(final String src) {
		StringBuilder result = new StringBuilder();
		for (char c : src.trim().toCharArray()) {
			switch (c) {
				case '"': result.append("&quot;"); break;
				case '<': result.append("&lt;"); break;
				case '>': result.append("&gt;"); break;
				case '&' : result.append("&amp;"); break;
				case '\'' : result.append("&apo;"); break;
				default: result.append(c);
			}			
		}
		return result.toString();
	}
	
	public static String extendUml(String src) {
		StringBuilder trg = new StringBuilder();
		for (char c : src.toCharArray()) {
			switch (c) {
			case 'Ä':
			case 'ä':
				trg.append("ae");
				break;
			case 'Ü':
			case 'ü':
				trg.append("ue");
				break;
			case 'Ö':
			case 'ö':
				trg.append("oe");
				break;				
			case 'ß':
				trg.append("ss");
				break;			
			case '"':
			case '\'':
				break;
			default:
				trg.append(Character.toLowerCase(c));
			}			
		}
		System.out.println(src + " :: " + trg.toString());
		return trg.toString();
	}
	
	public static String extractYear(String datum) {
		datum = datum.trim().replace("(", "").replace(")","");
		if (! datum.isEmpty()) {
			List<Pattern> patterns = new ArrayList<Pattern>();
			patterns.add(Pattern.compile("\\d{1,2}\\.\\d{1,2}\\.(\\d{4}|\\d{2})")); 
			patterns.add(Pattern.compile("\\d{1,2}\\.\\d{1,2}\\.(\\d{4}|\\d{2}) Taufe"));//
			patterns.add(Pattern.compile("\\d{1,2}/\\d{1,2}\\.\\d{1,2}.(\\d{4})")); 
			patterns.add(Pattern.compile("\\d{1,2}\\.\\d{1,2}\\./\\d{1,2}\\.\\d{1,2}\\.(\\d{4})")); 
			patterns.add(Pattern.compile("(\\d{4})"));							//
			patterns.add(Pattern.compile("(\\d{4})/\\d{1,2}"));						//
			patterns.add(Pattern.compile("\\d{1,2}\\.(\\d{4})"));						//
			patterns.add(Pattern.compile("\\d{1,2}\\./\\d{1,2}\\.\\d{1,2}\\.(\\d{4})"));		//
			patterns.add(Pattern.compile("(vor )\\d{1,2}\\.\\d{1,2}\\.(\\d{4})"));		//
			patterns.add(Pattern.compile("(vor \\d{4})"));						//
			patterns.add(Pattern.compile("(vor \\d{4})\\.\\d{1,2}\\.\\d{1,2}"));						//
			patterns.add(Pattern.compile("(vor )\\d{1,2}\\.\\d{1,2}\\.(\\d{4})"));						//
			patterns.add(Pattern.compile("(vor \\d{4}) \\?"));	  				//
			patterns.add(Pattern.compile("(um )(\\d{4})"));
			patterns.add(Pattern.compile("(um \\d{4}-\\d{4})"));	
			patterns.add(Pattern.compile("(um )\\d{1,2}\\.\\d{1,2}\\.(\\d{4})"));			//	
			patterns.add(Pattern.compile("(nach )(\\d{4})"));						//
			patterns.add(Pattern.compile("(nach )\\d{1,2}.\\d{1,2}.(\\d{4})"));		//
			patterns.add(Pattern.compile("(\\d{4})( \\?)"));
			patterns.add(Pattern.compile("(\\d{4})(\\?)"));//
			patterns.add(Pattern.compile("(\\d{4}/\\d{4})"));				//
			patterns.add(Pattern.compile("\\d{1,2}.(\\d{4})/\\d{4}"));		//
			patterns.add(Pattern.compile("\\d{1,2}.\\d{1,2}.(\\d{4})/\\d{4}"));//
			patterns.add(Pattern.compile("\\d{4}/\\d{1,2}.\\d{1,2}.(\\d{4})"));//
			patterns.add(Pattern.compile("(um \\d{4}/\\d{4})"));			 //
			patterns.add(Pattern.compile("(um \\d{4}/\\d{1,2})"));			
			patterns.add(Pattern.compile("\\d{1,2}..od..\\d{1,2}.\\d{1,2}.(\\d{4})"));	//
			patterns.add(Pattern.compile("\\d{1,2}.\\d{1,2}.(\\d{4})./.\\d{4}"));	
			patterns.add(Pattern.compile("Januar (\\d{4})"));
			patterns.add(Pattern.compile("Februar (\\d{4})"));
			patterns.add(Pattern.compile("März (\\d{4})"));
			patterns.add(Pattern.compile("April (\\d{4})"));
			patterns.add(Pattern.compile("Mai (\\d{4})"));
			patterns.add(Pattern.compile("Juni (\\d{4})"));
			patterns.add(Pattern.compile("Juli (\\d{4})"));
			patterns.add(Pattern.compile("August (\\d{4})"));
			patterns.add(Pattern.compile("September (\\d{4})"));
			patterns.add(Pattern.compile("Oktober (\\d{4})"));
			patterns.add(Pattern.compile("November (\\d{4})"));	
			patterns.add(Pattern.compile("Dezember (\\d{4})"));
			patterns.add(Pattern.compile("\\d{2}\\.(\\d{4})/\\d{2}\\.\\d{4}"));
			patterns.add(Pattern.compile("\\d{2}\\.\\d{2}\\./\\d{2}\\.(\\d{4})"));
			patterns.add(Pattern.compile("\\d{2}\\.\\d{2}\\.(\\d{4})/\\d{2}"));
			patterns.add(Pattern.compile("\\d{2}\\. oder \\d{2}\\.\\d{2}\\.(\\d{4})"));
			patterns.add(Pattern.compile("\\d{2}\\.\\d{2}\\.(\\d{4}) oder \\d{4}"));
			patterns.add(Pattern.compile("\\d{2}\\.\\d{2}\\. oder \\d{2}\\.\\d{2}\\.(\\d{4})"));
			patterns.add(Pattern.compile("zw. (\\d{4}) und \\d{4}"));
			patterns.add(Pattern.compile("(\\d{4})/\\d{4}/um \\d{4}"));
			patterns.add(Pattern.compile("(\\d{4}) oder um \\d{4}/\\d{2}"));
			patterns.add(Pattern.compile("\\d{2}\\./\\d{2}\\.(\\d{4})"));
			patterns.add(Pattern.compile("(\\d{4}) oder um \\d{4}"));	
			patterns.add(Pattern.compile("\\d{2}\\.\\d{2}\\.(\\d{4})/\\d{4}")); 
			patterns.add(Pattern.compile("(\\d{4})/\\d{2}")); 
			patterns.add(Pattern.compile("\\d{4}/\\d{2}\\.\\d{2}\\.(\\d{4})"));
			
			for(Pattern pat : patterns) {
				Matcher match = pat.matcher(datum);
				if (match.matches()) {
					String result = "";
					for (int i = 1; i <= match.groupCount(); i++) {
						String jahr = match.group(i);
						if (jahr.length() == 2) {
							jahr = "20" + jahr;
						}
						result = result + jahr;
					}
					return result;
				}
			}
			System.out.println(datum);
		}
		return "";
	}
		
	public static String replaceReverseQuestionmarkJSON(String src) {
		return toJson(replaceReverseQuestionmark(src));
	}	
	
	public static String replaceReverseQuestionmark(String src) {
				
		Map<String, String> questionmarkReplacements = new HashMap<String, String>();
		questionmarkReplacements.put(" ¿ ", " - "); 
		questionmarkReplacements.put(" ¿", " \""); 
		questionmarkReplacements.put("¿ ", "\" ");
		questionmarkReplacements.put("¿,", "\",");
		questionmarkReplacements.put("¿.", "\".");
		questionmarkReplacements.put(".¿", ".\"");
		questionmarkReplacements.put("\\(¿", "\\(¿");
		questionmarkReplacements.put("n¿y", "n'y");
		questionmarkReplacements.put("¿\\)", "\"\\)");
		questionmarkReplacements.put("!¿", "!\"");
		questionmarkReplacements.put("\"¿¿¿\"", "……………");
		questionmarkReplacements.put("Denkzettel¿", "Denkzettel\"");
		questionmarkReplacements.put("[¿]", "[...]");
		questionmarkReplacements.put("¿-", "\"-");
		questionmarkReplacements.put(">¿", ">\"");
		questionmarkReplacements.put("¿:", "\":");
		questionmarkReplacements.put("í¿á", "ížá");	
		
		String patternString = "(>¿|Denkzettel¿|\\s¿|n¿y|í¿á|¿,|¿\\)|¿-|¿\\.|\\.¿|\\(¿|¿\\s|\\s¿\\s|[¿]|¿:|\"¿¿¿\"|!¿)";
				
		Pattern questionmarkPattern = Pattern.compile(patternString);
		
		Matcher matcher = questionmarkPattern.matcher(src);
	
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			
			String replacement = questionmarkReplacements.get(matcher.group(1));
			if (replacement == null) {
				replacement = matcher.group(1);
			}
			matcher.appendReplacement(sb, replacement);
		}
		matcher.appendTail(sb);	
	
		return sb.toString();
	}
	
	public static String exName(String src) {
		if (src != null && ! src.isEmpty()) {
			String[] words = src.split(" ");
			return "XXX"+ words[words.length-1].toUpperCase();
		} else {
			return "XXX";
		}
	}
	
	public static String rtfToHtml(String src) throws IOException {
		JEditorPane p = new JEditorPane();
		p.setContentType("text/rtf");
		EditorKit kitRtf = p.getEditorKitForContentType("text/rtf");
		StringReader rtf = new StringReader(src);
		try {
			kitRtf.read(rtf, p.getDocument(), 0);
			kitRtf = null;
			EditorKit kitHtml = p.getEditorKitForContentType("text/plain");
			Writer writer = new StringWriter();
			kitHtml.write(writer, p.getDocument(), 0, p.getDocument().getLength());
			return toJson(writer.toString());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getSolrValue(final Object doc, final String name) {
		try {
			return ((SolrDocument)doc).getFieldValue(name).toString();
		} catch (Exception e) {
			return "Fehler: SolrFeld " + name + " lässt sich nicht ermitteln.";
		}
	}

}
