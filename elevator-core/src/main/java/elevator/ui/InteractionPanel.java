package elevator.ui;

import elevator.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

import static elevator.Direction.DOWN;
import static elevator.Direction.UP;
import static elevator.Door.OPEN;
import static elevator.engine.ElevatorEngine.HIGHER_FLOOR;
import static elevator.engine.ElevatorEngine.LOWER_FLOOR;

public class InteractionPanel extends JPanel {

    private final Map<BuildingAndElevator, Deque<JLabel>> buildings;

    InteractionPanel(List<BuildingAndElevator> buildings) {
        this.buildings = new HashMap<>();
        GridLayout layout = new GridLayout(0, 2 + buildings.size());
        setLayout(layout);

        add(new JLabel());
        add(new JLabel());
        for (BuildingAndElevator building : buildings) {
            ArrayDeque<JLabel> elevatorStack = new ArrayDeque<>(HIGHER_FLOOR);
            this.buildings.put(building, elevatorStack);
            add(new JLabel(building.elevator.getClass().getSimpleName()));
        }

        for (int i = HIGHER_FLOOR; i >= LOWER_FLOOR; i--) {
            if (i != HIGHER_FLOOR) {
                add(new JButton(new CallElevatorAction(buildings, i, UP)));
            } else {
                add(new JLabel());
            }
            if (i != LOWER_FLOOR) {
                add(new JButton(new CallElevatorAction(buildings, i, DOWN)));
            } else {
                add(new JLabel());
            }

            for (BuildingAndElevator building : buildings) {
                this.buildings.get(building).addFirst(new JLabel(String.valueOf(i)));
                add(this.buildings.get(building).getFirst());
                if (i == LOWER_FLOOR) {
                    this.buildings.get(building).getFirst().setText("[ | ]");
                }
            }
        }
    }

    public InteractionPanel update() {
        for (Map.Entry<BuildingAndElevator, Deque<JLabel>> building : buildings.entrySet()) {
            Integer i = 0;

            for (JLabel jLabel : building.getValue()) {
                if (building.getKey().building.floor().equals(i)) {
                    jLabel.setText(building.getKey().building.door().equals(OPEN) ? "[| |]" : "[ | ]");
                } else {
                    jLabel.setText(i.toString());
                }
                i++;
            }
        }
        return this;
    }

    private static class CallElevatorAction extends AbstractAction {

        private final List<BuildingAndElevator> buildings;
        private final int currentFloor;
        private final Direction direction;

        public CallElevatorAction(List<BuildingAndElevator> buildings, int currentFloor, Direction direction) {
            this.buildings = buildings;
            this.currentFloor = currentFloor;
            this.direction = direction;
            this.putValue(NAME, direction.toString());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (BuildingAndElevator building : buildings) {
                building.elevator.call(currentFloor, direction);
            }
        }

    }

}
