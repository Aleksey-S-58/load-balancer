package load.balancer.response.strategy;

public class ResourceNotFoundResponseStrategy extends CustomResponseStrategy {

    public ResourceNotFoundResponseStrategy(String pageFileName) {
        super(404, ResponseHeaders.DEFAULT_PAGE_HEADERS, pageFileName);
    }
}
