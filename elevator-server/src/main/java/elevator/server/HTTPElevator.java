package elevator.server;

import elevator.Command;
import elevator.Direction;
import elevator.User;
import elevator.engine.ElevatorEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.concurrent.ExecutorService;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

class HTTPElevator implements ElevatorEngine {

    private final URL server;
    private final ExecutorService executor;
    private final URLStreamHandler urlStreamHandler;
    private final URL nextCommand;
    private final URL reset;
    private final Score score;

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
        this.transportError = FALSE;
        this.score = new Score();
    }

    @Override
    public ElevatorEngine call(Integer atFloor, Direction to) {
        System.out.println(server.toString() + "/call?atFloor=" + atFloor + "&to=" + to.toString());
        httpGet("call?atFloor=" + atFloor + "&to=" + to);
        return this;
    }

    @Override
    public ElevatorEngine go(Integer floorToGo) {
        System.out.println(server.toString() + "/go?floorToGo=" + floorToGo);
        httpGet("go?floorToGo=" + floorToGo);
        return this;
    }

    @Override
    public Command nextCommand() {
        checkTransportError();
        try (InputStream in = nextCommand.openConnection().getInputStream()) {
            Command command = Command.valueOf(new BufferedReader(new InputStreamReader(in)).readLine());
            System.out.println(nextCommand.toString() + " " + command);
            return command;
        } catch (IllegalArgumentException e) {
            score.loose();
            return null;
        } catch (IOException e) {
            score.loose();
            transportError = TRUE;
            throw new RuntimeException(e);
        }
    }

    @Override
    public ElevatorEngine userHasEntered(User user) {
        System.out.println(server + "/userHasEntered");
        httpGet("userHasEntered");
        return this;
    }

    @Override
    public ElevatorEngine userHasExited(User user) {
        System.out.println(server + "/userHasExited");
        httpGet("userHasExited");
        score.success(user);
        return this;
    }

    @Override
    public ElevatorEngine reset() {
        System.out.println(reset);
        try (InputStream in = reset.openConnection().getInputStream()) {
            transportError = FALSE;
        } catch (IOException e) {
            score.kindLoose();
            transportError = TRUE;
        }
        return this;
    }

    Score getScore() {
        return score;
    }

    Boolean hasTransportError() {
        return transportError;
    }

    private void checkTransportError() {
        if (transportError) {
            throw new RuntimeException();
        }
    }

    private void httpGet(String parameters) {
        try {
            httpGet(new URL(server, parameters, urlStreamHandler));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void httpGet(final URL url) {
        checkTransportError();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try (InputStream in = url.openConnection().getInputStream()) {
                } catch (IOException e) {
                    transportError = TRUE;
                    score.loose();
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
