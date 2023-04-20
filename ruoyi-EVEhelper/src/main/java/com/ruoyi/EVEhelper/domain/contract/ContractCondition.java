package com.ruoyi.EVEhelper.domain.contract;

public class ContractCondition {
    private String type_name;
    private int page;
    private String contract_type;
    private String region_id;
    private String dataSource;
    private String is_included;

    public String getIs_included() {
        return is_included;
    }

    public void setIs_included(String is_included) {
        this.is_included = is_included;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if(page <= 0){
            page = 1;
        }
        this.page = page;
    }

    public String getContract_type() {
        return contract_type;
    }

    public void setContract_type(String contract_type) {
        this.contract_type = contract_type;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
