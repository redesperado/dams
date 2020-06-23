package com.jqf.dams.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jqf.dams.bean.AdminBean;
import com.jqf.dams.dao.AdminDao;
import com.jqf.dams.service.AdminService;
import com.jqf.dams.util.MD5Util;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminDao adminDao;

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public Page<AdminBean> selectAllUserInfo(Integer page,Integer limit) {
//        logger.info("用户信息查询入参："+page+","+limit);
        PageHelper.startPage(page, limit);
        return adminDao.selectAllUserInfo();
    }

    @Override
    public int selectUserCount() {
        return adminDao.selectUserCount();
    }

    @Override
    public String getPassword(String userName) {
        return adminDao.getPassword(userName);
    }

    @Override
    public String getUserRole(String userName) {
        return adminDao.getUserRole(userName);
    }

    @Override
    public int addUser(Map<String, Object> paramMap) {
        String pwd = MapUtils.getString(paramMap,"password");
        String encodePwd = MD5Util.encodeByMd5(pwd);
        paramMap.remove("password");
        paramMap.put("password",encodePwd);
        paramMap.put("pwdValue",pwd);
        return adminDao.addUser(paramMap);
    }

    @Override
    public int delUserById(String id) {
        return adminDao.delUserById(id);
    }

    @Override
    public Page<AdminBean> slectUserByName(String userName,Integer page,Integer limit) {
        PageHelper.startPage(page, limit);
        return adminDao.slectUserByName(userName);
    }

    @Override
    public int auditUserName(String userName) {
        return adminDao.auditUserName(userName);
    }

    @Override
    public int modifyPwdByUserName(Map<String,Object> paramMap) {
        String pwd = MapUtils.getString(paramMap,"password");
        String encodePwd = MD5Util.encodeByMd5(pwd);
        paramMap.remove("password");
        paramMap.put("password",encodePwd);
        paramMap.put("pwdValue",pwd);
        return adminDao.modifyPwdByUserName(paramMap);
    }

    @Override
    public int modifyAuthorById(Map<String, Object> paramMap) {
        return adminDao.modifyAuthorById(paramMap);
    }
}
