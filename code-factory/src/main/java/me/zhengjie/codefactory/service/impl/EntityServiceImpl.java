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

import me.zhengjie.codefactory.domain.EntityModel;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.EntityRepository;
import me.zhengjie.codefactory.service.EntityService;
import me.zhengjie.codefactory.service.dto.EntityDto;
import me.zhengjie.codefactory.service.dto.EntityQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.EntityMapper;
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
* @date 2023-02-15
**/
@Service
@RequiredArgsConstructor
public class EntityServiceImpl implements EntityService {

    private final EntityRepository entityRepository;
    private final EntityMapper entityMapper;

    @Override
    public Map<String,Object> queryAll(EntityQueryCriteria criteria, Pageable pageable){
        Page<EntityModel> page = entityRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(entityMapper::toDto));
    }

    @Override
    public List<EntityDto> queryAll(EntityQueryCriteria criteria){
        return entityMapper.toDto(entityRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public EntityDto findById(Long id) {
        EntityModel entity = entityRepository.findById(id).orElseGet(EntityModel::new);
        ValidationUtil.isNull(entity.getId(),"EntityModel","id",id);
        return entityMapper.toDto(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EntityDto create(EntityModel resources) {
        return entityMapper.toDto(entityRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EntityModel resources) {
        EntityModel entity = entityRepository.findById(resources.getId()).orElseGet(EntityModel::new);
        ValidationUtil.isNull( entity.getId(),"EntityModel","id",resources.getId());
        entity.copy(resources);
        entityRepository.save(entity);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            entityRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<EntityDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (EntityDto entity : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("领域id", entity.getDomainId());
            map.put("名称", entity.getName());
            map.put("标题", entity.getTitle());
            map.put("描述", entity.getComment());
            map.put("是否显示", entity.getShow());
            map.put("组id", entity.getGroupId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}