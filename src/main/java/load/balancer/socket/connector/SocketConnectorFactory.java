package load.balancer.socket.connector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import load.balancer.exceptions.ConnectorException;
import load.balancer.request.RequestInfo;
import load.balancer.request.RequestSchema;

public class SocketConnectorFactory implements ConnectorFactory {

    protected final static int[] SSL_PROTPCOL_BYTES = {20, 21, 22, 23, 255};
    public final static String HTTP_REQUEST_SCHEMA = "http";
    public final static String HTTPS_REQUEST_SCHEMA = "https";

    protected final SSLSocketFactory socketFactory;

    public SocketConnectorFactory(SSLSocketFactory socketFactory) {
        super();
        this.socketFactory = socketFactory;
    }

    @Override
    public SocketConnector createConnector(RequestInfo request) throws ConnectorException {
        try {
            if (RequestSchema.HTTP == request.schema()) {
                return new SocketConnector(new Socket(request.host(), request.port()), false);
            }
            if (RequestSchema.HTTPS == request.schema()) {
                return new SocketConnector(socketFactory.createSocket(request.host(), request.port()), true);
            }
        } catch (IOException e) {
            throw new ConnectorException(e);
        }
        throw new ConnectorException(String.format("Unsupported request schema %s", request.schema()));
    }

    @Override
    public SocketConnector createConnector(Socket socket) throws ConnectorException {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1];
            inputStream.read(bytes);
            InputStream consumed = new ByteArrayInputStream(bytes);
            if (isSSLConnection(bytes)) {
                SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(socket, consumed, 
                        false);
                sslSocket.setUseClientMode(false);
                return new SocketConnector(sslSocket, true);
            } else {
                return new SocketConnector(socket, bytes);
            }
        } catch (IOException e) {
            try {
                socket.close();
                throw new ConnectorException(e);
            } catch (IOException ex) {
                throw new ConnectorException(ex);
            }
        }
    }

    protected boolean isSSLConnection(byte[] bytes) {
        int firstByte = bytes[0];
        for (int b : SSL_PROTPCOL_BYTES) {
            if (b == firstByte) {
                return true;
            }
        }
        return false;
    }
}
