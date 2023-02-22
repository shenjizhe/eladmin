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

import me.zhengjie.codefactory.domain.MarketEntityRestriction;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.MarketEntityRestrictionRepository;
import me.zhengjie.codefactory.service.MarketEntityRestrictionService;
import me.zhengjie.codefactory.service.dto.MarketEntityRestrictionDto;
import me.zhengjie.codefactory.service.dto.MarketEntityRestrictionQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.MarketEntityRestrictionMapper;
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
public class MarketEntityRestrictionServiceImpl implements MarketEntityRestrictionService {

    private final MarketEntityRestrictionRepository marketEntityRestrictionRepository;
    private final MarketEntityRestrictionMapper marketEntityRestrictionMapper;

    @Override
    public Map<String,Object> queryAll(MarketEntityRestrictionQueryCriteria criteria, Pageable pageable){
        Page<MarketEntityRestriction> page = marketEntityRestrictionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(marketEntityRestrictionMapper::toDto));
    }

    @Override
    public List<MarketEntityRestrictionDto> queryAll(MarketEntityRestrictionQueryCriteria criteria){
        return marketEntityRestrictionMapper.toDto(marketEntityRestrictionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MarketEntityRestrictionDto findById(Long id) {
        MarketEntityRestriction marketEntityRestriction = marketEntityRestrictionRepository.findById(id).orElseGet(MarketEntityRestriction::new);
        ValidationUtil.isNull(marketEntityRestriction.getId(),"MarketEntityRestriction","id",id);
        return marketEntityRestrictionMapper.toDto(marketEntityRestriction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MarketEntityRestrictionDto create(MarketEntityRestriction resources) {
        return marketEntityRestrictionMapper.toDto(marketEntityRestrictionRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MarketEntityRestriction resources) {
        MarketEntityRestriction marketEntityRestriction = marketEntityRestrictionRepository.findById(resources.getId()).orElseGet(MarketEntityRestriction::new);
        ValidationUtil.isNull( marketEntityRestriction.getId(),"MarketEntityRestriction","id",resources.getId());
        marketEntityRestriction.copy(resources);
        marketEntityRestrictionRepository.save(marketEntityRestriction);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            marketEntityRestrictionRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MarketEntityRestrictionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MarketEntityRestrictionDto marketEntityRestriction : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("实体ID", marketEntityRestriction.getEntityId());
            map.put("其他实体ID(如外键)", marketEntityRestriction.getEntityOtherId());
            map.put("约束名称", marketEntityRestriction.getName());
            map.put("描述", marketEntityRestriction.getComment());
            map.put("约束类型", marketEntityRestriction.getType());
            map.put("约束表达式", marketEntityRestriction.getExpression());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}