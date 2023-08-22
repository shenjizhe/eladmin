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
import me.zhengjie.morpheme.domain.StudyEvent;
import me.zhengjie.morpheme.service.StudyEventService;
import me.zhengjie.morpheme.service.dto.StudyEventQueryCriteria;
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
* @date 2023-08-22
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "学习记录管理")
@RequestMapping("/api/studyEvent")
public class StudyEventController {

    private final StudyEventService studyEventService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('studyEvent:list')")
    public void exportStudyEvent(HttpServletResponse response, StudyEventQueryCriteria criteria) throws IOException {
        studyEventService.download(studyEventService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询学习记录")
    @ApiOperation("查询学习记录")
    @PreAuthorize("@el.check('studyEvent:list')")
    public ResponseEntity<Object> queryStudyEvent(StudyEventQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(studyEventService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(studyEventService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增学习记录")
    @ApiOperation("新增学习记录")
    @PreAuthorize("@el.check('studyEvent:add')")
    public ResponseEntity<Object> createStudyEvent(@Validated @RequestBody StudyEvent resources){
        return new ResponseEntity<>(studyEventService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改学习记录")
    @ApiOperation("修改学习记录")
    @PreAuthorize("@el.check('studyEvent:edit')")
    public ResponseEntity<Object> updateStudyEvent(@Validated @RequestBody StudyEvent resources){
        studyEventService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除学习记录")
    @ApiOperation("删除学习记录")
    @PreAuthorize("@el.check('studyEvent:del')")
    public ResponseEntity<Object> deleteStudyEvent(@RequestBody Long[] ids) {
        studyEventService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}