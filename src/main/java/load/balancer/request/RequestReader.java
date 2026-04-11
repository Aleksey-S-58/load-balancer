package load.balancer.request;

import java.net.ConnectException;

import load.balancer.socket.connector.SocketConnector;

public interface RequestReader {

    RequestInfo readRequestInfo(SocketConnector connector) throws ConnectException;
}
