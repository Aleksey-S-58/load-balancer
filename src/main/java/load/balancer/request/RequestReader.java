package load.balancer.request;

import load.balancer.socket.connector.SocketConnector;

public interface RequestReader {

    RequestInfo readRequestInfo(SocketConnector connector);
}
