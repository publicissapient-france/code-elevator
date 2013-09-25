package elevator.participant;

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
                    elevator.reset(req.getParameter("cause"));
                    break;
                case "/call":
                    elevator.call(parseInt(req.getParameter("atFloor")), Direction.valueOf(req.getParameter("to")));
                    break;
                case "/go":
                    elevator.go(parseInt(req.getParameter("floorToGo")));
                    break;
                case "/nextCommand":
                    resp.getWriter().print(elevator.nextCommand());
            }
        }
    }

}