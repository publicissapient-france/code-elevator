package elevator.participant;

import elevator.Command;
import elevator.Direction;
import elevator.engine.ElevatorEngine;
import elevator.logging.ElevatorLogger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import static java.lang.String.format;

public class ParticipantServer extends AbstractHandler {

    private final Logger logger;
    private final ElevatorEngine elevator;

    public ParticipantServer(ElevatorEngine elevator) {
        this.logger = new ElevatorLogger(elevator.getClass().getSimpleName()).logger();
        this.elevator = elevator;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        switch (target) {
            case "/nextCommand":
                synchronized (elevator) {
                    Command nextCommand = elevator.nextCommand();
                    baseRequest.getResponse().getWriter().println(nextCommand);
                    logger.info(format("%s %s", target, nextCommand));
                }
                break;
            case "/call":
                Integer atFloor = Integer.valueOf(baseRequest.getParameter("atFloor"));
                Direction to = Direction.valueOf(baseRequest.getParameter("to"));
                synchronized (elevator) {
                    elevator.call(atFloor, to);
                }
                logger.info(format("%s atFloor %d to %s", target, atFloor, to));
                break;
            case "/go":
                Integer floorToGo = Integer.valueOf(baseRequest.getParameter("floorToGo"));
                synchronized (elevator) {
                    elevator.go(floorToGo);
                }
                logger.info(format("%s floorToGo %d", target, floorToGo));
                break;
            case "/userHasEntered":
            case "/userHasExited":
                logger.info(target);
                break;
            case "/reset":
                String cause = baseRequest.getParameter("cause");
                synchronized (elevator) {
                    elevator.reset(cause);
                }
                logger.info(format("%s cause %s", target, cause));
                break;
            default:
                logger.warning(target);
        }
        baseRequest.setHandled(true);
    }

    public static void main(String... args) throws Exception {
        Iterator<ElevatorEngine> elevatorEngines = ServiceLoader.load(ElevatorEngine.class).iterator();

        Integer port = 1981;
        String elevatorEngineClassName = null;
        if (args.length == 2) {
            port = readPort(args[0], port);
            elevatorEngineClassName = args[1];
        }
        ElevatorEngine elevator = readElevatorEngine(elevatorEngineClassName, elevatorEngines);

        Server server = new Server(port);
        server.setHandler(new ParticipantServer(elevator));
        server.start();
        server.join();
    }

    private static Integer readPort(String arg, Integer defaultPort) {
        try {
            return Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            return defaultPort;
        }
    }

    private static ElevatorEngine readElevatorEngine(String elevatorEngineClassName, Iterator<ElevatorEngine> elevatorEngines) {
        if (!elevatorEngines.hasNext()) {
            throw new IllegalArgumentException("ServiceLoader has no implementation of class ElevatorEngine");
        }
        ElevatorEngine defaultElevator = null;
        while (elevatorEngines.hasNext()) {
            ElevatorEngine elevatorEngine = elevatorEngines.next();
            if (defaultElevator == null) {
                defaultElevator = elevatorEngine;
            }
            if (elevatorEngineClassName == null) {
                return defaultElevator;
            }
            if (elevatorEngine.getClass().getName().equals(elevatorEngineClassName)) {
                return elevatorEngine;
            }
        }
        return defaultElevator;
    }

}
