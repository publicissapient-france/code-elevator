package elevator.server;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import static org.fest.assertions.Assertions.assertThat;

class DontConnectURLStreamHandler extends URLStreamHandler {

    private final URLConnection urlConnection;
    private final String expectedURL;

    DontConnectURLStreamHandler(String expectedURL, URLConnection urlConnection) {
        this.expectedURL = expectedURL;
        this.urlConnection = urlConnection;
    }

    @Override
    protected URLConnection openConnection(URL actualURL) throws IOException {
        assertThat(actualURL.toExternalForm()).isEqualTo(expectedURL);
        return urlConnection;
    }

}
