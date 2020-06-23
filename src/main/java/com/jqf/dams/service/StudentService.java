package com.jqf.dams.service;

import com.github.pagehelper.Page;
import com.jqf.dams.bean.StudentBean;

import java.util.List;
import java.util.Map;

public interface StudentService {

    Page<StudentBean> selectAllStuInfo(Integer page, Integer limit);

    int selectStuCount();

    Page<StudentBean> selectStuByParam(Map<String,Object> params,Integer page, Integer limit);

    int addStudents(List<StudentBean> students);

    int queryStuNo(String stuNumber);

    int delStudentById(String id);

    int addQuestAnswerByStuNumber(Map<String,Object> params);

    String queryQuestAnswerByStuNumber(String stuNumber);

    List<StudentBean> queryStudentAnswerByProCode(String professionCode);

    int setStuDorm(Map<String,Object> params);

    List<String> queryProCode();
}
