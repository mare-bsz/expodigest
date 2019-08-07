package bsz.expo.trafo;

/**
 * Ein TrafoException ist eine Exception im der SwbTrafo-Anwendnung
 * <p>
 * Die TrafoException dient lediglich zur Vereinheitlich unterschiedlicher intern geworfener Exceptions.
 * 
 * @author Christof Mainberger *
 */
public class TrafoException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/** @param exception Packt eine gefangene Exception ein. */
	public TrafoException(Exception exception) {
		super(exception);
	}
	
	/** @param message erzeugt eine TrafoException mit einer Mitteilung */
	public TrafoException(String message) {
		super(message);
	}
	
	/** @param exception Packt eine gefangene Exception ein. */
	public TrafoException(String message, Exception exception) {
		super(message, exception);
	}
	
}
