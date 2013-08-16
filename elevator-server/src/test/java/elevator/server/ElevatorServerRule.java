package elevator.server;

import elevator.logging.ElevatorLogger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

class ElevatorServerRule implements TestRule {

    public WebTarget target;
    private Statement base;
    private String password;

    @Override
    public Statement apply(Statement base, Description description) {
        this.base = base;
        this.target = ClientBuilder.newClient().target("http://localhost:8080/resources");

        return new ElevatorServerStatement();
    }

    String password() {
        return password;
    }

    private class ElevatorServerStatement extends Statement {

        /**
         * Keep a strong reference on this {@link Logger} as described by {@link java.util.logging.LogManager#getLogger}
         */
        private Logger randomPasswordLogger;

        @Override
        public void evaluate() throws Throwable {
            InetSocketAddress address = new InetSocketAddress("localhost", 8080);
            Server server = new Server(address);
            server.setHandler(new WebAppContext("src/main/webapp", "/"));
            try {
                server.start();
                passwordRetrievedWhenLogged();
                warmup(address); // in order to retrieve logged password
                base.evaluate();
            } finally {
                try {
                    server.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void passwordRetrievedWhenLogged() {
            randomPasswordLogger = new ElevatorLogger("RandomPassword").logger();
            randomPasswordLogger.addHandler(new Handler() {
                @Override
                public void publish(LogRecord record) {
                    if (password != null) {
                        return; // password must be the first published record
                    }
                    password = record.getMessage();
                }

                @Override
                public void flush() {
                }

                @Override
                public void close() throws SecurityException {
                }
            });

        }

        private void warmup(InetSocketAddress address) {
            try {
                new URL("http://" + address.getHostName() + ":" + address.getPort() + "/resources/leaderboard").openStream().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
