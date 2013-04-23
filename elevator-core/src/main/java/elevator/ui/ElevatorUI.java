package elevator.ui;

import elevator.Building;
import elevator.Clock;
import elevator.ClockListener;
import elevator.engine.ElevatorEngine;
import elevator.engine.naive.NaiveElevator;
import elevator.engine.queue.QueueElevator;
import elevator.engine.scan.ScanElevator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
        Clock clock = new Clock();
        ElevatorEngine elevator;

        elevator = new NaiveElevator();
        final Building naiveBuilding = new Building(elevator);
        clock.addClockListener(new ClockListener() {
            @Override
            public ClockListener onTick() {
                naiveBuilding.updateBuildingState();
                return this;
            }
        });
        buildings.add(new BuildingAndElevator(naiveBuilding, elevator));

        elevator = new QueueElevator();
        final Building queueBuilding = new Building(elevator);
        clock.addClockListener(new ClockListener() {
            @Override
            public ClockListener onTick() {
                queueBuilding.updateBuildingState();
                return this;
            }
        });
        buildings.add(new BuildingAndElevator(queueBuilding, elevator));

        elevator = new ScanElevator();
        final Building scanElevator = new Building(elevator);
        clock.addClockListener(new ClockListener() {
            @Override
            public ClockListener onTick() {
                scanElevator.updateBuildingState();
                return this;
            }
        });
        buildings.add(new BuildingAndElevator(scanElevator, elevator));

        InteractionPanel interactionPanel = new InteractionPanel(buildings);
        add(interactionPanel, CENTER);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        Runnable periodicTask = () -> {
            clock.tick();
            interactionPanel.update();
        };

        executor.scheduleAtFixedRate(periodicTask, 0, 1, SECONDS);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        invokeLater(ElevatorUI::new);
    }

}
