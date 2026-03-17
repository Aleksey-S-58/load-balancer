package load.balancer.socket.connector;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class SocketConnector implements Closeable {

    protected final boolean isSSLSocket;
    protected final Socket socket;
    protected volatile byte[] initialBytes;

    public SocketConnector(Socket socket, boolean isSSLSocket) {
        this.isSSLSocket = isSSLSocket;
        this.socket = socket;
        initialBytes = null;
    }

    /**
     * Creates simple HTTP connector.
     * @param socket - an existing socket.
     * @param bytes - bytes were read during SSL protocol inspection or null.
     */
    public SocketConnector(Socket socket, byte[] bytes) {
        this.socket = socket;
        this.isSSLSocket = false;
        initialBytes = bytes;
    }

    public boolean isSSLSocket() {
        return isSSLSocket;
    }

    public void connect(SocketAddress endpoint) throws IOException {
        socket.connect(endpoint);
    }

    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        socket.connect(endpoint, timeout);
    }

    public void bind(SocketAddress bindpoint) throws IOException {
        socket.bind(bindpoint);
    }

    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }

    public InetAddress getLocalAddress() {
        return socket.getLocalAddress();
    }

    public int getPort() {
        return socket.getPort();
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }

    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    public SocketAddress getLocalSocketAddress() {
        return socket.getLocalSocketAddress();
    }

    public SocketChannel getChannel() {
        return socket.getChannel();
    }

    public synchronized InputStream getInputStream() throws IOException {
        if (!isSSLSocket && initialBytes != null && initialBytes.length > 0) {
            InputStream inputStream = new SequenceInputStream(new ByteArrayInputStream(initialBytes), 
                    socket.getInputStream());
            initialBytes = null; // TODO I don't like this solution.
            return inputStream;
        }
        return socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public void setTcpNoDelay(boolean on) throws SocketException {
        socket.setTcpNoDelay(on);
    }

    public boolean getTcpNoDelay() throws SocketException {
        return socket.getTcpNoDelay();
    }

    public void setSoLinger(boolean on, int linger) throws SocketException {
        socket.setSoLinger(on, linger);
    }

    public int getSoLinger() throws SocketException {
        return socket.getSoLinger();
    }

    public void sendUrgentData(int data) throws IOException {
        socket.sendUrgentData(data);
    }

    public void setOOBInline(boolean on) throws SocketException {
        socket.setOOBInline(on);
    }

    public boolean getOOBInline() throws SocketException {
        return socket.getOOBInline();
    }

    public void setSoTimeout(int timeout) throws SocketException {
        socket.setSoTimeout(timeout);
    }

    public int getSoTimeout() throws SocketException {
        return socket.getSoTimeout();
    }

    public void setSendBufferSize(int size) throws SocketException {
        socket.setSendBufferSize(size);
    }

    public int getSendBufferSize() throws SocketException {
        return socket.getSendBufferSize();
    }

    public void setReceiveBufferSize(int size) throws SocketException {
        socket.setReceiveBufferSize(size);
    }

    public int getReceiveBufferSize() throws SocketException {
        return socket.getReceiveBufferSize();
    }

    public void setKeepAlive(boolean on) throws SocketException {
        socket.setKeepAlive(on);
    }

    public boolean getKeepAlive() throws SocketException {
        return socket.getKeepAlive();
    }

    public void setTrafficClass(int tc) throws SocketException {
        socket.setTrafficClass(tc);
    }

    public int getTrafficClass() throws SocketException {
        return socket.getTrafficClass();
    }

    public void setReuseAddress(boolean on) throws SocketException {
        socket.setReuseAddress(on);
    }

    public boolean getReuseAddress() throws SocketException {
        return socket.getReuseAddress();
    }

    public String toString() {
        return socket.toString();
    }

    public boolean isConnected() {
        return socket.isConnected();
    }

    public boolean isBound() {
        return socket.isBound();
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
        socket.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

    public <T> Socket setOption(SocketOption<T> name, T value) throws IOException {
        return socket.setOption(name, value);
    }

    public <T> T getOption(SocketOption<T> name) throws IOException {
        return socket.getOption(name);
    }

    public Set<SocketOption<?>> supportedOptions() {
        return socket.supportedOptions();
    }

    @Override
    public void close() throws IOException {
        if (isSSLSocket) {
            closeSSLSocket();
        } else {
            closeSocket();
        }
    }

    protected void closeSSLSocket() throws IOException {
        if (!socket.isClosed()) {
            socket.close();
        }
    }

    protected void closeSocket() throws IOException {
        if (!socket.isInputShutdown() && !socket.isClosed()) {
            socket.shutdownInput();
        }
        if (!socket.isOutputShutdown() && !socket.isClosed()) {
            socket.shutdownOutput();
        }
        if (!socket.isClosed()) {
            socket.close();
        }
    }
}
