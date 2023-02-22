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

import me.zhengjie.codefactory.domain.MarketDomain;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.MarketDomainRepository;
import me.zhengjie.codefactory.service.MarketDomainService;
import me.zhengjie.codefactory.service.dto.MarketDomainDto;
import me.zhengjie.codefactory.service.dto.MarketDomainQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.MarketDomainMapper;
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
public class MarketDomainServiceImpl implements MarketDomainService {

    private final MarketDomainRepository marketDomainRepository;
    private final MarketDomainMapper marketDomainMapper;

    @Override
    public Map<String,Object> queryAll(MarketDomainQueryCriteria criteria, Pageable pageable){
        Page<MarketDomain> page = marketDomainRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(marketDomainMapper::toDto));
    }

    @Override
    public List<MarketDomainDto> queryAll(MarketDomainQueryCriteria criteria){
        return marketDomainMapper.toDto(marketDomainRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MarketDomainDto findById(Long id) {
        MarketDomain marketDomain = marketDomainRepository.findById(id).orElseGet(MarketDomain::new);
        ValidationUtil.isNull(marketDomain.getId(),"MarketDomain","id",id);
        return marketDomainMapper.toDto(marketDomain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MarketDomainDto create(MarketDomain resources) {
        return marketDomainMapper.toDto(marketDomainRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MarketDomain resources) {
        MarketDomain marketDomain = marketDomainRepository.findById(resources.getId()).orElseGet(MarketDomain::new);
        ValidationUtil.isNull( marketDomain.getId(),"MarketDomain","id",resources.getId());
        marketDomain.copy(resources);
        marketDomainRepository.save(marketDomain);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            marketDomainRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MarketDomainDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MarketDomainDto marketDomain : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", marketDomain.getName());
            map.put("描述", marketDomain.getComment());
            map.put("领域用途描述，便于关键字查询", marketDomain.getDescription());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}