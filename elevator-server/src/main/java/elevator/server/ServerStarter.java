package elevator.server;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import static org.glassfish.jersey.server.monitoring.ApplicationEvent.Type.INITIALIZATION_FINISHED;

class ServerStarter implements ApplicationEventListener {
    private final ElevatorServer server;

    ServerStarter(ElevatorServer server) {
        this.server = server;
    }

    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        if (INITIALIZATION_FINISHED == applicationEvent.getType()) {
            new Thread(server::start).start();
        }
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return null;
    }
}
