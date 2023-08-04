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

import me.zhengjie.morpheme.domain.ExerciseQuestion;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.repository.ExerciseQuestionRepository;
import me.zhengjie.morpheme.service.ExerciseQuestionService;
import me.zhengjie.morpheme.service.dto.ExerciseQuestionDto;
import me.zhengjie.morpheme.service.dto.ExerciseQuestionQueryCriteria;
import me.zhengjie.morpheme.service.mapstruct.ExerciseQuestionMapper;
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
* @date 2023-08-04
**/
@Service
@RequiredArgsConstructor
public class ExerciseQuestionServiceImpl implements ExerciseQuestionService {

    private final ExerciseQuestionRepository exerciseQuestionRepository;
    private final ExerciseQuestionMapper exerciseQuestionMapper;

    @Override
    public Map<String,Object> queryAll(ExerciseQuestionQueryCriteria criteria, Pageable pageable){
        Page<ExerciseQuestion> page = exerciseQuestionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(exerciseQuestionMapper::toDto));
    }

    @Override
    public List<ExerciseQuestionDto> queryAll(ExerciseQuestionQueryCriteria criteria){
        return exerciseQuestionMapper.toDto(exerciseQuestionRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ExerciseQuestionDto findById(Long id) {
        ExerciseQuestion exerciseQuestion = exerciseQuestionRepository.findById(id).orElseGet(ExerciseQuestion::new);
        ValidationUtil.isNull(exerciseQuestion.getId(),"ExerciseQuestion","id",id);
        return exerciseQuestionMapper.toDto(exerciseQuestion);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExerciseQuestionDto create(ExerciseQuestion resources) {
        return exerciseQuestionMapper.toDto(exerciseQuestionRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ExerciseQuestion resources) {
        ExerciseQuestion exerciseQuestion = exerciseQuestionRepository.findById(resources.getId()).orElseGet(ExerciseQuestion::new);
        ValidationUtil.isNull( exerciseQuestion.getId(),"ExerciseQuestion","id",resources.getId());
        exerciseQuestion.copy(resources);
        exerciseQuestionRepository.save(exerciseQuestion);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            exerciseQuestionRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ExerciseQuestionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ExerciseQuestionDto exerciseQuestion : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("词素ID", exerciseQuestion.getMorphemeId());
            map.put("编号", exerciseQuestion.getNumber());
            map.put("题目类型", exerciseQuestion.getType());
            map.put("题干", exerciseQuestion.getQuestionStem());
            map.put("题目内容", exerciseQuestion.getContent());
            map.put("选项", exerciseQuestion.getOptions());
            map.put("答案", exerciseQuestion.getAnswer());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}