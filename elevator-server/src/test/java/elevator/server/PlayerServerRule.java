package elevator.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;

import static javax.servlet.http.HttpServletResponse.SC_OK;

class PlayerServerRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new PlayerServerStatement(base);
    }

    private class PlayerServerStatement extends Statement {

        private final Statement base;

        private PlayerServerStatement(Statement base) {
            this.base = base;
        }

        @Override
        public void evaluate() throws Throwable {
            InetSocketAddress address = new InetSocketAddress("localhost", 9999);
            Server server = new Server(address);
            server.setHandler(new AlwaysOkHandler());
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

    private class AlwaysOkHandler extends AbstractHandler {

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            baseRequest.getResponse().setStatus(SC_OK);
            baseRequest.setHandled(true);
        }

    }

}
