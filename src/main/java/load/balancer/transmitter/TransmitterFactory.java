package load.balancer.transmitter;

import load.balancer.socket.connector.SocketConnector;

public interface TransmitterFactory {

    AbstractTransmitter createTransmitter(SocketConnector inputConnector, SocketConnector outputConnector, String name);
}
