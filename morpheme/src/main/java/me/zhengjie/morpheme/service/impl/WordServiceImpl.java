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

import me.zhengjie.morpheme.domain.Word;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.WordRepository;
import me.zhengjie.morpheme.service.WordService;
import me.zhengjie.morpheme.service.dto.WordDto;
import me.zhengjie.morpheme.service.dto.WordQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.WordMapper;
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
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;
    private final WordMapper wordMapper;

    @Override
    public Map<String,Object> queryAll(WordQueryCriteria criteria, Pageable pageable){
        Page<Word> page = wordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(wordMapper::toDto));
    }

    @Override
    public List<WordDto> queryAll(WordQueryCriteria criteria){
        return wordMapper.toDto(wordRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public WordDto findById(Long id) {
        Word word = wordRepository.findById(id).orElseGet(Word::new);
        ValidationUtil.isNull(word.getId(),"Word","id",id);
        return wordMapper.toDto(word);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WordDto create(Word resources) {
        return wordMapper.toDto(wordRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Word resources) {
        Word word = wordRepository.findById(resources.getId()).orElseGet(Word::new);
        ValidationUtil.isNull( word.getId(),"Word","id",resources.getId());
        word.copy(resources);
        wordRepository.save(word);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            wordRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<WordDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WordDto word : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("单词文本", word.getText());
            map.put("推导过程", word.getDeduction());
            map.put("词性", word.getNature());
            map.put("是否是派生词素(0-不是派生词 1-是派生词)", word.getIsDerive());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}