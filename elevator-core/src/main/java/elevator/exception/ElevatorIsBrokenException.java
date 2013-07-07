package elevator.exception;

public class ElevatorIsBrokenException extends RuntimeException {

    public ElevatorIsBrokenException(String message) {
        super(message);
    }

}