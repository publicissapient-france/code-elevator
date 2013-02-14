package elevator.ui;

import elevator.Clock;
import elevator.Elevator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static javax.swing.SwingUtilities.invokeLater;

public class ElevatorUI extends JFrame {

    static {
        try {
            URL resource = ElevatorUI.class.getResource("/logging.properties");
            System.setProperty("java.util.logging.config.file", new File(resource.toURI()).getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public ElevatorUI() throws HeadlessException {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        Elevator elevator = new Elevator();
        Clock clock = new Clock();
        clock.addObserver(elevator);

        InteractionPanel interactionPanel = new InteractionPanel(elevator);
        add(interactionPanel, CENTER);

        JButton clockButton = new JButton(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clock.tick();
                interactionPanel.update();
            }
        });
        clockButton.setText("tick");

        add(clockButton, SOUTH);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        invokeLater(ElevatorUI::new);
    }

}
