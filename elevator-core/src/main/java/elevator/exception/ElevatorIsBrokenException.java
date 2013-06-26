package elevator.exception;

import java.util.List;

public class ElevatorIsBrokenException extends RuntimeException {

    public ElevatorIsBrokenException(String message) {
        super(message);
    }

    public ElevatorIsBrokenException(List<String> transportErrorMessages) {
        super(getMessages(transportErrorMessages));
    }

    private static String getMessages(List<String> transportErrorMessages) {
        StringBuilder messages = new StringBuilder();

        for (String message : transportErrorMessages) {
            if (messages.length() > 0) {
                messages.append(", ");
            }
            messages.append(message);
        }
        transportErrorMessages.clear();

        return messages.toString();
    }
}
