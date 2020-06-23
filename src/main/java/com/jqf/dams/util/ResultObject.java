package com.jqf.dams.util;

public class ResultObject {

    private int code; // 状态码：0是正常调用 -1是程序出错

    private String msg; // 返回消息：成功还是失败

    private int count;//返回数据数量

    private Object data; // 返回的对象。有时候我们要返回一些数据就放在这个里，如果不需要返回数据则是null

    private Boolean result; // 接口调用的结果 true是成功。false是失败。

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {

        return msg;

    }

    public void setMsg(String msg) {

        this.msg = msg;

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getResult() {

        return result;

    }

    public void setResult(Boolean result) {

        this.result = result;

    }

    public Object getData() {

        return data;

    }

    public void setData(Object data) {

        this.data = data;

    }

    public ResultObject() {

    }

    public ResultObject(int code, String msg, int count, Object data, Boolean result) {

        super();

        this.code = code;

        this.msg = msg;

        this.count = count;

        this.data = data;

        this.result = result;

    }

    /**
     * 对返回值的封装
     * 
     * @param code
     * @param msg
     * @param count
     * @param object
     * @param flag
     * @return
     */
    public static ResultObject result(int code, String msg, int count, Object object, boolean flag) {
        return new ResultObject(code, msg, count, object, flag);
    }

}