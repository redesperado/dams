package com.jqf.dams.util;

import com.jqf.dams.bean.AnswerBean;
import com.jqf.dams.bean.DormitoryBean;
import com.jqf.dams.bean.StudentBean;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class KMeansUtil {

    static int k;    //k值
    static int questionNumber;//维度-题目数量
    static int num = 0;//迭代次数
//    static double[] coreMoveDist = new double[10];

    static AnswerBean[] oldCores = null;//老的聚类中心
    static AnswerBean[] newCores = null;//新的聚类中心
//    AnswerBean[] oldCores = null;//老的聚类中心

    static int studentSize;//学生人数
    static int dormCapacity;//宿舍容纳能力

    public static List<List<String>> autoAllocation(List<StudentBean> studentBeans, List<DormitoryBean> dormitoryBeans){
        studentSize = studentBeans.size();//学生人数
        dormCapacity = Integer.parseInt(dormitoryBeans.get(0).getDormCapacity());//宿舍容纳能力
        k = studentSize/dormCapacity;     //K值由学生人数除以宿舍容纳能力来确定
        //获取学生答题信息
        AnswerBean[] answerBeans = getStudentAnswer(studentBeans);
        //创建初始聚类中心
        oldCores = getAllocationCore(k,studentSize,answerBeans);
        //初始化新聚类中心
        newCores = new AnswerBean[k];
        List<List<String>> lists = kMeans(answerBeans,oldCores);
        return lists;

    }

    //核心算法
    public static List<List<String>> kMeans(AnswerBean[] answerBeans,AnswerBean[] cores){

        List<List<String>> stuList = new ArrayList<>();
        //根据质心进行聚类划分，划分为K个簇
        Map<AnswerBean,List<AnswerBean>> alloMap = new HashMap<>();
        for(int i = 0; i < answerBeans.length; i++){
            double[] distance = new double[k];
            for(int j = 0; j < cores.length; j++){
                distance[j] = getDist(answerBeans[i],oldCores[j]);
            }
            double minDist = distance[0];
            int minIndex = 0;
            for(int j = 0; j < distance.length; j ++){
                minDist = distance[j] > minDist ? minDist : distance[j];
                minIndex = distance[j] > minDist ? minIndex : j;
            }
            //将距离聚类中心最近的学生信息放进一个Map集合中，每个学生都有自己对应的聚类中心标记
            List<AnswerBean> list = alloMap.get(cores[minIndex]);
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(answerBeans[i]);
            alloMap.put(cores[minIndex],list);
        }

        //重新计算每个簇的质心
        for(int i = 0; i < oldCores.length; i ++){
            List<AnswerBean> answerBeanList = (List<AnswerBean>) MapUtils.getObject(alloMap,oldCores[i]);
            if(answerBeanList == null){
                newCores[i] = oldCores[i];
            }else {
                AnswerBean answerBean = getNewCore(answerBeanList);
                newCores[i] = answerBean;
            }

        }
//        for(int i = 0; i < k; i ++){
//            System.out.println("旧质心："+oldCores[i].toString());
//            System.out.println("新质心："+newCores[i].toString());
//        }


        //计算质心距离
        boolean flag = false;
        for(int i = 0; i < k; i ++){
            double dist = getDist(newCores[i],oldCores[i]);
//            coreMoveDist[num] = dist;
            System.out.println("质心移动距离："+dist);
            if(dist < 0.001){
                flag = true;
            }else {
                flag = false;
                break;
            }
        }

        if(flag){
            System.out.println("迭代完毕！！！！！");
            boolean flag1 = false;
            for(int i = 0; i < oldCores.length; i ++){
                List<String> list = new ArrayList<>();
                List<AnswerBean> answerBeanList = (List<AnswerBean>) MapUtils.getObject(alloMap,oldCores[i]);
                if(answerBeanList != null){
                    for (AnswerBean answerBean : answerBeanList) {
                        list.add(answerBean.getStuNmuber());
                    }
                }else {
                    flag1 = true;
                }
                stuList.add(list);
            }
            if(flag1){
                System.out.println("2234,再来一次");
                oldCores = getAllocationCore(k,studentSize,answerBeans);
                newCores = new AnswerBean[k];
                stuList = kMeans(answerBeans,oldCores);
            }
            return stuList;
        }else{
            num++;
            System.out.println("第几次迭代："+num);
            oldCores = newCores;
            newCores = new AnswerBean[k];
            stuList = kMeans(answerBeans,oldCores);
        }
        return stuList;
    }


    //计算簇的质心
    public static AnswerBean getNewCore(List<AnswerBean> answerBeanList){
        double[] node = new double[questionNumber];
        for (AnswerBean answerBean : answerBeanList) {
            String[] ansArry = answerBean.getAnswerVal().split(";");
            for(int i = 0; i < ansArry.length; i ++){
                node[i] = node[i] + Double.parseDouble(ansArry[i]);
            }
        }
        String nodes = "";
        for(int i = 0; i < questionNumber; i ++){
            node[i] = node[i]/questionNumber;
            nodes = nodes + Double.valueOf(node[i]) + ";";
        }
        AnswerBean answerBean = new AnswerBean();
        answerBean.setAnswerVal(nodes.substring(0,nodes.length()-1));
        return answerBean;

    }

    //n维欧式距离计算-质心-质心
    public static double getDist(AnswerBean core1,AnswerBean core2){
        double dist = 0;
        String[] coreArr1 = core1.getAnswerVal().split(";");
        String[] coreArr2 = core2.getAnswerVal().split(";");
        for(int i = 0; i < coreArr1.length; i ++){
            dist = Math.pow((Double.parseDouble(coreArr1[i]) - Double.parseDouble(coreArr2[i])), 2);
        }
        dist = Math.sqrt(dist);
        return dist;
    }


    //随机生成K个点作为初始聚类中心
    public static AnswerBean[] getAllocationCore(int coreQuantity,int stuSize,AnswerBean[] answerBeans){
        Set<AnswerBean> rSet = new HashSet<>();
        for(; rSet.size() < coreQuantity; ){
            int s = (int)(Math.random()*stuSize);
            rSet.add(answerBeans[s]);
        }
        AnswerBean[] beans = new AnswerBean[coreQuantity];
        Iterator<AnswerBean> it = rSet.iterator();
        int i = 0;
        System.out.println("初始质心：");
        while(it.hasNext()) {
            beans[i] = it.next();
            System.out.println(beans[i].toString());
            i++;
        }
        System.out.println();
        return beans;
    }

    //获取学生问卷答案，并转换成AnswerBean数组
    public static AnswerBean[] getStudentAnswer(List<StudentBean> studentBeans){
        AnswerBean[] answerBeans = new AnswerBean[studentSize];
        for (int i = 0; i < studentBeans.size(); i++) {
            AnswerBean answerBean = new AnswerBean();
            String answers = studentBeans.get(i).getQuestionAnswers();
            answerBean = answerToBean(answers);
            answerBean.setStuNmuber(studentBeans.get(i).getStuNumber());
            answerBean.setStuAnswer(studentBeans.get(i).getQuestionAnswers());
            answerBeans[i] = answerBean;
        }
        return answerBeans;
    }

    //将问卷答案字符串转化为AnswerBean对象
    public static AnswerBean answerToBean(String answers){
        String[] answerArr = answers.split(";");
        questionNumber = answerArr.length;
        AnswerBean answerBean = new AnswerBean();
        String answerVal = "";
        for(int i = 0; i < answerArr.length; i ++){
            String answer[] = answerArr[i].split(",");
            double ansVal = 0;
            if(answer.length == 1){
                ansVal = Integer.parseInt(MapUtils.getString(Constants.getAnswerVal,answerArr[i]));
            }else if(answer.length > 1){
                double ans = 0;
                for(int j = 0; j < answer.length; j ++){
                    ans = ans + Integer.parseInt(MapUtils.getString(Constants.getAnswerVal,answer[j]));
                }
                ansVal = ans*(2.0/5.0);
            }
            answerVal = answerVal + Double.valueOf(ansVal) + ";";
        }
        answerBean.setAnswerVal(answerVal.substring(0,answerVal.length()-1));
        return answerBean;
    }

    public static void main(String[] args) {
        List<StudentBean> studentBeans = new ArrayList<>();
        StudentBean studentBean0 = new StudentBean();
        studentBean0.setStuNumber("1000");
        studentBean0.setQuestionAnswers("A,B,D;B;D;C;B");
        studentBeans.add(studentBean0);
        StudentBean studentBean1 = new StudentBean();
        studentBean1.setStuNumber("1001");
        studentBean1.setQuestionAnswers("D;C;A;B;B");
        studentBeans.add(studentBean1);
        StudentBean studentBean2 = new StudentBean();
        studentBean2.setStuNumber("1002");
        studentBean2.setQuestionAnswers("C;C;A;B;A");
        studentBeans.add(studentBean2);
        StudentBean studentBean3 = new StudentBean();
        studentBean3.setStuNumber("1003");
        studentBean3.setQuestionAnswers("A,B,C,D;D;C;C;B");
        studentBeans.add(studentBean3);
        StudentBean studentBean4 = new StudentBean();
        studentBean4.setStuNumber("1004");
        studentBean4.setQuestionAnswers("A;C;D;A;A");
        studentBeans.add(studentBean4);
        StudentBean studentBean5 = new StudentBean();
        studentBean5.setStuNumber("1005");
        studentBean5.setQuestionAnswers("A,C,D;B;C;C;A");
        studentBeans.add(studentBean5);
        StudentBean studentBean6 = new StudentBean();
        studentBean6.setStuNumber("1006");
        studentBean6.setQuestionAnswers("B;A;A;B;A");
        studentBeans.add(studentBean6);
        StudentBean studentBean7 = new StudentBean();
        studentBean7.setStuNumber("1007");
        studentBean7.setQuestionAnswers("B,C,D;B;C;D;B");
        studentBeans.add(studentBean7);
//        StudentBean studentBean8 = new StudentBean();
////        studentBean8.setStuNumber("1008");
////        studentBean8.setQuestionAnswers("A,C,D;D;C;C");
////        studentBeans.add(studentBean8);
////        StudentBean studentBean9 = new StudentBean();
////        studentBean9.setStuNumber("1009");
////        studentBean9.setQuestionAnswers("A,C,D;D;C;C");
////        studentBeans.add(studentBean9);
        List<DormitoryBean> dormitoryBeans = new ArrayList<>();
        DormitoryBean dormitoryBean0 = new DormitoryBean();
        dormitoryBean0.setDormCapacity("4");
        dormitoryBeans.add(dormitoryBean0);
        KMeansUtil kMeansUtil = new KMeansUtil();
        List<List<String>> lists = kMeansUtil.autoAllocation(studentBeans,dormitoryBeans);
        System.out.println(lists.toString());

    }
}
