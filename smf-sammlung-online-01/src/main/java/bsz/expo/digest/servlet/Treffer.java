package bsz.expo.digest.servlet;

import java.util.List;

public class Treffer {
	
	final String id;
	final String header;
	final String detail;
	final List<String> images;
	public Treffer(final String id, final String header, final String detail, final List<String> images) {
		this.id = id;
		this.header = header;
		this.detail = detail;
		this.images = images;
	}
	
	public String getId() { return id; }
	public String getHeader() { return header; }
	public String getDetail() { return detail;}
	public List<String> getImages() { return images; }

}
