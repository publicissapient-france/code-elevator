package elevator.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.InetSocketAddress;

public class ElevatorServerRule implements TestRule {

    public WebTarget target;
    private Statement base;

    @Override
    public Statement apply(Statement base, Description description) {
        this.base = base;
        this.target = ClientBuilder.newClient().target("http://localhost:8080/resources");

        return new ElevatorServerStatement();
    }

    private class ElevatorServerStatement extends Statement {

        @Override
        public void evaluate() throws Throwable {
            Server server = new Server(new InetSocketAddress("localhost", 8080));
            server.setHandler(new WebAppContext("src/main/webapp", "/"));
            try {
                server.start();
                base.evaluate();
            } finally {
                try {
                    server.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
