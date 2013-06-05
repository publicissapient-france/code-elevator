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
    private Score score;

    HTTPElevator(URL server, ExecutorService executor) throws MalformedURLException {
        this(server, executor, null);
    }

    HTTPElevator(URL server, ExecutorService executor, URLStreamHandler urlStreamHandler) throws MalformedURLException {
        this.executor = executor;
        this.urlStreamHandler = urlStreamHandler;
        this.server = new URL(server, "", urlStreamHandler);
        this.nextCommand = new URL(server, "nextCommand", urlStreamHandler);
        this.reset = new URL(server, "reset", urlStreamHandler);
        this.score = new Score();
    }

    @Override
    public ElevatorEngine call(Integer atFloor, Direction to) {
        httpGet("call?atFloor=" + atFloor + "&to=" + to);
        return this;
    }

    @Override
    public ElevatorEngine go(Integer floorToGo) {
        httpGet("go?floorToGo=" + floorToGo);
        return this;
    }

    @Override
    public Command nextCommand() {
        try (InputStream in = nextCommand.openConnection().getInputStream()) {
            Command command = Command.valueOf(new BufferedReader(new InputStreamReader(in)).readLine());
            LOGGER.info(format("%s %s", nextCommand.toString(), command));
            return command;
        } catch (IllegalArgumentException | IOException e) {
            score = score.loose();
            return null;
        }
    }

    @Override
    public ElevatorEngine userHasEntered(User user) {
        httpGet("userHasEntered");
        return this;
    }

    @Override
    public ElevatorEngine userHasExited(User user) {
        httpGet("userHasExited");
        score = score.success(user);
        return this;
    }

    @Override
    public ElevatorEngine reset() {
        score = score.reset();
        httpGet(reset);
        return this;
    }

    Score getScore() {
        return score;
    }

    private void httpGet(String parameters) {
        try {
            httpGet(new URL(server, parameters, urlStreamHandler));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void httpGet(URL url) {
        executor.execute(() -> {
            try (InputStream in = url.openConnection().getInputStream()) {
            } catch (IOException e) {
                transportError = TRUE;
                score = score.loose();
                throw new RuntimeException(e);
            }
        });
    }

}
