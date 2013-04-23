package elevator.participant;

import elevator.Command;
import elevator.Direction;
import elevator.engine.ElevatorEngine;
import elevator.engine.scan.ScanElevator;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class ParticipantServer extends AbstractHandler {

    private static final Logger LOGGER = getLogger(ParticipantServer.class.getName());

    private final ElevatorEngine elevator;

    public ParticipantServer() {
        this.elevator = new ScanElevator();
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        switch (target) {
            case "/nextCommand":
                synchronized (elevator) {
                    Command nextCommand = elevator.nextCommand();
                    baseRequest.getResponse().getWriter().println(nextCommand);
                    LOGGER.info(target + " " + nextCommand);
                }
                break;
            case "/call":
                Integer atFloor = Integer.valueOf(baseRequest.getParameter("atFloor"));
                Direction to = Direction.valueOf(baseRequest.getParameter("to"));
                synchronized (elevator) {
                    elevator.call(atFloor, to);
                }
                LOGGER.info(target + " atFloor " + atFloor + " to " + to);
                break;
            case "/go":
                Integer floorToGo = Integer.valueOf(baseRequest.getParameter("floorToGo"));
                synchronized (elevator) {
                    elevator.go(floorToGo);
                }
                LOGGER.info(target + " " + floorToGo);
                break;
            case "/reset":
                synchronized (elevator) {
                    elevator.reset();
                }
                LOGGER.info(target);
                break;
            default:
                LOGGER.warning(target);
        }
        baseRequest.setHandled(true);
    }

    public static void main(String... args) throws Exception {
        Server server = new Server(1981);
        server.setHandler(new ParticipantServer());
        server.start();
        server.join();
    }

}
