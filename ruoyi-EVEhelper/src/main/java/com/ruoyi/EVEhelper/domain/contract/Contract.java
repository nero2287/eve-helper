package com.ruoyi.EVEhelper.domain.contract;

import java.util.List;

public class Contract {
    /**
     * 合同id
     */
    private int contract_id;
    /**
     * 抵押物
     */
    private int collateral;
    /**
     * 过期时间
     */
    private String date_expired;
    /**
     * 发布时间
     */
    private String date_issued;
    /**
     * 结束时间（拍卖）
     */
    private String days_to_complete;
    /**
     * 结束地点
     */
    private String end_location_id;
    /**
     * 是否军团合同
     */
    private String for_corporation;
    /**
     * 发布人军团
     */
    private String issuer_corporation_id;
    /**
     * 发布人id
     */
    private String issuer_id;
    /**
     * 合同出售金额
     */
    private String price;
    /**
     * 合同获得金额
     */
    private String reward;
    /**
     * 合同开始地点
     */
    private String start_location_id;
    /**
     * 合同备注
     */
    private String title;
    /**
     * 合同类型
     */
    private String type;
    /**
     * 合同所含物品总体积
     */
    private String volume;
    /**
     * 合同结束地点（星系）
     */
    private String location_name;
    /**
     * 发布人名称
     */
    private String character_name;
    /**
     * 合同所含物品
     */
    private List<ContractItem> contractItems;

    /**
     * 查询合同所在星域
     */
    private String region_id;

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public List<ContractItem> getContractItems() {
        return contractItems;
    }

    public void setContractItems(List<ContractItem> contractItems) {
        this.contractItems = contractItems;
    }

    public String getCharacter_name() {
        return character_name;
    }

    public void setCharacter_name(String character_name) {
        this.character_name = character_name;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getFor_corporation() {
        return for_corporation;
    }

    public void setFor_corporation(String for_corporation) {
        this.for_corporation = for_corporation;
    }

    public int getContract_id() {
        return contract_id;
    }

    public void setContract_id(int contract_id) {
        this.contract_id = contract_id;
    }

    public int getCollateral() {
        return collateral;
    }

    public void setCollateral(int collateral) {
        this.collateral = collateral;
    }

    public String getDate_expired() {
        return date_expired;
    }

    public void setDate_expired(String date_expired) {
        this.date_expired = date_expired;
    }

    public String getDate_issued() {
        return date_issued;
    }

    public void setDate_issued(String date_issued) {
        this.date_issued = date_issued;
    }

    public String getDays_to_complete() {
        return days_to_complete;
    }

    public void setDays_to_complete(String days_to_complete) {
        this.days_to_complete = days_to_complete;
    }

    public String getEnd_location_id() {
        return end_location_id;
    }

    public void setEnd_location_id(String end_location_id) {
        this.end_location_id = end_location_id;
    }

    public String getIssuer_corporation_id() {
        return issuer_corporation_id;
    }

    public void setIssuer_corporation_id(String issuer_corporation_id) {
        this.issuer_corporation_id = issuer_corporation_id;
    }

    public String getIssuer_id() {
        return issuer_id;
    }

    public void setIssuer_id(String issuer_id) {
        this.issuer_id = issuer_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getStart_location_id() {
        return start_location_id;
    }

    public void setStart_location_id(String start_location_id) {
        this.start_location_id = start_location_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
