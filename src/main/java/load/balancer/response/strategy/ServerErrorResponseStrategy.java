package load.balancer.response.strategy;

public class ServerErrorResponseStrategy extends CustomResponseStrategy {

    public ServerErrorResponseStrategy(String filename) {
        super(500, ResponseHeaders.DEFAULT_PAGE_HEADERS, filename);
    }
}
