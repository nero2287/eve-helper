package com.ruoyi.EVEhelper.service.contract.Imple;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.EVEhelper.domain.character.Character;
import com.ruoyi.EVEhelper.domain.contract.Contract;
import com.ruoyi.EVEhelper.domain.contract.ContractCondition;
import com.ruoyi.EVEhelper.domain.contract.ContractItem;
import com.ruoyi.EVEhelper.domain.universe.StarSystem;
import com.ruoyi.EVEhelper.mapper.character.SerenityCharacterMapper;
import com.ruoyi.EVEhelper.mapper.character.TranquilityCharacterMapper;
import com.ruoyi.EVEhelper.mapper.contract.SerenityContractMapper;
import com.ruoyi.EVEhelper.mapper.contract.TranquilityContractMapper;
import com.ruoyi.EVEhelper.mapper.universe.SerenityUniverseMapper;
import com.ruoyi.EVEhelper.mapper.universe.TranquilityUniverseMapper;
import com.ruoyi.EVEhelper.service.contract.ContractService;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.http.HttpUtils;
import lombok.SneakyThrows;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
public class ContractServiceImple implements ContractService {
    @Autowired
    private SerenityContractMapper serenityContractMapper;
    @Autowired
    private TranquilityContractMapper tranquilityContractMapper;
    @Autowired
    private SerenityUniverseMapper serenityUniverseMapper;
    @Autowired
    private TranquilityUniverseMapper tranquilityUniverseMapper;
    @Autowired
    private SerenityCharacterMapper serenityCharacterMapper;
    @Autowired
    private TranquilityCharacterMapper tranquilityCharacterMapper;

    /**
     * 创建线程池
     */
    private static ExecutorService contractInfoPool = Executors.newFixedThreadPool(20);

    @Override
    public Map<String,Object> getContractList(String dataSource) {
//        Map<String,List<Contract>> contractMap = new HashMap<String,List<Contract>>();
//        Map<String,Object> map = new HashMap<String,Object>();
//        List<Contract> newContractList = new ArrayList<Contract>();
//        boolean flag = true;
//        for(int i=1;flag;i++){
//            String data = "";
//            switch (dataSource){
//                case "serenity":
//                    data = HttpUtils.sendGet("https://esi.evepc.163.com/latest/contracts/public/10000002/","datasource=serenity&page="+i);
//                    break;
//                case "tranquility":
//                    data = HttpUtils.sendGet("https://esi.evetech.net/latest/contracts/public/10000002","datasource=tranquility&page="+i);
//                    break;
//            }
//            if(data.equals("")){
//                flag = false;
//            }else{
//                List<Contract> contractList = JSONObject.parseArray(data,Contract.class);
//                newContractList.addAll(contractList);
//            }
//        }
//        //将list分割成每个list20个元素
//        List<List<Contract>> listOfContractList =  ListUtils.partition(newContractList,20);
//
//        for(int i=0;i<listOfContractList.size();i++){
//            contractMap.put("page"+i,listOfContractList.get(i));
//        }
//        map.put("pageAmount",listOfContractList.size());
//        map.put(dataSource+"contractMap",contractMap);
//        contractInfoPool.execute(new Runnable() {
//            @Override
//            public void run() {
//                switch (dataSource){
//                    case "serenity":
//                        serenityContractMapper.deleteContractList();
//                        serenityContractMapper.saveContractList(newContractList);
//                        break;
//                    case "tranquility":
//                        tranquilityContractMapper.deleteContractList();
//                        tranquilityContractMapper.saveContractList(newContractList);
//                        break;
//                }
//            }
//        });
//        return map;
        return null;
    }

