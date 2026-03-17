package load.balancer.socket.connector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import load.balancer.exceptions.ConnectorException;
import load.balancer.request.RequestInfo;

public class SocketConnectorFactory implements ConnectorFactory {

    protected final static int[] SSL_PROTPCOL_BYTES = {20, 21, 22, 23, 255};

    protected final SSLSocketFactory socketFactory;

    public SocketConnectorFactory(SSLSocketFactory socketFactory) {
        super();
        this.socketFactory = socketFactory;
    }

    @Override
    public SocketConnector createConnector(RequestInfo request) throws ConnectorException {
        // TODO implement connector on the basis of request info with ssl/plain http socket according to the request schema.
        return null;
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
