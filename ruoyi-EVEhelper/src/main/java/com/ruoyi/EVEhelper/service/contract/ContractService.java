package com.ruoyi.EVEhelper.service.contract;

import com.ruoyi.EVEhelper.domain.contract.Contract;
import com.ruoyi.EVEhelper.domain.contract.ContractCondition;
import com.ruoyi.common.core.redis.RedisCache;

import java.util.List;
import java.util.Map;

public interface ContractService {
    /**
     * 根据数据源缓存不同服务器的全部合同数据
     * @param dataSource
     * @return
     */
    Map<String,Object> getContractList(String dataSource);

    void getContractList(String region_id,String dataSource, RedisCache redisCache);

    /**
     * 根据条件查询合同
     * @param contractCondition
     * @return
     */
    List<Contract> getContractByConditions(ContractCondition contractCondition);


    /**
     * 检查更新进程
     * @return
     */
    int checkContractDataProgress();

    /**
     * 获取合同详细信息
     * @param contractList
     * @param dataSource
     * @return
     */
    List<Contract> getContractDetails(List<Contract> contractList,String dataSource);

    /**
     * 获取条件查询总条数
     * @param contractCondition
     * @return
     */
    int getContractSizeByConditions(ContractCondition contractCondition);
}
