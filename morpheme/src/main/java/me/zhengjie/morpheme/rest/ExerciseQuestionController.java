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
package me.zhengjie.morpheme.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.morpheme.domain.ExerciseQuestion;
import me.zhengjie.morpheme.service.ExerciseQuestionService;
import me.zhengjie.morpheme.service.dto.ExerciseQuestionQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://eladmin.vip
* @author Jason Shen
* @date 2023-08-04
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "练习题管理")
@RequestMapping("/api/exerciseQuestion")
public class ExerciseQuestionController {

    private final ExerciseQuestionService exerciseQuestionService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('exerciseQuestion:list')")
    public void exportExerciseQuestion(HttpServletResponse response, ExerciseQuestionQueryCriteria criteria) throws IOException {
        exerciseQuestionService.download(exerciseQuestionService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询练习题")
    @ApiOperation("查询练习题")
    @PreAuthorize("@el.check('exerciseQuestion:list')")
    public ResponseEntity<Object> queryExerciseQuestion(ExerciseQuestionQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(exerciseQuestionService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(exerciseQuestionService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增练习题")
    @ApiOperation("新增练习题")
    @PreAuthorize("@el.check('exerciseQuestion:add')")
    public ResponseEntity<Object> createExerciseQuestion(@Validated @RequestBody ExerciseQuestion resources){
        return new ResponseEntity<>(exerciseQuestionService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改练习题")
    @ApiOperation("修改练习题")
    @PreAuthorize("@el.check('exerciseQuestion:edit')")
    public ResponseEntity<Object> updateExerciseQuestion(@Validated @RequestBody ExerciseQuestion resources){
        exerciseQuestionService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除练习题")
    @ApiOperation("删除练习题")
    @PreAuthorize("@el.check('exerciseQuestion:del')")
    public ResponseEntity<Object> deleteExerciseQuestion(@RequestBody Long[] ids) {
        exerciseQuestionService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}