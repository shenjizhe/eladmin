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

import me.zhengjie.morpheme.domain.WordMeaning;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.WordMeaningRepository;
import me.zhengjie.morpheme.service.WordMeaningService;
import me.zhengjie.morpheme.service.dto.WordMeaningDto;
import me.zhengjie.morpheme.service.dto.WordMeaningQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.WordMeaningMapper;
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
public class WordMeaningServiceImpl implements WordMeaningService {

    private final WordMeaningRepository wordMeaningRepository;
    private final WordMeaningMapper wordMeaningMapper;

    @Override
    public Map<String,Object> queryAll(WordMeaningQueryCriteria criteria, Pageable pageable){
        Page<WordMeaning> page = wordMeaningRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(wordMeaningMapper::toDto));
    }

    @Override
    public List<WordMeaningDto> queryAll(WordMeaningQueryCriteria criteria){
        return wordMeaningMapper.toDto(wordMeaningRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public WordMeaningDto findById(Long id) {
        WordMeaning wordMeaning = wordMeaningRepository.findById(id).orElseGet(WordMeaning::new);
        ValidationUtil.isNull(wordMeaning.getId(),"WordMeaning","id",id);
        return wordMeaningMapper.toDto(wordMeaning);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WordMeaningDto create(WordMeaning resources) {
        return wordMeaningMapper.toDto(wordMeaningRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WordMeaning resources) {
        WordMeaning wordMeaning = wordMeaningRepository.findById(resources.getId()).orElseGet(WordMeaning::new);
        ValidationUtil.isNull( wordMeaning.getId(),"WordMeaning","id",resources.getId());
        wordMeaning.copy(resources);
        wordMeaningRepository.save(wordMeaning);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            wordMeaningRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<WordMeaningDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WordMeaningDto wordMeaning : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("单词ID", wordMeaning.getWordId());
            map.put("词素ID", wordMeaning.getMorphemeId());
            map.put("词性", wordMeaning.getNature());
            map.put("中文含义（倾斜）", wordMeaning.getMeaningChinese());
            map.put("英文含义（to lean）", wordMeaning.getMeaningEnglish());
            map.put("中文例句", wordMeaning.getExampleSentenceChinese());
            map.put("英文例句", wordMeaning.getExampleSentenceEnglish());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}