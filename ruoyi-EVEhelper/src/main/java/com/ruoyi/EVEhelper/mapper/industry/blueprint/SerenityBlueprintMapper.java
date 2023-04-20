package com.ruoyi.EVEhelper.mapper.industry.blueprint;

import com.ruoyi.EVEhelper.domain.Industry.Blueprint;
import com.ruoyi.EVEhelper.domain.Industry.BlueprintMaterial;
import com.ruoyi.EVEhelper.domain.Industry.BlueprintMaterialItem;
import com.ruoyi.EVEhelper.domain.Industry.BlueprintOperateLog;
import com.ruoyi.EVEhelper.domain.universe.UniverseType;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface SerenityBlueprintMapper {
    /**
     * 检查蓝图名称是否重复
     * @param blueprintName
     * @return
     */
    int checkBlueprintName(String blueprintName);

    /**
     * 检查蓝图材料是否已录入
     * @param material_name
     * @return
     */
    BlueprintMaterialItem checkBlueprintMaterialItemByMaterialName(String material_name);

    /**
     * 检查蓝图材料外键id是否重复
     * @param blueprint_material_id
     * @return
     */
    int checkBlueprintMaterialId(String blueprint_material_id);

    /**
     * 保存蓝图材料信息
     * @param blueprint
     */
    void saveBlueprintInfo(Blueprint blueprint);

    /**
     * 保存蓝图材料信息
     * @param blueprintMaterialList
     */
    void saveBlueprintMaterial(List<BlueprintMaterial> blueprintMaterialList);

    /**
     * 根据材料类型id查询材料类型名称
     * @param materialTypeCode
     * @return
     */
    String getmMaterialTypeNameById(String materialTypeCode);

    /**
     * 根据材料类型名称查询材料类型id
     * @param materialTypeName
     * @return
     */
    String getMaterialTypeIdByName(String materialTypeName);

    /**
     * 保存已知材料基本信息
     * @param blueprintMaterialItem
     */
    void saveBlueprintMaterialItem(BlueprintMaterialItem blueprintMaterialItem);

    /**
     * 根据名称名字删除蓝图信息
     * @param blueprintName
     */
    void deleteBlueprintInfoByBlueprintName(String blueprintName);

    /**
     * 根据蓝图名称删除蓝图材料列表
     * @param blueprint_materials_id
     */
    void deleteBlueprintMaterialListByMaterialId(String blueprint_materials_id);

    /**
     * 保存操作记录
     * @param blueprintOperateLog
     */
    void saveBlueprintOperateLog(BlueprintOperateLog blueprintOperateLog);

    /**
     * 根据蓝图名称查询对应材料id
     * @param blueprintName
     * @return
     */
    String getBlueprintMaterialIdByBlueprintName(String blueprintName);

    /**
     * 查询最新10条 操作记录
     * @return
     */
    List<BlueprintOperateLog> getBlueprintOperateList();

    int cacheBlueprintInfo(@Param("blueprint_name") String blueprintName, @Param("blueprint_info") String stringBuffer);

    int checkBlueprintCache(String blueprintName);
}
