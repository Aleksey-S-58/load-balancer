package load.balancer.response.strategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageLoader {

    private static final Logger logger = LoggerFactory.getLogger(PageLoader.class);

    private final String resourceFolder;

    public PageLoader(String resourceFolder) {
        this.resourceFolder = resourceFolder;
    }

    public List<String> loadFromFile(String fileName) {
        try {
            var path = Paths.get(resourceFolder, fileName);
            var stream = Files.lines(path);
            var page = stream.collect(Collectors.toList());
            stream.close();
            return page;
        } catch (IOException e) {
            logger.error("Can't load page from file: {} Error: {}", fileName, e.getMessage());
        }
        return List.of();
    }
}
