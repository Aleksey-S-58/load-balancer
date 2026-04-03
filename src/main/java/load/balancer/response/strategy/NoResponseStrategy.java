package load.balancer.response.strategy;

import java.util.List;

public class NoResponseStrategy implements ResponseStrategy {

    @Override
    public List<String> getResponse() {
        return List.of();
    }

}
