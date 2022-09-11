package elevator.server;

import com.sun.net.httpserver.HttpServer;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.UriBuilder;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.rules.ExternalResource;

import java.net.URI;

class ElevatorServerRule extends ExternalResource {
    private static final URI URI = UriBuilder.fromUri("")
            .scheme("http").host("localhost").port(8080)
            .path("resources")
            .build();

    WebTarget target;

    private HttpServer server;

    ElevatorServerRule() {
        this.target = ClientBuilder.newClient()
                .register(MultiPartFeature.class)
                .target(URI);
    }

    @Override
    protected void before() throws Throwable {
        ElevatorApplication application = new ElevatorApplication();
        server = JdkHttpServerFactory.createHttpServer(URI, ResourceConfig.forApplication(application));
    }

    @Override
    protected void after() {
        try {
            server.stop(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
