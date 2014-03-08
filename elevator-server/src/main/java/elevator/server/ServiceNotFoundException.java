package elevator.server;

/**
 * Created by jojo on 04/03/2014.
 */
public class ServiceNotFoundException extends RuntimeException {
	public ServiceNotFoundException() {
	}

	public ServiceNotFoundException(String message) {
		super(message);
	}

	public ServiceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceNotFoundException(Throwable cause) {
		super(cause);
	}

	public ServiceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
