package bsz.expo.trafo.pipes;

import java.io.IOException;

import bsz.expo.trafo.TrafoException;
import bsz.expo.trafo.TrafoTicket;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Nodes;

public class CsvWriterPipe extends DownloadPipe {
	
	boolean headerLine = false;
	String currentType;
	char separator;
	char delimiter;	
	String zeilenende = null; 
	
	public void init() throws TrafoException {
		
		setMimetype("text/csv");
				
		if (getParameter("filename") != null) {
			setFilename(getParameter("filename").replace("%d", getCurrentDate()));
		} else {
			setFilename("download.txt");
		}
		setEncoding(getParameter("encoding") != null ? getParameter("encoding") : "UTF8");
		super.init();			
		if (getParameter("separator") != null) {
			if ("tab".equals(getParameter("separator"))) {
				separator = '\t';
			} else {
				separator = getParameter("separator").charAt(0);
			}
		} else {
			separator = ',';
		}
		if (getParameter("delimiter") != null) {
			if ("quote".equals(getParameter("delimiter"))) {
				delimiter = '\"';
			} else if ("none".equals(getParameter("delimiter"))) {
				delimiter = '\0';
			} else {
				delimiter = getParameter("delimiter").charAt(0);
			}
		} else {
			delimiter = '\"';
		}
		if ("true".equals(getParameter("headerline"))) {
			headerLine = true;
		}
		if (getParameter("zeilenende") != null) {
			switch (getParameter("zeilenende")) {
				case "CRLF": zeilenende = "\r\n"; break;
				case "LF": zeilenende = "\n"; break;
				default : zeilenende = "\n";
			}
		} else {
			zeilenende = "\n";
		}
		
	}
	
	@Override
	public void process(TrafoTicket ticket) throws TrafoException {
		try {
			final Nodes fields = ticket.getDocument().query("/docs/doc/field");
			if (fields.size() > 0) {
				if (headerLine) {
					for (int i = 0; i < fields.size(); i++) {
						if (i > 0) {
							wrt.write(separator);
						}
						wrt.write(((Element) fields.get(i)).getAttribute("name").getValue());
					}
					wrt.write(zeilenende);
				}		
				headerLine = false;				
				for (int i = 0; i < fields.size(); i++) {
					if (i > 0) {
						wrt.write(separator);
					}
					final Element field = (Element) fields.get(i);	
					final Attribute typeAtt = field.getAttribute("type");
					wrt.write(prepareCSV(field.getValue(), (typeAtt != null ? typeAtt.getValue() : "string")));
				}
				wrt.write(zeilenende);
			}
		} catch (IOException e) {
			throw new TrafoException(e);
		}
		super.process(ticket);
	}
	
	@Override
	public void finit() throws TrafoException {
		try {
			wrt.flush();
			wrt.close();
		} catch (IOException e) {
			throw new TrafoException(e);
		}
		super.finit();
	}
	
	private String prepareCSV(String value, String type) {
		try {
			value = value.replaceAll("&quot;", "\"");
			value = value.replaceAll("&apos;", "'");
			value = value.replaceAll("&gt;",">");
			value = value.replaceAll("&lt;", "<");
			value = value.replaceAll("&amp;", "&");
			if ("string".equals(type)) {
				StringBuilder result = new StringBuilder();
				if (delimiter != '\0') {
					result.append(delimiter);
				}
				for (char c : value.toCharArray()) {
					if (delimiter != '\0' && c == delimiter) { 
						result.append(" ");
					} else if (c == separator) {
						result.append(" ");
					} else {
						switch (c) {
						case '\'':
							result.append(' ');
							break;
						case '\n':
							result.append(' ');
							break;
						case '\r':
							result.append(' ');
						default:
							result.append(c);
						}
					}
				}
				if (delimiter != '\0') {
					result.append(delimiter);
				}				
				return result.toString();
			} else {
				return value.trim();
			}
		} catch (Exception e) {
			System.out.println(value + " :: " + e.getMessage());
		}
		return "";
	}	
	
}
