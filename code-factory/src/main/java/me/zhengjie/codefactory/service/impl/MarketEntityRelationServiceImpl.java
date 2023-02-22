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

import me.zhengjie.codefactory.domain.MarketEntityRelation;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.MarketEntityRelationRepository;
import me.zhengjie.codefactory.service.MarketEntityRelationService;
import me.zhengjie.codefactory.service.dto.MarketEntityRelationDto;
import me.zhengjie.codefactory.service.dto.MarketEntityRelationQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.MarketEntityRelationMapper;
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
public class MarketEntityRelationServiceImpl implements MarketEntityRelationService {

    private final MarketEntityRelationRepository marketEntityRelationRepository;
    private final MarketEntityRelationMapper marketEntityRelationMapper;

    @Override
    public Map<String,Object> queryAll(MarketEntityRelationQueryCriteria criteria, Pageable pageable){
        Page<MarketEntityRelation> page = marketEntityRelationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(marketEntityRelationMapper::toDto));
    }

    @Override
    public List<MarketEntityRelationDto> queryAll(MarketEntityRelationQueryCriteria criteria){
        return marketEntityRelationMapper.toDto(marketEntityRelationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MarketEntityRelationDto findById(Long id) {
        MarketEntityRelation marketEntityRelation = marketEntityRelationRepository.findById(id).orElseGet(MarketEntityRelation::new);
        ValidationUtil.isNull(marketEntityRelation.getId(),"MarketEntityRelation","id",id);
        return marketEntityRelationMapper.toDto(marketEntityRelation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MarketEntityRelationDto create(MarketEntityRelation resources) {
        return marketEntityRelationMapper.toDto(marketEntityRelationRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MarketEntityRelation resources) {
        MarketEntityRelation marketEntityRelation = marketEntityRelationRepository.findById(resources.getId()).orElseGet(MarketEntityRelation::new);
        ValidationUtil.isNull( marketEntityRelation.getId(),"MarketEntityRelation","id",resources.getId());
        marketEntityRelation.copy(resources);
        marketEntityRelationRepository.save(marketEntityRelation);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            marketEntityRelationRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MarketEntityRelationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MarketEntityRelationDto marketEntityRelation : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", marketEntityRelation.getName());
            map.put("描述", marketEntityRelation.getComment());
            map.put("关系实体1", marketEntityRelation.getEntity1Id());
            map.put("关系实体2", marketEntityRelation.getEntity2Id());
            map.put("扩展", marketEntityRelation.getExtra());
            map.put("关系类型", marketEntityRelation.getType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}