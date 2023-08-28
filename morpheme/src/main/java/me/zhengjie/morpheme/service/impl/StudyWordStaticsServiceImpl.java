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

import me.zhengjie.morpheme.domain.StudyWordStatics;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.StudyWordStaticsRepository;
import me.zhengjie.morpheme.service.StudyWordStaticsService;
import me.zhengjie.morpheme.service.dto.StudyWordStaticsDto;
import me.zhengjie.morpheme.service.dto.StudyWordStaticsQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.StudyWordStaticsMapper;
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
public class StudyWordStaticsServiceImpl implements StudyWordStaticsService {

    private final StudyWordStaticsRepository studyWordStaticsRepository;
    private final StudyWordStaticsMapper studyWordStaticsMapper;

    @Override
    public Map<String,Object> queryAll(StudyWordStaticsQueryCriteria criteria, Pageable pageable){
        Page<StudyWordStatics> page = studyWordStaticsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(studyWordStaticsMapper::toDto));
    }

    @Override
    public List<StudyWordStaticsDto> queryAll(StudyWordStaticsQueryCriteria criteria){
        return studyWordStaticsMapper.toDto(studyWordStaticsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StudyWordStaticsDto findById(Long id) {
        StudyWordStatics studyWordStatics = studyWordStaticsRepository.findById(id).orElseGet(StudyWordStatics::new);
        ValidationUtil.isNull(studyWordStatics.getId(),"StudyWordStatics","id",id);
        return studyWordStaticsMapper.toDto(studyWordStatics);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudyWordStaticsDto create(StudyWordStatics resources) {
        return studyWordStaticsMapper.toDto(studyWordStaticsRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StudyWordStatics resources) {
        StudyWordStatics studyWordStatics = studyWordStaticsRepository.findById(resources.getId()).orElseGet(StudyWordStatics::new);
        ValidationUtil.isNull( studyWordStatics.getId(),"StudyWordStatics","id",resources.getId());
        studyWordStatics.copy(resources);
        studyWordStaticsRepository.save(studyWordStatics);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            studyWordStaticsRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StudyWordStaticsDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StudyWordStaticsDto studyWordStatics : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户ID", studyWordStatics.getUid());
            map.put("学习知识ID", studyWordStatics.getObjectId());
            map.put("状态", studyWordStatics.getStatus());
            map.put("忘记次数", studyWordStatics.getForgetTimes());
            map.put("记忆等级", studyWordStatics.getMemeryLevel());
            map.put("学习次数", studyWordStatics.getStudyTimes());
            map.put("记得次数", studyWordStatics.getSimpleTimes());
            map.put("模糊次数", studyWordStatics.getConfuseTimes());
            map.put("最后学习日期", studyWordStatics.getLastReviewTime());
            map.put("最后一次学习结果", studyWordStatics.getLastReviewResult());
            map.put("学习比例", studyWordStatics.getReviewRate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}