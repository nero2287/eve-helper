package com.ruoyi.EVEhelper.domain.Industry;

import java.math.BigDecimal;
import java.util.List;

public class Blueprint {
    /**
     * 蓝图索引
     */
    private int blueprint_id;
    /**
     * 蓝图分类id
     */
    private int blueprint_group_id;
    /**
     * 蓝图物品id
     */
    private int blueprint_type_id;
    /**
     * 蓝图名称
     */
    private String blueprint_name;
    /**
     * 蓝图产物物品id
     */
    private int blueprint_product_type_id;
    /**
     * 蓝图产物物品名称
     */
    private String blueprint_product_name;
    /**
     * 蓝图产物数量
     */
    private BigDecimal blueprint_product_num;
    /**
     * 蓝图制造时间
     */
    private String blueprint_time;
    /**
     * 蓝图对应材料索引
     */
    private String blueprint_materials_id;

    /**
     * 蓝图材料集合
     */
    private List<BlueprintMaterial> blueprintMaterialList;

    public int getBlueprint_id() {
        return blueprint_id;
    }

    public void setBlueprint_id(int blueprint_id) {
        this.blueprint_id = blueprint_id;
    }

    public int getBlueprint_group_id() {
        return blueprint_group_id;
    }

    public void setBlueprint_group_id(int blueprint_group_id) {
        this.blueprint_group_id = blueprint_group_id;
    }

    public int getBlueprint_type_id() {
        return blueprint_type_id;
    }

    public void setBlueprint_type_id(int blueprint_type_id) {
        this.blueprint_type_id = blueprint_type_id;
    }

    public String getBlueprint_name() {
        return blueprint_name;
    }

    public void setBlueprint_name(String blueprint_name) {
        this.blueprint_name = blueprint_name;
    }

    public int getBlueprint_product_type_id() {
        return blueprint_product_type_id;
    }

    public void setBlueprint_product_type_id(int blueprint_product_type_id) {
        this.blueprint_product_type_id = blueprint_product_type_id;
    }

    public String getBlueprint_product_name() {
        return blueprint_product_name;
    }

    public void setBlueprint_product_name(String blueprint_product_name) {
        this.blueprint_product_name = blueprint_product_name;
    }

    public BigDecimal getBlueprint_product_num() {
        return blueprint_product_num;
    }

    public void setBlueprint_product_num(BigDecimal blueprint_product_num) {
        this.blueprint_product_num = blueprint_product_num;
    }

    public String getBlueprint_time() {
        return blueprint_time;
    }

    public void setBlueprint_time(String blueprint_time) {
        this.blueprint_time = blueprint_time;
    }

    public String getBlueprint_materials_id() {
        return blueprint_materials_id;
    }

    public void setBlueprint_materials_id(String blueprint_materials_id) {
        this.blueprint_materials_id = blueprint_materials_id;
    }

    public List<BlueprintMaterial> getBlueprintMaterialList() {
        return blueprintMaterialList;
    }

    public void setBlueprintMaterialList(List<BlueprintMaterial> blueprintMaterialList) {
        this.blueprintMaterialList = blueprintMaterialList;
    }
}
