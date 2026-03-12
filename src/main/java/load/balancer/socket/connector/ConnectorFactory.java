package load.balancer.socket.connector;

import java.net.Socket;

import load.balancer.exceptions.ConnectorException;

public interface ConnectorFactory {
    SocketConnector createConnector(Socket socket) throws ConnectorException;
}
