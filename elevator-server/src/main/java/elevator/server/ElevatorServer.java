package elevator.server;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.DefaultResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import java.io.IOException;

public class ElevatorServer {

    public RandomPort start() {
        RandomPort randomPort = new RandomPort();
        ResourceConfig resourceConfig = new DefaultResourceConfig();
        resourceConfig.getClasses().add(JacksonJsonProvider.class);
        resourceConfig.getSingletons().add(new WebResource(new StartedElevatorServer()));
        try {
            HttpServer httpServer = HttpServerFactory.create("http://localhost:" + randomPort.port + "/", resourceConfig);
            httpServer.start();
            return randomPort;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) {
        RandomPort port = new ElevatorServer().start();
        System.out.println("elevator server started on port " + port);
    }

}
