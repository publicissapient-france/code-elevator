package elevator.ui;

import elevator.Building;
import elevator.Direction;
import elevator.engine.ElevatorEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayDeque;
import java.util.Deque;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.Door.OPEN;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;

public class InteractionPanel extends JPanel {

    private final Deque<JLabel> elevatorStack;
    private final Building building;

    InteractionPanel(Building building, ElevatorEngine elevatorEngine) {
        this.building = building;
        GridLayout layout = new GridLayout(0, 3);
        setLayout(layout);

        elevatorStack = new ArrayDeque<>(HIGHER_FLOOR);

        for (int i = HIGHER_FLOOR; i >= LOWER_FLOOR; i--) {
            if (i != HIGHER_FLOOR) {
                add(new JButton(new CallElevatorAction(elevatorEngine, i, UP)));
            } else {
                add(new JLabel());
            }
            if (i != LOWER_FLOOR) {
                add(new JButton(new CallElevatorAction(elevatorEngine, i, DOWN)));
            } else {
                add(new JLabel());
            }
            elevatorStack.addFirst(new JLabel(String.valueOf(i)));
            add(elevatorStack.getFirst());
            if (i == LOWER_FLOOR) {
                elevatorStack.getFirst().setText("[ | ]");
            }
        }
    }

    public InteractionPanel update() {
        Integer i = 0;
        for (JLabel jLabel : elevatorStack) {
            if (building.floor().equals(i)) {
                jLabel.setText(building.door().equals(OPEN) ? "[| |]" : "[ | ]");
            } else {
                jLabel.setText(i.toString());
            }
            i++;
        }
        return this;
    }

    private static class CallElevatorAction extends AbstractAction {

        private final ElevatorEngine elevatorEngine;
        private final int currentFloor;
        private final Direction direction;

        public CallElevatorAction(ElevatorEngine elevatorEngine, int currentFloor, Direction direction) {
            this.elevatorEngine = elevatorEngine;
            this.currentFloor = currentFloor;
            this.direction = direction;
            this.putValue(NAME, direction.toString());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            elevatorEngine.call(currentFloor, direction);
        }

    }

}
