package load.balancer.socket;

import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainSocketListener extends AbstractSocketListener {

    private static final Logger logger = LoggerFactory.getLogger(MainSocketListener.class);

    public static final String MAIN_SOCKET_PORT = "main.port";

    public MainSocketListener(Properties config, ExecutorService requestFlowExecutor, ExecutorService responseFlowxecutor) {
        super(Integer.parseInt(config.getProperty(MAIN_SOCKET_PORT)), requestFlowExecutor, responseFlowxecutor);
    }

    protected Logger getLogger() {
        return logger;
    }

    @Override
    protected Runnable createSocketHandler(Socket socket, ExecutorService executor) {
        return new Runnable() {

            @Override
            public void run() {
                // TODO implement socket handler in a separate class.
            }

        };
    }
}
