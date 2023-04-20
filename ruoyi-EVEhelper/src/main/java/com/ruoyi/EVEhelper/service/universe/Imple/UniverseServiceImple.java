package com.ruoyi.EVEhelper.service.universe.Imple;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.EVEhelper.domain.market.MarketTypesGroup;
import com.ruoyi.EVEhelper.domain.universe.*;
import com.ruoyi.EVEhelper.mapper.market.SerenityMarketsMapper;
import com.ruoyi.EVEhelper.mapper.universe.SerenityUniverseMapper;
import com.ruoyi.EVEhelper.mapper.universe.TranquilityUniverseMapper;
import com.ruoyi.EVEhelper.service.universe.UniverseService;
import com.ruoyi.EVEhelper.service.universe.universeThread.UniverseTypeDataThread;
import com.ruoyi.common.utils.ReqTools;
import com.ruoyi.common.utils.http.HttpUtils;
import lombok.SneakyThrows;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

@Service
public class UniverseServiceImple implements UniverseService {

    @Autowired
    private SerenityUniverseMapper serenityUniverseMapper;
    @Autowired
    private TranquilityUniverseMapper tranquilityUniverseMapper;
    @Autowired
    private SerenityMarketsMapper serenityMarketsMapper;

    private ReqTools reqT;

    /**
     * 创建线程池
     */
    private static ExecutorService universeTypeDataPool = Executors.newFixedThreadPool(20);

    @Override
    public UniverseType getUniverseTypeInfoByTypeId(int type_id,String dataSource) {
        UniverseType universeType = null;
        switch (dataSource){
            case "serenity":
                universeType = serenityUniverseMapper.getSerenityUniverseTypeInfoByTypeId(type_id);
                break;
            case "tranquility":
                universeType = tranquilityUniverseMapper.getTranquilityUniverseTypeInfoByTypeId(type_id);
                break;
        }
        return universeType;
    }

