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

import me.zhengjie.morpheme.domain.WordDict;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.WordDictRepository;
import me.zhengjie.morpheme.service.WordDictService;
import me.zhengjie.morpheme.service.dto.WordDictDto;
import me.zhengjie.morpheme.service.dto.WordDictQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.WordDictMapper;
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
* @date 2023-08-18
**/
@Service
@RequiredArgsConstructor
public class WordDictServiceImpl implements WordDictService {

    private final WordDictRepository wordDictRepository;
    private final WordDictMapper wordDictMapper;

    @Override
    public Map<String,Object> queryAll(WordDictQueryCriteria criteria, Pageable pageable){
        Page<WordDict> page = wordDictRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(wordDictMapper::toDto));
    }

    @Override
    public List<WordDictDto> queryAll(WordDictQueryCriteria criteria){
        return wordDictMapper.toDto(wordDictRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public WordDictDto findById(Integer id) {
        WordDict wordDict = wordDictRepository.findById(id).orElseGet(WordDict::new);
        ValidationUtil.isNull(wordDict.getId(),"WordDict","id",id);
        return wordDictMapper.toDto(wordDict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WordDictDto create(WordDict resources) {
        return wordDictMapper.toDto(wordDictRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WordDict resources) {
        WordDict wordDict = wordDictRepository.findById(resources.getId()).orElseGet(WordDict::new);
        ValidationUtil.isNull( wordDict.getId(),"WordDict","id",resources.getId());
        wordDict.copy(resources);
        wordDictRepository.save(wordDict);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            wordDictRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<WordDictDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WordDictDto wordDict : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("单词", wordDict.getText());
            map.put("英语的解释", wordDict.getDescription());
            map.put("音标", wordDict.getPhonetic());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}