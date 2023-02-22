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

import me.zhengjie.codefactory.domain.MarketEntity;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.MarketEntityRepository;
import me.zhengjie.codefactory.service.MarketEntityService;
import me.zhengjie.codefactory.service.dto.MarketEntityDto;
import me.zhengjie.codefactory.service.dto.MarketEntityQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.MarketEntityMapper;
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
public class MarketEntityServiceImpl implements MarketEntityService {

    private final MarketEntityRepository marketEntityRepository;
    private final MarketEntityMapper marketEntityMapper;

    @Override
    public Map<String,Object> queryAll(MarketEntityQueryCriteria criteria, Pageable pageable){
        Page<MarketEntity> page = marketEntityRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(marketEntityMapper::toDto));
    }

    @Override
    public List<MarketEntityDto> queryAll(MarketEntityQueryCriteria criteria){
        return marketEntityMapper.toDto(marketEntityRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MarketEntityDto findById(Long id) {
        MarketEntity marketEntity = marketEntityRepository.findById(id).orElseGet(MarketEntity::new);
        ValidationUtil.isNull(marketEntity.getId(),"MarketEntity","id",id);
        return marketEntityMapper.toDto(marketEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MarketEntityDto create(MarketEntity resources) {
        return marketEntityMapper.toDto(marketEntityRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MarketEntity resources) {
        MarketEntity marketEntity = marketEntityRepository.findById(resources.getId()).orElseGet(MarketEntity::new);
        ValidationUtil.isNull( marketEntity.getId(),"MarketEntity","id",resources.getId());
        marketEntity.copy(resources);
        marketEntityRepository.save(marketEntity);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            marketEntityRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MarketEntityDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MarketEntityDto marketEntity : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("市场领域id", marketEntity.getDomainId());
            map.put("名称", marketEntity.getName());
            map.put("标题", marketEntity.getTitle());
            map.put("描述", marketEntity.getComment());
            map.put("实体类型", marketEntity.getType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}