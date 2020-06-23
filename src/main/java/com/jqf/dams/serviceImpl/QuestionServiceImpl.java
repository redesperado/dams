package com.jqf.dams.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jqf.dams.bean.QuestionBean;
import com.jqf.dams.dao.QuestionDao;
import com.jqf.dams.service.QuestionService;
import com.jqf.dams.util.DateUtil;
import com.jqf.dams.util.GenerateIdUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Resource
    QuestionDao questionDao;

    @Override
    public Page<QuestionBean> queryALLQuestion(String questionId, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        return questionDao.queryALLQuestion(questionId);
    }

    @Override
    public int addQuestion(List<Map<String, Object>> params) {
//        String questionId = GenerateIdUtil.generateId();
        for (Map<String, Object> param : params) {
            String questionTitle = MapUtils.getString(param,"titile");
            param.put("questionTitle",questionTitle);
            String isMoreSelect = MapUtils.getString(param,"isMoreSelect");
            param.put("isMoreSelect",isMoreSelect);
            String answers = "";
            String answerA = MapUtils.getString(param,"answerA");
            String answerB = MapUtils.getString(param,"answerB");
            String answerC = MapUtils.getString(param,"answerC");
            String answerD = MapUtils.getString(param,"answerD");
            if(StringUtils.isNotEmpty(answerA))
                answers = answers + answerA + ",";
            if(StringUtils.isNotEmpty(answerB))
                answers = answers + answerB + ",";
            if(StringUtils.isNotEmpty(answerC))
                answers = answers + answerC + ",";
            if(StringUtils.isNotEmpty(answerD))
                answers = answers + answerD + ",";
            param.put("answers",answers.substring(0,answers.length()-1));
            String questionId = MapUtils.getString(param,"questionId");
            param.put("questionId", questionId);
        }
        return questionDao.addQuestion(params);
    }

    @Override
    public int releaseQuestion(Map<String, Object> paramMap) {
        return questionDao.releaseQuestion(paramMap);
    }

    @Override
    public int queryReleaseCount() {
        return questionDao.queryReleaseCount();
    }

    @Override
    public List<QuestionBean> queryreleaseQuestion() {
        return questionDao.queryreleaseQuestion();
    }
}
