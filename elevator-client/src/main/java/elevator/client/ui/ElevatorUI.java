package elevator.client.ui;

import elevator.client.Clock;
import elevator.client.Elevator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static javax.swing.SwingUtilities.invokeLater;

public class ElevatorUI extends JFrame {

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
