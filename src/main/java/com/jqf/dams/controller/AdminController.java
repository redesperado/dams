package com.jqf.dams.controller;

import com.github.pagehelper.Page;
import com.jqf.dams.bean.AdminBean;
import com.jqf.dams.service.AdminService;
import com.jqf.dams.util.Constants;
import com.github.pagehelper.PageInfo;
import com.jqf.dams.util.ResultObject;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class AdminController {

    @Resource
    private AdminService adminService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @ResponseBody
    @RequestMapping("/users")
    public ResultObject showAlluserInfo(@RequestParam(value = "page") Integer page,
                                        @RequestParam(value = "limit") Integer limit){
        ResultObject resultObject = new ResultObject();

        int userCount = adminService.selectUserCount();

        Page<AdminBean> adminBeans = adminService.selectAllUserInfo(page,limit);

        // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
        PageInfo<AdminBean> pageInfo = new PageInfo<>(adminBeans);
        if (adminBeans == null) {
            logger.info("未查询到用户信息");
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("查询失败");
            resultObject.setResult(false);
        } else {
            logger.info("查询用户信息："+adminBeans.toString());
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("查询成功");
            resultObject.setResult(true);
            resultObject.setCount(userCount);
            resultObject.setData(pageInfo.getList());
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteUserById",method = RequestMethod.POST)
    public ResultObject deleteUserById(String id){
        logger.info("根据ID删除用户入参："+id);
        ResultObject resultObject = new ResultObject();

        int flag = adminService.delUserById(id);
        if(flag > 0){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("删除成功");
            resultObject.setResult(true);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("删除失败");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/slectUserByName",method = RequestMethod.GET)
    public ResultObject slectUserByName(String userName,Integer page,Integer limit){
        logger.info("根据用户名查询用户信息入参"+userName);
        ResultObject resultObject = new ResultObject();
        Page<AdminBean> adminBean = adminService.slectUserByName(userName,page,limit);
        PageInfo<AdminBean> pageInfo = new PageInfo<>(adminBean);
        if(adminBean != null){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("查询成功");
            resultObject.setResult(true);
            resultObject.setCount((int)pageInfo.getTotal());
            resultObject.setData(pageInfo.getList());
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("查询失败");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/auditUserName",method = RequestMethod.GET)
    public ResultObject auditUserName(String userName){
        logger.info("用户名校验入参"+userName);
        ResultObject resultObject = new ResultObject();
        int flag = adminService.auditUserName(userName);
        if(flag <= 0){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("校验通过");
            resultObject.setResult(true);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("用户名已存在,请重新输入！");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/modifyPwdByUserName",method = RequestMethod.POST)
    public ResultObject modifyPwdByUserName(@RequestBody Map<String,Object> paramMap){
        logger.info("根据用户名修改密码入参"+paramMap.toString());
        ResultObject resultObject = new ResultObject();
//        Map<String,Object> paramMap = new HashMap<>();
        int flag = adminService.modifyPwdByUserName(paramMap);
        if(flag > 0){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("密码修改成功！");
            resultObject.setResult(true);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("密码修改失败！");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/modifyAuthorById",method = RequestMethod.POST)
    public ResultObject modifyAuthorById(@RequestBody Map<String,Object> paramMap){
        logger.info("用户权限修改入参"+paramMap.toString());
        ResultObject resultObject = new ResultObject();
        int flag = adminService.modifyAuthorById(paramMap);
        if(flag > 0){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("权限修改成功！");
            resultObject.setResult(true);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("权限修改失败！");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/querRole",method = RequestMethod.POST)
    public ResultObject querRole(String userName){
        logger.info("用户权限查询入参"+userName);
        ResultObject resultObject = new ResultObject();
        String role = adminService.getUserRole(userName);
        if(!"".equals(role)){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("权限修改成功！");
            resultObject.setResult(true);
            resultObject.setData(role);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("权限修改失败！");
            resultObject.setResult(false);
        }
        return resultObject;
    }



}
