package elevator.client.ui;

import elevator.Building;
import elevator.Clock;
import elevator.client.Elevator;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.awt.BorderLayout.CENTER;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.swing.SwingUtilities.invokeLater;

public class ElevatorUI extends JFrame {

    public ElevatorUI() throws HeadlessException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        Elevator elevator = new Elevator();
        Building building = new Building(elevator);
        Clock clock = new Clock().addClockListener(building);

        InteractionPanel interactionPanel = new InteractionPanel(building, elevator);
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
