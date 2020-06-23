package com.jqf.dams.controller;

import com.jqf.dams.service.AuoAllocationService;
import com.jqf.dams.util.Constants;
import com.jqf.dams.util.ResultObject;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/auto")
public class AutoAllocationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoAllocationController.class);

    @Resource
    AuoAllocationService auoAllocationService;

    @ResponseBody
    @RequestMapping(value = "/autoAllocation",method = RequestMethod.POST)
    public ResultObject autoAllocation(@RequestBody Map<String,Object> paramMap){
        LOGGER.info("宿舍分配入参："+paramMap.toString());
        ResultObject resultObject = new ResultObject();
        String professionCode = MapUtils.getString(paramMap,"professionCode");
        String boyDormBuilding = MapUtils.getString(paramMap,"boyDormBuilding");
        String girlDormBuilding = MapUtils.getString(paramMap,"girlDormBuilding");
        boolean flag = auoAllocationService.judgeIsFullQuest(professionCode);
        if(flag){
            Map<String,Object> map = auoAllocationService.autoAllocationByProCode(professionCode,boyDormBuilding,girlDormBuilding);
            boolean boyFlag = MapUtils.getBoolean(map,"boyFlag");
            boolean girlFlag = MapUtils.getBoolean(map,"girlFlag");
            if(boyFlag && girlFlag){
                resultObject.setMsg(MapUtils.getString(map,"boy"));
                resultObject.setCode(Constants.REQUEST_SUCCESS);
                resultObject.setResult(true);
            }else {
                resultObject.setMsg("boy:"+MapUtils.getString(map,"boy")+",girl:"+MapUtils.getString(map,"girl"));
                resultObject.setCode(Constants.REQUEST_FAIL);
                resultObject.setResult(false);
            }
        }else {
            resultObject.setMsg("该专业未填写问卷，暂无法分配！");
            resultObject.setCode(Constants.REQUEST_FAIL);
            resultObject.setResult(false);
        }

        return resultObject;
    }
}
