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
import me.zhengjie.morpheme.domain.StudyMorphemeStatics;
import me.zhengjie.morpheme.service.StudyMorphemeStaticsService;
import me.zhengjie.morpheme.service.dto.StudyMorphemeStaticsQueryCriteria;
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
* @date 2023-08-29
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "学习词根统计管理")
@RequestMapping("/api/studyMorphemeStatics")
public class StudyMorphemeStaticsController {

    private final StudyMorphemeStaticsService studyMorphemeStaticsService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('studyMorphemeStatics:list')")
    public void exportStudyMorphemeStatics(HttpServletResponse response, StudyMorphemeStaticsQueryCriteria criteria) throws IOException {
        studyMorphemeStaticsService.download(studyMorphemeStaticsService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询学习词根统计")
    @ApiOperation("查询学习词根统计")
    @PreAuthorize("@el.check('studyMorphemeStatics:list')")
    public ResponseEntity<Object> queryStudyMorphemeStatics(StudyMorphemeStaticsQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(studyMorphemeStaticsService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(studyMorphemeStaticsService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增学习词根统计")
    @ApiOperation("新增学习词根统计")
    @PreAuthorize("@el.check('studyMorphemeStatics:add')")
    public ResponseEntity<Object> createStudyMorphemeStatics(@Validated @RequestBody StudyMorphemeStatics resources){
        return new ResponseEntity<>(studyMorphemeStaticsService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改学习词根统计")
    @ApiOperation("修改学习词根统计")
    @PreAuthorize("@el.check('studyMorphemeStatics:edit')")
    public ResponseEntity<Object> updateStudyMorphemeStatics(@Validated @RequestBody StudyMorphemeStatics resources){
        studyMorphemeStaticsService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除学习词根统计")
    @ApiOperation("删除学习词根统计")
    @PreAuthorize("@el.check('studyMorphemeStatics:del')")
    public ResponseEntity<Object> deleteStudyMorphemeStatics(@RequestBody Long[] ids) {
        studyMorphemeStaticsService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}