package com.ruoyi.EVEhelper.service.market.Imple;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.EVEhelper.domain.market.MarketTypeOrder;
import com.ruoyi.EVEhelper.domain.market.MarketTypesGroup;
import com.ruoyi.EVEhelper.domain.universe.UniverseType;
import com.ruoyi.EVEhelper.mapper.market.SerenityMarketsMapper;
import com.ruoyi.EVEhelper.mapper.market.TranquilityMarketsMapper;
import com.ruoyi.EVEhelper.mapper.universe.SerenityUniverseMapper;
import com.ruoyi.EVEhelper.mapper.universe.TranquilityUniverseMapper;
import com.ruoyi.EVEhelper.service.market.MarketService;
import com.ruoyi.EVEhelper.service.market.marketThread.MarketTypesGroupDataThread;
import com.ruoyi.common.utils.http.HttpUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class MarketServiceImple implements MarketService {

    @Autowired
    private TranquilityMarketsMapper tranquilityMarketsMapper;
    @Autowired
    private SerenityMarketsMapper serenityMarketsMapper;
    @Autowired
    private SerenityUniverseMapper serenityUniverseMapper;
    @Autowired
    private TranquilityUniverseMapper tranquilityUniverseMapper;

    /**
     * 创建线程池
     */
    private static ExecutorService marketGroupDataPool = Executors.newFixedThreadPool(20);

    @Override
    public boolean cacheSerenityMarketGroups() {
        boolean flag = false;
        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor)marketGroupDataPool;
        if(poolExecutor.getActiveCount()==0){
            flag = true;
            serenityMarketsMapper.dropSerenityMarketGroups();
            marketGroupDataPool.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    MarketTypesGroupDataThread marketTypesGroupDataThread = new MarketTypesGroupDataThread(serenityMarketsMapper,tranquilityMarketsMapper,marketGroupDataPool,"serenity");
                    marketTypesGroupDataThread.start();
                    marketTypesGroupDataThread.join();
                }
            });
        }
        return flag;
    }

    @Override
    public boolean cacheTranquilityMarketGroups() {
        boolean flag = false;
        ThreadPoolExecutor theadPoolStatic = ((ThreadPoolExecutor) marketGroupDataPool);
        if(theadPoolStatic.getActiveCount()==0){
            flag = true;
            tranquilityMarketsMapper.dropTranquilityMarketGroups();
            marketGroupDataPool.execute(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    MarketTypesGroupDataThread marketTypesGroupDataThread = new MarketTypesGroupDataThread(serenityMarketsMapper,tranquilityMarketsMapper,marketGroupDataPool,"tranquility");
                    marketTypesGroupDataThread.start();
                    marketTypesGroupDataThread.join();
                }
            });
        }
        return flag;
    }

    @Override
    public List<MarketTypesGroup> getMarketGroupTree(Long group_id,String dataSource) {
        List<MarketTypesGroup> universeTypeList = new ArrayList<MarketTypesGroup>();
        switch (dataSource){
            case "serenity":
                universeTypeList = serenityMarketsMapper.getSerenityMarketGroupsByParentGroups(group_id);
                break;
            case "tranquility":
                universeTypeList = tranquilityMarketsMapper.getTranquilityMarketGroupsByParentGroups(group_id);
                break;
        }
        return universeTypeList;
    }

    @Override
    public List<UniverseType> getMarketTypeByGroup(Long group_id,String dataSource) {
        MarketTypesGroup group = serenityMarketsMapper.getSerenityMarketGroupByGroupId(group_id);
        List<UniverseType> universeTypeList = new ArrayList<UniverseType>();
        switch (dataSource){
            case "serenity":
                universeTypeList = serenityUniverseMapper.getSerenityMarketTypeListByTypeIdList(group.getTypes().replace("[","").replace("]"," ").split(","));
                break;
            case "tranquility":
                universeTypeList = tranquilityUniverseMapper.getTranquilityMarketTypeListByTypeIdList(group.getTypes().replace("[","").replace("]"," ").split(","));
                break;
        }
        return universeTypeList;
    }

    @Override
    public List<MarketTypeOrder> getUniverseTypeMarketPrices(String region_id, int type_id, String dataSource, String is_buy) {
        String data ="";
        switch (dataSource){
            case "serenity":
                data = HttpUtils.sendGet("https://esi.evepc.163.com/latest/markets/"+region_id+"/orders/","datasource=serenity&order_type="+is_buy+"&page=1&type_id="+type_id);
                break;
            case "tranquility":
                data = HttpUtils.sendGet("https://esi.evetech.net/latest/markets/"+region_id+"/orders/","datasource=tranquility&order_type="+is_buy+"&page=1&type_id="+type_id);
                break;
        }

        List<MarketTypeOrder> marketTypeOrderList = new ArrayList<MarketTypeOrder>();
        JSONArray jsonArray = JSONArray.parseArray(data);
        for(int i=0;i<jsonArray.size();i++){
            JSONObject object = jsonArray.getJSONObject(i);
            MarketTypeOrder order = new MarketTypeOrder();
            order.setOrder_id(object.getIntValue("order_id"));
            order.setDuration(object.getIntValue("duration"));
            order.setIs_buy_order(object.getString("is_buy_order"));
            order.setIssued(object.getString("issued"));
            order.setLocation_id(object.getIntValue("location_id"));
            order.setMin_volume(object.getIntValue("min_volume"));
            order.setPrice(object.getDouble("price"));
            order.setRange(object.getString("range"));
            order.setSystem_id(object.getString("system_id"));
            order.setType_id(object.getIntValue("type_id"));
            order.setVolume_remain(object.getString("volume_remain"));
            order.setVolume_total(object.getString("volume_total"));
            marketTypeOrderList.add(order);
        }
        List<MarketTypeOrder> marketTypeOrder = new ArrayList<MarketTypeOrder>();
        Map<String,String> map = new HashMap<String,String>();
        for(MarketTypeOrder order : marketTypeOrderList){
            if(map.get(order.getSystem_id())==null){
                String systemInfo ="";
                //查询空间站所在星系信息
                switch (dataSource){
                    case "serenity":
                        systemInfo = HttpUtils.sendGet("https://esi.evepc.163.com/latest/universe/systems/"+order.getSystem_id(),"datasource="+dataSource+"&language=zh");
                        break;
                    case "tranquility":
                        systemInfo = HttpUtils.sendGet("https://esi.evetech.net/latest/universe/systems/"+order.getSystem_id(),"datasource="+dataSource+"&language=zh");
                        break;
                }
                //实例化星系
                JSONObject system = JSONObject.parseObject(systemInfo);
                String system_name = system.getString("name");
                order.setSystem_name(system_name);
                map.put(order.getSystem_id(),system_name);
            }else{
                order.setSystem_name(map.get(order.getSystem_id()));
            }
            marketTypeOrder.add(order);
        }
        return marketTypeOrder;
    }

    @Override
    public int checkCacheProgress() {
        ThreadPoolExecutor theadPoolStatic = ((ThreadPoolExecutor) marketGroupDataPool);
        int count = theadPoolStatic.getActiveCount();
        return count;
    }


    @Override
    public Map<String, Object> getMarketPriceList(String dataSource) {
        Map<String,Object> map = new HashMap<String,Object>();
        String[] ice = {"17888","17889","16272","16275","16273","16274","17887"};
        String[] mineral = {"34","35","36","37","38","39","40","11399","48927"};
        String[] planetMaterial = {"2867","2868","2869","2870","2871","2872","2875","2876"};
        String[] moonMaterial = {"16640","16641","16642","16643","16644","16646","16647","16648","16649","16650","16651","16652","16653","16633","16634","16635","16636","16637","16638","16639"};
        map.put("ice",getMarketPrice(dataSource, ice));
        map.put("mineral",getMarketPrice(dataSource, mineral));
        map.put("planetMaterial",getMarketPrice(dataSource, planetMaterial));
        map.put("moonMaterial",getMarketPrice(dataSource, moonMaterial));
        return map;
    }

    /**
     * 获取指定typeId的最低价格
     * @param dataSource
     * @param types_id
     * @return
     */
    private List<Map<String,Object>> getMarketPrice(String dataSource, String[] types_id) {
        List<Map<String,Object>> orderList = new ArrayList<Map<String,Object>>();
        List<UniverseType> universeTypeList = serenityUniverseMapper.getSerenityMarketTypeListByTypeIdList(types_id);
        for(UniverseType type : universeTypeList){
            Map<String,Object> map = new HashMap<String,Object>();
            List<MarketTypeOrder> marketTypeOrderList =   getUniverseTypeMarketPrices("10000002",type.getType_id(),dataSource,"sell");
            //按照价格升序排列
            Collections.sort(marketTypeOrderList, new Comparator<MarketTypeOrder>() {
                @Override
                public int compare(MarketTypeOrder order1, MarketTypeOrder order2) {
                    double orderPrice = order1.getPrice()-order2.getPrice();
                    return orderPrice>0?1:-1;
                }
            });
            map.put("name",type.getName());
            map.put("price",marketTypeOrderList.get(0));
            orderList.add(map);
        }
        return orderList;
    }
}
