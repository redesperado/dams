package com.jqf.dams.controller;


import com.github.pagehelper.Page;
import com.jqf.dams.bean.StudentBean;
import com.jqf.dams.service.StudentService;
import com.jqf.dams.util.Constants;
import com.jqf.dams.util.PageInfo;
import com.jqf.dams.util.ResultObject;
import com.jqf.dams.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/stu")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Resource
    private StudentService studentService;

    @ResponseBody
    @RequestMapping("/students")
    public ResultObject showAlluserInfo(@RequestParam(value = "page") Integer page,
                                        @RequestParam(value = "limit") Integer limit){
        logger.info("学生信息查询入参："+page+","+limit);
        ResultObject resultObject = new ResultObject();

        int userCount = studentService.selectStuCount();

        Page<StudentBean> studentBeans = studentService.selectAllStuInfo(page,limit);

        // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
        PageInfo<StudentBean> pageInfo = new PageInfo<>(studentBeans);
        if (studentBeans == null) {
            logger.info("未查询到用户信息");
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("学生信息查询失败");
            resultObject.setResult(false);
        } else {
            logger.info("查询用户信息："+studentBeans.toString());
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("学生信息查询成功");
            resultObject.setResult(true);
            resultObject.setCount(userCount);
            resultObject.setData(pageInfo.getList());
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/selectStuByParam",method = RequestMethod.GET)
    public ResultObject selectStuByParam( String stuName,String stuSex,String stuNumber,String professionCode,String professionName,String className,
            Integer page, Integer limit){
        logger.info("根据参数查询学生信息入参"+stuName+","+stuSex+","+stuNumber+","+professionCode+","+professionName+","+className);
        ResultObject resultObject = new ResultObject();
        Map<String,Object> params = new HashMap<>();
        if(!StringUtil.isEmpty(stuName)){
            params.put("stuName",stuName);
        }
        if(!StringUtil.isEmpty(stuSex)){
            params.put("stuSex",stuSex);
        }
        if(!StringUtil.isEmpty(stuNumber)){
            params.put("stuNumber",stuNumber);
        }
        if(!StringUtil.isEmpty(professionCode)){
            params.put("professionCode",professionCode);
        }
        if(!StringUtil.isEmpty(professionName)){
            params.put("professionName",professionName);
        }
        if(!StringUtil.isEmpty(className)){
            params.put("className",className);
        }

        Page<StudentBean> studentBeans = studentService.selectStuByParam(params,page,limit);
        PageInfo<StudentBean> pageInfo = new PageInfo<>(studentBeans);
        if(studentBeans != null){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("学生信息查询成功");
            resultObject.setResult(true);
            resultObject.setCount((int)pageInfo.getTotal());
            resultObject.setData(pageInfo.getList());
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("学生信息删除失败");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/queryStuNo",method = RequestMethod.POST)
    public ResultObject queryStuNo(String stuNumber){
        logger.info("学号校验入参："+stuNumber);
        ResultObject resultObject = new ResultObject();

        int flag = studentService.queryStuNo(stuNumber);
        String questAnswer = studentService.queryQuestAnswerByStuNumber(stuNumber);
        if(flag == 1){
            if(StringUtils.isNotEmpty(questAnswer)){
                resultObject.setCode(Constants.REQUEST_FAIL);
                resultObject.setMsg("问卷已填报，无需重复填报！");
                resultObject.setResult(false);
            }else {
                resultObject.setCode(Constants.REQUEST_SUCCESS);
                resultObject.setMsg("学号正确");
                resultObject.setResult(true);
            }

        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("学号不存在，请重新输入！");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/delStudentById",method = RequestMethod.POST)
    public ResultObject delStudentById(String id){
        logger.info("删除学生信息入参："+id);
        ResultObject resultObject = new ResultObject();

        int flag = studentService.delStudentById(id);
        if(flag == 1){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("删除成功！");
            resultObject.setResult(true);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("删除失败！");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/addQuestAnswerByStuNumber",method = RequestMethod.POST)
    public ResultObject addQuestAnswerByStuNumber(@RequestBody Map<String,Object> paramMap){
        logger.info("问卷填报入参"+paramMap.toString());
        ResultObject resultObject = new ResultObject();
        int flag = studentService.addQuestAnswerByStuNumber(paramMap);
        if(flag > 0){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("问卷已提交！");
            resultObject.setResult(true);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("问卷提交失败，请重新提交。");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/queryProCode",method = RequestMethod.POST)
    public ResultObject queryProCode(){
        ResultObject resultObject = new ResultObject();
        List<String> list = studentService.queryProCode();
        if(list.size() > 0){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("查询成功！");
            resultObject.setResult(true);
            resultObject.setData(list);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("查询失败！");
            resultObject.setResult(false);
        }
        return resultObject;
    }

}
