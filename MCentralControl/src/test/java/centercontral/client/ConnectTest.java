package centercontral.client;

import com.ll.centralcontrol.client.DeploymentClient;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/21
 */

@RunWith(JUnit4.class)
public class ConnectTest {

    @InjectMocks
    private DeploymentClient deploymentClient;

    @Mock
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(deploymentClient).build();
    }


}
