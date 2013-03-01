package elevator.server;

import static java.lang.Math.random;
import static java.lang.Math.round;

class RandomPort {

    final int port;

    RandomPort() {
        this.port = (int) round(random() * 10000) + 1024;
    }

}
