package com.ruoyi.EVEhelper.service.universe;

import com.ruoyi.EVEhelper.domain.universe.UniverseType;

import java.util.List;
import java.util.Map;

public interface UniverseService {
    /**
     * 根据物品id查询物品信息
     * @param type_id
     * @param dataSource
     * @return
     */
    UniverseType getUniverseTypeInfoByTypeId(int type_id,String dataSource);


    /**
     * 缓存国服所有物品信息到本地数据库
     * @return
     */
    boolean cacheSerenityUniverseTypeInfo();

    /**
     * 缓存欧服所有物品信息到本地数据库
     * @return
     */
    boolean cacheTranquilityUniverseTypeInfo();

    /**
     * 根据物品名称模糊查询物品
     * @param type_name
     * @param dataSource
     * @return
     */
    List<UniverseType> searchUniverseTypeByTypeName(String type_name, String dataSource);

    /**
     * 检查线程是否在运行
     * @return
     */
    int checkThreadTempData();

    /**
     * 缓存星系信息
     * @param dataSource
     */
    void cacheUniverseSystemInfo(String dataSource);

    /**
     * 缓存NPC空间站
     * @param DataSource
     */
    void cacheStationInfo(String DataSource);

    /**
     * 缓存物品分类信息
     * @param dataSource
     */
    void cacheUniverseGroupInfo(String dataSource);

    /**
     * 将市场物品转换成蓝图保存到数据库
     * @param blueprint_group_id
     * @param market_group_id
     */
    void blueprintCheck(String blueprint_group_id,String market_group_id);

    /**
     * 批量将市场物品转换成蓝图保存到数据库
     * @param market_group_id
     * @param blueprint_group_id
     */
    void blueprintGroupBuild(String market_group_id, String blueprint_group_id);
}
