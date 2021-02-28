import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public class BaseServiceTest {
    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.initMocks(this);
    }

}
