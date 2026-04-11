package load.balancer.request;

public enum RequestSchema {

    HTTP("http"), HTTPS("https");

    private String schema;

    private RequestSchema(String schema) {
        this.schema = schema;
    }

    public String getSchema() {
        return schema;
    }
}
