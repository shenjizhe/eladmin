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

import me.zhengjie.morpheme.domain.StudyMorphemeStatics;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.StudyMorphemeStaticsRepository;
import me.zhengjie.morpheme.service.StudyMorphemeStaticsService;
import me.zhengjie.morpheme.service.dto.StudyMorphemeStaticsDto;
import me.zhengjie.morpheme.service.dto.StudyMorphemeStaticsQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.StudyMorphemeStaticsMapper;
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
* @date 2023-08-28
**/
@Service
@RequiredArgsConstructor
public class StudyMorphemeStaticsServiceImpl implements StudyMorphemeStaticsService {

    private final StudyMorphemeStaticsRepository studyMorphemeStaticsRepository;
    private final StudyMorphemeStaticsMapper studyMorphemeStaticsMapper;

    @Override
    public Map<String,Object> queryAll(StudyMorphemeStaticsQueryCriteria criteria, Pageable pageable){
        Page<StudyMorphemeStatics> page = studyMorphemeStaticsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(studyMorphemeStaticsMapper::toDto));
    }

    @Override
    public List<StudyMorphemeStaticsDto> queryAll(StudyMorphemeStaticsQueryCriteria criteria){
        return studyMorphemeStaticsMapper.toDto(studyMorphemeStaticsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StudyMorphemeStaticsDto findById(Long id) {
        StudyMorphemeStatics studyMorphemeStatics = studyMorphemeStaticsRepository.findById(id).orElseGet(StudyMorphemeStatics::new);
        ValidationUtil.isNull(studyMorphemeStatics.getId(),"StudyMorphemeStatics","id",id);
        return studyMorphemeStaticsMapper.toDto(studyMorphemeStatics);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudyMorphemeStaticsDto create(StudyMorphemeStatics resources) {
        return studyMorphemeStaticsMapper.toDto(studyMorphemeStaticsRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StudyMorphemeStatics resources) {
        StudyMorphemeStatics studyMorphemeStatics = studyMorphemeStaticsRepository.findById(resources.getId()).orElseGet(StudyMorphemeStatics::new);
        ValidationUtil.isNull( studyMorphemeStatics.getId(),"StudyMorphemeStatics","id",resources.getId());
        studyMorphemeStatics.copy(resources);
        studyMorphemeStaticsRepository.save(studyMorphemeStatics);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            studyMorphemeStaticsRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StudyMorphemeStaticsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StudyMorphemeStaticsDto studyMorphemeStatics : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户ID", studyMorphemeStatics.getUid());
            map.put("学习知识ID", studyMorphemeStatics.getObjectId());
            map.put("状态", studyMorphemeStatics.getStatus());
            map.put("学习次数", studyMorphemeStatics.getStudyTims());
            map.put("记得次数", studyMorphemeStatics.getRememberTimes());
            map.put("模糊次数", studyMorphemeStatics.getObscureTimes());
            map.put("忘记次数", studyMorphemeStatics.getForgetTimes());
            map.put("最后学习日期", studyMorphemeStatics.getLastStudyTime());
            map.put("记忆等级", studyMorphemeStatics.getMemeryLevel());
            map.put("学习比例", studyMorphemeStatics.getStudyRate());
            map.put("今天是否需要学", studyMorphemeStatics.getNeedStudyTotday());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}