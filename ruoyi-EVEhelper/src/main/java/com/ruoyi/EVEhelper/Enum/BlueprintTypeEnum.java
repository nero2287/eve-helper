package com.ruoyi.EVEhelper.Enum;

public enum BlueprintTypeEnum {

    Minerals("1","矿物"),
    Planetary("2","行星材料"),
    Reaction("3","反应材料"),
    Components("4","组件"),
    Items("5","物品"),
    Salvage("6","打捞件");

    private String key;
    private String value;

    BlueprintTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() { return value; }

    public String getValueByKey(String value){
        String name = "";
        switch (value){
            case "1":
                name =  Minerals.value;
                break;
            case "2":
                name =  Planetary.value;
                break;
            case "3":
                name =  Reaction.value;
                break;
            case "4":
                name =  Components.value;
                break;
            case "5":
                name =  Items.value;
                break;
            case "6":
                name =  Salvage.value;
                break;
        }
        return name;
    }

    public String getKeyByValue(){
        String key = "";
        switch (value){
            case "矿物":
                key =  Minerals.key;
                break;
            case "行星材料":
                key =  Planetary.key;
                break;
            case "反应材料":
                key =  Reaction.key;
                break;
            case "组件":
                key =  Components.key;
                break;
            case "物品":
                key =  Items.key;
                break;
            case "打捞件":
                key =  Salvage.key;
                break;
        }
        return key;
    }
}
