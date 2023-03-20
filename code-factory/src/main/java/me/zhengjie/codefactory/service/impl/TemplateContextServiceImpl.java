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

import me.zhengjie.codefactory.domain.TemplateContext;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.TemplateContextRepository;
import me.zhengjie.codefactory.service.TemplateContextService;
import me.zhengjie.codefactory.service.dto.TemplateContextDto;
import me.zhengjie.codefactory.service.dto.TemplateContextQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.TemplateContextMapper;
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
* @date 2023-03-20
**/
@Service
@RequiredArgsConstructor
public class TemplateContextServiceImpl implements TemplateContextService {

    private final TemplateContextRepository templateContextRepository;
    private final TemplateContextMapper templateContextMapper;

    @Override
    public Map<String,Object> queryAll(TemplateContextQueryCriteria criteria, Pageable pageable){
        Page<TemplateContext> page = templateContextRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateContextMapper::toDto));
    }

    @Override
    public List<TemplateContextDto> queryAll(TemplateContextQueryCriteria criteria){
        return templateContextMapper.toDto(templateContextRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public TemplateContextDto findById(Long id) {
        TemplateContext templateContext = templateContextRepository.findById(id).orElseGet(TemplateContext::new);
        ValidationUtil.isNull(templateContext.getId(),"TemplateContext","id",id);
        return templateContextMapper.toDto(templateContext);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TemplateContextDto create(TemplateContext resources) {
        return templateContextMapper.toDto(templateContextRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TemplateContext resources) {
        TemplateContext templateContext = templateContextRepository.findById(resources.getId()).orElseGet(TemplateContext::new);
        ValidationUtil.isNull( templateContext.getId(),"TemplateContext","id",resources.getId());
        templateContext.copy(resources);
        templateContextRepository.save(templateContext);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            templateContextRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<TemplateContextDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateContextDto templateContext : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("模板主键", templateContext.getTemplateId());
            map.put("上下文关键字", templateContext.getKey());
            map.put("说明", templateContext.getContent());
            map.put("数据类型", templateContext.getType());
            map.put("数据类型", templateContext.getDataType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}