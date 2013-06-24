package elevator.ui;

import elevator.Building;
import elevator.Clock;
import elevator.ClockListener;
import elevator.engine.ElevatorEngine;

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

    public ElevatorUI() throws HeadlessException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        List<BuildingAndElevator> buildings = new ArrayList<>();
        final Clock clock = new Clock();

        for (ElevatorEngine elevatorEngine : ServiceLoader.load(ElevatorEngine.class)) {
            final Building building = new Building(elevatorEngine);
            clock.addClockListener(new ClockListener() {
                @Override
                public ClockListener onTick() {
                    building.updateBuildingState();
                    return this;
                }
            });
            buildings.add(new BuildingAndElevator(building, elevatorEngine));
        }

        final InteractionPanel interactionPanel = new InteractionPanel(buildings);
        add(interactionPanel, CENTER);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Runnable periodicTask = new Runnable() {
            @Override
            public void run() {
                clock.tick();
                interactionPanel.update();
            }

        };

        executor.scheduleAtFixedRate(periodicTask, 0, 1, SECONDS);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        invokeLater(new Runnable() {
            @Override
            public void run() {
                new ElevatorUI();
            }
        });
    }

}
