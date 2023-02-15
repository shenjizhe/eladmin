/*
*  Copyright 2019-2020 Zheng Jie
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
import me.zhengjie.codefactory.domain.Domain;
import me.zhengjie.codefactory.service.DomainService;
import me.zhengjie.codefactory.service.dto.DomainQueryCriteria;
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
* @date 2023-02-15
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "领域建模管理")
@RequestMapping("/api/domain")
public class DomainController {

    private final DomainService domainService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('domain:list')")
    public void exportDomain(HttpServletResponse response, DomainQueryCriteria criteria) throws IOException {
        domainService.download(domainService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询领域建模")
    @ApiOperation("查询领域建模")
    @PreAuthorize("@el.check('domain:list')")
    public ResponseEntity<Object> queryDomain(DomainQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(domainService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增领域建模")
    @ApiOperation("新增领域建模")
    @PreAuthorize("@el.check('domain:add')")
    public ResponseEntity<Object> createDomain(@Validated @RequestBody Domain resources){
        return new ResponseEntity<>(domainService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改领域建模")
    @ApiOperation("修改领域建模")
    @PreAuthorize("@el.check('domain:edit')")
    public ResponseEntity<Object> updateDomain(@Validated @RequestBody Domain resources){
        domainService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除领域建模")
    @ApiOperation("删除领域建模")
    @PreAuthorize("@el.check('domain:del')")
    public ResponseEntity<Object> deleteDomain(@RequestBody Long[] ids) {
        domainService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}