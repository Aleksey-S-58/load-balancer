package load.balancer.transmitter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import load.balancer.socket.connector.SocketConnector;

public class DataTransmitter extends AbstractTransmitter {

    private static final Logger logger = LoggerFactory.getLogger(DataTransmitter.class);

    public DataTransmitter(SocketConnector inputConnector, SocketConnector outputConnector, String name) {
        super(inputConnector, outputConnector, name);
    }

    @Override
    protected void transmit(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        do {
            read = inputStream.read(buffer);
            if (read > 0) {
                outputStream.write(buffer, 0, read);
                if (inputStream.available() < 1) {
                    outputStream.flush();
                }
            }
        } while (read >= 0);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
