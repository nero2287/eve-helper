package com.ruoyi.common.enums;

public enum DecompositionType {

    T0("0","原料",new String[]{"21415","14633","38215"}),

    T1("1","加工材料",new String[]{"44103","25636"}),

    T2("2","精练材料",new String[]{"53411"}),

    T3("3","高级组件",new String[]{"36708"});


    private String key;
    private String name;

    DecompositionType(String key, String name, String[] articleTypeList) {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
