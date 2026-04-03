package load.balancer.response;

import java.util.Map;
import java.util.function.Function;

import load.balancer.response.config.ResponseProviderConfiguration;
import load.balancer.response.config.StrategyConfiguration;
import load.balancer.response.strategy.CustomResponseStrategy;
import load.balancer.response.strategy.NoResponseStrategy;
import load.balancer.response.strategy.ResourceNotFoundResponseStrategy;
import load.balancer.response.strategy.ResponseStrategy;
import load.balancer.response.strategy.ServerErrorResponseStrategy;
import load.balancer.response.strategy.UnauthorizedResponseStrategy;

public class ResponseProviderFactory {
    private final Map<String, Function<StrategyConfiguration, ResponseStrategy>> builders;

    public ResponseProviderFactory() {
        builders = Map.of(CustomResponseStrategy.class.getSimpleName(), 
                c -> new CustomResponseStrategy(c.getStatusCode(), c.getHeaders(), c.getFileName()), 
                NoResponseStrategy.class.getSimpleName(), 
                c -> new NoResponseStrategy(), 
                ServerErrorResponseStrategy.class.getSimpleName(), 
                c -> new ServerErrorResponseStrategy(c.getFileName()), 
                ResourceNotFoundResponseStrategy.class.getSimpleName(), 
                c -> new ResourceNotFoundResponseStrategy(c.getFileName()), 
                UnauthorizedResponseStrategy.class.getSimpleName(), 
                c -> new UnauthorizedResponseStrategy(c.getChallenges(), c.getFileName()));
    }

    public ResponseProvider create(ResponseProviderConfiguration configuration) {
        // TODO Provide default configuration.
        var informationResponseStrategy = createStrategy(configuration.getInformationResponse());
        var serverErrorResponseStrategy = createStrategy(configuration.getServerErrorResponse());
        var resourceNotFoundResponseStrategy = createStrategy(configuration.getResourceNotFoundResponse());
        var unauthorizedResponseStrategy = createStrategy(configuration.getUnauthorizedResponse());
        return new ConfigurableResponseProvider(informationResponseStrategy, 
                unauthorizedResponseStrategy, 
                resourceNotFoundResponseStrategy, 
                serverErrorResponseStrategy);
    }

    private ResponseStrategy createStrategy(StrategyConfiguration configuration) {
        Function<StrategyConfiguration, ResponseStrategy> builder = builders.get(configuration.getName());
        if (builder == null) {
            throw new IllegalArgumentException(String.format("Unsupported strategy %s", configuration.getName()));
        }
        return builder.apply(configuration);
    }
}
