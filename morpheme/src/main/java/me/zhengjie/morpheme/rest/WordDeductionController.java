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
import me.zhengjie.morpheme.domain.WordDeduction;
import me.zhengjie.morpheme.service.WordDeductionService;
import me.zhengjie.morpheme.service.dto.WordDeductionQueryCriteria;
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
* @date 2023-05-24
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "单词推导管理")
@RequestMapping("/api/wordDeduction")
public class WordDeductionController {

    private final WordDeductionService wordDeductionService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('wordDeduction:list')")
    public void exportWordDeduction(HttpServletResponse response, WordDeductionQueryCriteria criteria) throws IOException {
        wordDeductionService.download(wordDeductionService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询单词推导")
    @ApiOperation("查询单词推导")
    @PreAuthorize("@el.check('wordDeduction:list')")
    public ResponseEntity<Object> queryWordDeduction(WordDeductionQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(wordDeductionService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(wordDeductionService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增单词推导")
    @ApiOperation("新增单词推导")
    @PreAuthorize("@el.check('wordDeduction:add')")
    public ResponseEntity<Object> createWordDeduction(@Validated @RequestBody WordDeduction resources){
        return new ResponseEntity<>(wordDeductionService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改单词推导")
    @ApiOperation("修改单词推导")
    @PreAuthorize("@el.check('wordDeduction:edit')")
    public ResponseEntity<Object> updateWordDeduction(@Validated @RequestBody WordDeduction resources){
        wordDeductionService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除单词推导")
    @ApiOperation("删除单词推导")
    @PreAuthorize("@el.check('wordDeduction:del')")
    public ResponseEntity<Object> deleteWordDeduction(@RequestBody Long[] ids) {
        wordDeductionService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}