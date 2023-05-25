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

import me.zhengjie.morpheme.domain.WordDeduction;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.WordDeductionRepository;
import me.zhengjie.morpheme.service.WordDeductionService;
import me.zhengjie.morpheme.service.dto.WordDeductionDto;
import me.zhengjie.morpheme.service.dto.WordDeductionQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.WordDeductionMapper;
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
public class WordDeductionServiceImpl implements WordDeductionService {

    private final WordDeductionRepository wordDeductionRepository;
    private final WordDeductionMapper wordDeductionMapper;

    @Override
    public Map<String,Object> queryAll(WordDeductionQueryCriteria criteria, Pageable pageable){
        Page<WordDeduction> page = wordDeductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(wordDeductionMapper::toDto));
    }

    @Override
    public List<WordDeductionDto> queryAll(WordDeductionQueryCriteria criteria){
        return wordDeductionMapper.toDto(wordDeductionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public WordDeductionDto findById(Long id) {
        WordDeduction wordDeduction = wordDeductionRepository.findById(id).orElseGet(WordDeduction::new);
        ValidationUtil.isNull(wordDeduction.getId(),"WordDeduction","id",id);
        return wordDeductionMapper.toDto(wordDeduction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WordDeductionDto create(WordDeduction resources) {
        return wordDeductionMapper.toDto(wordDeductionRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WordDeduction resources) {
        WordDeduction wordDeduction = wordDeductionRepository.findById(resources.getId()).orElseGet(WordDeduction::new);
        ValidationUtil.isNull( wordDeduction.getId(),"WordDeduction","id",resources.getId());
        wordDeduction.copy(resources);
        wordDeductionRepository.save(wordDeduction);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            wordDeductionRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<WordDeductionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WordDeductionDto wordDeduction : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("词根ID", wordDeduction.getMorphemeId());
            map.put("单词ID", wordDeduction.getWordId());
            map.put("词素文本", wordDeduction.getMorphemeText());
            map.put("源词素", wordDeduction.getSourceText());
            map.put("全文本", wordDeduction.getFullText());
            map.put("词缀类型", wordDeduction.getAffix());
            map.put("变形类型", wordDeduction.getShape());
            map.put("词性", wordDeduction.getNature());
            map.put("是否是派生词", wordDeduction.getIsDerive());
            map.put("中文含义", wordDeduction.getMeaningChinese());
            map.put("英文含义", wordDeduction.getMeaningEnglish());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}