package elevator.server;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import elevator.server.port.Port;
import elevator.server.port.PortFactory;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import java.io.IOException;

public class ElevatorServer {

    public Port start() {
        Port port = PortFactory.newPort();
        try {
            HttpServer httpServer = HttpServerFactory.create("http://localhost:" + port.port() + "/", new ElevatorApplication());
            httpServer.start();
            return port;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) {
        Port port = new ElevatorServer().start();
        System.out.println("elevator server started on port " + port);
    }

}
