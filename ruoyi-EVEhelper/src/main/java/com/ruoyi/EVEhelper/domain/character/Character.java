package com.ruoyi.EVEhelper.domain.character;

public class Character {
    private String character_id;
    private String alliance_id;
    private String birthday;
    private String bloodline_id;
    private String corporation_id;
    private String gender;
    private String name;
    private int race_id;
    private String security_status;

    public String getCharacter_id() {
        return character_id;
    }

    public void setCharacter_id(String character_id) {
        this.character_id = character_id;
    }

    public String getAlliance_id() {
        return alliance_id;
    }

    public void setAlliance_id(String alliance_id) {
        this.alliance_id = alliance_id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBloodline_id() {
        return bloodline_id;
    }

    public void setBloodline_id(String bloodline_id) {
        this.bloodline_id = bloodline_id;
    }

    public String getCorporation_id() {
        return corporation_id;
    }

    public void setCorporation_id(String corporation_id) {
        this.corporation_id = corporation_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRace_id() {
        return race_id;
    }

    public void setRace_id(int race_id) {
        this.race_id = race_id;
    }

    public String getSecurity_status() {
        return security_status;
    }

    public void setSecurity_status(String security_status) {
        this.security_status = security_status;
    }
}
