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

import me.zhengjie.codefactory.domain.EntityGroup;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.EntityGroupRepository;
import me.zhengjie.codefactory.service.EntityGroupService;
import me.zhengjie.codefactory.service.dto.EntityGroupDto;
import me.zhengjie.codefactory.service.dto.EntityGroupQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.EntityGroupMapper;
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
public class EntityGroupServiceImpl implements EntityGroupService {

    private final EntityGroupRepository entityGroupRepository;
    private final EntityGroupMapper entityGroupMapper;

    @Override
    public Map<String,Object> queryAll(EntityGroupQueryCriteria criteria, Pageable pageable){
        Page<EntityGroup> page = entityGroupRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(entityGroupMapper::toDto));
    }

    @Override
    public List<EntityGroupDto> queryAll(EntityGroupQueryCriteria criteria){
        return entityGroupMapper.toDto(entityGroupRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public EntityGroupDto findById(Long id) {
        EntityGroup entityGroup = entityGroupRepository.findById(id).orElseGet(EntityGroup::new);
        ValidationUtil.isNull(entityGroup.getId(),"EntityGroup","id",id);
        return entityGroupMapper.toDto(entityGroup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EntityGroupDto create(EntityGroup resources) {
        return entityGroupMapper.toDto(entityGroupRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EntityGroup resources) {
        EntityGroup entityGroup = entityGroupRepository.findById(resources.getId()).orElseGet(EntityGroup::new);
        ValidationUtil.isNull( entityGroup.getId(),"EntityGroup","id",resources.getId());
        entityGroup.copy(resources);
        entityGroupRepository.save(entityGroup);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            entityGroupRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<EntityGroupDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (EntityGroupDto entityGroup : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("域ID", entityGroup.getDomainId());
            map.put("组名称", entityGroup.getGroupName());
            map.put("组描述", entityGroup.getGroupInfo());
            map.put("组标签颜色", entityGroup.getGroupBackcolor());
            map.put("组图标", entityGroup.getGroupImage());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}