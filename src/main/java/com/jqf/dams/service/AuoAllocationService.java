package com.jqf.dams.service;

import java.util.Map;

public interface AuoAllocationService {

    Boolean judgeIsFullQuest(String professionCode);

    Map<String,Object> autoAllocationByProCode(String professionCode, String boyDormBuilding, String girlDormBuilding);

}
