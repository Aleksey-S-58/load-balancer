package load.balancer;

import java.io.Closeable;
import java.io.IOException;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class SimpleTest {

    @Mock
    private Closeable resource;

    @BeforeTest
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test() throws IOException {
        resource.close();
    }
}
