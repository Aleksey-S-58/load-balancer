package load.balancer;

import org.slf4j.LoggerFactory;

public class Application {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        printWelcomeMessage();
    }

    private static void printWelcomeMessage() {
        LoggerFactory.getLogger(Application.class).info("The load balancer started!");
    }
}
