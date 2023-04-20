package com.ruoyi.EVEhelper.domain.contract;

public class ContractItem {
    private int contract_id;
    private String is_blueprint_copy;
    private String is_included;
    private String item_id;
    private String material_efficiency;
    private String time_efficiency;
    private int quantity;
    private String record_id;
    private int runs;
    private int type_id;
    private String item_name;

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getContract_id() {
        return contract_id;
    }

    public void setContract_id(int contract_id) {
        this.contract_id = contract_id;
    }

    public String getIs_blueprint_copy() {
        return is_blueprint_copy;
    }

    public void setIs_blueprint_copy(String is_blueprint_copy) {
        this.is_blueprint_copy = is_blueprint_copy;
    }

    public String getIs_included() {
        return is_included;
    }

    public void setIs_included(String is_included) {
        this.is_included = is_included;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getMaterial_efficiency() {
        return material_efficiency;
    }

    public void setMaterial_efficiency(String material_efficiency) {
        this.material_efficiency = material_efficiency;
    }

    public String getTime_efficiency() {
        return time_efficiency;
    }

    public void setTime_efficiency(String time_efficiency) {
        this.time_efficiency = time_efficiency;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }
}
