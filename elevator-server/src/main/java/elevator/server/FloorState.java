package elevator.server;

public class FloorState {

    public final Integer floorNumber;
    public final Integer peopleWaitingTheElevator;
    public final Boolean upButtonState;
    public final Boolean downButtonState;
    public final Boolean floorButtonStateInElevator;

    public FloorState(Integer floor, Integer peopleWaitingTheElevator, Boolean upButtonState, Boolean downButtonState,
                      Boolean floorButtonStateInElevator) {
        this.floorNumber = floor;
        this.peopleWaitingTheElevator = peopleWaitingTheElevator;
        this.upButtonState = upButtonState;
        this.downButtonState = downButtonState;
        this.floorButtonStateInElevator = floorButtonStateInElevator;
    }

}
