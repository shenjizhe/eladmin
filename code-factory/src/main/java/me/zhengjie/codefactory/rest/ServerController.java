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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.codefactory.domain.Server;
import me.zhengjie.codefactory.service.ConfigService;
import me.zhengjie.codefactory.service.ServerService;
import me.zhengjie.codefactory.service.dto.ConfigDto;
import me.zhengjie.codefactory.service.dto.ConfigQueryCriteria;
import me.zhengjie.codefactory.service.dto.ServerQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Jason Shen
 * @website https://eladmin.vip
 * @date 2023-04-10
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "服务器管理")
@RequestMapping("/api/server")
public class ServerController {

    private final ServerService serverService;
    private final ConfigService configService;

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
    public ResponseEntity<Object> queryServer(ServerQueryCriteria criteria, Pageable pageable) {
        if (pageable.getPageSize() == -1) {
            return new ResponseEntity<>(serverService.queryAll(criteria), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(serverService.queryAll(criteria, pageable), HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增服务器")
    @ApiOperation("新增服务器")
    @PreAuthorize("@el.check('server:add')")
    public ResponseEntity<Object> createServer(@Validated @RequestBody Server resources) {
        return new ResponseEntity<>(serverService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改服务器")
    @ApiOperation("修改服务器")
    @PreAuthorize("@el.check('server:edit')")
    public ResponseEntity<Object> updateServer(@Validated @RequestBody Server resources) {
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

    @Log("执行脚本")
    @ApiOperation(value = "执行脚本")
    @PostMapping("/execute")
    @PreAuthorize("@el.check('server:edit')")
    public ResponseEntity<String> execute(@RequestBody Map<String, Object> map) {
        final boolean containsId = map.containsKey("serverId");
        final boolean containsSid = map.containsKey("scriptId");
        final boolean containsKey = map.containsKey("key");
        if (containsId && (containsSid || containsKey)) {
            final Long serverId = Long.parseLong(map.get("serverId").toString());
            if (containsSid) {
                final Long scriptId = Long.parseLong(map.get("scriptId").toString());
                return new ResponseEntity<String>(serverService.execute(serverId, scriptId), HttpStatus.CREATED);
            } else {
                final String key = map.get("key").toString();
                return new ResponseEntity<String>(serverService.execute(serverId, key), HttpStatus.CREATED);
            }

        }
        return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
    }

    @Log("覆盖文件")
    @ApiOperation(value = "覆盖文件")
    @PostMapping("/copy")
    @PreAuthorize("@el.check('server:edit')")
    public ResponseEntity<Boolean> copyFile(@RequestBody Map<String, Object> map) {
        final boolean containsId = map.containsKey("serverId");
        final boolean containsKey = map.containsKey("key");
        final boolean containsPath = map.containsKey("filePath");

        if (containsId && containsKey && containsPath) {
            final Long serverId = Long.parseLong(map.get("serverId").toString());
            final String key = map.get("key").toString();
            final String filePath = map.get("filePath").toString();

            final ConfigQueryCriteria configQueryCriteria = new ConfigQueryCriteria();
            configQueryCriteria.setKey(key);
            final List<ConfigDto> configDtos = configService.queryAll(configQueryCriteria);
            if (configDtos.size() > 0) {
                final ConfigDto config = configDtos.get(0);
                return new ResponseEntity<Boolean>(serverService.copyFileByKey(serverId, filePath, key), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
    }
}