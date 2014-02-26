package elevator.participant;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import elevator.Direction;
import elevator.engine.ElevatorEngine;
import elevator.engine.scan.ScanElevator;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;

public class ParticipantServer implements HttpHandler {
    private final HashMap<String, ElevatorEngine> elevators;

    public ParticipantServer() {
        elevators = new HashMap<>();
        final ServiceLoader<ElevatorEngine> serviceLoader = ServiceLoader.load(ElevatorEngine.class);
        for (ElevatorEngine elevatorEngine : serviceLoader) {
            elevators.put(elevatorEngine.getClass().getSimpleName(), elevatorEngine);
        }
        elevators.put("", new ScanElevator());
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        synchronized (elevators) {
            final Matcher matcher = Pattern.compile("^(.*)(/.+)$").matcher(httpExchange.getRequestURI().getPath());
            if (matcher.matches()) {
                String elevatorName = matcher.group(1);
                if (elevatorName.isEmpty()) {
                    elevatorName = "/ScanElevator";
                }
                elevatorName = elevatorName.substring(1);
                final ElevatorEngine elevator = elevators.get(elevatorName);
                if (elevator == null) {
                    final String errorMessage = format("Elevator implementation \"%s\" was not found", elevatorName);
                    System.out.println(errorMessage);
                    httpExchange.sendResponseHeaders(400, errorMessage.length());
                    try (Writer out = new OutputStreamWriter(httpExchange.getResponseBody())) {
                        out.write(errorMessage);
                    }
                    return;
                }
                Map<String, String> parameters = extractParameters(httpExchange.getRequestURI());
                switch (matcher.group(2)) {
                    case "/reset":
                        String cause = parameters.get("cause");
                        System.out.format("%13s /reset?cause=%s%n", elevatorName, cause);
                        elevator.reset(cause);
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    case "/call":
                        Integer atFloor = parseInt(parameters.get("atFloor"));
                        Direction to = Direction.valueOf(parameters.get("to"));
                        System.out.format("%13s /call?atFloor=%d&to=%s%n", elevatorName, atFloor, to);
                        elevator.call(atFloor, to);
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    case "/go":
                        Integer floorToGo = parseInt(parameters.get("floorToGo"));
                        System.out.format("%13s /go?floorToGo=%d%n", elevatorName, floorToGo);
                        elevator.go(floorToGo);
                        httpExchange.sendResponseHeaders(200, 0);
                        break;
                    case "/nextCommand":
                        String nextCommand = elevator.nextCommand().toString();
                        System.out.format("%13s /nextCommand %s%n", elevatorName, nextCommand);
                        httpExchange.sendResponseHeaders(200, nextCommand.length());
                        try (Writer out = new OutputStreamWriter(httpExchange.getResponseBody())) {
                            out.write(nextCommand);
                        }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(8081), 0);
        httpServer.createContext("/", new ParticipantServer());
        httpServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                httpServer.stop(1000);
            }
        }));
    }

    private static Map<String, String> extractParameters(URI uri) {
        String query = uri.getQuery();
        Map<String, String> parametersAndValues = new HashMap<>();
        if (query == null) {
            return parametersAndValues;
        }
        Scanner scanner = new Scanner(query).useDelimiter("&");
        while (scanner.hasNext()) {
            String[] parameterAndValue = scanner.next().split("=");
            parametersAndValues.put(parameterAndValue[0], parameterAndValue[1].replace('+', ' '));
        }
        return parametersAndValues;
    }
}
