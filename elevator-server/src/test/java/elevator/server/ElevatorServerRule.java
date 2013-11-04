package elevator.server;

import java.net.*;
import java.util.logging.*;

import javax.ws.rs.client.*;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.webapp.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.junit.runners.model.*;

class ElevatorServerRule implements TestRule {

    public WebTarget target;
    private Statement base;

    @Override
    public Statement apply(Statement base, Description description) {
        this.base = base;
        this.target = ClientBuilder.newClient().target("http://localhost:9999/resources");

        return new ElevatorServerStatement();
    }

    private class ElevatorServerStatement extends Statement {

        /**
         * Keep a strong reference on this {@link Logger} as described by {@link java.util.logging.LogManager#getLogger}
         */
        private Logger randomPasswordLogger;

        @Override
        public void evaluate() throws Throwable {
            InetSocketAddress address = new InetSocketAddress("localhost", 9999);
            Server server = new Server(address);
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
