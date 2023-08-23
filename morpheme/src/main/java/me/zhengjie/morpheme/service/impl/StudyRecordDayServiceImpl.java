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

import me.zhengjie.morpheme.domain.StudyRecordDay;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.StudyRecordDayRepository;
import me.zhengjie.morpheme.service.StudyRecordDayService;
import me.zhengjie.morpheme.service.dto.StudyRecordDayDto;
import me.zhengjie.morpheme.service.dto.StudyRecordDayQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.StudyRecordDayMapper;
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
* @date 2023-08-23
**/
@Service
@RequiredArgsConstructor
public class StudyRecordDayServiceImpl implements StudyRecordDayService {

    private final StudyRecordDayRepository studyRecordDayRepository;
    private final StudyRecordDayMapper studyRecordDayMapper;

    @Override
    public Map<String,Object> queryAll(StudyRecordDayQueryCriteria criteria, Pageable pageable){
        Page<StudyRecordDay> page = studyRecordDayRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(studyRecordDayMapper::toDto));
    }

    @Override
    public List<StudyRecordDayDto> queryAll(StudyRecordDayQueryCriteria criteria){
        return studyRecordDayMapper.toDto(studyRecordDayRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StudyRecordDayDto findById(Long id) {
        StudyRecordDay studyRecordDay = studyRecordDayRepository.findById(id).orElseGet(StudyRecordDay::new);
        ValidationUtil.isNull(studyRecordDay.getId(),"StudyRecordDay","id",id);
        return studyRecordDayMapper.toDto(studyRecordDay);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudyRecordDayDto create(StudyRecordDay resources) {
        return studyRecordDayMapper.toDto(studyRecordDayRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StudyRecordDay resources) {
        StudyRecordDay studyRecordDay = studyRecordDayRepository.findById(resources.getId()).orElseGet(StudyRecordDay::new);
        ValidationUtil.isNull( studyRecordDay.getId(),"StudyRecordDay","id",resources.getId());
        studyRecordDay.copy(resources);
        studyRecordDayRepository.save(studyRecordDay);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            studyRecordDayRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StudyRecordDayDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StudyRecordDayDto studyRecordDay : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户id", studyRecordDay.getUid());
            map.put("日期", studyRecordDay.getDate());
            map.put("学习内容类型（0 - 词根 1- 单词）", studyRecordDay.getObjectType());
            map.put("学习内容id", studyRecordDay.getObjectId());
            map.put("类型（0-初次看，1-再次看）", studyRecordDay.getType());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}