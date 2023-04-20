package com.ruoyi.EVEhelper.service.universe.universeThread;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.EVEhelper.domain.universe.UniverseType;
import com.ruoyi.EVEhelper.mapper.universe.SerenityUniverseMapper;
import com.ruoyi.EVEhelper.mapper.universe.TranquilityUniverseMapper;
import com.ruoyi.common.utils.http.HttpUtils;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class UniverseTypeDataThread extends Thread{
    private final SerenityUniverseMapper serenityUniverseMapper;
    private final TranquilityUniverseMapper tranquilityUniverseMapper;
    private final String dataSource;
    private final ExecutorService es;


    public UniverseTypeDataThread(ExecutorService es,SerenityUniverseMapper serenityUniverseMapper, TranquilityUniverseMapper tranquilityUniverseMapper, String dataSource){
        this.es = es;
        this.serenityUniverseMapper = serenityUniverseMapper;
        this.tranquilityUniverseMapper = tranquilityUniverseMapper;
        this.dataSource = dataSource;
    }

    @Override
    public void run() {
        List<String> typeList = new ArrayList<String>();
        for(int i=1;i<45;i++) {
            String types = "";
            switch (dataSource) {
                case "serenity":
                    types = HttpUtils.sendGet("https://esi.evepc.163.com/latest/universe/types/", "datasource=serenity&page=" + i);
                    break;
                case "tranquility":
                    types = HttpUtils.sendGet("https://esi.evetech.net/latest/universe/types/", "datasource=tranquility&page=" + i);
                    break;
            }
            String[] types_id = types.replace("[", " ").replace("]", " ").split(",");
            Collections.addAll(typeList,types_id);
        }
        List<List<String>> typeListList = ListUtils.partition(typeList,2000);
        for(List<String> typesList : typeListList){
            es.execute(new Runnable() {
                @Override
                public void run() {
                    boolean flag = true;
                    String unknownColumn = "";
                    String column = "type_id,capacity,description,dogma_attributes,dogma_effects,graphic_id,group_id,mass,name,packaged_volume,portion_size,published,radius,volume,icon_id,market_group_id";
                    List<UniverseType> universeTypeList = new ArrayList<UniverseType>();
                    for(String id : typesList){
                        String data = "";
                        switch (dataSource){
                            case "serenity":
                                data = HttpUtils.sendGet("https://esi.evepc.163.com/latest/universe/types/"+id+"/","datasource=serenity&language=zh");
                                break;
                            case "tranquility":
                                data = HttpUtils.sendGet("https://esi.evetech.net/latest/universe/types/"+id+"/","datasource=tranquility&language=zh");
                                break;
                        }
                        JSONObject jsonObject = JSONObject.parseObject(data);
                        if(jsonObject!=null){
                            for(String key : jsonObject.keySet()){
                                if(!column.contains(key)){
                                    unknownColumn = key;
                                    flag = false;
                                    break;
                                }
                            }
                            if(!flag){
                                System.out.println(id);
                                System.out.println("发现未知字段:"+unknownColumn);
                                break;
                            }
                            UniverseType type = new UniverseType();
                            type.setType_id(jsonObject.getInteger("type_id"));
                            type.setCapacity(jsonObject.getString("capacity"));
                            type.setDescription(jsonObject.getString("description"));
                            type.setDogma_attributes(jsonObject.getString("dogma_attributes"));
                            type.setDogma_effects(jsonObject.getString("dogma_effects"));
                            type.setGroup_id(jsonObject.getInteger("group_id"));
                            type.setMass(jsonObject.get("mass").toString());
                            type.setName(jsonObject.getString("name"));
                            type.setPackaged_volume(jsonObject.getString("packaged_volume"));
                            type.setRadius(jsonObject.getString("radius"));
                            type.setVolume(jsonObject.getString("volume"));
                            if(jsonObject.getInteger("portion_size")!=null){
                                type.setPortion_size(jsonObject.getInteger("portion_size"));
                            }
                            if(jsonObject.getInteger("published")!=null){
                                type.setPublished(jsonObject.getInteger("published"));
                            }
                            if(jsonObject.getInteger("icon_id")!=null){
                                type.setIcon_id(jsonObject.getInteger("icon_id"));
                            }
                            if(jsonObject.getInteger("graphic_id")!=null){
                                type.setGraphic_id(jsonObject.getInteger("graphic_id"));
                            }
                            if(jsonObject.getInteger("market_group_id")!=null){
                                type.setMarket_group_id(jsonObject.getInteger("market_group_id"));
                            }
                            universeTypeList.add(type);
                        }
                    }
                    try{
                        if(universeTypeList.size()>0){
                            switch (dataSource){
                                case "serenity":
                                    serenityUniverseMapper.cacheSerenityUniverseTypeInfo(universeTypeList);
                                    break;
                                case "tranquility":
                                    tranquilityUniverseMapper.cacheTranquilityUniverseTypeInfo(universeTypeList);
                                    break;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
