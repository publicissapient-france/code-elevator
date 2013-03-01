package elevator.server;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class ElevatorServer {
    public StartedElevatorServer start() {
        ResourceConfig resourceConfig = new PackagesResourceConfig("elevator");
        try {
            HttpServer httpServer = HttpServerFactory.create("http://localhost:" + new RandomPort().port + "/", resourceConfig);
            httpServer.start();
            return new StartedElevatorServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
