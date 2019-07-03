// Generated from src/main/java/de/jotwerk/supply/query/SupplyQL.g4 by ANTLR 4.7.1
package de.jotwerk.selekt.query;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SelektQLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SelektQLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code Not}
	 * labeled alternative in {@link SelektQLParser#anfrage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(SelektQLParser.NotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Or}
	 * labeled alternative in {@link SelektQLParser#anfrage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(SelektQLParser.OrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code And}
	 * labeled alternative in {@link SelektQLParser#anfrage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(SelektQLParser.AndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Simple}
	 * labeled alternative in {@link SelektQLParser#anfrage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimple(SelektQLParser.SimpleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Klammer}
	 * labeled alternative in {@link SelektQLParser#klausel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKlammer(SelektQLParser.KlammerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Any}
	 * labeled alternative in {@link SelektQLParser#klausel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny(SelektQLParser.AnyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code All}
	 * labeled alternative in {@link SelektQLParser#klausel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAll(SelektQLParser.AllContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Adj}
	 * labeled alternative in {@link SelektQLParser#klausel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdj(SelektQLParser.AdjContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Eq}
	 * labeled alternative in {@link SelektQLParser#klausel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEq(SelektQLParser.EqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Le}
	 * labeled alternative in {@link SelektQLParser#klausel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLe(SelektQLParser.LeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Gt}
	 * labeled alternative in {@link SelektQLParser#klausel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGt(SelektQLParser.GtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SelektQLParser#index}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIndex(SelektQLParser.IndexContext ctx);
}