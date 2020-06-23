package com.jqf.dams.bean;

public class AnswerBean {

    private String stuNmuber;

    private String stuAnswer;

    private String answerVal;


    public String getStuNmuber() {
        return stuNmuber;
    }

    public void setStuNmuber(String stuNmuber) {
        this.stuNmuber = stuNmuber;
    }

    public String getStuAnswer() {
        return stuAnswer;
    }

    public void setStuAnswer(String stuAnswer) {
        this.stuAnswer = stuAnswer;
    }

    public String getAnswerVal() {
        return answerVal;
    }

    public void setAnswerVal(String answerVal) {
        this.answerVal = answerVal;
    }

    public String toString(){
        return "stuNmuber:"+stuNmuber+",stuAnswer:"+stuAnswer+",answerVal:"+answerVal;
    }
}
