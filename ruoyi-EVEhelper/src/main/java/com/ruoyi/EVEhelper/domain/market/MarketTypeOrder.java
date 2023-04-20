package com.ruoyi.EVEhelper.domain.market;

public class MarketTypeOrder {
    private int order_id;
    private int duration;
    private String is_buy_order;
    private String issued;
    private String station;
    private int location_id;
    private int min_volume;
    private double price;
    private String range;
    private String system_id;
    private String system_name;
    private int type_id;
    private String volume_remain;
    private String volume_total;

    public String getSystem_name() {
        return system_name;
    }

    public void setSystem_name(String system_name) {
        this.system_name = system_name;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getIs_buy_order() {
        return is_buy_order;
    }

    public void setIs_buy_order(String is_buy_order) {
        this.is_buy_order = is_buy_order;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getMin_volume() {
        return min_volume;
    }

    public void setMin_volume(int min_volume) {
        this.min_volume = min_volume;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getVolume_remain() {
        return volume_remain;
    }

    public void setVolume_remain(String volume_remain) {
        this.volume_remain = volume_remain;
    }

    public String getVolume_total() {
        return volume_total;
    }

    public void setVolume_total(String volume_total) {
        this.volume_total = volume_total;
    }
}
