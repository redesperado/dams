package com.jqf.dams.dao;

import com.github.pagehelper.Page;
import com.jqf.dams.bean.StudentBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

//@Mapper
public interface StudentDao {

    Page<StudentBean> selectAllStuInfo();

    int selectStuCount();

    Page<StudentBean> selectStuByParam(Map<String,Object> params);

    int addStudents(List<StudentBean> students);

    int queryStuNo(String stuNumber);

    int delStudentById(String id);

    int addQuestAnswerByStuNumber(Map<String,Object> params);

    String queryQuestAnswerByStuNumber(String stuNumber);

    List<StudentBean> queryStudentAnswerByProCode(String professionCode);

    int setStuDorm(Map<String,Object> params);

    List<String> queryProCode();

}
