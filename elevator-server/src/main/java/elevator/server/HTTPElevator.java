package elevator.server;

import elevator.Command;
import elevator.Direction;
import elevator.User;
import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

class HTTPElevator implements ElevatorEngine {

    private final URL server;
    private final ExecutorService executor;
    private final URLStreamHandler urlStreamHandler;
    private final URL nextCommand;
    private final URL reset;
    private final List<String> transportErrorMessages;

    private Boolean transportError;

    HTTPElevator(URL server, ExecutorService executor) throws MalformedURLException {
        this(server, executor, null);
    }

    HTTPElevator(URL server, ExecutorService executor, URLStreamHandler urlStreamHandler) throws MalformedURLException {
        this.executor = executor;
        this.urlStreamHandler = urlStreamHandler;
        this.server = new URL(server, "", urlStreamHandler);
        this.nextCommand = new URL(server, "nextCommand", urlStreamHandler);
        this.reset = new URL(server, "reset", urlStreamHandler);
        this.transportErrorMessages = new LinkedList<>();
        this.transportError = FALSE;
    }

    @Override
    public ElevatorEngine call(Integer atFloor, Direction to) throws ElevatorIsBrokenException {
        System.out.println(server.toString() + "/call?atFloor=" + atFloor + "&to=" + to.toString());
        httpGet("call?atFloor=" + atFloor + "&to=" + to);
        return this;
    }

    @Override
    public ElevatorEngine go(Integer floorToGo) throws ElevatorIsBrokenException {
        System.out.println(server.toString() + "/go?floorToGo=" + floorToGo);
        httpGet("go?floorToGo=" + floorToGo);
        return this;
    }

    @Override
    public Command nextCommand() throws ElevatorIsBrokenException {
        String commandFromResponse = "";
        try (BufferedReader in = new BufferedReader(new InputStreamReader(nextCommand.openConnection().getInputStream()))) {
            commandFromResponse = in.readLine();
            Command command = Command.valueOf(commandFromResponse);
            System.out.println(nextCommand.toString() + " " + command);
            return command;
        } catch (IllegalArgumentException e) {
            throw new ElevatorIsBrokenException("Command \"" + commandFromResponse + "\" is not a valid command; valid commands are [UP|DOWN|OPEN|CLOSE|NOTHING] with case sensitive");
        } catch (IOException e) {
            transportError = TRUE;
            transportErrorMessages.add(e.getMessage());
            throw new ElevatorIsBrokenException(transportErrorMessages);
        }
    }

    @Override
    public ElevatorEngine userHasEntered(User user) throws ElevatorIsBrokenException {
        System.out.println(server + "/userHasEntered");
        httpGet("userHasEntered");
        return this;
    }

    @Override
    public ElevatorEngine userHasExited(User user) throws ElevatorIsBrokenException {
        System.out.println(server + "/userHasExited");
        httpGet("userHasExited");
        return this;
    }

    @Override
    public ElevatorEngine reset() throws ElevatorIsBrokenException {
        System.out.println(reset);
        httpGet(reset);
        return this;
    }

    private void httpGet(String parameters) throws ElevatorIsBrokenException {
        try {
            httpGet(new URL(server, parameters, urlStreamHandler));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void httpGet(final URL url) throws ElevatorIsBrokenException {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try (InputStream in = url.openConnection().getInputStream()) {
                    transportError = FALSE;
                    transportErrorMessages.clear();
                } catch (IOException e) {
                    transportError = TRUE;
                    transportErrorMessages.add(e.getMessage());
                }
            }
        });
        if (transportError) {
            throw new ElevatorIsBrokenException(transportErrorMessages);
        }
    }

}
