package com.ruoyi.web.controller.EVEHelper.universe;

import com.ruoyi.EVEhelper.service.universe.UniverseService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UniverseController extends BaseController {

    @Autowired
    private UniverseService universeService;

    /**
     * 获取国服所有物品信息并缓存到本地数据库
     * @return
     */
    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/serenity/cacheSerenityUniverseTypeInfo")
    public AjaxResult cacheSerenityUniverseTypeInfo(){
        if(universeService.cacheSerenityUniverseTypeInfo()){
            return AjaxResult.success();
        }else{
            return AjaxResult.success("系统正在更新信息请稍后再试。");
        }
    }

    /**
     * 缓存欧服所有物品信息并缓存到本地数据库
     * @return
     */
    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/tranquility/cacheTranquilityUniverseTypeInfo")
    public AjaxResult cacheTranquilityUniverseTypeInfo(){
        if(universeService.cacheTranquilityUniverseTypeInfo()){
            return AjaxResult.success();
        }else{
            return AjaxResult.success("系统正在更新信息请稍后再试。");
        }
    }

    /**
     * 查询线程进度
     * @return
     */
    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/checkThreadTempData")
    public AjaxResult checkThreadTempData(){
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data",universeService.checkThreadTempData());
        return ajax;
    }

    /**
     * 模糊查询物品信息
     * @param type_name
     * @param dataSource
     * @return
     */
    @GetMapping("/helper/universe/searchUniverseTypeByTypeName")
    public AjaxResult searchUniverseTypeByTypeName(String type_name,String dataSource){
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data",universeService.searchUniverseTypeByTypeName(type_name,dataSource));
        return ajax;
    }

    /**
     * 缓存国服星系信息
     * @return
     */
    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/cacheSerenityUniverseSystemInfo")
    public AjaxResult cacheSerenitySystem(){
        if(universeService.checkThreadTempData()==0){
            universeService.cacheUniverseSystemInfo("serenity");
            return AjaxResult.success();
        }else{
            return AjaxResult.success("系统正在更新信息请稍后再试。");
        }
    }

    /**
     * 缓存欧服星系信息
     * @return
     */
    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/cacheTranquilityUniverseSystemInfo")
    public AjaxResult cacheTranquilitySystem(){
        if(universeService.checkThreadTempData()==0){
            universeService.cacheUniverseSystemInfo("tranquility");
            return AjaxResult.success();
        }else{
            return AjaxResult.success("系统正在更新信息请稍后再试。");
        }
    }

    /**
     * 缓存国服空间站信息
     * @return
     */
    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/cacheSerenityStationInfo")
    public AjaxResult cacheSerenityStation(){
        if(universeService.checkThreadTempData()==0){
            universeService.cacheStationInfo("serenity");
            return AjaxResult.success();
        }else{
            return AjaxResult.success("系统正在更新信息请稍后再试。");
        }
    }

    /**
     * 缓存欧服空间站信息
     * @return
     */
    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/cacheTranquilityStationInfo")
    public AjaxResult cacheTranquilityStation(){
        if(universeService.checkThreadTempData()==0){
            universeService.cacheStationInfo("serenity");
            return AjaxResult.success();
        }else{
            return AjaxResult.success("系统正在更新信息请稍后再试。");
        }
    }

    /**
     * 缓存国服物品分类信息
     * @return
     */
    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/cacheSerenityUniverseGroupInfo")
    public AjaxResult cacheSerenityUniverseGroupInfo(){
        if(universeService.checkThreadTempData()==0){
            universeService.cacheUniverseGroupInfo("serenity");
            return AjaxResult.success();
        }else{
            return AjaxResult.success("系统正在更新信息请稍后再试。");
        }
    }

    /**
     * 缓存欧服物品分类信息
     * @return
     */
    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/cacheTranquilityUniverseGroupInfo")
    public AjaxResult cacheTranquilityUniverseGroupInfo(){
        if(universeService.checkThreadTempData()==0){
            universeService.cacheUniverseGroupInfo("tranquility");
            return AjaxResult.success();
        }else{
            return AjaxResult.success("系统正在更新信息请稍后再试。");
        }
    }

    @PreAuthorize("@ss.hasPermi('eve:manage:system')")
    @GetMapping("/system/manage/blueprintCheck")
    public AjaxResult blueprintCheck(String blueprint_group_id,String market_group_id){
        //universeService.blueprintCheck(blueprint_group_id,market_group_id);
        universeService.blueprintGroupBuild(market_group_id,blueprint_group_id);
        return AjaxResult.success();
    }
}
