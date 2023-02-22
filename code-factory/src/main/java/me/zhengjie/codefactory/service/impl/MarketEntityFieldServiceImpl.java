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

import me.zhengjie.codefactory.domain.MarketEntityField;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.MarketEntityFieldRepository;
import me.zhengjie.codefactory.service.MarketEntityFieldService;
import me.zhengjie.codefactory.service.dto.MarketEntityFieldDto;
import me.zhengjie.codefactory.service.dto.MarketEntityFieldQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.MarketEntityFieldMapper;
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
public class MarketEntityFieldServiceImpl implements MarketEntityFieldService {

    private final MarketEntityFieldRepository marketEntityFieldRepository;
    private final MarketEntityFieldMapper marketEntityFieldMapper;

    @Override
    public Map<String,Object> queryAll(MarketEntityFieldQueryCriteria criteria, Pageable pageable){
        Page<MarketEntityField> page = marketEntityFieldRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(marketEntityFieldMapper::toDto));
    }

    @Override
    public List<MarketEntityFieldDto> queryAll(MarketEntityFieldQueryCriteria criteria){
        return marketEntityFieldMapper.toDto(marketEntityFieldRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MarketEntityFieldDto findById(Long id) {
        MarketEntityField marketEntityField = marketEntityFieldRepository.findById(id).orElseGet(MarketEntityField::new);
        ValidationUtil.isNull(marketEntityField.getId(),"MarketEntityField","id",id);
        return marketEntityFieldMapper.toDto(marketEntityField);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MarketEntityFieldDto create(MarketEntityField resources) {
        return marketEntityFieldMapper.toDto(marketEntityFieldRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MarketEntityField resources) {
        MarketEntityField marketEntityField = marketEntityFieldRepository.findById(resources.getId()).orElseGet(MarketEntityField::new);
        ValidationUtil.isNull( marketEntityField.getId(),"MarketEntityField","id",resources.getId());
        marketEntityField.copy(resources);
        marketEntityFieldRepository.save(marketEntityField);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            marketEntityFieldRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MarketEntityFieldDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MarketEntityFieldDto marketEntityField : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("实体ID", marketEntityField.getEntityId());
            map.put("名称", marketEntityField.getName());
            map.put("描述", marketEntityField.getComment());
            map.put("是否主键", marketEntityField.getPk());
            map.put("数据类型", marketEntityField.getType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}