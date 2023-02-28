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

import me.zhengjie.codefactory.domain.EntityField;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.EntityFieldRepository;
import me.zhengjie.codefactory.service.EntityFieldService;
import me.zhengjie.codefactory.service.dto.EntityFieldDto;
import me.zhengjie.codefactory.service.dto.EntityFieldQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.EntityFieldMapper;
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
* @date 2023-02-28
**/
@Service
@RequiredArgsConstructor
public class EntityFieldServiceImpl implements EntityFieldService {

    private final EntityFieldRepository entityFieldRepository;
    private final EntityFieldMapper entityFieldMapper;

    @Override
    public Map<String,Object> queryAll(EntityFieldQueryCriteria criteria, Pageable pageable){
        Page<EntityField> page = entityFieldRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(entityFieldMapper::toDto));
    }

    @Override
    public List<EntityFieldDto> queryAll(EntityFieldQueryCriteria criteria){
        return entityFieldMapper.toDto(entityFieldRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public EntityFieldDto findById(Long id) {
        EntityField entityField = entityFieldRepository.findById(id).orElseGet(EntityField::new);
        ValidationUtil.isNull(entityField.getId(),"EntityField","id",id);
        return entityFieldMapper.toDto(entityField);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EntityFieldDto create(EntityField resources) {
        return entityFieldMapper.toDto(entityFieldRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EntityField resources) {
        EntityField entityField = entityFieldRepository.findById(resources.getId()).orElseGet(EntityField::new);
        ValidationUtil.isNull( entityField.getId(),"EntityField","id",resources.getId());
        entityField.copy(resources);
        entityFieldRepository.save(entityField);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            entityFieldRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<EntityFieldDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (EntityFieldDto entityField : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("实体ID", entityField.getEntityId());
            map.put("名称", entityField.getName());
            map.put("描述", entityField.getComment());
            map.put("是否显示", entityField.getShow());
            map.put("是否主键", entityField.getPk());
            map.put("数据类型", entityField.getType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}