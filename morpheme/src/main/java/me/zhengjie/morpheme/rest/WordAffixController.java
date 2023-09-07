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
import me.zhengjie.morpheme.domain.WordAffix;
import me.zhengjie.morpheme.service.WordAffixService;
import me.zhengjie.morpheme.service.dto.WordAffixQueryCriteria;
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
* @date 2023-09-07
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "词缀管理")
@RequestMapping("/api/wordAffix")
public class WordAffixController {

    private final WordAffixService wordAffixService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('wordAffix:list')")
    public void exportWordAffix(HttpServletResponse response, WordAffixQueryCriteria criteria) throws IOException {
        wordAffixService.download(wordAffixService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询词缀")
    @ApiOperation("查询词缀")
    @PreAuthorize("@el.check('wordAffix:list')")
    public ResponseEntity<Object> queryWordAffix(WordAffixQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(wordAffixService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(wordAffixService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增词缀")
    @ApiOperation("新增词缀")
    @PreAuthorize("@el.check('wordAffix:add')")
    public ResponseEntity<Object> createWordAffix(@Validated @RequestBody WordAffix resources){
        return new ResponseEntity<>(wordAffixService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改词缀")
    @ApiOperation("修改词缀")
    @PreAuthorize("@el.check('wordAffix:edit')")
    public ResponseEntity<Object> updateWordAffix(@Validated @RequestBody WordAffix resources){
        wordAffixService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除词缀")
    @ApiOperation("删除词缀")
    @PreAuthorize("@el.check('wordAffix:del')")
    public ResponseEntity<Object> deleteWordAffix(@RequestBody Long[] ids) {
        wordAffixService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}