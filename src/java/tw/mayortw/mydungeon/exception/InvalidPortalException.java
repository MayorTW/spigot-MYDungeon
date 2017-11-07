package tw.mayortw.mydungeon.exception;

public class InvalidPortalException extends RuntimeException {	
	
	private static final long serialVersionUID = 2779394547885109337L;

	public InvalidPortalException () {
		super();
	}
	
	public InvalidPortalException (String msg) {
		super(msg);
	}
	
	public InvalidPortalException (Throwable cause) {
		super(cause);
	}
	
	public InvalidPortalException (String msg, Throwable cause) {
		super(msg, cause);
	}
}
