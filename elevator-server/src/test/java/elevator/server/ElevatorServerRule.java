package elevator.server;

import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

class ElevatorServerRule implements TestRule {

    public WebTarget target;

    @Override
    public Statement apply(Statement base, Description description) {
        this.target = ClientBuilder.newClient().register(MultiPartFeature.class)
                .target("http://localhost:8080/resources");

        return new JettyServerStatement(base, new WebAppContext("src/main/webapp", "/"));
    }

}
