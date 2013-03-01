package elevator;

import com.sun.net.httpserver.HttpServer;

class StartedElevatorServer {
    StartedElevatorServer(HttpServer httpServer) {
    }

    ElevatorClient addElevatorClient(String email, String url) {
        return new ElevatorClient(email, url);
    }

    User addUser(ElevatorClient elevatorClient) {
        return new User();
    }

}
