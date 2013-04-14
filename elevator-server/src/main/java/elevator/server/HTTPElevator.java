package elevator.server;

import elevator.Command;
import elevator.Direction;
import elevator.engine.ElevatorEngine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newCachedThreadPool;

public class HTTPElevator implements ElevatorEngine {

    private static final ExecutorService EXECUTOR = newCachedThreadPool();

    private final URLStreamHandler urlStreamHandler;
    private final URL server;
    private final URL nextCommand;
    private final URL reset;

    HTTPElevator(URL server) throws MalformedURLException {
        this(server, null);
    }

    HTTPElevator(URL server, URLStreamHandler urlStreamHandler) throws MalformedURLException {
        this.urlStreamHandler = urlStreamHandler;
        this.server = new URL(server, "", urlStreamHandler);
        this.nextCommand = new URL(server, "nextCommand", urlStreamHandler);
        this.reset = new URL(server, "reset", urlStreamHandler);
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
            return Command.valueOf(new BufferedReader(new InputStreamReader(in)).readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ElevatorEngine reset() {
        httpGet(reset);
        return this;
    }

    private void httpGet(String parameters) {
        try {
            httpGet(new URL(server, parameters, urlStreamHandler));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void httpGet(URL url) {
        EXECUTOR.execute(() -> {
            try (InputStream in = url.openConnection().getInputStream()) {
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
