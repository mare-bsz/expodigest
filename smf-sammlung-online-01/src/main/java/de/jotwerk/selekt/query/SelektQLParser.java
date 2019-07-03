// Generated from src/main/java/de/jotwerk/supply/query/SupplyQL.g4 by ANTLR 4.7.1
package de.jotwerk.selekt.query;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SelektQLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, STRING=12, ZAHL=13, INDEX=14, WS=15;
	public static final int
		RULE_anfrage = 0, RULE_klausel = 1, RULE_index = 2;
	public static final String[] ruleNames = {
		"anfrage", "klausel", "index"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'and'", "'or'", "'not'", "'('", "')'", "'any'", "'all'", "'adj'", 
		"'eq'", "'le'", "'gt'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		"STRING", "ZAHL", "INDEX", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "SupplyQL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SelektQLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class AnfrageContext extends ParserRuleContext {
		public AnfrageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anfrage; }
	 
		public AnfrageContext() { }
		public void copyFrom(AnfrageContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NotContext extends AnfrageContext {
		public AnfrageContext anfrage() {
			return getRuleContext(AnfrageContext.class,0);
		}
		public KlauselContext klausel() {
			return getRuleContext(KlauselContext.class,0);
		}
		public NotContext(AnfrageContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SelektQLVisitor ) return ((SelektQLVisitor<? extends T>)visitor).visitNot(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrContext extends AnfrageContext {
		public AnfrageContext anfrage() {
			return getRuleContext(AnfrageContext.class,0);
		}
		public KlauselContext klausel() {
			return getRuleContext(KlauselContext.class,0);
		}
		public OrContext(AnfrageContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SelektQLVisitor ) return ((SelektQLVisitor<? extends T>)visitor).visitOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndContext extends AnfrageContext {
		public AnfrageContext anfrage() {
			return getRuleContext(AnfrageContext.class,0);
		}
		public KlauselContext klausel() {
			return getRuleContext(KlauselContext.class,0);
		}
		public AndContext(AnfrageContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SelektQLVisitor ) return ((SelektQLVisitor<? extends T>)visitor).visitAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SimpleContext extends AnfrageContext {
		public KlauselContext klausel() {
			return getRuleContext(KlauselContext.class,0);
		}
		public SimpleContext(AnfrageContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SelektQLVisitor ) return ((SelektQLVisitor<? extends T>)visitor).visitSimple(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnfrageContext anfrage() throws RecognitionException {
		return anfrage(0);
	}

	private AnfrageContext anfrage(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		AnfrageContext _localctx = new AnfrageContext(_ctx, _parentState);
		AnfrageContext _prevctx = _localctx;
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_anfrage, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			_localctx = new SimpleContext(_localctx);
			_ctx = _localctx;
			_prevctx = _localctx;

			setState(7);
			klausel();
			}
			_ctx.stop = _input.LT(-1);
			setState(20);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(18);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
					case 1:
						{
						_localctx = new AndContext(new AnfrageContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_anfrage);
						setState(9);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(10);
						match(T__0);
						setState(11);
						klausel();
						}
						break;
					case 2:
						{
						_localctx = new OrContext(new AnfrageContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_anfrage);
						setState(12);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(13);
						match(T__1);
						setState(14);
						klausel();
						}
						break;
					case 3:
						{
						_localctx = new NotContext(new AnfrageContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_anfrage);
						setState(15);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(16);
						match(T__2);
						setState(17);
						klausel();
						}
						break;
					}
					} 
				}
				setState(22);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class KlauselContext extends ParserRuleContext {
		public KlauselContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_klausel; }
	 
		public KlauselContext() { }
		public void copyFrom(KlauselContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class AllContext extends KlauselContext {
		public IndexContext index() {
			return getRuleContext(IndexContext.class,0);
		}
		public TerminalNode STRING() { return getToken(SelektQLParser.STRING, 0); }
		public AllContext(KlauselContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SelektQLVisitor ) return ((SelektQLVisitor<? extends T>)visitor).visitAll(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class KlammerContext extends KlauselContext {
		public AnfrageContext anfrage() {
			return getRuleContext(AnfrageContext.class,0);
		}
		public KlammerContext(KlauselContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SelektQLVisitor ) return ((SelektQLVisitor<? extends T>)visitor).visitKlammer(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AdjContext extends KlauselContext {
		public IndexContext index() {
			return getRuleContext(IndexContext.class,0);
		}
		public TerminalNode STRING() { return getToken(SelektQLParser.STRING, 0); }
		public AdjContext(KlauselContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SelektQLVisitor ) return ((SelektQLVisitor<? extends T>)visitor).visitAdj(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LeContext extends KlauselContext {
		public IndexContext index() {
			return getRuleContext(IndexContext.class,0);
		}
		public TerminalNode ZAHL() { return getToken(SelektQLParser.ZAHL, 0); }
		public LeContext(KlauselContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SelektQLVisitor ) return ((SelektQLVisitor<? extends T>)visitor).visitLe(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EqContext extends KlauselContext {
		public IndexContext index() {
			return getRuleContext(IndexContext.class,0);
		}
		public TerminalNode ZAHL() { return getToken(SelektQLParser.ZAHL, 0); }
		public EqContext(KlauselContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SelektQLVisitor ) return ((SelektQLVisitor<? extends T>)visitor).visitEq(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AnyContext extends KlauselContext {
		public IndexContext index() {
			return getRuleContext(IndexContext.class,0);
		}
		public TerminalNode STRING() { return getToken(SelektQLParser.STRING, 0); }
		public AnyContext(KlauselContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SelektQLVisitor ) return ((SelektQLVisitor<? extends T>)visitor).visitAny(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GtContext extends KlauselContext {
		public IndexContext index() {
			return getRuleContext(IndexContext.class,0);
		}
		public TerminalNode ZAHL() { return getToken(SelektQLParser.ZAHL, 0); }
		public GtContext(KlauselContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SelektQLVisitor ) return ((SelektQLVisitor<? extends T>)visitor).visitGt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KlauselContext klausel() throws RecognitionException {
		KlauselContext _localctx = new KlauselContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_klausel);
		try {
			setState(51);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				_localctx = new KlammerContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(23);
				match(T__3);
				setState(24);
				anfrage(0);
				setState(25);
				match(T__4);
				}
				break;
			case 2:
				_localctx = new AnyContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(27);
				index();
				setState(28);
				match(T__5);
				setState(29);
				match(STRING);
				}
				break;
			case 3:
				_localctx = new AllContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(31);
				index();
				setState(32);
				match(T__6);
				setState(33);
				match(STRING);
				}
				break;
			case 4:
				_localctx = new AdjContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(35);
				index();
				setState(36);
				match(T__7);
				setState(37);
				match(STRING);
				}
				break;
			case 5:
				_localctx = new EqContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(39);
				index();
				setState(40);
				match(T__8);
				setState(41);
				match(ZAHL);
				}
				break;
			case 6:
				_localctx = new LeContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(43);
				index();
				setState(44);
				match(T__9);
				setState(45);
				match(ZAHL);
				}
				break;
			case 7:
				_localctx = new GtContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(47);
				index();
				setState(48);
				match(T__10);
				setState(49);
				match(ZAHL);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IndexContext extends ParserRuleContext {
		public TerminalNode INDEX() { return getToken(SelektQLParser.INDEX, 0); }
		public IndexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_index; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SelektQLVisitor ) return ((SelektQLVisitor<? extends T>)visitor).visitIndex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IndexContext index() throws RecognitionException {
		IndexContext _localctx = new IndexContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_index);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(53);
			match(INDEX);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0:
			return anfrage_sempred((AnfrageContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean anfrage_sempred(AnfrageContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 3);
		case 2:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\21:\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7\2\25\n\2"+
		"\f\2\16\2\30\13\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\66\n"+
		"\3\3\4\3\4\3\4\2\3\2\5\2\4\6\2\2\2?\2\b\3\2\2\2\4\65\3\2\2\2\6\67\3\2"+
		"\2\2\b\t\b\2\1\2\t\n\5\4\3\2\n\26\3\2\2\2\13\f\f\6\2\2\f\r\7\3\2\2\r\25"+
		"\5\4\3\2\16\17\f\5\2\2\17\20\7\4\2\2\20\25\5\4\3\2\21\22\f\4\2\2\22\23"+
		"\7\5\2\2\23\25\5\4\3\2\24\13\3\2\2\2\24\16\3\2\2\2\24\21\3\2\2\2\25\30"+
		"\3\2\2\2\26\24\3\2\2\2\26\27\3\2\2\2\27\3\3\2\2\2\30\26\3\2\2\2\31\32"+
		"\7\6\2\2\32\33\5\2\2\2\33\34\7\7\2\2\34\66\3\2\2\2\35\36\5\6\4\2\36\37"+
		"\7\b\2\2\37 \7\16\2\2 \66\3\2\2\2!\"\5\6\4\2\"#\7\t\2\2#$\7\16\2\2$\66"+
		"\3\2\2\2%&\5\6\4\2&\'\7\n\2\2\'(\7\16\2\2(\66\3\2\2\2)*\5\6\4\2*+\7\13"+
		"\2\2+,\7\17\2\2,\66\3\2\2\2-.\5\6\4\2./\7\f\2\2/\60\7\17\2\2\60\66\3\2"+
		"\2\2\61\62\5\6\4\2\62\63\7\r\2\2\63\64\7\17\2\2\64\66\3\2\2\2\65\31\3"+
		"\2\2\2\65\35\3\2\2\2\65!\3\2\2\2\65%\3\2\2\2\65)\3\2\2\2\65-\3\2\2\2\65"+
		"\61\3\2\2\2\66\5\3\2\2\2\678\7\20\2\28\7\3\2\2\2\5\24\26\65";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}