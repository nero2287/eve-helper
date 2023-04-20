package com.ruoyi.common.enums;

public enum RaceType {

    G("1","盖伦特"),
    C("2","加达里"),
    A("3","艾玛"),
    M("4","米玛塔尔");

    private String key;
    private String name;

    RaceType(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() { return name; }
}
