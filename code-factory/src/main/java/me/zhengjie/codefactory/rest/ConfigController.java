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
import me.zhengjie.codefactory.domain.Config;
import me.zhengjie.codefactory.service.ConfigService;
import me.zhengjie.codefactory.service.dto.ConfigQueryCriteria;
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
* @date 2023-04-03
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "配置管理")
@RequestMapping("/api/config")
public class ConfigController {

    private final ConfigService configService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('config:list')")
    public void exportConfig(HttpServletResponse response, ConfigQueryCriteria criteria) throws IOException {
        configService.download(configService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询配置")
    @ApiOperation("查询配置")
    @PreAuthorize("@el.check('config:list')")
    public ResponseEntity<Object> queryConfig(ConfigQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(configService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(configService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增配置")
    @ApiOperation("新增配置")
    @PreAuthorize("@el.check('config:add')")
    public ResponseEntity<Object> createConfig(@Validated @RequestBody Config resources){
        return new ResponseEntity<>(configService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改配置")
    @ApiOperation("修改配置")
    @PreAuthorize("@el.check('config:edit')")
    public ResponseEntity<Object> updateConfig(@Validated @RequestBody Config resources){
        configService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除配置")
    @ApiOperation("删除配置")
    @PreAuthorize("@el.check('config:del')")
    public ResponseEntity<Object> deleteConfig(@RequestBody Long[] ids) {
        configService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}