    @Override
    public void getContractList(String region_id,String dataSource, RedisCache redisCache) {
        contractInfoPool.execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                Map<String,List<Contract>> contractMap = new HashMap<String,List<Contract>>();
                List<Contract> newContractList = new ArrayList<Contract>();
                boolean flag = true;
                for(int i=1;flag;i++){
                    String data = "";
                    switch (dataSource){
                        case "serenity":
                            data = HttpUtils.sendGet("https://esi.evepc.163.com/latest/contracts/public/"+region_id+"/","datasource=serenity&page="+i);
                            break;
                        case "tranquility":
                            data = HttpUtils.sendGet("https://esi.evetech.net/latest/contracts/public/"+region_id+"/","datasource=tranquility&page="+i);
                            break;
                    }
                    if(data.equals("")){
                        flag = false;
                    }else{
                        List<Contract> contractList = JSONObject.parseArray(data,Contract.class);
                        newContractList.addAll(contractList);
                    }
                }
                //将list分割成每个list100个元素
                List<List<Contract>> listOfContractList =  ListUtils.partition(newContractList,100);

                //使用CountDownLatch计数器
                CountDownLatch countDown = new CountDownLatch(listOfContractList.size());
                List<Contract> contractList = new ArrayList<>();
                switch (dataSource){
                    case "serenity":
                        serenityContractMapper.deleteContractItems();
                        break;
                    case "tranquility":
                        tranquilityContractMapper.deleteContractItems();
                        break;
                }

                for(List<Contract> list : listOfContractList){
                    contractInfoPool.execute(new Runnable() {
                        @Override
                        public void run() {
                            //获取合同页面
                            List<ContractItem> contractItems= new ArrayList<>();
                            try{
                                for(Contract contract : list){
                                    String data = "";
                                    //获取所有合同
                                    switch (dataSource){
                                        case "serenity":
                                            data = HttpUtils.sendGet("https://esi.evepc.163.com/latest/contracts/public/items/"+contract.getContract_id()+"/","datasource=serenity");
                                            break;
                                        case "tranquility":
                                            data = HttpUtils.sendGet("https://esi.evetech.net/latest/contracts/public/items/"+contract.getContract_id()+"/","datasource=tranquility");
                                            break;
                                    }
                                    if(!data.equals("")) {
                                        if(contract.getContract_id()!=0){
                                            contract.setRegion_id(region_id);
                                            contractList.add(contract);
                                        }else{
                                            System.out.println("----------------");
                                        }

                                        JSONArray jsonArray = JSONArray.parseArray(data);
                                        for (int i = 0; i < jsonArray.size(); i++) {
                                            ContractItem contractItem = JSONObject.parseObject(jsonArray.getString(i),ContractItem.class);
                                            contractItem.setContract_id(contract.getContract_id());
                                            contractItems.add(contractItem);
                                        }
                                    }
                                }
                            }catch (ConcurrentModificationException e){
                                e.printStackTrace();
                            }

                            if(contractItems.size()>0){
                                //保存所有合同
                                switch (dataSource){
                                    case "serenity":
                                        serenityContractMapper.saveContractIncludedItem(contractItems);
                                        break;
                                    case "tranquility":
                                        tranquilityContractMapper.saveContractIncludedItem(contractItems);
                                        break;
                                }
                            }
                            countDown.countDown();
                        }
                    });
                }
                countDown.await();
                //将list分割成每个list20个元素
                List<List<Contract>> listList =  ListUtils.partition(contractList,20);
                for(int i=0;i<listList.size();i++){
                    contractMap.put("page"+i,listList.get(i));
                }
                switch (dataSource){
                    case "serenity":
                        redisCache.deleteObject("SerenityContract");
                        redisCache.setCacheMap("SerenityContract",contractMap);//合同信息
                        redisCache.deleteObject("SerenityAmountPage");
                        redisCache.setCacheObject("SerenityAmountPage",listList.size());
                        serenityContractMapper.deleteContractList();
                        serenityContractMapper.saveContractList(contractList);
                        break;
                    case "tranquility":
                        tranquilityContractMapper.deleteContractList();
                        tranquilityContractMapper.saveContractList(contractList);
                        break;
                }
                cacheCharactersInContractInfo(dataSource);
            }
        });
    }

    @Override
    public List<Contract> getContractByConditions(ContractCondition contractCondition) {
        return serenityContractMapper.getContractList(contractCondition);
    }

    @Override
    public int getContractSizeByConditions(ContractCondition contractCondition) {
        return serenityContractMapper.getContractSizeByConditions(contractCondition);
    }

    @Override
    public int checkContractDataProgress() {
        ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor)contractInfoPool;
        //执行完成线程数
        long completedTaskCount = poolExecutor.getCompletedTaskCount();
        //执行完成线程总数
        long taskCount = poolExecutor.getTaskCount();
        return taskCount==completedTaskCount?0:1;
    }

    @Override
    public List<Contract> getContractDetails(List<Contract> contractList,String dataSource) {
        for(Contract contract : contractList){
            if(contract.getContract_id()==48320368){
                System.out.print("---------------");
            }
            List<ContractItem> contractItemList = new ArrayList<>();
            String location_name = "";
            String character_name = "";
            //合同地点
            if(contract.getStart_location_id().length()==13){
                location_name = "未公开";
            }else{
                //获取空间站所在星系
                switch (dataSource){
                    case "serenity":
                        location_name = serenityUniverseMapper.getUniverseStationSystemByStation_id(contract.getStart_location_id());
                        break;
                    case "tranquility":
                        location_name = tranquilityUniverseMapper.getUniverseStationSystemByStation_id(contract.getStart_location_id());
                        break;
                }
                if(location_name.equals("")){
                    location_name = "未知";
                }
            }
            Character character = null;
            //从数据库中查询角色名称
            switch (dataSource){
                case "serenity":
                    character = serenityCharacterMapper.getCharacterNameByCharacterId(contract.getIssuer_id());
                    break;
                case "tranquility":
                    character = tranquilityCharacterMapper.getCharacterNameByCharacterId(contract.getIssuer_id());
                    break;
            }
            if(character!=null){
                character_name = character.getName();
            }else{
                character_name = contract.getIssuer_id();
            }

            //合同包含物品
            switch (dataSource){
                case "serenity":
                    contractItemList = serenityContractMapper.getContractItemsByContractId(contract.getContract_id());
                    break;
                case "tranquility":
                    contractItemList = tranquilityContractMapper.getContractItemsByContractId(contract.getContract_id());
                    break;
            }
            if(contractItemList.size()>0)contract.setContractItems(contractItemList);
            //合同地点
            contract.setLocation_name(location_name);
            //发布人
            contract.setCharacter_name(character_name);
            //合同类型
            switch (contract.getType()){
                case "item_exchange":
                    contract.setType("物品交换");
                    break;
                case "auction":
                    contract.setType("拍卖");
                    break;
            }
        }
        return contractList;
    }

    private void cacheCharactersInContractInfo(String dataSource){
        List<String> characterIdList = new ArrayList<>();
        //从合同中获取id
        switch (dataSource){
            case "serenity":
                characterIdList= serenityContractMapper.getCharacterIdByContractInfo();
                break;
            case "tranquility":
                characterIdList= tranquilityContractMapper.getCharacterIdByContractInfo();
                break;
        }
        //遍历集合提取出没有信息的id
        List<List<String>> characterListList = ListUtils.partition(characterIdList,100);
        for(List<String> idList : characterListList){
            contractInfoPool.execute(new Runnable() {
                @Override
                public void run() {
                    List<String> noMessageCharacterList = new ArrayList<>();
                    List<Character> characterList = new ArrayList<>();
                    for(String character_id : idList){
                        switch (dataSource){
                            case "serenity":
                                if(serenityCharacterMapper.checkCharacterUnique(character_id)==0){
                                    noMessageCharacterList.add(character_id);
                                }
                                break;
                            case "tranquility":
                                if(tranquilityCharacterMapper.checkCharacterUnique(character_id)==0){
                                    noMessageCharacterList.add(character_id);
                                }
                                break;
                        }
                    }
                    if(noMessageCharacterList.size()>0){
                        for(String character_id :noMessageCharacterList){
                            String data = "";
                            //缓存没有数据的角色
                            switch (dataSource){
                                case "serenity":
                                    data = HttpUtils.sendGet("https://esi.evepc.163.com/latest/characters/"+character_id+"/","datasource=serenity");
                                    break;
                                case "tranquility":
                                    data = HttpUtils.sendGet("https://esi.evetech.net/latest/characters/"+character_id+"/","datasource=serenity");
                                    break;
                            }

                            if(!data.equals("")){
                                Character character = JSONObject.parseObject(data, Character.class);
                                character.setCharacter_id(character_id);
                                characterList.add(character);
                            }
                        }
                    }
                    if(characterList.size()>0){
                        switch (dataSource){
                            case "serenity":
                                serenityCharacterMapper.cacheCharacterInfo(characterList);
                                break;
                            case "tranquility":
                                tranquilityCharacterMapper.cacheCharacterInfo(characterList);
                                break;
                        }
                    }
                }
            });
        }
    }
}
