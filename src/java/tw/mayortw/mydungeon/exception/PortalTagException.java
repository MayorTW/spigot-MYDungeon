package tw.mayortw.mydungeon.exception;

public class PortalTagException extends RuntimeException {
	private static final long serialVersionUID = 7696124331375819318L;
	
	public PortalTagException () {
		super();
	}
	
	public PortalTagException (String msg) {
		super(msg);
	}
	
	public PortalTagException (Throwable cause) {
		super(cause);
	}
	
	public PortalTagException (String msg, Throwable cause) {
		super(msg, cause);
	}
}
