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
import me.zhengjie.morpheme.domain.UserStatus;
import me.zhengjie.morpheme.service.UserStatusService;
import me.zhengjie.morpheme.service.dto.UserStatusQueryCriteria;
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
* @date 2023-07-25
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "用户状态管理")
@RequestMapping("/api/userStatus")
public class UserStatusController {

    private final UserStatusService userStatusService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('userStatus:list')")
    public void exportUserStatus(HttpServletResponse response, UserStatusQueryCriteria criteria) throws IOException {
        userStatusService.download(userStatusService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询用户状态")
    @ApiOperation("查询用户状态")
    @PreAuthorize("@el.check('userStatus:list')")
    public ResponseEntity<Object> queryUserStatus(UserStatusQueryCriteria criteria, Pageable pageable){
        if(pageable.getPageSize() == -1) {
            return new ResponseEntity<>(userStatusService.queryAll(criteria),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(userStatusService.queryAll(criteria,pageable),HttpStatus.OK);
        }
    }

    @PostMapping
    @Log("新增用户状态")
    @ApiOperation("新增用户状态")
    @PreAuthorize("@el.check('userStatus:add')")
    public ResponseEntity<Object> createUserStatus(@Validated @RequestBody UserStatus resources){
        return new ResponseEntity<>(userStatusService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改用户状态")
    @ApiOperation("修改用户状态")
    @PreAuthorize("@el.check('userStatus:edit')")
    public ResponseEntity<Object> updateUserStatus(@Validated @RequestBody UserStatus resources){
        userStatusService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除用户状态")
    @ApiOperation("删除用户状态")
    @PreAuthorize("@el.check('userStatus:del')")
    public ResponseEntity<Object> deleteUserStatus(@RequestBody Long[] ids) {
        userStatusService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}