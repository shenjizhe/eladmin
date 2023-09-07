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
import me.zhengjie.morpheme.domain.AffixDeductionRelation;
import me.zhengjie.morpheme.service.AffixDeductionRelationService;
import me.zhengjie.morpheme.service.dto.AffixDeductionRelationQueryCriteria;
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
@Api(tags = "词缀推导关系管理")
@RequestMapping("/api/affixDeductionRelation")
public class AffixDeductionRelationController {

    private final AffixDeductionRelationService affixDeductionRelationService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('affixDeductionRelation:list')")
    public void exportAffixDeductionRelation(HttpServletResponse response, AffixDeductionRelationQueryCriteria criteria) throws IOException {
        affixDeductionRelationService.download(affixDeductionRelationService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询词缀推导关系")
    @ApiOperation("查询词缀推导关系")
    @PreAuthorize("@el.check('affixDeductionRelation:list')")
    public ResponseEntity<Object> queryAffixDeductionRelation(AffixDeductionRelationQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(affixDeductionRelationService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(affixDeductionRelationService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增词缀推导关系")
    @ApiOperation("新增词缀推导关系")
    @PreAuthorize("@el.check('affixDeductionRelation:add')")
    public ResponseEntity<Object> createAffixDeductionRelation(@Validated @RequestBody AffixDeductionRelation resources){
        return new ResponseEntity<>(affixDeductionRelationService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改词缀推导关系")
    @ApiOperation("修改词缀推导关系")
    @PreAuthorize("@el.check('affixDeductionRelation:edit')")
    public ResponseEntity<Object> updateAffixDeductionRelation(@Validated @RequestBody AffixDeductionRelation resources){
        affixDeductionRelationService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除词缀推导关系")
    @ApiOperation("删除词缀推导关系")
    @PreAuthorize("@el.check('affixDeductionRelation:del')")
    public ResponseEntity<Object> deleteAffixDeductionRelation(@RequestBody Long[] ids) {
        affixDeductionRelationService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}