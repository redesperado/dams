package com.jqf.dams.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jqf.dams.bean.StudentBean;
import com.jqf.dams.dao.StudentDao;
import com.jqf.dams.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Resource
    private StudentDao studentDao;

    @Override
    public Page<StudentBean> selectAllStuInfo(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        return studentDao.selectAllStuInfo();
    }

    @Override
    public int selectStuCount() {
        return studentDao.selectStuCount();
    }

    @Override
    public Page<StudentBean> selectStuByParam(Map<String, Object> params,Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        return studentDao.selectStuByParam(params);
    }

    @Override
    public int addStudents(List<StudentBean> students) {
        return studentDao.addStudents(students);
    }

    @Override
    public int queryStuNo(String stuNumber) {
        return studentDao.queryStuNo(stuNumber);
    }

    @Override
    public int delStudentById(String id) {
        return studentDao.delStudentById(id);
    }

    @Override
    public int addQuestAnswerByStuNumber(Map<String, Object> params) {
        return studentDao.addQuestAnswerByStuNumber(params);
    }

    @Override
    public String queryQuestAnswerByStuNumber(String stuNumber) {
        return studentDao.queryQuestAnswerByStuNumber(stuNumber);
    }

    @Override
    public List<StudentBean> queryStudentAnswerByProCode(String professionCode) {
        return studentDao.queryStudentAnswerByProCode(professionCode);
    }

    @Override
    public int setStuDorm(Map<String, Object> params) {
        return studentDao.setStuDorm(params);
    }

    @Override
    public List<String> queryProCode() {
        return studentDao.queryProCode();
    }
}
