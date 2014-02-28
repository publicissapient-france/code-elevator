package elevator.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.rules.ExternalResource;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.InetSocketAddress;

class ElevatorServerRule extends ExternalResource {
    public WebTarget target;

    private Server server;
    private Handler jettyHandler;

    ElevatorServerRule() {
        this(new WebAppContext("src/main/webapp", "/"));
    }

    ElevatorServerRule(Handler jettyHandler) {
        this.jettyHandler = jettyHandler;
        this.target = ClientBuilder.newClient()
                .register(MultiPartFeature.class)
                .target("http://localhost:8080/resources");
    }

    @Override
    protected void before() throws Throwable {
        InetSocketAddress address = new InetSocketAddress("localhost", 8080);
        server = new Server(address);
        server.setHandler(jettyHandler);
        server.start();
    }

    @Override
    protected void after() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Throwable {
        final ElevatorServerRule elevatorServer = new ElevatorServerRule();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                elevatorServer.after();
            }
        }));

        elevatorServer.before();
    }
}
