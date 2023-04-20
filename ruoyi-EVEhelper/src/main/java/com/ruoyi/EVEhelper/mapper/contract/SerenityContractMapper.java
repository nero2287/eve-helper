package com.ruoyi.EVEhelper.mapper.contract;

import com.ruoyi.EVEhelper.domain.contract.Contract;
import com.ruoyi.EVEhelper.domain.contract.ContractCondition;
import com.ruoyi.EVEhelper.domain.contract.ContractItem;

import java.util.List;

public interface SerenityContractMapper {
    /**
     * 保存合同列表
     * @param newContractList
     */
    void saveContractList(List<Contract> newContractList);

    /**
     * 情况合同列表
     */
    void deleteContractList();

    /**
     * 根据条件查询合同
     * @param contractCondition
     * @return
     */
    List<Contract> getContractList(ContractCondition contractCondition);

    /**
     * 保存合同所含物品
     * @param contractItems
     */
    void saveContractIncludedItem(List<ContractItem> contractItems);

    /**
     * 清空数据库重新导入数据
     */
    void deleteContractItems();

    /**
     * 获取合同内容
     * @param contract_id
     * @return
     */
    List<ContractItem> getContractItemsByContractId(int contract_id);

    /**
     * 获取合同包含物品名称
     * @param contract_id
     * @return
     */
    List<String> getContractItemNamesByContractId(int contract_id);

    /**
     * 获取合同包含物品信息
     * @param contract_id
     * @return
     */
    List<ContractItem> getContractItemByContractId(int contract_id);

    /**
     * 根据合同信息获取角色id
     * @return
     */
    List<String> getCharacterIdByContractInfo();

    /**
     * 获取条件查询总条数
     * @param contractCondition
     * @return
     */
    int getContractSizeByConditions(ContractCondition contractCondition);
}
