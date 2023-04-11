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
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.ConfigRepository;
import me.zhengjie.codefactory.service.ConfigService;
import me.zhengjie.codefactory.service.dto.ConfigDto;
import me.zhengjie.codefactory.service.dto.ConfigQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.ConfigMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
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
public class ConfigServiceImpl implements ConfigService {

    private final ConfigRepository configRepository;
    private final ConfigMapper configMapper;

    @Override
    public Map<String,Object> queryAll(ConfigQueryCriteria criteria, Pageable pageable){
        Page<Config> page = configRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(configMapper::toDto));
    }

    @Override
    public List<ConfigDto> queryAll(ConfigQueryCriteria criteria){
        return configMapper.toDto(configRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ConfigDto findById(Long id) {
        Config config = configRepository.findById(id).orElseGet(Config::new);
        ValidationUtil.isNull(config.getId(),"Config","id",id);
        return configMapper.toDto(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConfigDto create(Config resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId()); 
        if(configRepository.findByKey(resources.getKey()) != null){
            throw new EntityExistException(Config.class,"key",resources.getKey());
        }
        if (resources.getBuidIn() == null) {
            resources.setBuidIn(false);
        }
        return configMapper.toDto(configRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Config resources) {
        Config config = configRepository.findById(resources.getId()).orElseGet(Config::new);
        ValidationUtil.isNull( config.getId(),"Config","id",resources.getId());
        Config config1 = null;
        config1 = configRepository.findByKey(resources.getKey());
        if(config1 != null && !config1.getId().equals(config.getId())){
            throw new EntityExistException(Config.class,"key",resources.getKey());
        }
        config.copy(resources);
        configRepository.save(config);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            final Config config = configRepository.getById(id);
            if(config!= null && !config.getBuidIn()){
                configRepository.deleteById(id);
            }
        }
    }

    @Override
    public void download(List<ConfigDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ConfigDto config : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("关键词( 唯一 )", config.getKey());
            map.put("类型", config.getType());
            map.put("描述", config.getComment());
            map.put("数值", config.getValue());
            map.put("内置参数", config.getBuidIn());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}