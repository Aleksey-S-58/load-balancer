package load.balancer.request;

import java.util.List;
import java.util.Map;

/**
 * Contains HTTP-request schema, HTTP-method and headers.
 */
public record RequestInfo(RequestSchema schema, HttpMethod method, String host, int port,
        String uri, String protocolVersion, Map<String, String> headers, Map<String, List<String>> uriParameters) {

}
