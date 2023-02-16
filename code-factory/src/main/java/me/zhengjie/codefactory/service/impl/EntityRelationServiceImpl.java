/*
*  Copyright 2019-2020 Zheng Jie
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

import me.zhengjie.codefactory.domain.EntityRelation;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.EntityRelationRepository;
import me.zhengjie.codefactory.service.EntityRelationService;
import me.zhengjie.codefactory.service.dto.EntityRelationDto;
import me.zhengjie.codefactory.service.dto.EntityRelationQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.EntityRelationMapper;
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
public class EntityRelationServiceImpl implements EntityRelationService {

    private final EntityRelationRepository entityRelationRepository;
    private final EntityRelationMapper entityRelationMapper;

    @Override
    public Map<String,Object> queryAll(EntityRelationQueryCriteria criteria, Pageable pageable){
        Page<EntityRelation> page = entityRelationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(entityRelationMapper::toDto));
    }

    @Override
    public List<EntityRelationDto> queryAll(EntityRelationQueryCriteria criteria){
        return entityRelationMapper.toDto(entityRelationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public EntityRelationDto findById(Long id) {
        EntityRelation entityRelation = entityRelationRepository.findById(id).orElseGet(EntityRelation::new);
        ValidationUtil.isNull(entityRelation.getId(),"EntityRelation","id",id);
        return entityRelationMapper.toDto(entityRelation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EntityRelationDto create(EntityRelation resources) {
        return entityRelationMapper.toDto(entityRelationRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EntityRelation resources) {
        EntityRelation entityRelation = entityRelationRepository.findById(resources.getId()).orElseGet(EntityRelation::new);
        ValidationUtil.isNull( entityRelation.getId(),"EntityRelation","id",resources.getId());
        entityRelation.copy(resources);
        entityRelationRepository.save(entityRelation);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            entityRelationRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<EntityRelationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (EntityRelationDto entityRelation : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", entityRelation.getName());
            map.put("描述", entityRelation.getComment());
            map.put("是否显示", entityRelation.getShow());
            map.put("关系实体1", entityRelation.getEntity1Id());
            map.put("关系实体2", entityRelation.getEntity2Id());
            map.put("扩展", entityRelation.getExtra());
            map.put("关系类型", entityRelation.getType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}