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
import me.zhengjie.morpheme.domain.StudyAffixStatic;
import me.zhengjie.morpheme.service.StudyAffixStaticService;
import me.zhengjie.morpheme.service.dto.StudyAffixStaticQueryCriteria;
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
* @date 2023-09-14
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "学习词缀统计管理")
@RequestMapping("/api/studyAffixStatic")
public class StudyAffixStaticController {

    private final StudyAffixStaticService studyAffixStaticService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('studyAffixStatic:list')")
    public void exportStudyAffixStatic(HttpServletResponse response, StudyAffixStaticQueryCriteria criteria) throws IOException {
        studyAffixStaticService.download(studyAffixStaticService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询学习词缀统计")
    @ApiOperation("查询学习词缀统计")
    @PreAuthorize("@el.check('studyAffixStatic:list')")
    public ResponseEntity<Object> queryStudyAffixStatic(StudyAffixStaticQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(studyAffixStaticService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(studyAffixStaticService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增学习词缀统计")
    @ApiOperation("新增学习词缀统计")
    @PreAuthorize("@el.check('studyAffixStatic:add')")
    public ResponseEntity<Object> createStudyAffixStatic(@Validated @RequestBody StudyAffixStatic resources){
        return new ResponseEntity<>(studyAffixStaticService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改学习词缀统计")
    @ApiOperation("修改学习词缀统计")
    @PreAuthorize("@el.check('studyAffixStatic:edit')")
    public ResponseEntity<Object> updateStudyAffixStatic(@Validated @RequestBody StudyAffixStatic resources){
        studyAffixStaticService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除学习词缀统计")
    @ApiOperation("删除学习词缀统计")
    @PreAuthorize("@el.check('studyAffixStatic:del')")
    public ResponseEntity<Object> deleteStudyAffixStatic(@RequestBody Long[] ids) {
        studyAffixStaticService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}