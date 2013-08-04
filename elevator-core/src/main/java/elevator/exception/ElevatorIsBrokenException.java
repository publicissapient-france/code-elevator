package elevator.exception;

public class ElevatorIsBrokenException extends RuntimeException {

    private static final long serialVersionUID = 3904354772651327178L;

    public ElevatorIsBrokenException(String message) {
        super(message);
    }

}