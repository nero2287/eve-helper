package com.ruoyi.EVEhelper.mapper.universe;

import com.ruoyi.EVEhelper.domain.universe.StarSystem;
import com.ruoyi.EVEhelper.domain.universe.Stations;
import com.ruoyi.EVEhelper.domain.universe.UniverseType;
import com.ruoyi.EVEhelper.domain.universe.UniverseTypesGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TranquilityUniverseMapper {
    /**
     * 根据type_id数组获取欧服type集合（物品集合）
     * @param types_id
     * @return
     */
    List<UniverseType> getTranquilityMarketTypeListByTypeIdList(@Param("typesId") String[] types_id);

    /**
     * 根据type_id获取欧服物品信息
     * @param type_id
     * @return
     */
    UniverseType getTranquilityUniverseTypeInfoByTypeId(int type_id);

    /**
     * 缓存欧服物品信息数据
     * @param universeTypeList
     * @return
     */
    int cacheTranquilityUniverseTypeInfo(List<UniverseType> universeTypeList);

    /**
     * 根据名称模糊查询欧服物品数据
     * @param type_name
     * @return
     */
    List<UniverseType> searchTranquilityUniverseTypeByTypeName(String type_name);

    /**
     * 清空重建欧服物品数据库
     */
    void dropTranquilityUniverseTypes();

    /**
     * 清空欧服所有星系信息
     */
    void dropTranquilitySystem();

    /**
     * 缓存欧服信息
     * @param starSystemList
     */
    void cacheTranquilitySystem(List<StarSystem> starSystemList);

    /**
     * 根据星系id查询星系信息
     * @return
     */
    StarSystem getTranquilityUniverseSystemInfoBySystemId();

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
     * 获取本地数据库中所有欧服物品分类
     * @return
     */
    int getUniverseGroupInfo();

    /**
     * 缓存物品分类信息
     * @param universeTypesGroupList
     */
    void cacheUniverseTypeGroups(List<UniverseTypesGroup> universeTypesGroupList);
}
