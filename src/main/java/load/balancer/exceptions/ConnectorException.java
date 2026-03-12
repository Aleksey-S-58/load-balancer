package load.balancer.exceptions;

public class ConnectorException extends Exception {

    private static final long serialVersionUID = -2041588538951324819L;

    public ConnectorException(Exception e) {
        super(e);
    }

    public ConnectorException(String message) {
        super(message);
    }
}
