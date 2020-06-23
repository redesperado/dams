package com.jqf.dams.shiroCfg;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    private static final Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        //初始化
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //必须配置SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //setLoginUrl如果不设置，默认会自动寻找Web工程目录下的“/login.jsp”页面或“/login”的映射
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        //登陆成功后的页面
        shiroFilterFactoryBean.setSuccessUrl("/main.html");
        //设置无权限时跳转的URL
        shiroFilterFactoryBean.setUnauthorizedUrl("/noRole");
        //设置拦截器
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        //游客，开放权限
//        filterChainDefinitionMap.put("/guest/**","anon");
        //用户，需要角色权限“user”
        filterChainDefinitionMap.put("/user/**","roles[user]");
        //管理员，需要角色权限“admin”
        filterChainDefinitionMap.put("/admin/**","roles[admin]");
        //开放登录接口
        filterChainDefinitionMap.put("/login","anon");

        //其余接口一律拦截
        //主要这行代码必须放在所有权限设置的最后，不然会导致所有 url 都被拦截
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        //filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        logger.info("------------Shiro拦截器工厂类注入成功-----------");
        return shiroFilterFactoryBean;
    }

    /**
     * 注入SecurityManager
     * @return
     */
    @Bean
    public DefaultSecurityManager securityManager(){
        //初始化
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm
        securityManager.setRealm(customRealm());
        return securityManager;
    }

    /**
     * 自定义身份认证realm
     * 必须加这个类，并加上@Bean注解，目的是注入CustomRealm
     * 否则会影响CustomRealm类中其他类的注入
     */
    @Bean
    public CustomRealm customRealm(){
        CustomRealm customRealm = new CustomRealm();
//        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customRealm;
    }

//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher(){
//        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
//        credentialsMatcher.setHashIterations(2);
//        credentialsMatcher.setHashAlgorithmName("md5");
//        return credentialsMatcher;
//    }

}