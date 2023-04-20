package com.ruoyi.EVEhelper.mapper.universe;

import com.ruoyi.EVEhelper.domain.Industry.BlueprintMaterialItem;
import com.ruoyi.EVEhelper.domain.universe.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SerenityUniverseMapper {
    /**
     * 根据type_id数组获取国服type集合（物品集合）
     * @param types_id
     * @return
     */
    List<UniverseType> getSerenityMarketTypeListByTypeIdList(@Param("typesId") String[] types_id);

    /**
     * 根据type_id获取国服物品信息
     * @param type_id
     * @return
     */
    UniverseType getSerenityUniverseTypeInfoByTypeId(int type_id);

    /**
     * 缓存国服物品信息数据
     * @param universeTypeList
     * @return
     */
    int cacheSerenityUniverseTypeInfo(List<UniverseType> universeTypeList);

    /**
     * 根据名称模糊查询国服物品数据
     * @param type_name
     */
    List<UniverseType> searchSerenityUniverseTypeByTypeName(String type_name);

    /**
     * 清空重建国服物品数据库
     */
    void dropSerenityUniverseTypes();

    /**
     * 清空国服所有星系信息
     */
    void dropSerenitySystem();

    /**
     * 缓存国服星系
     * @param starSystemList
     */
    void cacheSerenitySystem(List<StarSystem> starSystemList);

    /**
     * 根据星系id获取星系信息
     * @param system_id
     * @return
     */
    StarSystem getSerenityUniverseSystemInfoBySystemId(String system_id);

    /**
     * 获取所有星系中的空间站id
     * @return
     */
    List<String> getUniverseStations();

    /**
     * 检查空间站是否唯一
     * @param station_id
     * @return
     */
    int checkUniverseStationUnique(String station_id);

    /**
     * 缓存空间站信息到数据库
     * @param stationsList
     */
    void cacheUniverseSystemStations(List<Stations> stationsList);

    /**
     * 获取指定空间站所在星系
     * @param start_location_id
     * @return
     */
    String getUniverseStationSystemByStation_id(String start_location_id);

    /**
     * 从本地数据库中获取国服所有物品分类
     * @return
     */
    int getUniverseGroupInfo();

    /**
     * 缓存物品分类信息
     * @param universeTypesGroupList
     */
    void cacheUniverseTypeGroups(List<UniverseTypesGroup> universeTypesGroupList);

    /**
     * 检查分类id是否唯一
     * @param group_id
     * @return
     */
    int checkUniverseTypeGroupUnique(int group_id);

    /**
     * 根据物品名称查询对应蓝图
     * @param name
     * @return
     */
    String checkUniverseTypeBlueprint(String name);

    /**
     * 根据市场列表id更新蓝图id列对应的蓝图列表
     * @param blueprint_group_id
     * @param types
     */
    void updateBlueprint(@Param("blueprint_group_id")String blueprint_group_id,@Param("types")String types);

    /**
     * 根据id获取蓝图子列表
     * @param group_id
     */
    List<BlueprintTypesGroup> getSerenityUniverseBlueprintTypeGroupsByParentGroupId(Long group_id);

    /**
     * 保存蓝图目录数据到数据库
     * @param blueprintTypesGroups
     */
    void saveBlueprintTypeGroups(List<BlueprintTypesGroup> blueprintTypesGroups);

    /**
     * 根据名称查询目录id
     * @param name
     * @return
     */
    int getBlueprintTypeGroupIdByGroupName(@Param("name")String name,@Param("description")String description);

    /**
     * 根据物品名称获取物品信息
     * @param type_name
     * @return
     */
    UniverseType getUniverseTypeInfoByTypeName(String type_name);

    List<UniverseType> getUniverseTypeInfoListByTypeName(String type_name);
}
