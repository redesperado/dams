package com.jqf.dams.controller;

import com.jqf.dams.bean.DormitoryBean;
import com.jqf.dams.bean.StudentBean;
import com.jqf.dams.service.DormitoryService;
import com.jqf.dams.service.StudentService;
import com.jqf.dams.util.Constants;
import com.jqf.dams.util.ExcelUtil;
import com.jqf.dams.util.FileUtil;
import com.jqf.dams.util.ResultObject;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/file")
@RestController
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    private static final String path = "C:\\Users\\Administrator\\Desktop\\ExcelTest\\";

    @javax.annotation.Resource
    StudentService studentService;

    @Resource
    DormitoryService dormitoryService;

    @RequestMapping(value = "upload")
    public ResultObject upLoad(String fileName){
        ResultObject resultObject = new ResultObject();
        List<Map<String,String>> students = ExcelUtil.readExcel(path+fileName);
        List<StudentBean> studentBeans = new ArrayList<>();
        for (Map<String, String> student : students) {
            StudentBean studentBean = new StudentBean();
            studentBean.setStuNumber(MapUtils.getString(student,"stuNumber"));
            studentBean.setStuName(MapUtils.getString(student,"stuName"));
            studentBean.setStuSex(MapUtils.getString(student,"stuSex"));
            studentBean.setProfessionCode(MapUtils.getString(student,"professionCode"));
            studentBean.setProfessionName(MapUtils.getString(student,"professionName"));
            studentBean.setClassName(MapUtils.getString(student,"className"));
            studentBean.setPassword(MapUtils.getString(student,"stuNumber"));
            studentBeans.add(studentBean);
        }
        int flag = studentService.addStudents(studentBeans);
        if(flag > 0){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("学生信息导入成功");
            resultObject.setResult(true);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("学生信息导入失败");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @RequestMapping("/download")
    public void downloadExcel(HttpServletResponse res, HttpServletRequest req) throws Exception {
        String fileName = "学生表模板.xlsx";
        ServletOutputStream out;
        res.setContentType("multipart/form-data");
        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/html");
        String filePath = getClass().getResource("/lib/" + fileName).getPath();
        String userAgent = req.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String((fileName).getBytes("UTF-8"), "ISO-8859-1");
        }
        filePath = URLDecoder.decode(filePath, "UTF-8");
        res.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        FileInputStream inputStream = new FileInputStream(filePath);
        out = res.getOutputStream();
        int b = 0;
        byte[] buffer = new byte[1024];
        while ((b = inputStream.read(buffer)) != -1) {
            // 4.写到输出流(out)中
            out.write(buffer, 0, b);
        }
        inputStream.close();

        if (out != null) {
            out.flush();
            out.close();
        }

    }

    @RequestMapping(value = "dormUpload")
    public ResultObject dormUpload(String fileName){
        ResultObject resultObject = new ResultObject();
        List<Map<String,String>> dormitories = ExcelUtil.readExcel(path+fileName);
        List<DormitoryBean> dormitoryBeans = new ArrayList<>();
        for (Map<String, String> dormitory : dormitories) {
            DormitoryBean dormitoryBean = new DormitoryBean();
            dormitoryBean.setDormBuilding(MapUtils.getString(dormitory,"dormBuilding"));
            dormitoryBean.setDormSex(MapUtils.getString(dormitory,"dormSex"));
            dormitoryBean.setDormNumber(MapUtils.getString(dormitory,"dormNumber"));
            dormitoryBean.setDormCapacity(MapUtils.getString(dormitory,"dormCapacity"));
            dormitoryBean.setDormStudents(MapUtils.getString(dormitory,"dormStudents"));
            dormitoryBean.setIsFull(MapUtils.getString(dormitory,"isFull"));
            dormitoryBean.setRemainNumber(MapUtils.getString(dormitory,"remainNumber"));
            dormitoryBeans.add(dormitoryBean);
        }
        int flag = dormitoryService.addDormitories(dormitoryBeans);
        if(flag > 0){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("宿舍信息导入成功");
            resultObject.setResult(true);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("宿舍信息导入失败");
            resultObject.setResult(false);
        }
        return resultObject;
    }

    @RequestMapping("/downloadDormExcel")
    public void downloadDormExcel(HttpServletResponse res, HttpServletRequest req) throws Exception {
        String fileName = "宿舍表模板.xlsx";
        ServletOutputStream out;
        res.setContentType("multipart/form-data");
        res.setCharacterEncoding("UTF-8");
        res.setContentType("text/html");
        String filePath = getClass().getResource("/lib/" + fileName).getPath();
        String userAgent = req.getHeader("User-Agent");
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String((fileName).getBytes("UTF-8"), "ISO-8859-1");
        }
        filePath = URLDecoder.decode(filePath, "UTF-8");
        res.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        FileInputStream inputStream = new FileInputStream(filePath);
        out = res.getOutputStream();
        int b = 0;
        byte[] buffer = new byte[1024];
        while ((b = inputStream.read(buffer)) != -1) {
            // 4.写到输出流(out)中
            out.write(buffer, 0, b);
        }
        inputStream.close();

        if (out != null) {
            out.flush();
            out.close();
        }

    }

}
