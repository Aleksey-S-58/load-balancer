package load.balancer.handler;

import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;

import load.balancer.exceptions.ConnectorException;
import load.balancer.request.RequestInfo;
import load.balancer.socket.connector.SocketConnector;
import load.balancer.transmitter.AbstractTransmitter;

public abstract class AbstractSocketHandler implements Runnable {

    protected final Socket socket;
    protected final ExecutorService responseExecutor;

    public AbstractSocketHandler(Socket socket, ExecutorService responseExecutor) {
        super();
        this.socket = socket;
        this.responseExecutor = responseExecutor;
    }

    @Override
    public void run() {
        try {
            SocketConnector connector = getSocketConnector(socket);
            try {
                RequestInfo request = readRequestHeaders(connector);
                if (isAuthenticated(request)) {
                    Optional<RequestInfo> destinationRequest = analyzeRequest(request);
                    if (destinationRequest.isPresent()) {
                        SocketConnector destinationEndpointConnector = createDestinationEndpointConnector(destinationRequest.get());
                        AbstractTransmitter responseTransmitter = createDataTransmitter(destinationEndpointConnector, connector, "Response-transmitter");
                        responseExecutor.submit(responseTransmitter);
                        forwardRequestHeaders(destinationEndpointConnector);
                        AbstractTransmitter requestTransmitter = createDataTransmitter(connector, destinationEndpointConnector, "Request-transmitter");
                        requestTransmitter.run();
                    } else {
                        sendNotFoundResponse(connector);
                    }
                } else {
                    sendUnauthorizedResponse(connector); 
                }
            } catch (ConnectorException e) {
                getLogger().error("An error occurred during request processing!", e);
                reportServerErrorAndClose(connector);
            }
        } catch (ConnectorException e) {
            getLogger().error("Can't establish client connection!", e);
        }
    }

    protected abstract boolean isAuthenticated(RequestInfo request);
    /**
     * The method reads request schema, HTTP-method and HTTP-headers from the request.
     * Use RequestReader implementation: {@link load.balancer.request.RequestReader#readRequestInfo(connector)}.
     * @throws ConnectorException if method can't read data from the request connection.
     */
    protected abstract RequestInfo readRequestHeaders(SocketConnector connector) throws ConnectorException;
    /**
     * The method analyzes request schema, HTTP-method and HTTP-headers to identify destination endpoint.
     * If necessary the method can modify the request to the destination endpoint, 
     * the request schema and headers can be changed. 
     * (see {@link load.balancer.request.RequestAnalyzer#getDestinationRequest(request)})
     * @param request - contains schema, HTTP-method and HTTP-headers
     * @return a request to the destination endpoint if the endpoint presents in a list or Optional.empty
     */
    protected abstract Optional<RequestInfo> analyzeRequest(RequestInfo request);
    /**
     * You can create socket based SocketConnector via {@link load.balancer.socket.connector.ConnectorFactory#createConnector(socket)}
     * It will automatically detect SSL protocol and create appropriate connector.
     */
    protected abstract SocketConnector getSocketConnector(Socket socket) throws ConnectorException;
    /**
     * The method creates HTTP/SSL connection to the specified in destinationRequest endpoint according to the specified request schema.
     * (see {@link load.balancer.socket.connector.ConnectorFactory#createConnector(destinationRequest)})
     * @throws ConnectorException - if the connection to the destination endpoint can't be established.
     */
    protected abstract SocketConnector createDestinationEndpointConnector(RequestInfo destinationRequest) throws ConnectorException;
    /**
     * The method creates a transmitter which transmits data from input connector to output connector.
     * @throws ConnectorException if transmitter can't read from input connector or can't write to output connector
     */
    protected abstract AbstractTransmitter createDataTransmitter(SocketConnector inputConnector, SocketConnector outputConnector, String name) throws ConnectorException;
    /**
     * The method sends unauthorized response to the client according to the specified strategy 
     * which may include redirect to a new location.
     * @throws ConnectorException - if the method can't write to the connection.
     */
    protected abstract void sendUnauthorizedResponse(SocketConnector connector) throws ConnectorException;
    /**
     * The method responds to the client with 404 response according to the specified strategy.
     */
    protected abstract void sendNotFoundResponse(SocketConnector connector) throws ConnectorException;
    /**
     * The method responds to the client with the Internal Server Error message 
     * not revealing the error and closes the client connection.
     */
    protected abstract void reportServerErrorAndClose(SocketConnector connector);
    /**
     * The method writes only request headers to the destination endpoint.
     * @throws ConnectorException - if the method can't write to the destination endpoint connection.
     */
    protected abstract void forwardRequestHeaders(SocketConnector destinationEndpointConnector) throws ConnectorException;
    protected abstract Logger getLogger();
}
