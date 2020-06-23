package com.jqf.dams.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jqf.dams.bean.DormitoryBean;
import com.jqf.dams.dao.DormitoryDao;
import com.jqf.dams.service.DormitoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class DormitoryServiceImpl implements DormitoryService {

    @Resource
    DormitoryDao dormitoryDao;

    @Override
    public Page<DormitoryBean> queryDormInfo(Map<String,Object> params,Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        return dormitoryDao.queryDormInfo(params);
    }

    @Override
    public int addDormitories(List<DormitoryBean> dormitoryBeans) {
        return dormitoryDao.addDormitories(dormitoryBeans);
    }

    @Override
    public List<DormitoryBean> queryDormsByBuilding(String dormBuilding) {
        return dormitoryDao.queryDormsByBuilding(dormBuilding);
    }

    @Override
    public List<DormitoryBean> queryNotFullDormByParam(Map<String, Object> params) {
        return dormitoryDao.queryNotFullDormByParam(params);
    }

    @Override
    public List<DormitoryBean> queryNotFullDorm(String dormBuilding) {
        return dormitoryDao.queryNotFullDorm(dormBuilding);
    }

    @Override
    public int setDormStu(Map<String, Object> params) {
        return dormitoryDao.setDormStu(params);
    }

    @Override
    public List<String> queryBuildingBySex(String dormSex) {
        return dormitoryDao.queryBuildingBySex(dormSex);
    }
}
