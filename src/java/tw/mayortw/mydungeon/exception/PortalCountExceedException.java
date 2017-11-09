package tw.mayortw.mydungeon.exception;

public class PortalCountExceedException extends RuntimeException{

	private static final long serialVersionUID = 7859380153723994054L;
	
	public PortalCountExceedException () {
		super("The max number of portals is reached.");
	}
	
	public PortalCountExceedException (int maxCountIn) {
		this(maxCountIn, "The max number of portals is reached.");
	}
	
	public PortalCountExceedException (int maxCountIn, String msg) {
		super(msg + " Max number of portals = " + maxCountIn);
	}
	
	public PortalCountExceedException (int maxCountIn, Throwable cause) {
		this(maxCountIn, "The max number of portals is reached.", cause);
	}
	
	public PortalCountExceedException (int maxCountIn, String msg, Throwable cause) {
		super(msg + " Max number of portals = " + maxCountIn, cause);
	}
}
