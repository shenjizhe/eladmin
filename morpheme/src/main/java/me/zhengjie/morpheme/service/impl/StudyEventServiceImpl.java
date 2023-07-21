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

import me.zhengjie.morpheme.domain.StudyEvent;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.StudyEventRepository;
import me.zhengjie.morpheme.service.StudyEventService;
import me.zhengjie.morpheme.service.dto.StudyEventDto;
import me.zhengjie.morpheme.service.dto.StudyEventQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.StudyEventMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
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
* @date 2023-07-21
**/
@Service
@RequiredArgsConstructor
public class StudyEventServiceImpl implements StudyEventService {

    private final StudyEventRepository studyEventRepository;
    private final StudyEventMapper studyEventMapper;

    @Override
    public Map<String,Object> queryAll(StudyEventQueryCriteria criteria, Pageable pageable){
        Page<StudyEvent> page = studyEventRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(studyEventMapper::toDto));
    }

    @Override
    public List<StudyEventDto> queryAll(StudyEventQueryCriteria criteria){
        return studyEventMapper.toDto(studyEventRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public StudyEventDto findById(Long id) {
        StudyEvent studyEvent = studyEventRepository.findById(id).orElseGet(StudyEvent::new);
        ValidationUtil.isNull(studyEvent.getId(),"StudyEvent","id",id);
        return studyEventMapper.toDto(studyEvent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudyEventDto create(StudyEvent resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId()); 
        return studyEventMapper.toDto(studyEventRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StudyEvent resources) {
        StudyEvent studyEvent = studyEventRepository.findById(resources.getId()).orElseGet(StudyEvent::new);
        ValidationUtil.isNull( studyEvent.getId(),"StudyEvent","id",resources.getId());
        studyEvent.copy(resources);
        studyEventRepository.save(studyEvent);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            studyEventRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<StudyEventDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (StudyEventDto studyEvent : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("操作时间", studyEvent.getTime());
            map.put("事件", studyEvent.getEvent());
            map.put("内容", studyEvent.getContent());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}