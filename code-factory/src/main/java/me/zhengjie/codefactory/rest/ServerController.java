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
import me.zhengjie.codefactory.domain.Server;
import me.zhengjie.codefactory.service.ServerService;
import me.zhengjie.codefactory.service.dto.ServerQueryCriteria;
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
* @date 2023-04-06
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "服务器管理")
@RequestMapping("/api/server")
public class ServerController {

    private final ServerService serverService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('server:list')")
    public void exportServer(HttpServletResponse response, ServerQueryCriteria criteria) throws IOException {
        serverService.download(serverService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询服务器")
    @ApiOperation("查询服务器")
    @PreAuthorize("@el.check('server:list')")
    public ResponseEntity<Object> queryServer(ServerQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(serverService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(serverService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增服务器")
    @ApiOperation("新增服务器")
    @PreAuthorize("@el.check('server:add')")
    public ResponseEntity<Object> createServer(@Validated @RequestBody Server resources){
        return new ResponseEntity<>(serverService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改服务器")
    @ApiOperation("修改服务器")
    @PreAuthorize("@el.check('server:edit')")
    public ResponseEntity<Object> updateServer(@Validated @RequestBody Server resources){
        serverService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除服务器")
    @ApiOperation("删除服务器")
    @PreAuthorize("@el.check('server:del')")
    public ResponseEntity<Object> deleteServer(@RequestBody Long[] ids) {
        serverService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}