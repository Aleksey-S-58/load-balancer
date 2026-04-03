package load.balancer.response.config;

public class ResponseProviderConfiguration {
    private StrategyConfiguration informationResponse;
    private StrategyConfiguration serverErrorResponse;
    private StrategyConfiguration resourceNotFoundResponse;
    private StrategyConfiguration unauthorizedResponse;

    public ResponseProviderConfiguration() {

    }

    public StrategyConfiguration getInformationResponse() {
        return informationResponse;
    }

    public void setInformationResponse(StrategyConfiguration informationResponse) {
        this.informationResponse = informationResponse;
    }

    public StrategyConfiguration getServerErrorResponse() {
        return serverErrorResponse;
    }

    public void setServerErrorResponse(StrategyConfiguration serverErrorResponse) {
        this.serverErrorResponse = serverErrorResponse;
    }

    public StrategyConfiguration getResourceNotFoundResponse() {
        return resourceNotFoundResponse;
    }

    public void setResourceNotFoundResponse(StrategyConfiguration resourceNotFoundResponse) {
        this.resourceNotFoundResponse = resourceNotFoundResponse;
    }

    public StrategyConfiguration getUnauthorizedResponse() {
        return unauthorizedResponse;
    }

    public void setUnauthorizedResponse(StrategyConfiguration unauthorizedResponse) {
        this.unauthorizedResponse = unauthorizedResponse;
    }
}
