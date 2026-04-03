package load.balancer.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import load.balancer.response.strategy.ResponseStrategy;
import load.balancer.socket.connector.SocketConnector;

public class ConfigurableResponseProvider implements ResponseProvider {

    private final ResponseStrategy informationResponseStrategy;
    private final ResponseStrategy unauthorizedResponseStrategy;
    private final ResponseStrategy resourceNotFoundStrategy;
    private final ResponseStrategy serverErrorStrategy;

    public ConfigurableResponseProvider(ResponseStrategy informationResponseStrategy,
            ResponseStrategy unauthorizedResponseStrategy, ResponseStrategy resourceNotFoundStrategy,
            ResponseStrategy serverErrorStrategy) {
        super();
        this.informationResponseStrategy = informationResponseStrategy;
        this.unauthorizedResponseStrategy = unauthorizedResponseStrategy;
        this.resourceNotFoundStrategy = resourceNotFoundStrategy;
        this.serverErrorStrategy = serverErrorStrategy;
    }

    @Override
    public void provideInformationResponse(SocketConnector connector) throws IOException {
        provideResponse(informationResponseStrategy, connector);
    }

    @Override
    public void provideErrorResponse(SocketConnector connector) throws IOException {
        provideResponse(serverErrorStrategy, connector);
    }

    @Override
    public void provideResourceNotFoundResponse(SocketConnector connector) throws IOException {
        provideResponse(resourceNotFoundStrategy, connector);
    }

    @Override
    public void provideUnauthorizedResponse(SocketConnector connector) throws IOException {
        provideResponse(unauthorizedResponseStrategy, connector);
    }

    protected void provideResponse(ResponseStrategy strategy, SocketConnector connector) throws IOException {
        List<String> response = strategy.getResponse();
        if (!response.isEmpty()) {
            OutputStream stream = connector.getOutputStream();
            for (String line : response) {
                stream.write(line.getBytes());
            }
            stream.flush();
        }
    }
}
