package load.balancer.socket.connector;

import java.net.Socket;

import load.balancer.exceptions.ConnectorException;
import load.balancer.request.RequestInfo;

public interface ConnectorFactory {
    SocketConnector createConnector(Socket socket) throws ConnectorException;
    /**
     * The method creates connector to the destination endpoint specified by request.
     * @throws ConnectorException - if the connection can't be established.
     */
    SocketConnector createConnector(RequestInfo request) throws ConnectorException;
}
