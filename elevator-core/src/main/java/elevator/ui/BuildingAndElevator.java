package elevator.ui;

import elevator.Building;
import elevator.engine.ElevatorEngine;

class BuildingAndElevator {

    final Building building;
    final ElevatorEngine elevator;

    BuildingAndElevator(Building building, ElevatorEngine elevator) {
        this.building = building;
        this.elevator = elevator;
    }

    Building building() {
        return building;
    }
}
