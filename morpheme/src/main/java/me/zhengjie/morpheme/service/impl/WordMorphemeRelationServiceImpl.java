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

import me.zhengjie.morpheme.domain.WordMorphemeRelation;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.WordMorphemeRelationRepository;
import me.zhengjie.morpheme.service.WordMorphemeRelationService;
import me.zhengjie.morpheme.service.dto.WordMorphemeRelationDto;
import me.zhengjie.morpheme.service.dto.WordMorphemeRelationQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.WordMorphemeRelationMapper;
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
public class WordMorphemeRelationServiceImpl implements WordMorphemeRelationService {

    private final WordMorphemeRelationRepository wordMorphemeRelationRepository;
    private final WordMorphemeRelationMapper wordMorphemeRelationMapper;

    @Override
    public Map<String,Object> queryAll(WordMorphemeRelationQueryCriteria criteria, Pageable pageable){
        Page<WordMorphemeRelation> page = wordMorphemeRelationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(wordMorphemeRelationMapper::toDto));
    }

    @Override
    public List<WordMorphemeRelationDto> queryAll(WordMorphemeRelationQueryCriteria criteria){
        return wordMorphemeRelationMapper.toDto(wordMorphemeRelationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public WordMorphemeRelationDto findById(Long id) {
        WordMorphemeRelation wordMorphemeRelation = wordMorphemeRelationRepository.findById(id).orElseGet(WordMorphemeRelation::new);
        ValidationUtil.isNull(wordMorphemeRelation.getId(),"WordMorphemeRelation","id",id);
        return wordMorphemeRelationMapper.toDto(wordMorphemeRelation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WordMorphemeRelationDto create(WordMorphemeRelation resources) {
        return wordMorphemeRelationMapper.toDto(wordMorphemeRelationRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WordMorphemeRelation resources) {
        WordMorphemeRelation wordMorphemeRelation = wordMorphemeRelationRepository.findById(resources.getId()).orElseGet(WordMorphemeRelation::new);
        ValidationUtil.isNull( wordMorphemeRelation.getId(),"WordMorphemeRelation","id",resources.getId());
        wordMorphemeRelation.copy(resources);
        wordMorphemeRelationRepository.save(wordMorphemeRelation);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            wordMorphemeRelationRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<WordMorphemeRelationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WordMorphemeRelationDto wordMorphemeRelation : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("词素主键", wordMorphemeRelation.getMorphemeId());
            map.put("单词主键", wordMorphemeRelation.getWordId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}