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

import me.zhengjie.morpheme.domain.StudyAffixStatic;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.StudyAffixStaticRepository;
import me.zhengjie.morpheme.service.StudyAffixStaticService;
import me.zhengjie.morpheme.service.dto.StudyAffixStaticDto;
import me.zhengjie.morpheme.service.dto.StudyAffixStaticQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.StudyAffixStaticMapper;
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
* @date 2023-09-14
**/
@Service
@RequiredArgsConstructor
public class StudyAffixStaticServiceImpl implements StudyAffixStaticService {

    private final StudyAffixStaticRepository studyAffixStaticRepository;
    private final StudyAffixStaticMapper studyAffixStaticMapper;

    @Override
    public Map<String,Object> queryAll(StudyAffixStaticQueryCriteria criteria, Pageable pageable){
        Page<StudyAffixStatic> page = studyAffixStaticRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(studyAffixStaticMapper::toDto));
    }

    @Override
    public List<StudyAffixStaticDto> queryAll(StudyAffixStaticQueryCriteria criteria){
        return studyAffixStaticMapper.toDto(studyAffixStaticRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StudyAffixStaticDto findById(Long id) {
        StudyAffixStatic studyAffixStatic = studyAffixStaticRepository.findById(id).orElseGet(StudyAffixStatic::new);
        ValidationUtil.isNull(studyAffixStatic.getId(),"StudyAffixStatic","id",id);
        return studyAffixStaticMapper.toDto(studyAffixStatic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudyAffixStaticDto create(StudyAffixStatic resources) {
        return studyAffixStaticMapper.toDto(studyAffixStaticRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StudyAffixStatic resources) {
        StudyAffixStatic studyAffixStatic = studyAffixStaticRepository.findById(resources.getId()).orElseGet(StudyAffixStatic::new);
        ValidationUtil.isNull( studyAffixStatic.getId(),"StudyAffixStatic","id",resources.getId());
        studyAffixStatic.copy(resources);
        studyAffixStaticRepository.save(studyAffixStatic);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            studyAffixStaticRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StudyAffixStaticDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StudyAffixStaticDto studyAffixStatic : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户ID", studyAffixStatic.getUid());
            map.put("学习知识ID", studyAffixStatic.getObjectId());
            map.put("学习次数", studyAffixStatic.getStudyTimes());
            map.put("记得次数", studyAffixStatic.getSimpleTimes());
            map.put("模糊次数", studyAffixStatic.getConfuseTimes());
            map.put("忘记次数", studyAffixStatic.getForgetTimes());
            map.put("最后学习日期", studyAffixStatic.getLastReviewTime());
            map.put("最后一次学习结果", studyAffixStatic.getLastReviewResult());
            map.put("记忆等级", studyAffixStatic.getMemeryLevel());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}