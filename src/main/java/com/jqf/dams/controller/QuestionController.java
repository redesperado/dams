package com.jqf.dams.controller;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.jqf.dams.bean.QuestionBean;
import com.jqf.dams.service.QuestionService;
import com.jqf.dams.util.Constants;
import com.jqf.dams.util.ResultObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/quest")
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Resource
    QuestionService questionService;

    @ResponseBody
    @RequestMapping(value = "/queryQuest",method = RequestMethod.GET)
    public ResultObject queryQuest(String questionId,Integer page,Integer limit){
        logger.info("问卷查询入参:"+questionId);
        Page<QuestionBean> questionBeans = questionService.queryALLQuestion(questionId,page,limit);
        for (QuestionBean questionBean : questionBeans) {
            String isMoreSelect = questionBean.getIsMoreSelect();
            if(StringUtils.equals(isMoreSelect,Constants.SINGLE_SELECT)){
                questionBean.setIsMoreSelectDisplay("单选");
            }else if(StringUtils.equals(isMoreSelect,Constants.MORE_SELECT)){
                questionBean.setIsMoreSelectDisplay("多选");
            }
            String isRelease = questionBean.getIsRelease();
            if(StringUtils.equals(isRelease,Constants.RELEASING)){
                questionBean.setIsReleaseDisplay("发布中");
            }else if(StringUtils.equals(isRelease,Constants.NOT_RELEASE)){
                questionBean.setIsReleaseDisplay("未发布");
            }
        }
        ResultObject resultObject = new ResultObject();
        PageInfo<QuestionBean> pageInfo = new PageInfo<>(questionBeans);
        if(CollectionUtils.isNotEmpty(questionBeans)){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("问卷查询成功");
            resultObject.setResult(true);
            resultObject.setCount((int)pageInfo.getTotal());
            resultObject.setData(pageInfo.getList());
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("问卷查询失败");
            resultObject.setResult(false);
        }
        return resultObject;

    }

    @ResponseBody
    @RequestMapping(value = "/addQuest",method = RequestMethod.POST)
    public ResultObject addQuest(@RequestBody List<Map<String,Object>> params){
        logger.info("新增问卷入参:"+params.toString());
        ResultObject resultObject = new ResultObject();
        int flag = questionService.addQuestion(params);
        if(flag > 0){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("创建问卷成功");
            resultObject.setResult(true);
            resultObject.setData(params);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("创建问卷失败");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/modifyReleaseStatu",method = RequestMethod.POST)
    public ResultObject modifyReleaseStatu(@RequestBody Map<String,Object> paramMap){
        logger.info("发布状态修改入参:"+paramMap.toString());
        ResultObject resultObject = new ResultObject();
        String releaseStatu = MapUtils.getString(paramMap,"isRelease");
        if(StringUtils.equals(releaseStatu,Constants.RELEASING)){
            int releaseQuestion = questionService.queryReleaseCount();
            if(releaseQuestion > 0){
                resultObject.setCode(Constants.REQUEST_FAIL);
                resultObject.setMsg("已有问卷在发布状态，不能重复发布！");
                resultObject.setResult(false);
            }else{
                int flag = questionService.releaseQuestion(paramMap);
                if(flag > 0){
                    resultObject.setCode(Constants.REQUEST_SUCCESS);
                    resultObject.setMsg("问卷发布成功！");
                    resultObject.setResult(true);
                }else {
                    resultObject.setCode(Constants.REQUEST_FAIL);
                    resultObject.setMsg("问卷发布失败！");
                    resultObject.setResult(false);
                }
            }
        }else if(StringUtils.equals(releaseStatu,Constants.NOT_RELEASE)){
            int flag = questionService.releaseQuestion(paramMap);
            if(flag > 0){
                resultObject.setCode(Constants.REQUEST_SUCCESS);
                resultObject.setMsg("问卷已取消发布！");
                resultObject.setResult(true);
            }else {
                resultObject.setCode(Constants.REQUEST_FAIL);
                resultObject.setMsg("问卷取消发布失败！");
                resultObject.setResult(false);
            }
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/queryReleaseCount",method = RequestMethod.POST)
    public ResultObject queryReleaseCount(){
        ResultObject resultObject = new ResultObject();
        int flag = questionService.queryReleaseCount();
        if(flag > 0){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("已有问卷在发布状态，不能重复发布！");
            resultObject.setResult(true);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("允许发布！");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/queryreleaseQuestion",method = RequestMethod.POST)
    public ResultObject queryreleaseQuestion(){
        ResultObject resultObject = new ResultObject();
        List<QuestionBean> questionBeans = questionService.queryreleaseQuestion();
        if(CollectionUtils.isNotEmpty(questionBeans)){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("查询成功！");
            resultObject.setResult(true);
            resultObject.setData(questionBeans);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("没有发布的问卷可填报！");
            resultObject.setResult(false);
        }
        return resultObject;
    }
}
