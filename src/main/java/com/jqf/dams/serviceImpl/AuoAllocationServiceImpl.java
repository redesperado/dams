package com.jqf.dams.serviceImpl;

import com.jqf.dams.bean.DormitoryBean;
import com.jqf.dams.bean.StudentBean;
import com.jqf.dams.dao.DormitoryDao;
import com.jqf.dams.dao.StudentDao;
import com.jqf.dams.service.AuoAllocationService;
import com.jqf.dams.util.Constants;
import com.jqf.dams.util.KMeansUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuoAllocationServiceImpl implements AuoAllocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuoAllocationServiceImpl.class);

    @Resource
    StudentDao studentDao;

    @Resource
    DormitoryDao dormitoryDao;

    @Override
    public Boolean judgeIsFullQuest(String professionCode) {
        Map<String,Object> returnMap = new HashMap<>();
        List<StudentBean> studentBeanList = studentDao.queryStudentAnswerByProCode(professionCode);
        for (StudentBean studentBean : studentBeanList) {
            String answer = studentBean.getQuestionAnswers();
            if(StringUtils.isEmpty(answer)){
                return false;
            }
        }
        return true;
    }

    public Map<String,Object> autoAllocationByProCode(String professionCode, String boyDormBuilding, String girlDormBuilding){
        //一、获取改专业学生
        List<StudentBean> studentBeanList = studentDao.queryStudentAnswerByProCode(professionCode);
        List<StudentBean> boyStudents = new ArrayList<>();
        List<StudentBean> girlStudents = new ArrayList<>();
        for (StudentBean studentBean : studentBeanList) {
            if(StringUtils.equals(studentBean.getStuSex(), Constants.BOY)){
                boyStudents.add(studentBean);
            }else if(StringUtils.equals(studentBean.getStuSex(),Constants.GIRL)){
                girlStudents.add(studentBean);
            }
        }
        Map<String,Object> resulBoytMap = new HashMap<>();
        if(boyStudents.size() >0){
            resulBoytMap = autoAllocation(boyStudents,boyDormBuilding);
        }
        Map<String,Object> resulGirltMap = new HashMap<>();
        if(boyStudents.size() >0){
            resulGirltMap = autoAllocation(girlStudents,girlDormBuilding);
        }
        Map<String,Object> returnMap = new HashMap<>();
        if(resulBoytMap != null){
            returnMap.put("boy",MapUtils.getString(resulBoytMap,"msg"));
            returnMap.put("boyFlag",MapUtils.getBoolean(resulBoytMap,"flag"));
        }
        if(resulGirltMap != null){
            returnMap.put("girl",MapUtils.getString(resulGirltMap,"msg"));
            returnMap.put("girlFlag",MapUtils.getBoolean(resulGirltMap,"flag"));
        }
        return returnMap;

    }

    public Map<String,Object> autoAllocation(List<StudentBean> studentBeans,String dormBuilding){

        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> map = judgeAllocation(dormBuilding,studentBeans);
        String allocation1 = MapUtils.getString(map,"allocation");
        List<DormitoryBean> notFullDorm = (List<DormitoryBean>) MapUtils.getObject(map,"notFullDorm");
        if(notFullDorm.size() == 0){
            resultMap.put("msg","该栋宿舍楼已满！");
            resultMap.put("flag",false);
            return resultMap;
        }
        int capacity = Integer.parseInt(notFullDorm.get(0).getDormCapacity());
        if(StringUtils.equals(allocation1,Constants.NORMAL)){
            DormitoryBean firstDorm = notFullDorm.get(0);
            int firstDormRemainNumber = Integer.parseInt(firstDorm.getRemainNumber());
            if(studentBeans.size() <= firstDormRemainNumber){
                //设置学生宿舍
                int addDorm = setDormForStu(dormBuilding,studentBeans,firstDorm);
                //设置宿舍学生
                int addStu = setStuForDorm(dormBuilding,studentBeans,firstDorm);
                if(addDorm >1 && addStu > 1){
                    resultMap.put("msg","分配成功");
                    resultMap.put("flag",true);
                }else {
                    resultMap.put("msg","分配失败");
                    resultMap.put("flag",false);
                }
            }else{
                if(notFullDorm.size() < 2){
                    resultMap.put("msg","该栋宿舍楼已满！");
                    resultMap.put("flag",false);
                    return resultMap;
                }
                DormitoryBean secondDorm = notFullDorm.get(1);
                List<StudentBean> firstStu = new ArrayList<>();
                List<StudentBean> secondtStu = new ArrayList<>();
                for(int i =0; i < studentBeans.size(); i++){
                    if(i < firstDormRemainNumber){
                        firstStu.add(studentBeans.get(i));
                    }else {
                        secondtStu.add(studentBeans.get(i));
                    }
                }
                //设置学生宿舍
                int addDorm1 = setDormForStu(dormBuilding,firstStu,firstDorm);
                int addDorm2 = setDormForStu(dormBuilding,secondtStu,secondDorm);
                //设置宿舍学生
                int addStu1 = setStuForDorm(dormBuilding,firstStu,firstDorm);
                int addStu2 = setStuForDorm(dormBuilding,secondtStu,secondDorm);
                if(addDorm1 >=1 && addStu1 >= 1 && addDorm2 >=1 && addStu2 >= 1){
                    resultMap.put("msg","分配成功");
                    resultMap.put("flag",true);
                }else {
                    resultMap.put("msg","分配失败");
                    resultMap.put("flag",false);
                }
            }
        }else if(StringUtils.equals(allocation1,Constants.CLUSTRRING)){
            List<List<String>> stuLists = KMeansUtil.autoAllocation(studentBeans,notFullDorm);
            LOGGER.info("聚类结果集："+stuLists.toString());
            int listSize = stuLists.size();
            if(notFullDorm.size() < listSize){
                resultMap.put("msg","该栋宿舍楼不够分配！");
                resultMap.put("flag",false);
                return resultMap;
            }
            boolean flag = false;
            for(int i = 0; i < listSize; i ++){
                List<String> stulist = stuLists.get(i);
                List<StudentBean> beans = new ArrayList<>();
                for (String s : stulist) {
                    StudentBean studentBean =new StudentBean();
                    studentBean.setStuNumber(s);
                    beans.add(studentBean);
                }
                if(stulist.size() <= capacity){
                    int addDormForStu = setDormForStu(dormBuilding,beans,notFullDorm.get(0));
                    int addStuForDorm = setStuForDorm(dormBuilding,beans,notFullDorm.get(0));
                    if(addDormForStu >= 1 && addStuForDorm >=1){
                        flag = false;
                    }
                    notFullDorm = dormitoryDao.queryNotFullDorm(dormBuilding);
                }else {
                    List<StudentBean> fulltStu = new ArrayList<>();
                    List<StudentBean> remainStu = new ArrayList<>();
                    for(int j =0; j < studentBeans.size(); j++){
                        if(j < capacity){
                            fulltStu.add(studentBeans.get(j));
                        }else {
                            remainStu.add(studentBeans.get(j));
                        }
                    }
                    int addDorm1 = setDormForStu(dormBuilding,fulltStu,notFullDorm.get(0));
                    int addStu1 = setStuForDorm(dormBuilding,fulltStu,notFullDorm.get(0));

                    int addDorm2 = setDormForStu(dormBuilding,remainStu,notFullDorm.get(1));
                    int addStu2 = setStuForDorm(dormBuilding,remainStu,notFullDorm.get(1));
                    if(addDorm1 >=1 && addStu1 >= 1 && addDorm2 >=1 && addStu2 >= 1){
                        resultMap.put("msg","分配成功");
                        resultMap.put("flag",true);
                    }else {
                        resultMap.put("msg","分配失败");
                        resultMap.put("flag",false);
                    }
                    notFullDorm = dormitoryDao.queryNotFullDorm(dormBuilding);
                }
            }
            if(flag){
                resultMap.put("msg","分配成功");
                resultMap.put("flag",true);
            }else {
                resultMap.put("msg","分配失败");
                resultMap.put("flag",false);
            }
        }
        return resultMap;
    }

    //学生入宿舍
    public int setDormForStu(String dormBuilding,List<StudentBean> studentBeans,DormitoryBean dorm){
        Map<String,Object> addDormMap = new HashMap<>();
        addDormMap.put("stuDormitory",dormBuilding+"#"+dorm.getDormNumber());
        addDormMap.put("stuNumbers",studentBeans);
        LOGGER.info(studentBeans.toString()+"学生的宿舍为："+dormBuilding+"#"+dorm.getDormNumber());
        int addDorm = studentDao.setStuDorm(addDormMap);
        return addDorm;
    }
    //宿舍收学生
    public  int setStuForDorm(String dormBuilding,List<StudentBean> studentBeans,DormitoryBean dorm){
        //设置宿舍学生
        int remainNumber = Integer.parseInt(dorm.getRemainNumber());
        Map<String,Object> setDormStuMap = new HashMap<>();
        setDormStuMap.put("dormBuilding",dormBuilding);
        setDormStuMap.put("dormNumber",dorm.getDormNumber());
        String dormStudents = "";
        for (StudentBean studentBean : studentBeans) {
            dormStudents = dormStudents + studentBean.getStuNumber() +",";
        }
        setDormStuMap.put("dormStudents",dormStudents.substring(0,dormStudents.length()-1));
        if(studentBeans.size() == remainNumber){
            setDormStuMap.put("isFull",Constants.FULL);
            setDormStuMap.put("remainNumber","0");
        }else if(studentBeans.size() < remainNumber) {
            setDormStuMap.put("isFull",Constants.NOTFULL);
            setDormStuMap.put("remainNumber",remainNumber-studentBeans.size());
        }else {
            return 0;
        }
        LOGGER.info(dormBuilding+"号宿舍楼，"+dorm.getDormNumber()+"宿舍入住新生："+studentBeans.toString());
        int addStu = dormitoryDao.setDormStu(setDormStuMap);
        return addStu;
    }

    //获取该栋宿舍楼的没住满宿舍
    public Map<String,Object> judgeAllocation(String dormBuilding, List<StudentBean> studentBeans){
        Map<String,Object> resultMap = new HashMap<>();
        List<DormitoryBean> dormitoryBeanList = dormitoryDao.queryDormsByBuilding(dormBuilding);
        List<DormitoryBean> notFullDorm = new ArrayList<>();
        for (DormitoryBean dormitoryBean : dormitoryBeanList) {
            if(StringUtils.equals(dormitoryBean.getIsFull(),Constants.NOTFULL)){
                notFullDorm.add(dormitoryBean);
            }
        }
        resultMap.put("notFullDorm",notFullDorm);
        int firstCapacity = Integer.parseInt(notFullDorm.get(0).getDormCapacity());
        //2.首先判断人数，看有没有必要进行分配
        int boySize = studentBeans.size();
        if(boySize <= firstCapacity){
            //普通分配
            resultMap.put("allocation","normal");
        }else {
            //聚类分配
            resultMap.put("allocation","clustering");
        }
        return resultMap;
    }
}
