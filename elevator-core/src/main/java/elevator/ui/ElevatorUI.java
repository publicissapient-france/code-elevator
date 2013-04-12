package elevator.ui;

import elevator.Building;
import elevator.Clock;
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
        Building building;

        elevator = new NaiveElevator();
        building = new Building(elevator);
        clock.addClockListener(building);
        buildings.add(new BuildingAndElevator(building, elevator));

        elevator = new QueueElevator();
        building = new Building(elevator);
        clock.addClockListener(building);
        buildings.add(new BuildingAndElevator(building, elevator));

        elevator = new ScanElevator();
        building = new Building(elevator);
        clock.addClockListener(building);
        buildings.add(new BuildingAndElevator(building, elevator));

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
