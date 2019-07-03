package de.jotwerk.selekt.query;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class SelektQLErrorListener extends BaseErrorListener {
	
	public boolean error = false;
	public String message = "";
	
	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, 
			Object offendingSymbol, 
			int line, 
			int charPositionInLine,
			String msg, 
			RecognitionException e) {
		error = true;
		message = msg;
	}

}
