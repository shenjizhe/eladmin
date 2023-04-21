package me.zhengjie.codefactory.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.Result;
import me.zhengjie.codefactory.service.GitlabService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * gitlab 的接口
 *
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/11 17:05
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "代码仓库")
@RequestMapping("/api/gitlab")
public class GitlabController {
    private final GitlabService gitlabService;

    @Log("创建项目")
    @ApiOperation("创建项目")
    @GetMapping(value = "/create-project")
    @PreAuthorize("@el.check('component:add')")
    public Result createProject(Long componentId) throws IOException {
        return gitlabService.createProject(componentId);
    }

    @Log("上传代码")
    @ApiOperation("上传代码")
    @GetMapping(value = "/push-code")
    @PreAuthorize("@el.check('component:add')")
    public Result pushCode(Long componentId) throws IOException {
        return gitlabService.pushCode(componentId);
    }

    @Log("拉取代码")
    @ApiOperation("拉取代码")
    @GetMapping(value = "/pull-code")
    @PreAuthorize("@el.check('component:add')")
    public void pullCode(Long componentId) throws IOException {

    }
}
