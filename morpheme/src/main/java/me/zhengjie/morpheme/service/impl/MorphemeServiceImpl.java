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

import me.zhengjie.morpheme.domain.Morpheme;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.MorphemeRepository;
import me.zhengjie.morpheme.service.MorphemeService;
import me.zhengjie.morpheme.service.dto.MorphemeDto;
import me.zhengjie.morpheme.service.dto.MorphemeQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.MorphemeMapper;
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
public class MorphemeServiceImpl implements MorphemeService {

    private final MorphemeRepository morphemeRepository;
    private final MorphemeMapper morphemeMapper;

    @Override
    public Map<String,Object> queryAll(MorphemeQueryCriteria criteria, Pageable pageable){
        Page<Morpheme> page = morphemeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(morphemeMapper::toDto));
    }

    @Override
    public List<MorphemeDto> queryAll(MorphemeQueryCriteria criteria){
        return morphemeMapper.toDto(morphemeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public MorphemeDto findById(Long id) {
        Morpheme morpheme = morphemeRepository.findById(id).orElseGet(Morpheme::new);
        ValidationUtil.isNull(morpheme.getId(),"Morpheme","id",id);
        return morphemeMapper.toDto(morpheme);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MorphemeDto create(Morpheme resources) {
        return morphemeMapper.toDto(morphemeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Morpheme resources) {
        Morpheme morpheme = morphemeRepository.findById(resources.getId()).orElseGet(Morpheme::new);
        ValidationUtil.isNull( morpheme.getId(),"Morpheme","id",resources.getId());
        morpheme.copy(resources);
        morphemeRepository.save(morpheme);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            morphemeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<MorphemeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MorphemeDto morpheme : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("字典ID", morpheme.getDictionaryId());
            map.put("编号", morpheme.getNumber());
            map.put("词素文本", morpheme.getText());
            map.put("词素类型", morpheme.getType());
            map.put("词源", morpheme.getSource());
            map.put("中文含义", morpheme.getMeaningChinese());
            map.put("英文含义", morpheme.getMeaningEnglish());
            map.put("说明", morpheme.getDescription());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}