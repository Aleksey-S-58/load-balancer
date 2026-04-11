package load.balancer.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import load.balancer.socket.connector.SocketConnector;

public class SimpleRequestReader implements RequestReader {

    protected Logger logger = LoggerFactory.getLogger(SimpleRequestReader.class);

    @Override
    public RequestInfo readRequestInfo(SocketConnector connector) throws ConnectException {
        try {
            RequestSchema schema = connector.isSSLSocket() ? RequestSchema.HTTPS : RequestSchema.HTTP;
            String hostname = connector.getLocalAddress().getCanonicalHostName();
            int port = connector.getLocalPort();
            InputStream stream = connector.getInputStream();
            String request = readLine(stream);
            String[] requestComponents = request.split(" ");
            HttpMethod method = HttpMethod.valueOf(requestComponents[0].toUpperCase());
            String uri = requestComponents[1];
            String protocolVersion = requestComponents[2];
            Map<String, String> headers = readHeaders(stream);
            Map<String, List<String>> uriParameters = getUriParameters(uri);
            return new RequestInfo(schema, method, hostname, port, 
                    uri, protocolVersion, headers, uriParameters);
        } catch (IOException e) {
            logger.error("An error occurred during the request reading", e);
            throw new ConnectException(e.getMessage());
        }
    }

    protected Map<String, List<String>> getUriParameters(String uri) {
        Map<String, List<String>> parameters = new HashMap<>();
        int questionMark = uri.indexOf("?");
        if (questionMark > -1) {
            String str = uri.substring(1 + questionMark);
            String[] components = str.split("&");
            for (String component : components) {
                int equalSign = component.indexOf("=");
                if (equalSign > -1) {
                    String[] pair = component.split("=");
                    List<String> values = parameters.get(pair[0]);
                    if (values != null) {
                        values.add(pair[1]);
                    } else {
                        values = new ArrayList<>();
                        values.add(pair[1]);
                        parameters.put(pair[0], values);
                    }
                }
            }
        }
        return parameters;
    }

    protected Map<String, String> readHeaders(InputStream stream) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String header;
        do {
            header = readLine(stream);
            if (!"".equals(header)) {
                int colun = header.indexOf(":");
                if (colun > -1) {
                    headers.put(header.substring(0, colun), header.substring(colun + 1));
                }
            }
        } while (!"".equals(header));
        return headers;
    }

    protected String readLine(InputStream stream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int c;
        while ((c = stream.read()) != -1 || c != '\n') {
            if (c != '\r') {
                byteArrayOutputStream.write(c);
            }
        }
        return byteArrayOutputStream.toString("ISO-8859-1");
    }
}
