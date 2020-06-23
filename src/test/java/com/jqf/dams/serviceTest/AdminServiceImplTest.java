package com.jqf.dams.serviceTest;

import com.jqf.dams.bean.AdminBean;
import com.jqf.dams.service.AdminService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Transactional
public class AdminServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImplTest.class);

    @Resource
    private AdminService adminService;

    @Test
    public void selectAllUserInfo(){
        try{
//            List<AdminBean> adminBeans = adminService.selectAllUserInfo(1,10);
//            for (AdminBean adminBean : adminBeans) {
//                System.out.println(adminBean.getUserName());
//            }
        }catch (Exception e){
            logger.error("selectAllUserInfo error:",e);
        }
    }

    @Test
    public void getPassword(){
        try{
            String pwd = adminService.getPassword("admin");
            System.out.println(pwd);
        }catch (Exception e){
            logger.error("getPassword error:",e);
        }
    }

    @Test
    public void getUserRole(){
        try{
            String role = adminService.getUserRole("admin");
            System.out.println(role);
        }catch (Exception e){
            logger.error("getUserRole error:",e);
        }
    }
}
