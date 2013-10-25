package elevator.server;

import elevator.Command;
import elevator.Direction;
import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;
import elevator.logging.ElevatorLogger;
import elevator.user.User;

import java.io.*;
import java.net.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.net.URLEncoder.encode;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.logging.Level.WARNING;

class HTTPElevator implements ElevatorEngine {

    private static final String SHUTDOWN_URL = "shutdown";

    private final URL server;
    private final URLStreamHandler urlStreamHandler;
    private final URL nextCommand;
    private final URL userHasEntered;
    private final URL userHasExited;
    private final URL reset;
    private final Pattern errorStatusMessage;
    private final String validCommands;
    private final Logger logger;
    private final BlockingQueue<URL> requests;

    private String transportErrorMessage;

    HTTPElevator(URL server, ExecutorService executor, URLStreamHandler urlStreamHandler) throws MalformedURLException {
        this.urlStreamHandler = urlStreamHandler;
        this.server = new URL(server, "", urlStreamHandler);
        this.nextCommand = new URL(server, "nextCommand", urlStreamHandler);
        this.userHasEntered = new URL(server, "userHasEntered", urlStreamHandler);
        this.userHasExited = new URL(server, "userHasExited", urlStreamHandler);
        this.reset = new URL(server, "reset", urlStreamHandler);
        this.errorStatusMessage = Pattern.compile("Server returned HTTP response code: (\\d+).+");
        this.validCommands = "valid commands are [UP|DOWN|OPEN|CLOSE|NOTHING] with case sensitive";
        this.logger = new ElevatorLogger("HTTPElevator").logger();
        this.requests = new LinkedBlockingQueue<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                while (url == null || !url.getPath().equals(format("/%s", SHUTDOWN_URL))) {
                    try {
                        url = requests.take();
                        URLConnection urlConnection = getUrlConnection(url);
                        try (InputStream in = urlConnection.getInputStream()) {
                            transportErrorMessage = null;
                        }
                    } catch (IOException e) {
                        transportErrorMessage = createErrorMessage(url, e);
                    } catch (InterruptedException e) {
                        logger.log(WARNING, "Error occured when waiting for new URL", e);
                    }
                }
            }
        });
    }

    @Override
    public ElevatorEngine call(Integer atFloor, Direction to) throws ElevatorIsBrokenException {
        checkTransportError();
        httpGet("call?atFloor=" + atFloor + "&to=" + to);
        return this;
    }

    @Override
    public ElevatorEngine go(Integer floorToGo) throws ElevatorIsBrokenException {
        checkTransportError();
        httpGet("go?floorToGo=" + floorToGo);
        return this;
    }

    @Override
    public Command nextCommand() throws ElevatorIsBrokenException {
        checkTransportError();
        while (!requests.isEmpty()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                logger.log(WARNING, "Error occured when waiting for requests to be send", e);
            }
        }
        StringBuilder out = new StringBuilder(nextCommand.toString());
        String commandFromResponse = "";
        try {
            URLConnection urlConnection = getUrlConnection(nextCommand);
            try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                commandFromResponse = in.readLine();
                if (commandFromResponse == null) {
                    throw new ElevatorIsBrokenException(format("No command was provided; %s", validCommands));
                }
                transportErrorMessage = null;
                Command command = Command.valueOf(commandFromResponse);
                out.append(" ").append(command);
                return command;
            }
        } catch (IllegalArgumentException e) {
            out.append(" ").append(commandFromResponse);
            throw new ElevatorIsBrokenException(format("Command \"%s\" is not a valid command; %s", commandFromResponse, validCommands));
        } catch (IOException e) {
            transportErrorMessage = createErrorMessage(nextCommand, e);
            throw new ElevatorIsBrokenException(transportErrorMessage);
        } finally {
            logger.info(out.toString());
        }
    }

    @Override
    public ElevatorEngine userHasEntered(User user) throws ElevatorIsBrokenException {
        checkTransportError();
        httpGet(userHasEntered);
        return this;
    }

    @Override
    public ElevatorEngine userHasExited(User user) throws ElevatorIsBrokenException {
        checkTransportError();
        httpGet(userHasExited);
        return this;
    }

    @Override
    public ElevatorEngine reset(String cause) throws ElevatorIsBrokenException {
        // do not check transport error
        httpGet(reset + "?cause=" + urlEncode(cause));
        return this;
    }

    public void shutdown() {
        httpGet(SHUTDOWN_URL);
    }

    private void httpGet(String pathAndParameters) throws ElevatorIsBrokenException {
        try {
            httpGet(new URL(server, pathAndParameters, urlStreamHandler));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void httpGet(final URL url) throws ElevatorIsBrokenException {
        logger.info(url.toString());
        requests.offer(url);
    }

    private URLConnection getUrlConnection(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        urlConnection.setConnectTimeout(1000);
        urlConnection.setReadTimeout(1000);
        return urlConnection;
    }

    private void checkTransportError() {
        if (transportErrorMessage != null) {
            throw new ElevatorIsBrokenException(transportErrorMessage);
        }
    }

    private String urlEncode(String cause) {
        try {
            return encode(cause, defaultCharset().name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String createErrorMessage(URL url, IOException e) {
        if (e instanceof FileNotFoundException) {
            return format("Resource \"%s\" is not found", urlWithoutQuery(url));
        }

        if (e instanceof UnknownHostException) {
            return format("IP address of \"%s\" could not be determined", e.getMessage());
        }

        Matcher matcher = errorStatusMessage.matcher(e.getMessage());
        if (matcher.matches()) {
            return format("Server returned HTTP response code: %s for URL: %s", matcher.group(1), urlWithoutQuery(url));
        }

        return e.getMessage();
    }

    private String urlWithoutQuery(URL url) {
        if (url == null) {
            return "null";
        }
        return format("%s://%s%s", url.getProtocol(), url.getAuthority(), url.getPath());
    }
}
