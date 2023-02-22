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

import me.zhengjie.codefactory.domain.EntityRestriction;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.EntityRestrictionRepository;
import me.zhengjie.codefactory.service.EntityRestrictionService;
import me.zhengjie.codefactory.service.dto.EntityRestrictionDto;
import me.zhengjie.codefactory.service.dto.EntityRestrictionQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.EntityRestrictionMapper;
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
* @date 2023-02-16
**/
@Service
@RequiredArgsConstructor
public class EntityRestrictionServiceImpl implements EntityRestrictionService {

    private final EntityRestrictionRepository entityRestrictionRepository;
    private final EntityRestrictionMapper entityRestrictionMapper;

    @Override
    public Map<String,Object> queryAll(EntityRestrictionQueryCriteria criteria, Pageable pageable){
        Page<EntityRestriction> page = entityRestrictionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(entityRestrictionMapper::toDto));
    }

    @Override
    public List<EntityRestrictionDto> queryAll(EntityRestrictionQueryCriteria criteria){
        return entityRestrictionMapper.toDto(entityRestrictionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public EntityRestrictionDto findById(Long id) {
        EntityRestriction entityRestriction = entityRestrictionRepository.findById(id).orElseGet(EntityRestriction::new);
        ValidationUtil.isNull(entityRestriction.getId(),"EntityRestriction","id",id);
        return entityRestrictionMapper.toDto(entityRestriction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EntityRestrictionDto create(EntityRestriction resources) {
        return entityRestrictionMapper.toDto(entityRestrictionRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EntityRestriction resources) {
        EntityRestriction entityRestriction = entityRestrictionRepository.findById(resources.getId()).orElseGet(EntityRestriction::new);
        ValidationUtil.isNull( entityRestriction.getId(),"EntityRestriction","id",resources.getId());
        entityRestriction.copy(resources);
        entityRestrictionRepository.save(entityRestriction);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            entityRestrictionRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<EntityRestrictionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (EntityRestrictionDto entityRestriction : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("实体ID", entityRestriction.getEntityId());
            map.put("约束名称", entityRestriction.getName());
            map.put("描述", entityRestriction.getComment());
            map.put("是否显示", entityRestriction.getShow());
            map.put("约束类型", entityRestriction.getType());
            map.put("约束表达式", entityRestriction.getExpression());
            map.put("其他实体ID(如外键)", entityRestriction.getEntityOtherId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}