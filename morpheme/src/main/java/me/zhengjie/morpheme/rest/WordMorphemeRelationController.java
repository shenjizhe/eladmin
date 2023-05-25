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
import me.zhengjie.morpheme.domain.WordMorphemeRelation;
import me.zhengjie.morpheme.service.WordMorphemeRelationService;
import me.zhengjie.morpheme.service.dto.WordMorphemeRelationQueryCriteria;
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
@Api(tags = "单词词素关系管理")
@RequestMapping("/api/wordMorphemeRelation")
public class WordMorphemeRelationController {

    private final WordMorphemeRelationService wordMorphemeRelationService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('wordMorphemeRelation:list')")
    public void exportWordMorphemeRelation(HttpServletResponse response, WordMorphemeRelationQueryCriteria criteria) throws IOException {
        wordMorphemeRelationService.download(wordMorphemeRelationService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询单词词素关系")
    @ApiOperation("查询单词词素关系")
    @PreAuthorize("@el.check('wordMorphemeRelation:list')")
    public ResponseEntity<Object> queryWordMorphemeRelation(WordMorphemeRelationQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(wordMorphemeRelationService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(wordMorphemeRelationService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增单词词素关系")
    @ApiOperation("新增单词词素关系")
    @PreAuthorize("@el.check('wordMorphemeRelation:add')")
    public ResponseEntity<Object> createWordMorphemeRelation(@Validated @RequestBody WordMorphemeRelation resources){
        return new ResponseEntity<>(wordMorphemeRelationService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改单词词素关系")
    @ApiOperation("修改单词词素关系")
    @PreAuthorize("@el.check('wordMorphemeRelation:edit')")
    public ResponseEntity<Object> updateWordMorphemeRelation(@Validated @RequestBody WordMorphemeRelation resources){
        wordMorphemeRelationService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除单词词素关系")
    @ApiOperation("删除单词词素关系")
    @PreAuthorize("@el.check('wordMorphemeRelation:del')")
    public ResponseEntity<Object> deleteWordMorphemeRelation(@RequestBody Long[] ids) {
        wordMorphemeRelationService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}