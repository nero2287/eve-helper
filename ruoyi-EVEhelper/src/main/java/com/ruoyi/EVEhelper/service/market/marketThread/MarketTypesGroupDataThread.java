package com.ruoyi.EVEhelper.service.market.marketThread;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.EVEhelper.domain.market.MarketTypesGroup;
import com.ruoyi.EVEhelper.mapper.market.SerenityMarketsMapper;
import com.ruoyi.EVEhelper.mapper.market.TranquilityMarketsMapper;
import com.ruoyi.common.utils.http.HttpUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MarketTypesGroupDataThread extends Thread{
    private final SerenityMarketsMapper serenityMarketsMapper;
    private final TranquilityMarketsMapper tranquilityMarketsMapper;
    private final ExecutorService marketGroupDataPool;
    private final String dataSource;
    public MarketTypesGroupDataThread(SerenityMarketsMapper serenityMarketsMapper, TranquilityMarketsMapper tranquilityMarketsMapper, ExecutorService marketGroupDataPool, String dataSource){
        this.tranquilityMarketsMapper = tranquilityMarketsMapper;
        this.serenityMarketsMapper = serenityMarketsMapper;
        this.dataSource = dataSource;
        this.marketGroupDataPool = marketGroupDataPool;
    }
    @Override
    public void run() {
        String data = "";
        switch (dataSource){
            case "serenity":
                //获取国服市场物品列表
                data = HttpUtils.sendGet("https://esi.evepc.163.com/latest/markets/groups/","datasource=serenity");
                break;
            case "tranquility":
                //获取欧服市场物品列表
                data = HttpUtils.sendGet("https://esi.evetech.net/latest/markets/groups/","datasource=tranquility");
                break;
        }

        String[] groups_id = data.replace("[","").replace("]","").split(",");
        List<String> groupIdList = new ArrayList<String>();
        Collections.addAll(groupIdList,groups_id);
        int n = 200;
        int remainder = groupIdList.size() % n;
        int size  = (groupIdList.size() /n);
        List<List<String>> typeList = new ArrayList<List<String>>();
        for(int i=0;i<size;i++){
            List<String> subList = groupIdList.subList(i*n,(i+1)*n);
            typeList.add(subList);
        }
        if(remainder >0){
            List<String> subList = groupIdList.subList(size*n,size*n+remainder);
            typeList.add(subList);
        }

        for(List<String> list : typeList){
            marketGroupDataPool.execute(new Runnable() {
                @Override
                public void run() {
                    boolean flag = false;
                    String unknownColumn = "";
                    int i=1;
                    List<MarketTypesGroup> groupList = new ArrayList<MarketTypesGroup>();
                    for(String group : list){
                        System.out.println("数据量："+i);
                        String data = "";
                        switch (dataSource){
                            case "serenity":
                                data = HttpUtils.sendGet("https://esi.evepc.163.com/latest/markets/groups/"+group+"/","datasource=serenity&language=zh");
                                break;
                            case "tranquility":
                                data = HttpUtils.sendGet("https://esi.evetech.net/latest/markets/groups/"+group+"/","datasource=tranquility&language=zh");
                                break;
                        }

                        JSONObject jsonObject = JSONObject.parseObject(data);
                        for(String key:jsonObject.keySet()){
                            String column = "description,market_group_id,name,parent_group_id,types";
                            if(!column.contains(key)){
                                flag = true;
                                unknownColumn = key;
                                break;
                            }
                        }
                        if(flag){
                            System.out.println("id:"+group+",发现未知字段:"+unknownColumn);
                            break;
                        }
                        MarketTypesGroup groups = new MarketTypesGroup();
                        groups.setDescription(jsonObject.getString("description"));
                        groups.setName(jsonObject.getString("name"));
                        if(jsonObject.getInteger("market_group_id")!=null){
                            groups.setMarket_group_id(jsonObject.getInteger("market_group_id"));
                        }
                        if(jsonObject.getInteger("parent_group_id")!=null){
                            groups.setParent_group_id(jsonObject.getInteger("parent_group_id"));
                        }
                        if(jsonObject.getString("types")!=null){
                            groups.setTypes(jsonObject.getString("types"));
                        }
                        groupList.add(groups);
                        i++;
                    }
                    if(groupList.size()>0){
                        switch (dataSource){
                            case "serenity":
                                serenityMarketsMapper.cacheSerenityMarketGroups(groupList);
                                break;
                            case "tranquility":
                                tranquilityMarketsMapper.cacheTranquilityMarketGroups(groupList);
                                break;
                        }
                    }
                }
            });
        }
    }
}
