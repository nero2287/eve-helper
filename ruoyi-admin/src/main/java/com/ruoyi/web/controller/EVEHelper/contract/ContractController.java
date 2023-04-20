package com.ruoyi.web.controller.EVEHelper.contract;

import com.ruoyi.EVEhelper.domain.contract.Contract;
import com.ruoyi.EVEhelper.domain.contract.ContractCondition;
import com.ruoyi.EVEhelper.service.contract.ContractService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ContractController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private RedisCache redisCache;

    public AjaxResult get(){
        return null;
    }

    public AjaxResult get1(Contract contract){
        return null;
    }

    //根据条件模糊查询合同
    @GetMapping("/helper/contract/contractConditions")
    public AjaxResult getContractDataByConditions(ContractCondition contractCondition){
        AjaxResult ajax = AjaxResult.success();
        Map<String, List<Contract>> map = new HashMap<String, List<Contract>> ();
        Map<String,Object> ajaxMap = new HashMap<String,Object>();
        int pageAmount = 0;
        if(contractCondition.getDataSource().equals("")){
            contractCondition.setDataSource("serenity");
        }
        //如果没有条件
        if(contractCondition.getContract_type().equals("")&&contractCondition.getType_name().equals("")&&contractCondition.getRegion_id().equals("10000002")){
            //从内存获取合同
            switch (contractCondition.getDataSource()){
                case "serenity":
                    map = redisCache.getCacheMap("SerenityContract");
                    pageAmount =  redisCache.getCacheObject("SerenityAmountPage");
                    break;
                case "tranquility":
                    map = redisCache.getCacheObject("TranquilityContract");
                    pageAmount =  redisCache.getCacheObject("TranquilityAmountPage");
                    break;
            }
            if(map.size()==0){
                return AjaxResult.success("无法获取合同信息");
            }
            //从缓存中获取
            if(contractCondition.getPage()<=1){
                ajaxMap.put("contractList",contractService.getContractDetails(map.get("page1"),contractCondition.getDataSource()));
            }else{
                ajaxMap.put("contractList",contractService.getContractDetails(map.get("page"+contractCondition.getPage()),contractCondition.getDataSource()));
            }
        }else{
            switch (contractCondition.getContract_type()){
                case "sell":
                    contractCondition.setContract_type("");
                    contractCondition.setIs_included("true");
                    break;
                case "buy":
                    contractCondition.setContract_type("");
                    contractCondition.setIs_included("false");
                    break;
                case "auction":
                    contractCondition.setIs_included("");
                    contractCondition.setContract_type("auction");
                    break;
                case "item_exchange":
                    contractCondition.setIs_included("");
                    contractCondition.setContract_type("item_exchange");
                    break;
            }
            int contract_size = contractService.getContractSizeByConditions(contractCondition);
            if(contract_size%20==0){
                pageAmount  = contract_size/20;
            }else{
                pageAmount =  contract_size/20+1;
            }
            ajaxMap.put("contractList",contractService.getContractDetails(contractService.getContractByConditions(contractCondition),contractCondition.getDataSource()));
        }
        ajaxMap.put("amount",pageAmount);
        ajax.put("data",ajaxMap);
        return ajax;
    }

    //从官方获取合同数据到服务器内存
    @PreAuthorize("@ss.hasPermi('eve:manage:market')")
    @GetMapping("/system/manage/cacheContractData")
    public AjaxResult cacheContractDataFromOffice(String dataSource) {
        Map<String,Object> map = new HashMap<String,Object>();
        if(contractService.checkContractDataProgress()==0){
            contractService.getContractList("10000002",dataSource,redisCache);
        }else{
            return AjaxResult.success("系统正在更新信息请稍后再试");
        }
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('eve:manage:market')")
    @GetMapping("/system/manage/checkContractDataProgress")
    public AjaxResult checkContractDataProgress() {
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data", contractService.checkContractDataProgress());
        return ajax;
    }
}
