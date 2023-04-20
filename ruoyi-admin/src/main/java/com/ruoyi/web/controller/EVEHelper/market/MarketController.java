package com.ruoyi.web.controller.EVEHelper.market;

import com.ruoyi.EVEhelper.domain.market.MarketTypeOrder;
import com.ruoyi.EVEhelper.domain.market.MarketOrder;
import com.ruoyi.EVEhelper.domain.market.MarketTypesGroup;
import com.ruoyi.EVEhelper.domain.universe.UniverseType;
import com.ruoyi.EVEhelper.service.market.MarketService;
import com.ruoyi.EVEhelper.service.universe.UniverseService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MarketController extends BaseController {

    @Autowired
    private MarketService marketService;
    @Autowired
    private UniverseService universeService;
    @Autowired
    private RedisCache redisCache;

    /**
     * 获取国服市场分类列表缓存到本地数据库
     * @return
     */
//    @GetMapping("/marketGroups")
    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/serenity/cacheSerenityMarketGroups")
    public AjaxResult cacheSerenityMarketGroups(){
        if(marketService.cacheSerenityMarketGroups()){
            return AjaxResult.success();
        }else{
            return AjaxResult.success("系统正在更新信息请稍后再试。");
        }
    }

    /**
     * 获取欧服市场分类列表缓存到本地数据库
     * @return
     */
    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/tranquility/cacheTranquilityMarketGroups")
    public AjaxResult cacheTranquilityMarketGroups(){
        if(marketService.cacheTranquilityMarketGroups()){
            return AjaxResult.success();
        }else{
            return AjaxResult.success("系统正在更新信息请稍后再试。");
        }
    }

    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/checkCacheProgress")
    public AjaxResult checkCacheProgress(){
        AjaxResult ajax =  AjaxResult.success();
        ajax.put("data",marketService.checkCacheProgress());
        return ajax;
    }

    /**
     * 获取市场列表（包括物品）
     * @param group_id
     * @return
     */
//    @GetMapping("/getMarketGroups/{group_id}")
    @GetMapping("/helper/markets/getMarketGroupTree/{group_id}")
    public AjaxResult getMarketGroupTree(@PathVariable Long group_id,String dataSource){
        AjaxResult ajax = AjaxResult.success();
        Map<String,Object> map = new HashMap<String,Object>();
        List<MarketTypesGroup> groupsList = marketService.getMarketGroupTree(group_id,dataSource);
        if(groupsList.size()==0){
            map.put("type","types");
            map.put("list",marketService.getMarketTypeByGroup(group_id,dataSource));
        }else{
            map.put("type","groups");
            map.put("list",groupsList);
        }
        ajax.put("data",map);
        return ajax;
    }

    /**
     * 查询物品订单信息（买单价格升序前10条，卖单降序前5条 ）
     * @param marketOrder
     * @return
     */
//    @GetMapping("/getMarketType")
    @GetMapping("/helper/markets/getUniverseTypeInfoOrder")
    public AjaxResult getUniverseTypeInfoOrder(MarketOrder marketOrder){
        AjaxResult ajax = AjaxResult.success();
        Map<String,Object> map = new HashMap<String,Object>();
        //物品对象
        UniverseType type = universeService.getUniverseTypeInfoByTypeId(marketOrder.getType_id(),"serenity");
        //物品价格数组
        List<MarketTypeOrder> market_order = marketService.getUniverseTypeMarketPrices(marketOrder.getRegion_id(), marketOrder.getType_id(), marketOrder.getDataSource(), marketOrder.getIs_buy());
        map.put("type",type);
        map.put("orderList",market_order);
        ajax.put("data",map);
        return ajax;
    }

    /**
     * 获取冰矿，矿物，行星矿，卫矿等材料价格吉他最低价格
     * @param dataSource
     * @return
     */
    @GetMapping("/helper/markets/marketPriceList")
    public AjaxResult marketPriceList(String dataSource){
        AjaxResult ajax = AjaxResult.success();
        Map<String,Object> map = redisCache.getCacheMap("marketPriceList");
        //该操作需要定时任务
        if(map.size()==0){
            map=marketService.getMarketPriceList(dataSource);
            redisCache.setCacheMap("marketPriceList",map);
        }
        ajax.put("data",map);
        return ajax;
    }
}
