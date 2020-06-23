package com.jqf.dams.controller;

import com.github.pagehelper.Page;
import com.jqf.dams.bean.DormitoryBean;
import com.jqf.dams.service.DormitoryService;
import com.jqf.dams.util.Constants;
import com.jqf.dams.util.PageInfo;
import com.jqf.dams.util.ResultObject;
import com.jqf.dams.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dorm")
public class DormitoryController {

    private static final Logger logger = LoggerFactory.getLogger(DormitoryController.class);

    @Resource
    DormitoryService dormitoryService;

    @ResponseBody
    @RequestMapping("/queryDormInfo")
    public ResultObject queryDormInfo(String dormBuilding,String dormSex,String dormNumber,String dormCapacity,String dormStudents,@RequestParam(value = "page") Integer page,
                                        @RequestParam(value = "limit") Integer limit){
        ResultObject resultObject = new ResultObject();
        Map<String,Object> params = new HashMap<>();
        if(!StringUtil.isEmpty(dormBuilding)){
            params.put("dormBuilding",dormBuilding);
        }
        if(!StringUtil.isEmpty(dormSex)){
            params.put("dormSex",dormSex);
        }
        if(!StringUtil.isEmpty(dormNumber)){
            params.put("dormNumber",dormNumber);
        }
        if(!StringUtil.isEmpty(dormCapacity)){
            params.put("dormCapacity",dormCapacity);
        }
        if(!StringUtil.isEmpty(dormStudents)){
            params.put("dormStudents",dormStudents);
        }

        Page<DormitoryBean> dormitoryBeans = dormitoryService.queryDormInfo(params,page,limit);

        // 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
        PageInfo<DormitoryBean> pageInfo = new PageInfo<>(dormitoryBeans);
        if (dormitoryBeans == null) {
            logger.info("未查询到用户信息");
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("宿舍信息查询失败");
            resultObject.setResult(false);
        } else {
            logger.info("查询用户信息："+dormitoryBeans.toString());
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("宿舍信息查询成功");
            resultObject.setResult(true);
            resultObject.setCount((int)pageInfo.getTotal());
            resultObject.setData(pageInfo.getList());
        }
        return resultObject;
    }

    @ResponseBody
    @RequestMapping(value = "/queryBuildingBySex",method = RequestMethod.POST)
    public ResultObject queryBuildingBySex(String dormSex){
        ResultObject resultObject = new ResultObject();
        List<String> list = dormitoryService.queryBuildingBySex(dormSex);
        if(list.size() > 0){
            resultObject.setCode(Constants.REQUEST_SUCCESS);
            resultObject.setMsg("查询成功！");
            resultObject.setResult(true);
            resultObject.setData(list);
        }else {
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setMsg("查询失败！");
            resultObject.setResult(false);
        }
        return resultObject;
    }

}
