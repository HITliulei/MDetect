package centercontral.controller;

import com.alibaba.fastjson.JSONObject;
import com.ll.centralcontrol.controller.DeploymentController;
import com.ll.centralcontrol.service.DeploymentService;
import com.ll.common.utils.MResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/21
 */

@RunWith(JUnit4.class)
public class DeploymentTest {
    @InjectMocks
    private DeploymentController deploymentController;

    @Mock
    private DeploymentService deploymentService;
    private MockMvc mockMvc;

    private MResponse mResponse = new MResponse();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(deploymentController).build();
    }


    @Test
    public void testExecuteTicket() throws Exception {
        Mockito.when(deploymentController.deploycommand(Mockito.any(HttpHeaders.class))).thenReturn(mResponse);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/deployment/deployment"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assert.assertEquals(mResponse, JSONObject.parseObject(result, MResponse.class));
    }
}
