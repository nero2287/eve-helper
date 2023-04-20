package com.ruoyi.EVEhelper.service.industry.blueprint;

import com.ruoyi.EVEhelper.domain.Industry.BlueprintOperateLog;

import java.util.List;
import java.util.Map;

public interface BlueprintService {

    /**
     * 获取蓝图材料
     * @param stringArray
     */
    String inputBlueprintInfo(String[] stringArray);

    /**
     * 检查蓝图是否存在
     * @param blueprintName
     * @return
     */
    Map<String,Object> checkBlueprintName(String blueprintName);

    /**
     * 缓存新材料
     * @param material_name
     * @param materialTypeName
     */
    void addNewMaterialItem(String material_name, String materialTypeName);

    /**
     * 查询最新10条操作记录
     * @return
     */
    List<BlueprintOperateLog> getBlueprintOperateList();
}
