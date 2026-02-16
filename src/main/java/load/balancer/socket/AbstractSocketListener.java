package load.balancer.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import javax.net.ServerSocketFactory;

import org.slf4j.Logger;

public abstract class AbstractSocketListener implements Runnable {

    protected final int port;
    protected final ServerSocketFactory serverSocketFactory;
    protected final ExecutorService requestFlowExecutor;
    protected final ExecutorService responseFlowxecutor;

    public AbstractSocketListener(int port, ExecutorService requestFlowExecutor, ExecutorService responseFlowxecutor) {
        this.port = port;
        this.requestFlowExecutor = requestFlowExecutor;
        this.responseFlowxecutor = responseFlowxecutor;
        serverSocketFactory = ServerSocketFactory.getDefault();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = serverSocketFactory.createServerSocket(port)) {
            Socket socket;
            try {
                while ((socket = serverSocket.accept()) != null && !Thread.currentThread().isInterrupted()) {
                    requestFlowExecutor.submit(createSocketHandler(socket, responseFlowxecutor));
                }
            } catch (IOException e) {
                getLogger().error("Unable to read from socket", e);
            }
        } catch (IOException e) {
            getLogger().error("Can't initiate server socket!", e);
        }
    }

    protected abstract Logger getLogger();
    protected abstract Runnable createSocketHandler(Socket socket, ExecutorService executor);
}
