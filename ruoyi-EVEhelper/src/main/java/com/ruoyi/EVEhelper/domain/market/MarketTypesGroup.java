package com.ruoyi.EVEhelper.domain.market;

public class MarketTypesGroup {
    private String description;
    private int market_group_id;
    private String name;
    private int parent_group_id;
    private String types;
    private String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMarket_group_id() {
        return market_group_id;
    }

    public void setMarket_group_id(int market_group_id) {
        this.market_group_id = market_group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParent_group_id() {
        return parent_group_id;
    }

    public void setParent_group_id(int parent_group_id) {
        this.parent_group_id = parent_group_id;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }
}
