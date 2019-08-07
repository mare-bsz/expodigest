package bsz.expo.trafo;

import org.xml.sax.helpers.DefaultHandler;

/**
 * Die abstrakte Klasse TrafoListener ermöglicht Seitenaspekte, wie Zählen, Loggen etc.
 *  
 * @author Christof Mainberger
 */
public abstract class TrafoListener extends DefaultHandler {
	
	protected TrafoPipe trafoPipe;
	protected TrafoPipeline trafoPipeline;	
	
	/**
	 * init initialisiert den Listener, während der Initalisierung der Pipeline.
	 * @param pipe die {@link TrafoPipe} bei der der Listener registriert ist.
	 * @param context der {@link TrafoContext}, in dem die zugehörige {@link TrafoPipeline} eingesetzt wird. 
	 */
	protected void init(final TrafoPipe pipe, final TrafoPipeline pipeline) {
		this.trafoPipe = pipe;
		this.trafoPipeline = pipeline;
	}
	
	/**
	 * process wird ausgeführt, wenn die {@link TrafoPipe}, bei der der Listener registriert ist, ausgeführt wird.
	 *  
	 * @param ticket das {@link TrafoTicket}, das aktuell verarbeitet wird.
	 */
	protected void process(final TrafoTicket ticket) {
		
	}	
	
	/**
	 * finit wird ausgeführt, wenn die {@link TrafoPipe}, bei der der Listener registriert ist, aufgeräumt wird.
	 */
	protected void finit()  {
		
	}

}
