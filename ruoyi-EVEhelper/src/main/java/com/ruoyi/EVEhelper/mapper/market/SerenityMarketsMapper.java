package com.ruoyi.EVEhelper.mapper.market;

import com.ruoyi.EVEhelper.domain.market.MarketTypesGroup;
import com.ruoyi.EVEhelper.domain.universe.Groups;
import com.ruoyi.EVEhelper.domain.universe.UniverseType;

import java.util.List;

public interface SerenityMarketsMapper {

    /**
     * 保存国服市场分类
     * @param groups
     * @return
     */
    int cacheSerenityMarketGroups(List<MarketTypesGroup> groups);

    /**
     * 检查分类id是否唯一
     * @param group_id
     * @return
     */
    int checkSerenityGroupUnique(String group_id);

    /**
     * 根据id获取分类列表
     * @param group_id
     * @return
     */
    List<MarketTypesGroup> getSerenityMarketGroupsByParentGroups(Long group_id);

    /**
     * 根据分类id获取市场分类详情
     * @param group_id
     * @return
     */
    MarketTypesGroup getSerenityMarketGroupByGroupId(Long group_id);

    /**
     * 清空国服本地数据库重新缓存
     */
    void dropSerenityMarketGroups();

}
