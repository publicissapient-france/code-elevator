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

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

@WebServlet("/*")
public class ParticipantServlet extends HttpServlet {

    private final ElevatorEngine elevator;

    public ParticipantServlet() {
        elevator = new ScanElevator();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        synchronized (elevator) {
            switch (req.getPathInfo()) {
                case "/reset":
                    String cause = req.getParameter("cause");
                    getServletContext().log(format("/reset?cause=%s", cause));
                    elevator.reset(cause);
                    break;
                case "/call":
                    Integer atFloor = parseInt(req.getParameter("atFloor"));
                    Direction to = Direction.valueOf(req.getParameter("to"));
                    getServletContext().log(format("/call?atFloor=%d&to=%s", atFloor, to));
                    elevator.call(atFloor, to);
                    break;
                case "/go":
                    Integer floorToGo = parseInt(req.getParameter("floorToGo"));
                    getServletContext().log(format("/go?floorToGo=%d", floorToGo));
                    elevator.go(floorToGo);
                    break;
                case "/nextCommand":
                    Command nextCommand = elevator.nextCommand();
                    getServletContext().log(format("/nextCommand %s", nextCommand));
                    resp.getWriter().print(nextCommand);
            }
        }
    }

}