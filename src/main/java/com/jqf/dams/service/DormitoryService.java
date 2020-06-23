package com.jqf.dams.service;

import com.github.pagehelper.Page;
import com.jqf.dams.bean.DormitoryBean;

import java.util.List;
import java.util.Map;

public interface DormitoryService {

    Page<DormitoryBean> queryDormInfo(Map<String,Object> params,Integer page, Integer limit);

    int addDormitories(List<DormitoryBean> dormitoryBeans);

    List<DormitoryBean> queryDormsByBuilding(String dormBuilding);

    List<DormitoryBean> queryNotFullDormByParam(Map<String,Object> params);

    List<DormitoryBean> queryNotFullDorm(String dormBuilding);

    int setDormStu(Map<String,Object> params);

    List<String> queryBuildingBySex(String dormSex);

}
