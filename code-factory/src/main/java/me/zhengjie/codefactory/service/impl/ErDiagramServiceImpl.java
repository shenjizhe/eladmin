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

import me.zhengjie.codefactory.domain.ErDiagram;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.ErDiagramRepository;
import me.zhengjie.codefactory.service.ErDiagramService;
import me.zhengjie.codefactory.service.dto.ErDiagramDto;
import me.zhengjie.codefactory.service.dto.ErDiagramQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.ErDiagramMapper;
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
* @date 2023-02-22
**/
@Service
@RequiredArgsConstructor
public class ErDiagramServiceImpl implements ErDiagramService {

    private final ErDiagramRepository erDiagramRepository;
    private final ErDiagramMapper erDiagramMapper;

    @Override
    public Map<String,Object> queryAll(ErDiagramQueryCriteria criteria, Pageable pageable){
        Page<ErDiagram> page = erDiagramRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(erDiagramMapper::toDto));
    }

    @Override
    public List<ErDiagramDto> queryAll(ErDiagramQueryCriteria criteria){
        return erDiagramMapper.toDto(erDiagramRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ErDiagramDto findById(Long id) {
        ErDiagram erDiagram = erDiagramRepository.findById(id).orElseGet(ErDiagram::new);
        ValidationUtil.isNull(erDiagram.getId(),"ErDiagram","id",id);
        return erDiagramMapper.toDto(erDiagram);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErDiagramDto create(ErDiagram resources) {
        return erDiagramMapper.toDto(erDiagramRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ErDiagram resources) {
        ErDiagram erDiagram = erDiagramRepository.findById(resources.getId()).orElseGet(ErDiagram::new);
        ValidationUtil.isNull( erDiagram.getId(),"ErDiagram","id",resources.getId());
        erDiagram.copy(resources);
        erDiagramRepository.save(erDiagram);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            erDiagramRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ErDiagramDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ErDiagramDto erDiagram : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("领域ID", erDiagram.getDomainId());
            map.put("实体主键", erDiagram.getEntityId());
            map.put("索引", erDiagram.getIndex());
            map.put("位置X", erDiagram.getX());
            map.put("位置Y", erDiagram.getY());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}