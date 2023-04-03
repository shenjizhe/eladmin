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

import me.zhengjie.codefactory.domain.Component;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.ComponentRepository;
import me.zhengjie.codefactory.service.ComponentService;
import me.zhengjie.codefactory.service.dto.ComponentDto;
import me.zhengjie.codefactory.service.dto.ComponentQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.ComponentMapper;
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
* @date 2023-04-03
**/
@Service
@RequiredArgsConstructor
public class ComponentServiceImpl implements ComponentService {

    private final ComponentRepository componentRepository;
    private final ComponentMapper componentMapper;

    @Override
    public Map<String,Object> queryAll(ComponentQueryCriteria criteria, Pageable pageable){
        Page<Component> page = componentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(componentMapper::toDto));
    }

    @Override
    public List<ComponentDto> queryAll(ComponentQueryCriteria criteria){
        return componentMapper.toDto(componentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ComponentDto findById(Long id) {
        Component component = componentRepository.findById(id).orElseGet(Component::new);
        ValidationUtil.isNull(component.getId(),"Component","id",id);
        return componentMapper.toDto(component);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ComponentDto create(Component resources) {
        if(componentRepository.findByName(resources.getName()) != null){
            throw new EntityExistException(Component.class,"name",resources.getName());
        }
        return componentMapper.toDto(componentRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Component resources) {
        Component component = componentRepository.findById(resources.getId()).orElseGet(Component::new);
        ValidationUtil.isNull( component.getId(),"Component","id",resources.getId());
        Component component1 = null;
        component1 = componentRepository.findByName(resources.getName());
        if(component1 != null && !component1.getId().equals(component.getId())){
            throw new EntityExistException(Component.class,"name",resources.getName());
        }
        component.copy(resources);
        componentRepository.save(component);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            componentRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ComponentDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ComponentDto component : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", component.getName());
            map.put("描述", component.getComment());
            map.put("是否显示", component.getShow());
            map.put("模型ID", component.getModelId());
            map.put("模块ID", component.getTemplateId());
            map.put("文件路径", component.getFilePath());
            map.put("结果端口", component.getPort());
            map.put("结果包名", component.getRootPackage());
            map.put("域名", component.getDeployUrl());
            map.put("输出目录", component.getDeployPath());
            map.put("git目录", component.getGitPath());
            map.put("git命名空间", component.getGitGroup());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}