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
import me.zhengjie.codefactory.domain.MarketDomain;
import me.zhengjie.codefactory.service.MarketDomainService;
import me.zhengjie.codefactory.service.dto.MarketDomainQueryCriteria;
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
@Api(tags = "市场领域管理")
@RequestMapping("/api/marketDomain")
public class MarketDomainController {

    private final MarketDomainService marketDomainService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('marketDomain:list')")
    public void exportMarketDomain(HttpServletResponse response, MarketDomainQueryCriteria criteria) throws IOException {
        marketDomainService.download(marketDomainService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询市场领域")
    @ApiOperation("查询市场领域")
    @PreAuthorize("@el.check('marketDomain:list')")
    public ResponseEntity<Object> queryMarketDomain(MarketDomainQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(marketDomainService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增市场领域")
    @ApiOperation("新增市场领域")
    @PreAuthorize("@el.check('marketDomain:add')")
    public ResponseEntity<Object> createMarketDomain(@Validated @RequestBody MarketDomain resources){
        return new ResponseEntity<>(marketDomainService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改市场领域")
    @ApiOperation("修改市场领域")
    @PreAuthorize("@el.check('marketDomain:edit')")
    public ResponseEntity<Object> updateMarketDomain(@Validated @RequestBody MarketDomain resources){
        marketDomainService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除市场领域")
    @ApiOperation("删除市场领域")
    @PreAuthorize("@el.check('marketDomain:del')")
    public ResponseEntity<Object> deleteMarketDomain(@RequestBody Long[] ids) {
        marketDomainService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}