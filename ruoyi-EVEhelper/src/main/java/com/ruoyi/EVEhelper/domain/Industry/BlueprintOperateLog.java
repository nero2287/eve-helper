package com.ruoyi.EVEhelper.domain.Industry;

public class BlueprintOperateLog implements Comparable<BlueprintOperateLog>{
    private int log_id;
    private String blueprint_name;
    private String blueprint_materials_id;
    private int  blueprint_type_id;
    private String operate_type;

    @Override
    public int compareTo(BlueprintOperateLog log) {
        return this.getLog_id()-log.getLog_id();
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public String getBlueprint_name() {
        return blueprint_name;
    }

    public void setBlueprint_name(String blueprint_name) {
        this.blueprint_name = blueprint_name;
    }

    public String getBlueprint_materials_id() {
        return blueprint_materials_id;
    }

    public void setBlueprint_materials_id(String blueprint_materials_id) {
        this.blueprint_materials_id = blueprint_materials_id;
    }

    public int getBlueprint_type_id() {
        return blueprint_type_id;
    }

    public void setBlueprint_type_id(int blueprint_type_id) {
        this.blueprint_type_id = blueprint_type_id;
    }

    public String getOperate_type() {
        return operate_type;
    }

    public void setOperate_type(String operate_type) {
        this.operate_type = operate_type;
    }
}
