package com.ruoyi.web.controller.EVEHelper.industry;

import com.ruoyi.EVEhelper.service.industry.blueprint.BlueprintService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

@RestController
public class IndustryController extends BaseController {
    @Autowired
    private BlueprintService blueprintService;

    @GetMapping("/system/manage/blueprintInput")
    public AjaxResult blueprintInput(String blueprintCode) {
        System.out.print(blueprintCode);
        String[] stringArray = blueprintCode.split("回车符");
        String material_name = blueprintService.inputBlueprintInfo(stringArray);
        if(material_name==null){
            return AjaxResult.success("添加成功");
        }else if(material_name.equals("蓝图信息有误")) {
            return AjaxResult.error(material_name);
        }else{
            AjaxResult ajax = AjaxResult.success("发现新材料");
            ajax.put("data",material_name);
            return ajax;
        }
    }

    /**
     * 该函数主要作用是将数据库中没有匹配到的材料缓存到一个表中
     * 方便以后在添加新蓝图的时候直接在材料表中查询一些基本信息
     * @param material_name
     * @param materialTypeCode
     * @return
     */
    @GetMapping("/system/manage/addNewMaterialItem")
    public AjaxResult addNewMaterialItem(String material_name,String materialTypeCode){
        blueprintService.addNewMaterialItem(material_name,materialTypeCode);
        return AjaxResult.success();
    }

    @GetMapping("/system/manage/checkBlueprint")
    public AjaxResult checkBlueprint(String blueprintName){
        AjaxResult ajax = AjaxResult.success();
        Map<String,Object> map = blueprintService.checkBlueprintName(blueprintName);
        ajax.put("data",map);
        return ajax;
    }

    @GetMapping("/system/manage/getBlueprintOperate")
    public AjaxResult getBlueprintOperate(){
        AjaxResult ajax = AjaxResult.success();
        ajax.put("data",blueprintService.getBlueprintOperateList());
        return ajax;
    }
}
