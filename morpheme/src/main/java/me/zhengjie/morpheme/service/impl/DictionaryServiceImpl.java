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
package me.zhengjie.morpheme.service.impl;

import me.zhengjie.morpheme.domain.Dictionary;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.DictionaryRepository;
import me.zhengjie.morpheme.service.DictionaryService;
import me.zhengjie.morpheme.service.dto.DictionaryDto;
import me.zhengjie.morpheme.service.dto.DictionaryQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.DictionaryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
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
* @date 2023-05-24
**/
@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;
    private final DictionaryMapper dictionaryMapper;

    @Override
    public Map<String,Object> queryAll(DictionaryQueryCriteria criteria, Pageable pageable){
        Page<Dictionary> page = dictionaryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(dictionaryMapper::toDto));
    }

    @Override
    public List<DictionaryDto> queryAll(DictionaryQueryCriteria criteria){
        return dictionaryMapper.toDto(dictionaryRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public DictionaryDto findById(Long id) {
        Dictionary dictionary = dictionaryRepository.findById(id).orElseGet(Dictionary::new);
        ValidationUtil.isNull(dictionary.getId(),"Dictionary","id",id);
        return dictionaryMapper.toDto(dictionary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictionaryDto create(Dictionary resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId()); 
        return dictionaryMapper.toDto(dictionaryRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Dictionary resources) {
        Dictionary dictionary = dictionaryRepository.findById(resources.getId()).orElseGet(Dictionary::new);
        ValidationUtil.isNull( dictionary.getId(),"Dictionary","id",resources.getId());
        dictionary.copy(resources);
        dictionaryRepository.save(dictionary);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            dictionaryRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<DictionaryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DictionaryDto dictionary : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("词典名称", dictionary.getName());
            map.put("词典说明", dictionary.getDescription());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}