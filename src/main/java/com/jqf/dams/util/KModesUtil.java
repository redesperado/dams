package com.jqf.dams.util;

//import com.rierbo.pickdormitory.bean.Student;
//import com.rierbo.pickdormitory.dao.KModesDao;
//import com.rierbo.pickdormitory.service.KModesSV;
import com.jqf.dams.bean.StudentBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KModesUtil {
    public static Logger logger = LoggerFactory.getLogger(KModesUtil.class);

//    @Autowired
//    private KModesDao kModesDao;

    /**
     * 入口
     */
    public Map<StudentBean, List<StudentBean>> mainKModes(List<StudentBean> StudentBeans, boolean noAns, Map<String, Object> params) {
        boolean isLast = false;
        float minSumOff = -1;
        float minSumOff2 = -1;
        int t = getT(noAns,params);
        if(t==0){
            return null;
        }
        int k=StudentBeans.size()/t;
        List<StudentBean> centerKModes=null;
        Map<StudentBean,List<Map<String,Object>>> sumMap=null;
        do {
            if(minSumOff<0){
                centerKModes = getRandomItemInList(StudentBeans, k);
            }else {
                centerKModes = findCentersInSumMap(sumMap);
            }
            sumMap = new HashMap<>();
            for(StudentBean stu:centerKModes){
                sumMap.put(stu,new ArrayList<>());
            }
            for (StudentBean stu : StudentBeans) {
                //遍历非聚类中心学生
                if (!centerKModes.contains(stu)) {
                    float minoff = -1;
                    StudentBean minstu = null;
                    for (StudentBean s : centerKModes) {
                        float off;
                        if (minstu == null) {
                            minstu = s;
                            minoff = compareStudent(s, stu);
                        } else {
                            off = compareStudent(s, stu);
                            if (off < minoff) {
                                minoff = off;
                                minstu = s;
                            }
                        }
                    }
                    //把对应学生放到差距最小的聚类中心的集合里
                    if (sumMap.containsKey(minstu)) {
                        List<Map<String, Object>> list = sumMap.get(minstu);
                        Map<String, Object> map = new HashMap<>();
                        map.put("off", minoff);
                        map.put("stu", stu);
                        list.add(map);
                        Collections.sort(list, new Comparator<Map<String, Object>>() {
                            @Override
                            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                                return (int) ((Float) o1.get("off") * 100 - (Float) o2.get("off") * 100);
                            }
                        });
                    } else {
                        List<Map<String, Object>> list = new ArrayList<>();
                        Map<String, Object> map = new HashMap<>();
                        map.put("off", minoff);
                        map.put("stu", stu);
                        list.add(map);
                        sumMap.put(minstu, list);
                    }
                }
            }
//            logger.info(sumMap.toString());
            float sumOff = getSumOff(sumMap);
            if(isLast){
                break;
            }
            if(sumOff==minSumOff2){
                if(minSumOff<minSumOff2){
                    isLast=true;
                }else{
                    break;
                }
            }
            else{
                minSumOff2=minSumOff;
                minSumOff = sumOff;
            }
        }while (true);
        System.out.println(sumMap);
        //封装返回值
        return packageResult(sumMap);
    }

    private int getT(boolean noAns,Map<String,Object> params){
        return 3;
//        if(noAns){
//            return kModesDao.getTnoAns(params);
//        }else {
//            return kModesDao.getT(params);
//
//        }
    }

    private List<StudentBean> getRandomItemInList(List<StudentBean> list, int num){
        List<StudentBean> returnList= new ArrayList<>();
        Random random = new Random();
        int count=0;
        do{
            StudentBean StudentBean = list.get(random.nextInt(list.size()));
            int have = 0;
            for(StudentBean stu : returnList){
                if(StringUtils.equals(stu.getQuestionAnswers(),StudentBean.getQuestionAnswers())){
                    have=1;
                    break;
                }
            }
            if(count>=5){
                have=0;
            }
            if(have==1){
                count++;
                continue;
            }
            returnList.add(StudentBean);
        }while (returnList.size()<num*2);
        //得到需要数量的两倍，然后把差异最大的几个单独取出
        getCompareToList(returnList);
        return returnList.subList(0,num);
    }

    private void getCompareToList(List<StudentBean> StudentBeans){
        Map<StudentBean,Float> sumMap = new HashMap<>();
        for(StudentBean StudentBean:StudentBeans){
            float sum=0f;
            for(StudentBean stu:StudentBeans){
                if(StudentBean!=stu){
                    sum+=compareStudent(stu,StudentBean);
                }
            }
            sumMap.put(StudentBean,sum);
        }
        List<Map.Entry<StudentBean, Float>> entries = new ArrayList<>(sumMap.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<StudentBean, Float>>() {
            @Override
            public int compare(Map.Entry<StudentBean, Float> o1, Map.Entry<StudentBean, Float> o2) {
                return (int)((o2.getValue()-o1.getValue())*100);
            }
        });
        StudentBeans.clear();
        for(Map.Entry<StudentBean, Float> entry:entries){
            StudentBeans.add(entry.getKey());
        }
    }

    //比较两个学生差异度
    public float compareStudent(StudentBean stu1,StudentBean stu2){
        float result=0;
        String que_ans1 = stu1.getQuestionAnswers();
        String que_ans2 = stu2.getQuestionAnswers();

        String[] answers1 = que_ans1.split("-",-1);
        String[] answers2 = que_ans2.split("-",-1);
        if(answers1.length!=answers2.length){
            logger.error("问卷答案题目数量不同 1:"+stu1.getQuestionAnswers()+"  2:"+stu2.getQuestionAnswers());
            new Exception("问卷答案题目数量不同");
        }
        for(int i=0;i<answers1.length;i++){
            String answer1 = answers1[i];
            String answer2 = answers2[i];
            if(answer1.indexOf(",")>-1 || answer2.indexOf(",")>-1){
                String[] str1 = answer1.split(",");
                String[] str2 = answer2.split(",");

                float num = 0;
                for(String s1 : str1){
                    for(String s2 : str2){
                        if(!StringUtil.equals(s1,s2)){
                            num++;
                        }
                    }
                }
                result += num/(str1.length * str2.length);
            }else{
                if(!StringUtil.equals(answer1,answer2)){
                    result++;
                }
            }
        }
        return result;
    }

    //从聚类里找到新的聚类中心
    private StudentBean findCenterInList(List<StudentBean> StudentBeans){
        List<Map<String,Integer>> list = new ArrayList<>();
        int size = StudentBeans.get(0).getQuestionAnswers().split("-",-1).length;
        for(int i=0;i<size;i++){
            list.add(new HashMap<>());
        }
        for(StudentBean stu : StudentBeans){
            String[] split = stu.getQuestionAnswers().split("-", -1);
            for(int i=0;i<split.length;i++){
                Map<String,Integer> map = list.get(i);
                String s = split[i];
                for(String s1 : s.split(",")){
                    if(map.containsKey(s1)){
                        map.put(s1,map.get(s1)+1);
                    }else {
                        map.put(s1,1);
                    }
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        for(Map<String,Integer> map : list){
            String minkey=null;
            int minValue = -1;
            for(Map.Entry<String,Integer> entry: map.entrySet()){
                if(minValue<0){
                    minkey=entry.getKey();
                    minValue=entry.getValue();
                }else{
                    if(minValue<entry.getValue()){
                        minValue=entry.getValue();
                        minkey=entry.getKey();
                    }
                }
            }
            sb.append(minkey+"-");
        }
        sb.deleteCharAt(sb.length()-1);
        StudentBean other = new StudentBean();
        other.setQuestionAnswers(sb.toString());
        StudentBean minStu = null;
        float minOff = -1;
        for(StudentBean s: StudentBeans){
            if(minOff<0){
                minStu = s;
                minOff = compareStudent(other,s);
            }else {
                float off = compareStudent(other,s);
                if(off<minOff){
                    minStu = s;
                    minOff = off;
                }
            }

        }
        return minStu;
    }

    private List<StudentBean> findCentersInSumMap(Map<StudentBean,List<Map<String,Object>>> sumMap){
        List<StudentBean> sumList = new ArrayList<>();
        for(Map.Entry<StudentBean,List<Map<String,Object>>> entry :sumMap.entrySet()){
            List<StudentBean> list = new ArrayList<>();
            for(Map<String,Object> map:entry.getValue()){
                list.add((StudentBean) map.get("stu"));
            }
            list.add(entry.getKey());
            StudentBean center = findCenterInList(list);
            sumList.add(center);
        }
        return sumList;
    }

    private float getSumOff(Map<StudentBean,List<Map<String,Object>>> sumMap){
        float sum=0;
        for(Map.Entry<StudentBean,List<Map<String,Object>>> entry:sumMap.entrySet()){
            List<Map<String, Object>> value = entry.getValue();
            for(Map<String, Object> map:value){
                sum += (Float)(map.get("off"));
            }
        }
        return sum;
    }

    private Map<StudentBean,List<StudentBean>> packageResult(Map<StudentBean,List<Map<String,Object>>> sumMap){
        Map<StudentBean,List<StudentBean>> sumList = new HashMap<>();
        for(Map.Entry<StudentBean,List<Map<String,Object>>> entry :sumMap.entrySet()){
            List<StudentBean> list = new ArrayList<>();
            list.add(entry.getKey());
            for(Map<String,Object> map:entry.getValue()){
                list.add((StudentBean) map.get("stu"));
            }
            sumList.put(entry.getKey(),list);
        }
        return sumList;
    }
}
