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

import me.zhengjie.codefactory.domain.MarketEntityMethod;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.MarketEntityMethodRepository;
import me.zhengjie.codefactory.service.MarketEntityMethodService;
import me.zhengjie.codefactory.service.dto.MarketEntityMethodDto;
import me.zhengjie.codefactory.service.dto.MarketEntityMethodQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.MarketEntityMethodMapper;
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
public class MarketEntityMethodServiceImpl implements MarketEntityMethodService {

    private final MarketEntityMethodRepository marketEntityMethodRepository;
    private final MarketEntityMethodMapper marketEntityMethodMapper;

    @Override
    public Map<String,Object> queryAll(MarketEntityMethodQueryCriteria criteria, Pageable pageable){
        Page<MarketEntityMethod> page = marketEntityMethodRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(marketEntityMethodMapper::toDto));
    }

    @Override
    public List<MarketEntityMethodDto> queryAll(MarketEntityMethodQueryCriteria criteria){
        return marketEntityMethodMapper.toDto(marketEntityMethodRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MarketEntityMethodDto findById(Long id) {
        MarketEntityMethod marketEntityMethod = marketEntityMethodRepository.findById(id).orElseGet(MarketEntityMethod::new);
        ValidationUtil.isNull(marketEntityMethod.getId(),"MarketEntityMethod","id",id);
        return marketEntityMethodMapper.toDto(marketEntityMethod);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MarketEntityMethodDto create(MarketEntityMethod resources) {
        return marketEntityMethodMapper.toDto(marketEntityMethodRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MarketEntityMethod resources) {
        MarketEntityMethod marketEntityMethod = marketEntityMethodRepository.findById(resources.getId()).orElseGet(MarketEntityMethod::new);
        ValidationUtil.isNull( marketEntityMethod.getId(),"MarketEntityMethod","id",resources.getId());
        marketEntityMethod.copy(resources);
        marketEntityMethodRepository.save(marketEntityMethod);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            marketEntityMethodRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MarketEntityMethodDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MarketEntityMethodDto marketEntityMethod : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("实体ID", marketEntityMethod.getEntityId());
            map.put("名称", marketEntityMethod.getName());
            map.put("描述", marketEntityMethod.getComment());
            map.put("方法代码", marketEntityMethod.getCode());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}