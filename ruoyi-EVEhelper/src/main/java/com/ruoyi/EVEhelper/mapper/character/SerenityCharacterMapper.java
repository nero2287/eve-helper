package com.ruoyi.EVEhelper.mapper.character;

import com.ruoyi.EVEhelper.domain.character.Character;
import com.ruoyi.EVEhelper.domain.contract.Contract;
import com.ruoyi.EVEhelper.domain.contract.ContractCondition;
import com.ruoyi.EVEhelper.domain.contract.ContractItem;

import java.util.List;

public interface SerenityCharacterMapper {
    /**
     * 查询角色是否存在
     * @param character_id
     * @return
     */
    int checkCharacterUnique(String character_id);

    /**
     * 缓存角色信息
     * @param characterList
     */
    void cacheCharacterInfo(List<Character> characterList);

    /**
     * 根据角色id获取角色信息
     * @param character_id
     */
    Character getCharacterNameByCharacterId(String character_id);
}
