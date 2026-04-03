package load.balancer.response.strategy;

import java.util.Map;

public class ResponseHeaders {
    public final static String DEFAULT_AUTHENTICATION_HEADER_NAME = "WWW-Authenticate";
    public final static String BASIC_CHALLENGE_TEMPLATE = "Basic realm=\"%s\", charset=\"UTF-8\"";
    public final static String DIGEST_CHALLENGE_TEMPLATE = "Digest realm=\"%s\", qop=\"%s\", nonce=\"%s\", algorithm=%s, opaque=\"%s\"";

    public final static Map<String, String> DEFAULT_PAGE_HEADERS = Map.of(
            "content-type","text/html; charset=utf-8"
            );
    public static Map<String, String> basicAuthenticationHeader(String realm) {
        return Map.of(DEFAULT_AUTHENTICATION_HEADER_NAME, String.format(BASIC_CHALLENGE_TEMPLATE, realm));
    }
    public static Map<String, String> digestAuthenticationHeader(String realm, String qop, String nonce, String algorithm, String opaque) {
        return Map.of(DEFAULT_AUTHENTICATION_HEADER_NAME, String.format(DIGEST_CHALLENGE_TEMPLATE, realm, qop, nonce, algorithm, opaque));
    }
}
