package elevator.server;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import static org.fest.assertions.Assertions.assertThat;

class MockURLStreamHandler extends URLStreamHandler {

    private final URLConnection mock;
    private final String expectedURL;

    MockURLStreamHandler(String expectedURL, URLConnection mock) {
        this.expectedURL = expectedURL;
        this.mock = mock;
    }

    @Override
    protected URLConnection openConnection(URL actualURL) throws IOException {
        assertThat(actualURL.toExternalForm()).isEqualTo(expectedURL);
        return mock;
    }

}
