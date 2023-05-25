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

import me.zhengjie.morpheme.domain.DifferentMorpheme;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.DifferentMorphemeRepository;
import me.zhengjie.morpheme.service.DifferentMorphemeService;
import me.zhengjie.morpheme.service.dto.DifferentMorphemeDto;
import me.zhengjie.morpheme.service.dto.DifferentMorphemeQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.DifferentMorphemeMapper;
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
* @date 2023-05-24
**/
@Service
@RequiredArgsConstructor
public class DifferentMorphemeServiceImpl implements DifferentMorphemeService {

    private final DifferentMorphemeRepository differentMorphemeRepository;
    private final DifferentMorphemeMapper differentMorphemeMapper;

    @Override
    public Map<String,Object> queryAll(DifferentMorphemeQueryCriteria criteria, Pageable pageable){
        Page<DifferentMorpheme> page = differentMorphemeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(differentMorphemeMapper::toDto));
    }

    @Override
    public List<DifferentMorphemeDto> queryAll(DifferentMorphemeQueryCriteria criteria){
        return differentMorphemeMapper.toDto(differentMorphemeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public DifferentMorphemeDto findById(Long id) {
        DifferentMorpheme differentMorpheme = differentMorphemeRepository.findById(id).orElseGet(DifferentMorpheme::new);
        ValidationUtil.isNull(differentMorpheme.getId(),"DifferentMorpheme","id",id);
        return differentMorphemeMapper.toDto(differentMorpheme);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DifferentMorphemeDto create(DifferentMorpheme resources) {
        return differentMorphemeMapper.toDto(differentMorphemeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DifferentMorpheme resources) {
        DifferentMorpheme differentMorpheme = differentMorphemeRepository.findById(resources.getId()).orElseGet(DifferentMorpheme::new);
        ValidationUtil.isNull( differentMorpheme.getId(),"DifferentMorpheme","id",resources.getId());
        differentMorpheme.copy(resources);
        differentMorphemeRepository.save(differentMorpheme);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            differentMorphemeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<DifferentMorphemeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DifferentMorphemeDto differentMorpheme : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("词素ID", differentMorpheme.getMorphemeId());
            map.put("异形词素文本(ag)", differentMorpheme.getText());
            map.put("词性(v-动词)", differentMorpheme.getNature());
            map.put("词源", differentMorpheme.getSource());
            map.put("词源单词", differentMorpheme.getSourceWord());
            map.put("词源说明", differentMorpheme.getSourceDescription());
            map.put("中文含义", differentMorpheme.getMeaningChinese());
            map.put("英文含义", differentMorpheme.getMeaningEnglish());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}