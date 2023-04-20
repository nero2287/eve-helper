package com.ruoyi.EVEhelper.domain.universe;

/**
 * 蓝图分类
 */
public class BlueprintTypesGroup {
    private int blueprint_group_id;
    private String description;
    private String name;
    private int parent_blueprint_group_id;
    private String types;

    public int getBlueprint_group_id() {
        return blueprint_group_id;
    }

    public void setBlueprint_group_id(int blueprint_group_id) {
        this.blueprint_group_id = blueprint_group_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParent_blueprint_group_id() {
        return parent_blueprint_group_id;
    }

    public void setParent_blueprint_group_id(int parent_blueprint_group_id) {
        this.parent_blueprint_group_id = parent_blueprint_group_id;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
}
