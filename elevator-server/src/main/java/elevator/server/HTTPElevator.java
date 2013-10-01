package elevator.server;

import elevator.Command;
import elevator.Direction;
import elevator.user.User;
import elevator.engine.ElevatorEngine;
import elevator.exception.ElevatorIsBrokenException;
import elevator.logging.ElevatorLogger;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.net.URLEncoder.encode;
import static java.nio.charset.Charset.defaultCharset;

class HTTPElevator implements ElevatorEngine {

    private final URL server;
    private final ExecutorService executor;
    private final URLStreamHandler urlStreamHandler;
    private final URL nextCommand;
    private final URL userHasEntered;
    private final URL userHasExited;
    private final URL reset;
    private final Pattern errorStatusMessage;
    private final String validCommands;
    private final Logger logger;

    private String transportErrorMessage;

    HTTPElevator(URL server, ExecutorService executor) throws MalformedURLException {
        this(server, executor, null);
    }

    HTTPElevator(URL server, ExecutorService executor, URLStreamHandler urlStreamHandler) throws MalformedURLException {
        this.executor = executor;
        this.urlStreamHandler = urlStreamHandler;
        this.server = new URL(server, "", urlStreamHandler);
        this.nextCommand = new URL(server, "nextCommand", urlStreamHandler);
        this.userHasEntered = new URL(server, "userHasEntered", urlStreamHandler);
        this.userHasExited = new URL(server, "userHasExited", urlStreamHandler);
        this.reset = new URL(server, "reset", urlStreamHandler);
        this.errorStatusMessage = Pattern.compile("Server returned HTTP response code: (\\d+).+");
        this.validCommands = "valid commands are [UP|DOWN|OPEN|CLOSE|NOTHING] with case sensitive";
        this.logger = new ElevatorLogger("HTTPElevator").logger();
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

    private void httpGet(String pathAndParameters) throws ElevatorIsBrokenException {
        try {
            httpGet(new URL(server, pathAndParameters, urlStreamHandler));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private void httpGet(final URL url) throws ElevatorIsBrokenException {
        logger.info(url.toString());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URLConnection urlConnection = getUrlConnection(url);
                    try (InputStream in = urlConnection.getInputStream()) {
                        transportErrorMessage = null;
                    }
                } catch (IOException e) {
                    transportErrorMessage = createErrorMessage(url, e);
                }
            }
        });
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
        return format("%s://%s%s", url.getProtocol(), url.getAuthority(), url.getPath());
    }

}
