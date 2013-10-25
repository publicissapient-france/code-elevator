package elevator.participant;

import elevator.Command;
import elevator.Direction;
import elevator.engine.ElevatorEngine;
import elevator.engine.scan.ScanElevator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@WebServlet("/*")
public class ParticipantServlet extends HttpServlet {

    private final Map<String, ElevatorEngine> elevators;

    public ParticipantServlet() {
        elevators = new HashMap<>();
        final ServiceLoader<ElevatorEngine> serviceLoader = ServiceLoader.load(ElevatorEngine.class);
        for (ElevatorEngine elevatorEngine : serviceLoader) {
            elevators.put(elevatorEngine.getClass().getSimpleName(), elevatorEngine);
        }
        elevators.put("", new ScanElevator());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (elevators) {
            final Matcher matcher = Pattern.compile("^(.*)(/.+)$").matcher(req.getPathInfo());
            if (matcher.matches()) {
                String elevatorName = matcher.group(1);
                if (elevatorName.isEmpty()) {
                    elevatorName = "/ScanElevator";
                }
                elevatorName = elevatorName.substring(1);
                final ElevatorEngine elevator = elevators.get(elevatorName);
                if (elevator == null) {
                    final String errorMessage = format("Elevator implementation \"%s\" was not found", elevatorName);
                    getServletContext().log(errorMessage);
                    resp.sendError(SC_BAD_REQUEST, errorMessage);
                    return;
                }
                switch (matcher.group(2)) {
                    case "/reset":
                        String cause = req.getParameter("cause");
                        getServletContext().log(format("%13s /reset?cause=%s", elevatorName, cause));
                        elevator.reset(cause);
                        break;
                    case "/call":
                        Integer atFloor = parseInt(req.getParameter("atFloor"));
                        Direction to = Direction.valueOf(req.getParameter("to"));
                        getServletContext().log(format("%13s /call?atFloor=%d&to=%s", elevatorName, atFloor, to));
                        elevator.call(atFloor, to);
                        break;
                    case "/go":
                        Integer floorToGo = parseInt(req.getParameter("floorToGo"));
                        getServletContext().log(format("%13s /go?floorToGo=%d", elevatorName, floorToGo));
                        elevator.go(floorToGo);
                        break;
                    case "/nextCommand":
                        Command nextCommand = elevator.nextCommand();
                        getServletContext().log(format("%13s /nextCommand %s", elevatorName, nextCommand));
                        resp.getWriter().print(nextCommand);
                }
            }
        }
    }

}