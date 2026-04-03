package load.balancer.response.strategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UnauthorizedResponseStrategy extends CustomResponseStrategy {

    public UnauthorizedResponseStrategy(List<String> challenges, String filename) {
        super(401, 
                challenges.stream()
                    .collect(Collectors.toMap(
                            c -> ResponseHeaders.DEFAULT_AUTHENTICATION_HEADER_NAME, 
                            Function.identity())), 
                filename);
    }

    public UnauthorizedResponseStrategy(String challenge, String filename) {
        super(401, Map.of(ResponseHeaders.DEFAULT_AUTHENTICATION_HEADER_NAME, challenge), filename);
    }

    private UnauthorizedResponseStrategy(Map<String, String> headers, String filename) {
        super(401, headers, filename);
    }

    public static UnauthorizedResponseStrategy basicUnauthorizedResponseStrategy(String realm, String filename) {
        return new UnauthorizedResponseStrategy(
                ResponseHeaders.basicAuthenticationHeader(realm), 
                filename);
    }
}
