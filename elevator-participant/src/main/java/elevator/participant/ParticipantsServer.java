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
import static java.util.concurrent.Executors.newCachedThreadPool;

public class ParticipantsServer implements HttpHandler {
    public static final Pattern PATH_PATTERN = Pattern.compile("^(.*)(/.+)$");

    private final HashMap<String, ElevatorEngine> elevators;

    public ParticipantsServer() {
        elevators = new HashMap<>();
        final ServiceLoader<ElevatorEngine> serviceLoader = ServiceLoader.load(ElevatorEngine.class);
        for (ElevatorEngine elevatorEngine : serviceLoader) {
            elevators.put(elevatorEngine.getClass().getSimpleName(), elevatorEngine);
        }
        elevators.put("", new ScanElevator());
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();

        Matcher matcher = PATH_PATTERN.matcher(path);
        if (!matcher.matches()) {
            sendBadRequest(format("%s doesn't conform to \"%s\"", path, PATH_PATTERN), httpExchange);
            return;
        }

        String elevatorName = extractElevatorName(matcher);

        ElevatorEngine elevator = elevators.get(elevatorName);
        if (elevator == null) {
            sendBadRequest(format("Elevator implementation \"%s\" was not found", elevatorName), httpExchange);
            return;
        }

        dispatchToElevatorEngine(httpExchange, elevatorName, elevator, matcher.group(2));
    }

    private String extractElevatorName(Matcher matcher) {
        String elevatorName = matcher.group(1);
        if (elevatorName.isEmpty()) {
            elevatorName = "/ScanElevator";
        }
        elevatorName = elevatorName.substring(1);
        return elevatorName;
    }

    private void dispatchToElevatorEngine(HttpExchange httpExchange, String elevatorName, ElevatorEngine elevator, String request) throws IOException {
        Map<String, String> parameters = extractParameters(httpExchange.getRequestURI());
        switch (request) {
            case "/reset":
                String cause = parameters.get("cause");
                System.out.format("%13s /reset?cause=%s%n", elevatorName, cause);
                elevator.reset(cause);
                sendResponse(200, httpExchange);
                break;
            case "/call":
                Integer atFloor = parseInt(parameters.get("atFloor"));
                Direction to = Direction.valueOf(parameters.get("to"));
                System.out.format("%13s /call?atFloor=%d&to=%s%n", elevatorName, atFloor, to);
                elevator.call(atFloor, to);
                sendResponse(200, httpExchange);
                break;
            case "/go":
                Integer floorToGo = parseInt(parameters.get("floorToGo"));
                System.out.format("%13s /go?floorToGo=%d%n", elevatorName, floorToGo);
                elevator.go(floorToGo);
                sendResponse(200, httpExchange);
                break;
            case "/nextCommand":
                String nextCommand = elevator.nextCommand().toString();
                System.out.format("%13s /nextCommand %s%n", elevatorName, nextCommand);
                sendResponse(200, nextCommand, httpExchange);
                break;
            default:
                sendResponse(200, httpExchange);
        }
    }

    private void sendBadRequest(String errorMessage, HttpExchange httpExchange) throws IOException {
        System.out.println(errorMessage);
        sendResponse(400, errorMessage, httpExchange);
    }

    private void sendResponse(int responseCode, HttpExchange httpExchange) throws IOException {
        sendResponse(responseCode, "", httpExchange);
    }

    private void sendResponse(int responseCode, String responseBody, HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(responseCode, responseBody.length());
        if (!responseBody.isEmpty()) {
            try (Writer out = new OutputStreamWriter(httpExchange.getResponseBody())) {
                out.write(responseBody);
            }
        }
        httpExchange.close();
    }

    public static void main(String[] args) throws IOException {
        final HttpServer httpServer = HttpServer.create(new InetSocketAddress(8081), 0);
        httpServer.createContext("/", new ParticipantsServer());
        httpServer.setExecutor(newCachedThreadPool());
        httpServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                httpServer.stop(0);
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
