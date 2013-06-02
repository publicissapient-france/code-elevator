package elevator.server;

import elevator.server.port.Port;
import elevator.server.port.PortFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class ElevatorServer {

    public Port start() {
        Port port = PortFactory.newPort();
        Server server = new Server(port.port());
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder holder = context.addServlet(DefaultServlet.class, "/*");
        holder.setInitParameter("resourceBase", "elevator-server/src/main/webapp");
        holder.setInitParameter("pathInfoOnly", "true");

        ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/resources/*");
        servletHolder.setInitParameter("javax.ws.rs.Application", ElevatorApplication.class.getName());

        try {
            server.start();
            return port;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) {
        Port port = new ElevatorServer().start();
        System.out.println("elevator server started on port " + port);
    }

}
