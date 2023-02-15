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

import me.zhengjie.codefactory.domain.Domain;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.repository.DomainRepository;
import me.zhengjie.codefactory.service.DomainService;
import me.zhengjie.codefactory.service.dto.DomainDto;
import me.zhengjie.codefactory.service.dto.DomainQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.DomainMapper;
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
public class DomainServiceImpl implements DomainService {

    private final DomainRepository domainRepository;
    private final DomainMapper domainMapper;

    @Override
    public Map<String,Object> queryAll(DomainQueryCriteria criteria, Pageable pageable){
        Page<Domain> page = domainRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(domainMapper::toDto));
    }

    @Override
    public List<DomainDto> queryAll(DomainQueryCriteria criteria){
        return domainMapper.toDto(domainRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public DomainDto findById(Long id) {
        Domain domain = domainRepository.findById(id).orElseGet(Domain::new);
        ValidationUtil.isNull(domain.getId(),"Domain","id",id);
        return domainMapper.toDto(domain);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DomainDto create(Domain resources) {
        return domainMapper.toDto(domainRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Domain resources) {
        Domain domain = domainRepository.findById(resources.getId()).orElseGet(Domain::new);
        ValidationUtil.isNull( domain.getId(),"Domain","id",resources.getId());
        domain.copy(resources);
        domainRepository.save(domain);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            domainRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<DomainDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DomainDto domain : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("名称", domain.getName());
            map.put("描述", domain.getComment());
            map.put("启用", domain.getShow());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}