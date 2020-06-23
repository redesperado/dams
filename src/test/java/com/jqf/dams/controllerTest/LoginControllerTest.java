package com.jqf.dams.controllerTest;

import com.jqf.dams.controller.LoginController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class LoginControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(LoginControllerTest.class);

    private MockMvc mockMvc = null;

    @InjectMocks
    @Autowired
    private LoginController loginController;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();

    }

    @Test
    public void loginIn() throws Exception {
        try{
            mockMvc.perform(MockMvcRequestBuilders.post("/login/loginIn")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .param("userName","admin")
                    .param("userPwd","admin"))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        }catch (Exception e){
            logger.error("loginIn error:",e);
        }
    }

}
