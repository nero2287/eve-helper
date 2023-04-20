package com.ruoyi.EVEhelper.service.industry.blueprint.Imple;


import com.ruoyi.EVEhelper.domain.Industry.Blueprint;
import com.ruoyi.EVEhelper.domain.Industry.BlueprintMaterial;
import com.ruoyi.EVEhelper.domain.Industry.BlueprintMaterialItem;
import com.ruoyi.EVEhelper.domain.Industry.BlueprintOperateLog;
import com.ruoyi.EVEhelper.domain.universe.UniverseType;
import com.ruoyi.EVEhelper.mapper.industry.blueprint.SerenityBlueprintMapper;
import com.ruoyi.EVEhelper.mapper.universe.SerenityUniverseMapper;
import com.ruoyi.EVEhelper.service.industry.blueprint.BlueprintService;
import com.ruoyi.common.utils.ReqTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BlueprintServiceImple implements BlueprintService {


    public static final String PLACEHOLDER = " x ";

    @Autowired
    private SerenityBlueprintMapper serenityBlueprintMapper;
    @Autowired
    private SerenityUniverseMapper serenityUniverseMapper;
    @Autowired
    private ReqTools reqTools;

    @Override
    public String inputBlueprintInfo(String[] stringArray) {
        BlueprintOperateLog blueprintOperateLog = new BlueprintOperateLog();
        Blueprint blueprint = new Blueprint();
        //index = XXX,hh:mm:ss
        String[] index = stringArray[0].split("每流程时间");
        //index[0]=XXX  index[1]=hh:mm:ss
        for(int i=0;i<index.length;i++){
            index[i] = index[i].trim();
        }
        if(index.length==2) {
            String product_name = stringArray[1].split(PLACEHOLDER)[1];
            if(index[0].equals("***")){
                //没有找到蓝图 保存蓝图信息
                StringBuilder stringBuilder = new StringBuilder(stringArray[0].split("\\*\\*\\* ")[1]);
                for(int i =1;i<stringArray.length;i++){
                    stringBuilder.append(stringArray[i]).append("回车符");
                }
                if(serenityBlueprintMapper.checkBlueprintCache(product_name)==0){
                    blueprintOperateLog.setOperate_type("缓存");
                    blueprintOperateLog.setBlueprint_name(product_name);
                    serenityBlueprintMapper.cacheBlueprintInfo(product_name,stringBuilder.toString());
                    serenityBlueprintMapper.saveBlueprintOperateLog(blueprintOperateLog);
                }
            }else{
                //有蓝图信息
                String blueprintName = index[0];
                Map<String,Object> map = checkBlueprintName(blueprintName);
                if((boolean)map.get("exits")){
                    blueprintOperateLog.setOperate_type("新增");
                    if((boolean)map.get("repeat")){
                        blueprintOperateLog.setOperate_type("覆盖");
                        serenityBlueprintMapper.deleteBlueprintInfoByBlueprintName(blueprintName);
                        serenityBlueprintMapper.deleteBlueprintMaterialListByMaterialId((String)map.get("BlueprintMaterialId"));
                    }
                    //查询蓝图物品信息
                    UniverseType blueprintInfo = serenityUniverseMapper.getUniverseTypeInfoByTypeName(blueprintName);
                    if(blueprintInfo!=null){
                        //蓝图名称
                        blueprint.setBlueprint_name(blueprintName);
                        blueprintOperateLog.setBlueprint_name(blueprintName);
                        //蓝图制造时间
                        blueprint.setBlueprint_time(index[1]);
                        //蓝图物品分类id
                        blueprint.setBlueprint_group_id(blueprintInfo.getGroup_id());
                        //蓝图物品id
                        blueprint.setBlueprint_type_id(blueprintInfo.getType_id());
                        blueprintOperateLog.setBlueprint_type_id(blueprint.getBlueprint_type_id());
                        //查询蓝图产物物品信息
                        UniverseType blueprintProductInfo = serenityUniverseMapper.getUniverseTypeInfoByTypeName(product_name);
                        //蓝图产物物品id
                        if(blueprintProductInfo!=null){
                            blueprint.setBlueprint_product_type_id(blueprintProductInfo.getType_id());
                        }else{
                            blueprint.setBlueprint_product_type_id(0);
                        }
                        //蓝图产物物品名称
                        blueprint.setBlueprint_product_name(product_name);
                        //蓝图产物物品数量
                        String product_num = stringArray[2].split(PLACEHOLDER)[0];
                        long num = 0L;
                        if(product_num.contains(",")){
                            num = Long.parseLong(product_num.replace(",", ""));
                        }else{
                            num = Long.parseLong(product_num);
                        }
                        blueprint.setBlueprint_product_num(BigDecimal.valueOf(num));

                        String blueprint_Material_id = reqTools.getRandomNumber(6);
                        if(serenityBlueprintMapper.checkBlueprintMaterialId(blueprint_Material_id)==0){
                            blueprint.setBlueprint_materials_id(blueprint_Material_id);
                        }else{
                            blueprint.setBlueprint_materials_id(reqTools.getRandomNumber(6));
                        }
                        blueprintOperateLog.setBlueprint_materials_id(blueprint.getBlueprint_materials_id());
                        List<BlueprintMaterial> blueprintMaterialList = new ArrayList<BlueprintMaterial>();
                        for(int i=2;i<stringArray.length;i++){
                            BlueprintMaterial blueprintMaterial = new BlueprintMaterial();
                            String info = stringArray[i];
                            if(info.split(PLACEHOLDER).length==1)
                                continue;
                            String material_name=info.split(PLACEHOLDER)[1];
                            String material_num= info.split(PLACEHOLDER)[0].replace(",","");
                            BlueprintMaterialItem blueprintMaterialItem = checkBlueprintMaterialType(material_name);
                            if(blueprintMaterialItem!=null){
                                blueprintMaterial.setMaterials_type_name(blueprintMaterialItem.getMaterial_type_name());
                                blueprintMaterial.setMaterials_type_id(blueprintMaterialItem.getMaterial_type_id());
                                blueprintMaterial.setMaterials_name(material_name);
                                blueprintMaterial.setMaterials_num(BigDecimal.valueOf(Long.parseLong(material_num)));
                                blueprintMaterial.setBlueprint_materials_id(blueprint.getBlueprint_materials_id());
                            }else
                                return material_name;
                            blueprintMaterialList.add(blueprintMaterial);
                        }
                        blueprint.setBlueprintMaterialList(blueprintMaterialList);
                        //保存
                        serenityBlueprintMapper.saveBlueprintInfo(blueprint);
                        serenityBlueprintMapper.saveBlueprintMaterial(blueprint.getBlueprintMaterialList());
                        serenityBlueprintMapper.saveBlueprintOperateLog(blueprintOperateLog);
                    }else{
                        stringArray[0] = "*** "+stringArray[0].split("\\*\\*\\* ")[1];
                        inputBlueprintInfo(stringArray);
                    }
                }else{
                    stringArray[0] = "*** 每流程时间 "+stringArray[0].split("每流程时间")[1];
                    inputBlueprintInfo(stringArray);
                }
            }
        }else{
            return "蓝图信息有误";
        }
        return null;
    }


    @Override
    public Map<String,Object> checkBlueprintName(String blueprintName) {
        Map<String,Object> map = new HashMap<String,Object>();
        String name = "";
        String size = "";
        String blueprintString ="";
        boolean repeat = false;
        boolean exits = false;
        UniverseType universeType = serenityUniverseMapper.getUniverseTypeInfoByTypeName(blueprintName);
        if((blueprintName.contains("蓝图")||blueprintName.contains("反应配方"))&&universeType!=null){
            repeat = serenityBlueprintMapper.checkBlueprintName(blueprintName)!=0;
            exits = true;
            blueprintString = blueprintName;
        }else{
            if(!blueprintName.contains("蓝图")&&!blueprintName.contains("反应配方")){
                StringBuffer stringBuffer = new StringBuffer(blueprintName);
                String[] blueprintNameArray = null;
                if(blueprintName.contains(" I")||blueprintName.contains(" II")||blueprintName.contains(" S")||blueprintName.contains(" M")||blueprintName.contains(" L")||blueprintName.contains(" XL")){
                    blueprintNameArray = blueprintName.split(" ");
                    name = blueprintNameArray[0];
                    size = blueprintNameArray[1];
                    if(!name.contains("蓝图")){
                        stringBuffer = new StringBuffer(name);
                        stringBuffer.append("蓝图").append(" ").append(size);
                    }
                }else if(blueprintName.contains("R.Db")){
                    if(blueprintName.contains(" ")){
                        name = blueprintName.split(" ")[0];
                        size = blueprintName.split(" ")[1];
                        stringBuffer = new StringBuffer();
                        stringBuffer.append(size).append("-").append(name).append("蓝图");
                    }else{
                        stringBuffer = new StringBuffer(blueprintName);
                    }
                }else if(blueprintName.contains("电容注电器装料")&&!blueprintName.contains("3200")){
                    blueprintNameArray = blueprintName.split(" ");
                    if(!blueprintName.contains("—")){
                        name = blueprintNameArray[0];
                        size = blueprintNameArray[1];
                    }else{
                        name = blueprintName;
                    }
                    if(!name.contains("蓝图")){
                        stringBuffer = new StringBuffer();
                        stringBuffer.append("高速").append(name).append("—").append(size).append("蓝图");
                    }
                }else if(blueprintName.contains("海盗监测阵列")||blueprintName.contains("诱捕阵列")||blueprintName.contains("量子通量发生器")||blueprintName.contains("勘探网络")||blueprintName.contains("矿石勘探阵列")){
                    name = blueprintName.split(" ")[0];
                    size = blueprintName.split(" ")[1];
                    stringBuffer = new StringBuffer(name);
                    stringBuffer.append(size).append(" 蓝图");
                }else{
                    if(!blueprintName.contains("蓝图")){
                        stringBuffer.append("蓝图");
                    }
                }
                blueprintString = stringBuffer.toString();
                map = checkBlueprintName(blueprintString);
                map.put("blueprintName",blueprintString);
                return map;
            }else if(blueprintName.contains("增效体")){
                blueprintString = blueprintString.replaceAll("纯","").replaceAll("蓝图","反应配方");
                map = checkBlueprintName(blueprintString);
                map.put("blueprintName",blueprintString);
                return map;
            }else if(blueprintName.contains("蓝图")&&!blueprintName.contains("反应配方")){
                blueprintString = blueprintName.replace("蓝图","反应配方");
                map = checkBlueprintName(blueprintString);
                map.put("blueprintName",blueprintString);
                return map;
            }
        }
        map.put("blueprintName",blueprintString);
        map.put("exits",exits);
        map.put("repeat",repeat);
        map.put("blueprintMaterialId",serenityBlueprintMapper.getBlueprintMaterialIdByBlueprintName(blueprintString));
        return map;
    }

    @Override
    public void addNewMaterialItem(String material_name, String materialTypeName) {
        UniverseType universeType = serenityUniverseMapper.getUniverseTypeInfoByTypeName(material_name);
        BlueprintMaterialItem blueprintMaterialItem = new BlueprintMaterialItem();
        //从数据库中查询
        blueprintMaterialItem.setMaterial_type_name(materialTypeName);
        blueprintMaterialItem.setMaterial_name(material_name);
        blueprintMaterialItem.setMaterial_type_id(Integer.parseInt(serenityBlueprintMapper.getMaterialTypeIdByName(materialTypeName)));
        if(universeType!=null){
            blueprintMaterialItem.setType_id(universeType.getType_id());
        }else{
            blueprintMaterialItem.setType_id(-1);
        }
        //保存到数据库
        serenityBlueprintMapper.saveBlueprintMaterialItem(blueprintMaterialItem);
    }

    @Override
    public List<BlueprintOperateLog> getBlueprintOperateList() {
        List<BlueprintOperateLog> blueprintOperateLogList = serenityBlueprintMapper.getBlueprintOperateList();
        Collections.sort(blueprintOperateLogList);
        return blueprintOperateLogList;
    }

    private BlueprintMaterialItem checkBlueprintMaterialType(String material_name) {
        //从数据库中查询材料
        BlueprintMaterialItem blueprintMaterialItem = serenityBlueprintMapper.checkBlueprintMaterialItemByMaterialName(material_name);
        if(blueprintMaterialItem!=null){
            return blueprintMaterialItem;
        }else{
            return null;
        }
    }
}
