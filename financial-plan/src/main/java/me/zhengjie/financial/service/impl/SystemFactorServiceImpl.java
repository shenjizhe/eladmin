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
package me.zhengjie.financial.service.impl;

import me.zhengjie.financial.domain.SystemFactor;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.financial.repository.SystemFactorRepository;
import me.zhengjie.financial.service.SystemFactorService;
import me.zhengjie.financial.service.dto.SystemFactorDto;
import me.zhengjie.financial.service.dto.SystemFactorQueryCriteria;
import me.zhengjie.financial.service.mapstruct.SystemFactorMapper;
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
* @date 2023-05-09
**/
@Service
@RequiredArgsConstructor
public class SystemFactorServiceImpl implements SystemFactorService {

    private final SystemFactorRepository systemFactorRepository;
    private final SystemFactorMapper systemFactorMapper;

    @Override
    public Map<String,Object> queryAll(SystemFactorQueryCriteria criteria, Pageable pageable){
        Page<SystemFactor> page = systemFactorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(systemFactorMapper::toDto));
    }

    @Override
    public List<SystemFactorDto> queryAll(SystemFactorQueryCriteria criteria){
        return systemFactorMapper.toDto(systemFactorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public SystemFactorDto findById(Integer id) {
        SystemFactor systemFactor = systemFactorRepository.findById(id).orElseGet(SystemFactor::new);
        ValidationUtil.isNull(systemFactor.getId(),"SystemFactor","id",id);
        return systemFactorMapper.toDto(systemFactor);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemFactorDto create(SystemFactor resources) {
        return systemFactorMapper.toDto(systemFactorRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SystemFactor resources) {
        SystemFactor systemFactor = systemFactorRepository.findById(resources.getId()).orElseGet(SystemFactor::new);
        ValidationUtil.isNull( systemFactor.getId(),"SystemFactor","id",resources.getId());
        systemFactor.copy(resources);
        systemFactorRepository.save(systemFactor);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            systemFactorRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<SystemFactorDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemFactorDto systemFactor : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("系数腱", systemFactor.getKey());
            map.put("系数值", systemFactor.getValue());
            map.put("系数类型", systemFactor.getType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}