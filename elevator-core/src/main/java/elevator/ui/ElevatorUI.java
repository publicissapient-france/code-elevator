package elevator.ui;

import elevator.Building;
import elevator.engine.ElevatorEngine;
import elevator.user.ConstantMaxNumberOfUsers;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.awt.BorderLayout.CENTER;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.swing.SwingUtilities.invokeLater;

public class ElevatorUI extends JFrame {

    private static final long serialVersionUID = -7040543347613308702L;

    public ElevatorUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        List<BuildingAndElevator> buildings = new ArrayList<>();

        for (ElevatorEngine elevatorEngine : ServiceLoader.load(ElevatorEngine.class)) {
            final Building building = new Building(elevatorEngine, new ConstantMaxNumberOfUsers());
            buildings.add(new BuildingAndElevator(building, elevatorEngine));
        }

        final InteractionPanel interactionPanel = new InteractionPanel(buildings);
        add(interactionPanel, CENTER);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            buildings.stream().map(BuildingAndElevator::building)
                              .forEach(Building::updateBuildingState);
            interactionPanel.update();
        }, 0, 1, SECONDS);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        invokeLater(ElevatorUI::new);
    }

}
