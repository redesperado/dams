package com.jqf.dams.service;

import com.github.pagehelper.Page;
import com.jqf.dams.bean.QuestionBean;

import java.util.List;
import java.util.Map;

public interface QuestionService {

    Page<QuestionBean> queryALLQuestion(String questionId, Integer page, Integer limit);

    int addQuestion(List<Map<String,Object>> params);

    int releaseQuestion(Map<String,Object> paramMap);

    int queryReleaseCount();

    List<QuestionBean> queryreleaseQuestion();
}
