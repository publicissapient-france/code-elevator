package elevator.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.junit.runners.model.Statement;

import java.net.InetSocketAddress;

class JettyServerStatement extends Statement {

    private final Statement base;
    private final Handler handler;

    JettyServerStatement(Statement base, Handler handler) {
        this.base = base;
        this.handler = handler;
    }

    @Override
    public void evaluate() throws Throwable {
        InetSocketAddress address = new InetSocketAddress("localhost", 8080);
        Server server = new Server(address);
        server.setHandler(handler);
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
