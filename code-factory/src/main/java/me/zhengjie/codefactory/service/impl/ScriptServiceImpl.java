/*
*  Copyright 2019-2020 Jason Shen
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.codefactory.service.impl;

import me.zhengjie.codefactory.domain.Config;
import me.zhengjie.codefactory.domain.Script;
import me.zhengjie.codefactory.repository.ConfigRepository;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.ScriptRepository;
import me.zhengjie.codefactory.service.ScriptService;
import me.zhengjie.codefactory.service.dto.ScriptDto;
import me.zhengjie.codefactory.service.dto.ScriptQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.ScriptMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author Jason Shen
* @date 2023-04-11
**/
@Service
@RequiredArgsConstructor
public class ScriptServiceImpl implements ScriptService {

    private final ScriptRepository scriptRepository;
    private final ScriptMapper scriptMapper;
    private final ConfigRepository configRepository;

    @Override
    public Map<String,Object> queryAll(ScriptQueryCriteria criteria, Pageable pageable){
        Page<Script> page = scriptRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(scriptMapper::toDto));
    }

    @Override
    public List<ScriptDto> queryAll(ScriptQueryCriteria criteria){
        return scriptMapper.toDto(scriptRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ScriptDto findById(Long id) {
        Script script = scriptRepository.findById(id).orElseGet(Script::new);
        ValidationUtil.isNull(script.getId(),"Script","id",id);
        return scriptMapper.toDto(script);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScriptDto create(Script resources) {
        if(scriptRepository.findByKey(resources.getKey()) != null){
            throw new EntityExistException(Script.class,"key",resources.getKey());
        }
        return scriptMapper.toDto(scriptRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Script resources) {
        Script script = scriptRepository.findById(resources.getId()).orElseGet(Script::new);
        ValidationUtil.isNull( script.getId(),"Script","id",resources.getId());
        Script script1 = scriptRepository.findByKey(resources.getKey());
        if(script1 != null && !script1.getId().equals(script.getId())){
            throw new EntityExistException(Script.class,"key",resources.getKey());
        }
        script.copy(resources);
        scriptRepository.save(script);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            scriptRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ScriptDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ScriptDto script : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("脚本", script.getScript());
            map.put("使用系统", script.getSystem());
            map.put("语言", script.getLanguage());
            map.put("脚本类型", script.getType());
            map.put("参数列表", script.getParams());
            map.put("脚本名称", script.getName());
            map.put("脚本描述", script.getComment());
            map.put("查找键", script.getKey());
            map.put("内置参数", script.getBuidIn());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public String getScriptText(Script script) {
        if(script != null){
            String scriptText = script.getScript();
            final String params = script.getParams();
            if(params != null && !params.isEmpty()){
                final String[] split = params.split("\n");

                for (String s : split) {
                    final Config config = configRepository.findByKey(s.trim());
                    if(config != null) {
                        scriptText = scriptText.replace("${" + config.getKey() + "}", config.getValue());
                    }
                }
            }
            return scriptText;
        }else{
            return null;
        }
    }
}