    @Override
    public boolean cacheSerenityUniverseTypeInfo() {
        boolean flag = true;
        //获取所有物品id
        try {
            ThreadPoolExecutor theadPoolStatic = ((ThreadPoolExecutor) universeTypeDataPool);
            if(theadPoolStatic.getActiveCount()==0){
                serenityUniverseMapper.dropSerenityUniverseTypes();
                //清空数据库
                universeTypeDataPool.execute(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        UniverseTypeDataThread universeTypeDataThread = new UniverseTypeDataThread(universeTypeDataPool,serenityUniverseMapper,tranquilityUniverseMapper,"serenity");
                        universeTypeDataThread.start();
                        universeTypeDataThread.join();
                    }
                });
            }else{
                flag = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean cacheTranquilityUniverseTypeInfo() {
        boolean flag = false;
        //获取所有物品id
        try {
            ThreadPoolExecutor theadPoolStatic = ((ThreadPoolExecutor) universeTypeDataPool);
            if(theadPoolStatic.getActiveCount()==0){
                //清空数据库
                tranquilityUniverseMapper.dropTranquilityUniverseTypes();
                universeTypeDataPool.execute(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        UniverseTypeDataThread universeTypeDataThread = new UniverseTypeDataThread(universeTypeDataPool,serenityUniverseMapper,tranquilityUniverseMapper,"tranquility");
                        universeTypeDataThread.start();
                        universeTypeDataThread.join();
                    }
                });
                flag= true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public List<UniverseType> searchUniverseTypeByTypeName(String type_name, String dataSource) {
        List<UniverseType> universeTypeList = new ArrayList<UniverseType>();
        switch (dataSource){
            case "serenity":
                universeTypeList = serenityUniverseMapper.searchSerenityUniverseTypeByTypeName(type_name);
                break;
            case "tranquility":
                universeTypeList = tranquilityUniverseMapper.searchTranquilityUniverseTypeByTypeName(type_name);
                break;
        }
        return universeTypeList;
    }

    @Override
    public void cacheUniverseSystemInfo(String dataSource) {
        List<String> systemIdList = new ArrayList<>();
        //获取所有星系编号
        String systemData = "";
        switch (dataSource){
            case "serenity":
                systemData = HttpUtils.sendGet("https://esi.evepc.163.com/latest/universe/systems/","datasource="+dataSource);
                if(!systemData.equals(""))serenityUniverseMapper.dropSerenitySystem();

                break;
            case "tranquility":
                systemData = HttpUtils.sendGet("https://esi.evetech.net/latest/universe/systems/","datasource="+dataSource);
                if(!systemData.equals(""))tranquilityUniverseMapper.dropTranquilitySystem();
                break;
        }
        JSONArray systemArray = JSONArray.parseArray(systemData);
        for(int i=0;i<systemArray.size();i++){
            String system_id = JSONObject.parseObject(systemArray.getString(i),String.class);
            systemIdList.add(system_id);
        }

        List<List<String>> star_ids = ListUtils.partition(systemIdList,100);
        for(List<String> idList : star_ids){
            universeTypeDataPool.execute(new Runnable() {
                @Override
                public void run() {
                    List<StarSystem> starSystemList = new ArrayList<>();
                    for(String id :idList){
                        String systemInfo = "";
                        switch (dataSource){
                            case "serenity":
                                systemInfo = HttpUtils.sendGet("https://esi.evepc.163.com/latest/universe/systems/"+id+"/","datasource="+dataSource+"&language=zh");
                                break;
                            case "tranquility":
                                systemInfo = HttpUtils.sendGet("https://esi.evetech.net/latest/universe/systems/"+id+"/","datasource="+dataSource+"&language=zh");
                                break;
                        }
                        if(!systemInfo.equals("")){
                            StarSystem starSystem = JSONObject.parseObject(systemInfo,StarSystem.class);
                            starSystemList.add(starSystem);
                        }
                    }
                    if(starSystemList.size()>0){
                        switch (dataSource){
                            case "serenity":
                                serenityUniverseMapper.cacheSerenitySystem(starSystemList);
                                break;
                            case "tranquility":
                                tranquilityUniverseMapper.cacheTranquilitySystem(starSystemList);
                                break;
                        }
                    }
                }
            });
        }
    }

    @Override
    public void cacheStationInfo(String dataSource) {
        List<String> stationsArrayObject = new ArrayList<>();
        //从数据库的星系信息中获取空间站列表
        switch (dataSource){
            case "serenity":
                stationsArrayObject = serenityUniverseMapper.getUniverseStations();
                break;
            case "tranquility":
                stationsArrayObject = tranquilityUniverseMapper.getUniverseStations();
                break;
        }
        if(stationsArrayObject.size()>0){
            List<String> stationList = new ArrayList<>();
            for(String stationsJson : stationsArrayObject){
                String[] stationArray = stationsJson.replace("[","").replace("]","").split(",");
                stationList.addAll(Arrays.asList(stationArray));
            }
            List<List<String>> stationIdListList = ListUtils.partition(stationList,100);
            for(List<String> stationIdList : stationIdListList){
                universeTypeDataPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Stations> stationsList = new ArrayList<>();
                        for(String station_id : stationIdList){
                            String stationData = "";
                            switch (dataSource){
                                case "serenity":
                                    stationData = HttpUtils.sendGet("https://esi.evepc.163.com/latest/universe/stations/"+station_id+"/","datasource="+dataSource);
                                    break;
                                case "tranquility":
                                    stationData = HttpUtils.sendGet("https://esi.evetech.net/latest/universe/stations/"+station_id+"/","datasource="+dataSource);
                                    break;
                            }
                            if(!stationData.equals("")){
                                Stations station = JSONObject.parseObject(stationData, Stations.class);
                                int num = -1;
                                //查询数据库是否已缓存
                                switch (dataSource){
                                    case "serenity":
                                        num = serenityUniverseMapper.checkUniverseStationUnique(station_id);
                                        break;
                                    case "tranquility":
                                        num =  tranquilityUniverseMapper.checkUniverseStationUnique(station_id);
                                        break;
                                }
                                if(num==0){
                                    stationsList.add(station);
                                }
                            }
                        }
                        if(stationsList.size()>0){
                            switch (dataSource){
                                case "serenity":
                                    serenityUniverseMapper.cacheUniverseSystemStations(stationsList);
                                    break;
                                case "tranquility":
                                    tranquilityUniverseMapper.cacheUniverseSystemStations(stationsList);
                                    break;
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void cacheUniverseGroupInfo(String dataSource) {
        int universeGroupInfo = 0;
        //从本地数据库获取物品分类信息
        switch (dataSource){
            case "serenity":
                universeGroupInfo = serenityUniverseMapper.getUniverseGroupInfo();
                break;
            case "tranquility":
                universeGroupInfo = tranquilityUniverseMapper.getUniverseGroupInfo();
                break;
        }
        if(universeGroupInfo==0){
            List<String> groupIdList = new ArrayList<String>();
            //从服务器中获取所有物品分类数组
            for(int i=1;i<3;i++){
                String data = "";
                switch(dataSource){
                    case "serenity":
                        data = HttpUtils.sendGet("https://esi.evepc.163.com/latest/universe/groups/","datasource="+dataSource+"&page=" + i);
                        break;
                    case "tranquility":
                        data = HttpUtils.sendGet("https://esi.evetech.net/latest/universe/groups/","datasource="+dataSource+"&page=" + i);
                        break;
                }
                String[] groupIds = data.replace("["," ").replace("]"," ").replace(" ","").split(",");
                Collections.addAll(groupIdList,groupIds);
            }
            //将分类id集合分成每份2000份的小集合
            List<List<String>> groupIdLists = ListUtils.partition(groupIdList,200);
            //通过线程将物品分类插入到数据库
            for(List<String> groupList : groupIdLists){
                universeTypeDataPool.execute(new Runnable(){
                    @Override
                    public void run() {
                        List<UniverseTypesGroup> universeTypesGroupList = new ArrayList<UniverseTypesGroup>();
                        for(String group_id : groupList){
                            //获取物品分类信息
                            String groupInfoData = "";
                            switch(dataSource){
                                case "serenity":
                                    groupInfoData = HttpUtils.sendGet("https://esi.evepc.163.com/latest/universe/groups/"+group_id+"/","datasource="+dataSource+"&language=zh");
                                    break;
                                case "tranquility":
                                    groupInfoData = HttpUtils.sendGet("https://esi.evetech.net/latest/universe/groups/"+group_id+"/","datasource="+dataSource+"&language=zh");
                                    break;
                            }
                            JSONObject jsonObject = JSONObject.parseObject(groupInfoData);
                            if(jsonObject!=null){
                                UniverseTypesGroup typeGroup = new UniverseTypesGroup();
                                typeGroup.setCategory_id(jsonObject.getInteger("category_id"));
                                typeGroup.setGroup_id(jsonObject.getInteger("group_id"));
                                typeGroup.setName(jsonObject.getString("name"));
                                typeGroup.setPublished(jsonObject.getString("publish"));
                                typeGroup.setTypes(jsonObject.getString("types"));
                                universeTypesGroupList.add(typeGroup);
                            }
                        }
                        //将集合插入到数据库
                        if(universeTypesGroupList.size()>0){
                            switch (dataSource){
                                case "serenity":
                                    serenityUniverseMapper.cacheUniverseTypeGroups(universeTypesGroupList);
                                    break;
                                case "tranquility":
                                    tranquilityUniverseMapper.cacheUniverseTypeGroups(universeTypesGroupList);
                                    break;
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void blueprintCheck(String blueprint_group_id,String market_group_id) {
        //查询市场分类
        MarketTypesGroup marketTypesGroup = serenityMarketsMapper.getSerenityMarketGroupByGroupId(Long.valueOf(market_group_id));
        List<String> typesList = new ArrayList<String>();
        String[] typesArray = marketTypesGroup.getTypes().replace("[","").replace("]","").split(",");
        Collections.addAll(typesList,typesArray);
        StringBuffer types = new StringBuffer();
        types.append("[");
        List<String> blueprintTypeIdList = new ArrayList<String>();
        //查询对应蓝图
        for(String type_id : typesList){
            //查询物品
            UniverseType universeType= serenityUniverseMapper.getSerenityUniverseTypeInfoByTypeId(Integer.valueOf(type_id));
            if(universeType!=null){
                //查询物品对应蓝图
                String[] universeTypeStringArray = universeType.getName().split(" ");
                String blueprintTypeId = null;
                if(!(universeType.getName().contains("蓝图")||universeType.getName().contains("配方"))){
                    if(universeTypeStringArray.length>1){
                        blueprintTypeId = serenityUniverseMapper.checkUniverseTypeBlueprint(universeTypeStringArray[0]+"蓝图 "+universeTypeStringArray[1]);
                    }else{
                        blueprintTypeId = serenityUniverseMapper.checkUniverseTypeBlueprint(universeTypeStringArray[0]+"蓝图");
                    }
                }else{
                    blueprintTypeId = serenityUniverseMapper.checkUniverseTypeBlueprint(universeType.getName());
                }
                if(blueprintTypeId!=null){
                    blueprintTypeIdList.add(blueprintTypeId);
                    if(types.toString().equals("[")){
                        types.append(blueprintTypeId);
                    }else{
                        types.append(","+blueprintTypeId);
                    }
                }
            }
        }
        types.append("]");
        //效验
        if(typesList.size()!=blueprintTypeIdList.size()){
            System.out.println("物品与蓝图不匹配");
            System.out.println("物品数："+typesList.size());
            System.out.println("蓝图数"+blueprintTypeIdList.size());
        }
        //保存到数据库
        serenityUniverseMapper.updateBlueprint(blueprint_group_id,types.toString());
    }

    @Override
    public void blueprintGroupBuild(String market_group_id, String blueprint_group_id) {
        universeTypeDataPool.execute(new Runnable(){
            @Override
            public void run() {
                Long long_market_group_id = Long.valueOf(market_group_id);
                Long long_blueprint_group_id = Long.valueOf(blueprint_group_id);
                //检查市场列表是否包含物品
                MarketTypesGroup marketTypesGroup = serenityMarketsMapper.getSerenityMarketGroupByGroupId(long_market_group_id);
                if(marketTypesGroup.getTypes().equals("[]")){
                    //查询所属id的子目录
                    List<MarketTypesGroup> marketTypesGroupList = serenityMarketsMapper.getSerenityMarketGroupsByParentGroups(long_market_group_id);
                    if(marketTypesGroupList.size()>0){
                        //检查蓝图目录是否完整
                        List<BlueprintTypesGroup> blueprintTypesGroupList =  serenityUniverseMapper.getSerenityUniverseBlueprintTypeGroupsByParentGroupId(long_blueprint_group_id);
                        if(blueprintTypesGroupList.size()!=marketTypesGroupList.size()){
                            List<BlueprintTypesGroup> blueprintTypesGroups = new ArrayList<BlueprintTypesGroup>();
                            //没有目录或者目录缺失
                            for(MarketTypesGroup marketMenu : marketTypesGroupList){
                                BlueprintTypesGroup blueprintMenu = new BlueprintTypesGroup();
                                blueprintMenu.setDescription(marketMenu.getDescription());
                                blueprintMenu.setName(marketMenu.getName());
                                blueprintMenu.setParent_blueprint_group_id(long_blueprint_group_id.intValue());
                                blueprintMenu.setTypes(marketMenu.getTypes());
                                blueprintTypesGroups.add(blueprintMenu);
                            }
                            //保存到数据库
                            serenityUniverseMapper.saveBlueprintTypeGroups(blueprintTypesGroups);
                            for(BlueprintTypesGroup blueprintMenu : blueprintTypesGroups){
                                //从数据库中查询出id
                                int menuId = serenityUniverseMapper.getBlueprintTypeGroupIdByGroupName(blueprintMenu.getName(),blueprintMenu.getDescription());
                                for(MarketTypesGroup marketMenu : marketTypesGroupList){
                                    if(marketMenu.getName().equals(blueprintMenu.getName())){
                                        blueprintGroupBuild(String.valueOf(marketMenu.getMarket_group_id()),String.valueOf(menuId));
                                    }
                                }
                            }
                        }else{
                            //检查蓝图目录完整
                            for(MarketTypesGroup marketMenu : marketTypesGroupList)
                                for(BlueprintTypesGroup blueprintMenu : blueprintTypesGroupList){
                                    if(marketMenu.getName().equals(blueprintMenu.getName())){
                                        blueprintGroupBuild(String.valueOf(marketMenu.getMarket_group_id()),String.valueOf(blueprintMenu.getBlueprint_group_id()));
                                    }
                                }
                        }
                    }
                }else {
                    blueprintCheck(blueprint_group_id,market_group_id);
                }
            }
        });
    }

    @Override
    public int checkThreadTempData() {
        ThreadPoolExecutor theadPoolStatic = ((ThreadPoolExecutor) universeTypeDataPool);
        int queueSize = theadPoolStatic.getQueue().size();
        System.out.println("当前排队线程数：" + queueSize);
        int activeCount = theadPoolStatic.getActiveCount();
        System.out.println("当前活动线程数：" + activeCount);
        long completedTaskCount = theadPoolStatic.getCompletedTaskCount();
        System.out.println("执行完成线程数：" + completedTaskCount);
        long taskCount = theadPoolStatic.getTaskCount();
        System.out.println("总线程数：" + taskCount);
        return completedTaskCount==taskCount?0:1;
    }
}
