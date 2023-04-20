package com.ruoyi.EVEhelper.service.market;

import com.ruoyi.EVEhelper.domain.market.MarketTypeOrder;
import com.ruoyi.EVEhelper.domain.market.MarketTypesGroup;
import com.ruoyi.EVEhelper.domain.universe.UniverseType;

import java.util.List;
import java.util.Map;

public interface MarketService {

    /**
     * 从国服接口获取市场分类列表缓存到本地数据库
     * @return
     */
    boolean cacheSerenityMarketGroups();

    /**
     * 从欧服接口获取市场分类列表缓存到本地数据库
     * @return
     */
    boolean cacheTranquilityMarketGroups();

    /**
     * 根据分类id查询分类列表
     * @param group_id
     * @return
     */
    List<MarketTypesGroup> getMarketGroupTree(Long group_id, String dataSource);

    /**
     * 获取物品分类id获取物品
     * @param group_id
     * @return
     */
    List<UniverseType> getMarketTypeByGroup(Long group_id,String dataSource);

    /**
     * 根据物品id获取市场数据
     * @param region_id
     * @param type_id
     * @param dataSource
     * @param is_buy
     * @return
     */
    List<MarketTypeOrder> getUniverseTypeMarketPrices(String region_id, int type_id, String dataSource, String is_buy);

    /**
     * 检查线程池是否在工作
     * @return
     */
    int checkCacheProgress();


    /**
     * 获取市场价格牌列表
     * @return
     */
    Map<String,Object> getMarketPriceList(String dataSource);
}
