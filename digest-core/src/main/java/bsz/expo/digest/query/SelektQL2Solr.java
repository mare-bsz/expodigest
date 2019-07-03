package bsz.expo.digest.query;

import bsz.expo.digest.query.SelektQLParser.AdjContext;
import bsz.expo.digest.query.SelektQLParser.AllContext;
import bsz.expo.digest.query.SelektQLParser.AnyContext;
import bsz.expo.digest.query.SelektQLParser.EqContext;
import bsz.expo.digest.query.SelektQLParser.GtContext;
import bsz.expo.digest.query.SelektQLParser.IndexContext;
import bsz.expo.digest.query.SelektQLParser.KlammerContext;
import bsz.expo.digest.query.SelektQLParser.LeContext;

public class SelektQL2Solr extends SelektQLBaseVisitor<String> {
	
	final String indexe;
	
	public SelektQL2Solr(String indexe) {
		this.indexe = indexe;
	}
	
	SelektQLErrorListener errorListener;
	public void setSupplyQLErrorListener(SelektQLErrorListener errorListener ) { this.errorListener = errorListener; }

	@Override public String visitNot(SelektQLParser.NotContext ctx) { 
		return "(" + visit(ctx.anfrage()) + ") AND NOT (" + visit(ctx.klausel())  + ")"; 
	}
	
	@Override public String visitOr(SelektQLParser.OrContext ctx) { 
		return "(" + visit(ctx.anfrage()) + ") OR (" + visit(ctx.klausel()) + ")";  
	}
	 
	@Override public String visitAnd(SelektQLParser.AndContext ctx) { 
		return "(" + visit(ctx.anfrage()) + ") AND (" + visit(ctx.klausel()) + ")"; 
	}
	 
	@Override public String visitSimple(SelektQLParser.SimpleContext ctx) { 
		return visit(ctx.klausel()); 
	}	
	
	@Override
	public String visitKlammer(KlammerContext ctx) {
		return visit(ctx.anfrage());
	}

	@Override
	public String visitAny(AnyContext ctx) {
		return fold(stripQuotes(ctx.STRING().getText()).split(" "), visit(ctx.index()), " OR ");
	}

	@Override
	public String visitAll(AllContext ctx) {
		return fold(stripQuotes(ctx.STRING().getText()).split(" "), visit(ctx.index()), " AND ");
	}

	@Override
	public String visitAdj(AdjContext ctx) {
		return visit(ctx.index()) + ":\"" + stripQuotes(ctx.STRING().getText()) + "\"";		
	}

	@Override
	public String visitEq(EqContext ctx) {
		return visit(ctx.index()) + " : " + ctx.ZAHL();
	}

	@Override
	public String visitLe(LeContext ctx) {
		return visit(ctx.index()) + "[* TO " + ctx.ZAHL() + "]" ;
	}

	@Override
	public String visitGt(GtContext ctx) {
		return visit(ctx.index()) + "[" + ctx.ZAHL() + " TO *]" ;
	}
	
	@Override
	public String visitIndex(IndexContext ctx) {
		if (indexe.contains(ctx.INDEX().getText())) {
			return ("text".equals(ctx.INDEX().getText()) ? "_text_" : ("f_" + ctx.INDEX().getText()));
		} else {
			this.errorListener.error = true;
			this.errorListener.message = "Index " + ctx.INDEX().getText() + " ist nicht in der Indexliste enthalten: " + this.indexe;
			return "";
		}
	}

	private String fold(String[] terme, String index, String op) {
		final StringBuilder result = new StringBuilder();
		for (String term : terme) {
			if (result.length() > 0) {
				result.append(op);
			}
			result.append("(" + index + " : " + term + ")");
		}
		return result.toString();
	}
	
	private String stripQuotes(final String src) {
		return src.substring(1, src.length()-1);
	}

}
