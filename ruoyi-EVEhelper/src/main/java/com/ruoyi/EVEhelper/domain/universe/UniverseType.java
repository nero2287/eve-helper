package com.ruoyi.EVEhelper.domain.universe;

import com.ruoyi.common.core.domain.BaseEntity;

public class UniverseType extends BaseEntity {
    private int type_id;
    private String capacity;
    private String description;
    private String dogma_attributes;
    private String dogma_effects;
    private int graphic_id;
    private int icon_id;
    private int group_id;
    private String mass;
    private String name;
    private String packaged_volume;
    private int portion_size;
    private int published;
    private String radius;
    private String volume;
    private int market_group_id;
    private String picture;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getMarket_group_id() {
        return market_group_id;
    }

    public void setMarket_group_id(int market_group_id) {
        this.market_group_id = market_group_id;
    }

    public int getIcon_id() {
        return icon_id;
    }

    public void setIcon_id(int icon_id) {
        this.icon_id = icon_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDogma_attributes() {
        return dogma_attributes;
    }

    public void setDogma_attributes(String dogma_attributes) {
        this.dogma_attributes = dogma_attributes;
    }

    public String getDogma_effects() {
        return dogma_effects;
    }

    public void setDogma_effects(String dogma_effects) {
        this.dogma_effects = dogma_effects;
    }

    public int getGraphic_id() {
        return graphic_id;
    }

    public void setGraphic_id(int graphic_id) {
        this.graphic_id = graphic_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackaged_volume() {
        return packaged_volume;
    }

    public void setPackaged_volume(String packaged_volume) {
        this.packaged_volume = packaged_volume;
    }

    public int getPortion_size() {
        return portion_size;
    }

    public void setPortion_size(int portion_size) {
        this.portion_size = portion_size;
    }

    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
