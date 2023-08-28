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
import me.zhengjie.morpheme.domain.StudyWordStatics;
import me.zhengjie.morpheme.service.StudyWordStaticsService;
import me.zhengjie.morpheme.service.dto.StudyWordStaticsQueryCriteria;
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
* @date 2023-08-28
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "学习单词统计管理")
@RequestMapping("/api/studyWordStatics")
public class StudyWordStaticsController {

    private final StudyWordStaticsService studyWordStaticsService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('studyWordStatics:list')")
    public void exportStudyWordStatics(HttpServletResponse response, StudyWordStaticsQueryCriteria criteria) throws IOException {
        studyWordStaticsService.download(studyWordStaticsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询学习单词统计")
    @ApiOperation("查询学习单词统计")
    @PreAuthorize("@el.check('studyWordStatics:list')")
    public ResponseEntity<Object> queryStudyWordStatics(StudyWordStaticsQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(studyWordStaticsService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(studyWordStaticsService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增学习单词统计")
    @ApiOperation("新增学习单词统计")
    @PreAuthorize("@el.check('studyWordStatics:add')")
    public ResponseEntity<Object> createStudyWordStatics(@Validated @RequestBody StudyWordStatics resources){
        return new ResponseEntity<>(studyWordStaticsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改学习单词统计")
    @ApiOperation("修改学习单词统计")
    @PreAuthorize("@el.check('studyWordStatics:edit')")
    public ResponseEntity<Object> updateStudyWordStatics(@Validated @RequestBody StudyWordStatics resources){
        studyWordStaticsService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除学习单词统计")
    @ApiOperation("删除学习单词统计")
    @PreAuthorize("@el.check('studyWordStatics:del')")
    public ResponseEntity<Object> deleteStudyWordStatics(@RequestBody Long[] ids) {
        studyWordStaticsService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}