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

import me.zhengjie.codefactory.domain.TemplateBlock;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.TemplateBlockRepository;
import me.zhengjie.codefactory.service.TemplateBlockService;
import me.zhengjie.codefactory.service.dto.TemplateBlockDto;
import me.zhengjie.codefactory.service.dto.TemplateBlockQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.TemplateBlockMapper;
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
* @date 2023-03-13
**/
@Service
@RequiredArgsConstructor
public class TemplateBlockServiceImpl implements TemplateBlockService {

    private final TemplateBlockRepository templateBlockRepository;
    private final TemplateBlockMapper templateBlockMapper;

    @Override
    public Map<String,Object> queryAll(TemplateBlockQueryCriteria criteria, Pageable pageable){
        Page<TemplateBlock> page = templateBlockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateBlockMapper::toDto));
    }

    @Override
    public List<TemplateBlockDto> queryAll(TemplateBlockQueryCriteria criteria){
        return templateBlockMapper.toDto(templateBlockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TemplateBlockDto findById(Long id) {
        TemplateBlock templateBlock = templateBlockRepository.findById(id).orElseGet(TemplateBlock::new);
        ValidationUtil.isNull(templateBlock.getId(),"TemplateBlock","id",id);
        return templateBlockMapper.toDto(templateBlock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TemplateBlockDto create(TemplateBlock resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId()); 
        return templateBlockMapper.toDto(templateBlockRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TemplateBlock resources) {
        TemplateBlock templateBlock = templateBlockRepository.findById(resources.getId()).orElseGet(TemplateBlock::new);
        ValidationUtil.isNull( templateBlock.getId(),"TemplateBlock","id",resources.getId());
        templateBlock.copy(resources);
        templateBlockRepository.save(templateBlock);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            templateBlockRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TemplateBlockDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateBlockDto templateBlock : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", templateBlock.getName());
            map.put("描述", templateBlock.getComment());
            map.put("是否显示", templateBlock.getShow());
            map.put("模板ID", templateBlock.getTemplateId());
            map.put("代码", templateBlock.getCode());
            map.put("级别", templateBlock.getLevel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}