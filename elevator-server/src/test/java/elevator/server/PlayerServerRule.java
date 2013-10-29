package elevator.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_OK;

class PlayerServerRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new JettyServerStatement(base, new AlwaysOkHandler());
    }

    private class AlwaysOkHandler extends AbstractHandler {

        @Override
        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            baseRequest.getResponse().setStatus(SC_OK);
            baseRequest.setHandled(true);
        }

    }

}
