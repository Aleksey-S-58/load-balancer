package load.balancer.response.strategy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CustomResponseStrategy implements ResponseStrategy {

    public static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";
    public static final String PAGE_FOLDER = "pages";
    protected final int status;
    protected final List<String> response;

    public CustomResponseStrategy(int status, Map<String, String> headers, String pageFileName) {
        this.status = status;
        PageLoader pageLoader = new PageLoader(PAGE_FOLDER);
        List<String> page = pageLoader.loadFromFile(pageFileName);
        response = new ArrayList<>(page.size() + 3 + headers.size());
        addProtocolVersionAndStatus(response, status);
        addDateHeader(response);
        addAllHeaders(response, headers);
        addSeparator(response);
        response.addAll(page);
    }

    protected void addProtocolVersionAndStatus(List<String> response, int status) {
        StringBuilder builder = new StringBuilder("HTTP/2 ");
        builder.append(status);
        builder.append("\r\n");
        response.add(builder.toString());
    }

    protected void addDateHeader(List<String> response) {
        StringBuilder builder = new StringBuilder("date: ");
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        builder.append(formatter.format(new Date()));
        builder.append("\r\n");
        response.add(builder.toString());
    }

    protected void addAllHeaders(List<String> response, Map<String, String> headers) {
        headers.forEach((name, value) -> {
            StringBuilder builder = new StringBuilder(name);
            builder.append(": ");
            builder.append(value);
            builder.append("\r\n");
            response.add(builder.toString());
        });
    }

    protected void addSeparator(List<String> response) {
        response.add("\r\n");
    }

    @Override
    public List<String> getResponse() {
        return response;
    }

}
