package load.balancer.request;

import java.util.Map;

/**
 * Contains HTTP-request schema, HTTP-method and headers.
 */
public record RequestInfo(String schema, HttpMethod method, String host, int port,
        String uri, Map<String, String> headers, Map<String, String> uriParameters) {

}
