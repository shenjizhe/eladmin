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

import me.zhengjie.codefactory.domain.Template;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.TemplateRepository;
import me.zhengjie.codefactory.service.TemplateService;
import me.zhengjie.codefactory.service.dto.TemplateDto;
import me.zhengjie.codefactory.service.dto.TemplateQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.TemplateMapper;
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
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final TemplateMapper templateMapper;

    @Override
    public Map<String,Object> queryAll(TemplateQueryCriteria criteria, Pageable pageable){
        Page<Template> page = templateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateMapper::toDto));
    }

    @Override
    public List<TemplateDto> queryAll(TemplateQueryCriteria criteria){
        return templateMapper.toDto(templateRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TemplateDto findById(Long id) {
        Template template = templateRepository.findById(id).orElseGet(Template::new);
        ValidationUtil.isNull(template.getId(),"Template","id",id);
        return templateMapper.toDto(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TemplateDto create(Template resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId()); 
        return templateMapper.toDto(templateRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Template resources) {
        Template template = templateRepository.findById(resources.getId()).orElseGet(Template::new);
        ValidationUtil.isNull( template.getId(),"Template","id",resources.getId());
        template.copy(resources);
        templateRepository.save(template);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            templateRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TemplateDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateDto template : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", template.getName());
            map.put("描述", template.getComment());
            map.put("是否显示", template.getShow());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}