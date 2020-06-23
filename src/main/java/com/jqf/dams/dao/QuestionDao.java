package com.jqf.dams.dao;

import com.github.pagehelper.Page;
import com.jqf.dams.bean.QuestionBean;

import java.util.List;
import java.util.Map;

public interface QuestionDao {

    Page<QuestionBean> queryALLQuestion(String questionId);

    int addQuestion(List<Map<String,Object>> params);

    int releaseQuestion(Map<String,Object> paramMap);

    int queryReleaseCount();

    List<QuestionBean> queryreleaseQuestion();
}
