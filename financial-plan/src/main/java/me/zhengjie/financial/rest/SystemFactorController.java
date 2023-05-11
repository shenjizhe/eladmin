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
package me.zhengjie.financial.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.financial.domain.SystemFactor;
import me.zhengjie.financial.service.SystemFactorService;
import me.zhengjie.financial.service.dto.SystemFactorQueryCriteria;
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
* @date 2023-05-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "系统系数管理")
@RequestMapping("/api/systemFactor")
public class SystemFactorController {

    private final SystemFactorService systemFactorService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('systemFactor:list')")
    public void exportSystemFactor(HttpServletResponse response, SystemFactorQueryCriteria criteria) throws IOException {
        systemFactorService.download(systemFactorService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询系统系数")
    @ApiOperation("查询系统系数")
    @PreAuthorize("@el.check('systemFactor:list')")
    public ResponseEntity<Object> querySystemFactor(SystemFactorQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(systemFactorService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(systemFactorService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增系统系数")
    @ApiOperation("新增系统系数")
    @PreAuthorize("@el.check('systemFactor:add')")
    public ResponseEntity<Object> createSystemFactor(@Validated @RequestBody SystemFactor resources){
        return new ResponseEntity<>(systemFactorService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改系统系数")
    @ApiOperation("修改系统系数")
    @PreAuthorize("@el.check('systemFactor:edit')")
    public ResponseEntity<Object> updateSystemFactor(@Validated @RequestBody SystemFactor resources){
        systemFactorService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除系统系数")
    @ApiOperation("删除系统系数")
    @PreAuthorize("@el.check('systemFactor:del')")
    public ResponseEntity<Object> deleteSystemFactor(@RequestBody Integer[] ids) {
        systemFactorService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}