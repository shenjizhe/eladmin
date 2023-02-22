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
package me.zhengjie.codefactory.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.codefactory.domain.MarketEntityRestriction;
import me.zhengjie.codefactory.service.MarketEntityRestrictionService;
import me.zhengjie.codefactory.service.dto.MarketEntityRestrictionQueryCriteria;
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
* @date 2023-02-22
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "市场实体约束管理")
@RequestMapping("/api/marketEntityRestriction")
public class MarketEntityRestrictionController {

    private final MarketEntityRestrictionService marketEntityRestrictionService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('marketEntityRestriction:list')")
    public void exportMarketEntityRestriction(HttpServletResponse response, MarketEntityRestrictionQueryCriteria criteria) throws IOException {
        marketEntityRestrictionService.download(marketEntityRestrictionService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询市场实体约束")
    @ApiOperation("查询市场实体约束")
    @PreAuthorize("@el.check('marketEntityRestriction:list')")
    public ResponseEntity<Object> queryMarketEntityRestriction(MarketEntityRestrictionQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(marketEntityRestrictionService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增市场实体约束")
    @ApiOperation("新增市场实体约束")
    @PreAuthorize("@el.check('marketEntityRestriction:add')")
    public ResponseEntity<Object> createMarketEntityRestriction(@Validated @RequestBody MarketEntityRestriction resources){
        return new ResponseEntity<>(marketEntityRestrictionService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改市场实体约束")
    @ApiOperation("修改市场实体约束")
    @PreAuthorize("@el.check('marketEntityRestriction:edit')")
    public ResponseEntity<Object> updateMarketEntityRestriction(@Validated @RequestBody MarketEntityRestriction resources){
        marketEntityRestrictionService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除市场实体约束")
    @ApiOperation("删除市场实体约束")
    @PreAuthorize("@el.check('marketEntityRestriction:del')")
    public ResponseEntity<Object> deleteMarketEntityRestriction(@RequestBody Long[] ids) {
        marketEntityRestrictionService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}