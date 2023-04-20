package com.ruoyi.EVEhelper.domain.Industry;

import java.math.BigDecimal;

public class BlueprintMaterial {
    private int material_id;
    private String blueprint_materials_id;
    private String materials_name;
    private int materials_type_id;
    private String materials_type_name;
    private BigDecimal materials_num;

    public int getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(int material_id) {
        this.material_id = material_id;
    }

    public String getBlueprint_materials_id() {
        return blueprint_materials_id;
    }

    public void setBlueprint_materials_id(String blueprint_materials_id) {
        this.blueprint_materials_id = blueprint_materials_id;
    }

    public String getMaterials_name() {
        return materials_name;
    }

    public void setMaterials_name(String materials_name) {
        this.materials_name = materials_name;
    }

    public int getMaterials_type_id() {
        return materials_type_id;
    }

    public void setMaterials_type_id(int materials_type_id) {
        this.materials_type_id = materials_type_id;
    }

    public String getMaterials_type_name() {
        return materials_type_name;
    }

    public void setMaterials_type_name(String materials_type_name) {
        this.materials_type_name = materials_type_name;
    }

    public BigDecimal getMaterials_num() {
        return materials_num;
    }

    public void setMaterials_num(BigDecimal materials_num) {
        this.materials_num = materials_num;
    }
}
