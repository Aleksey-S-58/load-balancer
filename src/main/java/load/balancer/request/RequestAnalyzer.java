package load.balancer.request;

public interface RequestAnalyzer {
    /**
     * The method analyzes request schema, HTTP-method and HTTP-headers to identify destination endpoint.
     * If necessary the method can modify the request to the destination endpoint, 
     * the request schema and headers can be changed. 
     * @param request - contains schema, HTTP-method and HTTP-headers
     * @return a request to the destination endpoint if the endpoint presents in a list or Optional.empty
     */
    RequestInfo getDestinationRequest(RequestInfo receivedRequest);
}
