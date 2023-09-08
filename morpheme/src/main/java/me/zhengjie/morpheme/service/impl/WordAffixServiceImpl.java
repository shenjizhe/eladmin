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

import me.zhengjie.morpheme.domain.WordAffix;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.WordAffixRepository;
import me.zhengjie.morpheme.service.WordAffixService;
import me.zhengjie.morpheme.service.dto.WordAffixDto;
import me.zhengjie.morpheme.service.dto.WordAffixQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.WordAffixMapper;
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
* @date 2023-09-08
**/
@Service
@RequiredArgsConstructor
public class WordAffixServiceImpl implements WordAffixService {

    private final WordAffixRepository wordAffixRepository;
    private final WordAffixMapper wordAffixMapper;

    @Override
    public Map<String,Object> queryAll(WordAffixQueryCriteria criteria, Pageable pageable){
        Page<WordAffix> page = wordAffixRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(wordAffixMapper::toDto));
    }

    @Override
    public List<WordAffixDto> queryAll(WordAffixQueryCriteria criteria){
        return wordAffixMapper.toDto(wordAffixRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public WordAffixDto findById(Long id) {
        WordAffix wordAffix = wordAffixRepository.findById(id).orElseGet(WordAffix::new);
        ValidationUtil.isNull(wordAffix.getId(),"WordAffix","id",id);
        return wordAffixMapper.toDto(wordAffix);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WordAffixDto create(WordAffix resources) {
        return wordAffixMapper.toDto(wordAffixRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WordAffix resources) {
        WordAffix wordAffix = wordAffixRepository.findById(resources.getId()).orElseGet(WordAffix::new);
        ValidationUtil.isNull( wordAffix.getId(),"WordAffix","id",resources.getId());
        wordAffix.copy(resources);
        wordAffixRepository.save(wordAffix);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            wordAffixRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<WordAffixDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WordAffixDto wordAffix : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("词缀文本", wordAffix.getText());
            map.put("类型(1前缀,2后缀)", wordAffix.getAffix());
            map.put("中文含义", wordAffix.getMeaningChinese());
            map.put("英文含义（to lean）", wordAffix.getMeaningEnglish());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}