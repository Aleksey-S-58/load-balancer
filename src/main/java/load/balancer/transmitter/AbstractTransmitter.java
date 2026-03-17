package load.balancer.transmitter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractTransmitter implements Runnable {

    @Override
    public void run() {
        // TODO call method transmit
    }

    protected abstract void transmit(InputStream inputStream, OutputStream outputStream) throws IOException;

}
