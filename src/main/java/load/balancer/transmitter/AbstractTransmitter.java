package load.balancer.transmitter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;

import load.balancer.socket.connector.SocketConnector;

public abstract class AbstractTransmitter implements Runnable {

    protected final SocketConnector inputConnector;
    protected final SocketConnector outputConnector;
    protected final String name;

    protected AbstractTransmitter(SocketConnector inputConnector, SocketConnector outputConnector, String name) {
        this.name = name;
        this.inputConnector = inputConnector;
        this.outputConnector = outputConnector;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = inputConnector.getInputStream();
            OutputStream outputStream = outputConnector.getOutputStream();
            transmit(inputStream, outputStream);
            inputConnector.close();
            outputConnector.close();
        } catch (IOException e) {
            getLogger().error("Transmitter {} can't read/write from connection: {}", name, e);
        }
    }

    protected abstract void transmit(InputStream inputStream, OutputStream outputStream) throws IOException;
    protected abstract Logger getLogger();

}
