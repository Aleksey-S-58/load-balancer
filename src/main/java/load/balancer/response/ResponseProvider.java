package load.balancer.response;

import java.io.IOException;

import load.balancer.socket.connector.SocketConnector;

public interface ResponseProvider {
    void provideInformationResponse(SocketConnector connector) throws IOException;
    void provideErrorResponse(SocketConnector connector) throws IOException;
    void provideResourceNotFoundResponse(SocketConnector connector) throws IOException;
    void provideUnauthorizedResponse(SocketConnector connector) throws IOException;
}
