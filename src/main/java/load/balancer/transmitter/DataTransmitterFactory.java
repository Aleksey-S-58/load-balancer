package load.balancer.transmitter;

import load.balancer.socket.connector.SocketConnector;

public class DataTransmitterFactory implements TransmitterFactory {

    @Override
    public AbstractTransmitter createTransmitter(SocketConnector inputConnector, SocketConnector outputConnector,
            String name) {
        return new DataTransmitter(inputConnector, outputConnector, name);
    }

}
