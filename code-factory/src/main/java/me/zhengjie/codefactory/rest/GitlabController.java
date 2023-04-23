package me.zhengjie.codefactory.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.base.Result;
import me.zhengjie.codefactory.service.GitlabService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @Log("上传工程")
    @ApiOperation("上传工程")
    @PostMapping(value = "/push-project/{component-id}")
    @PreAuthorize("@el.check('component:add')")
    public Result pushProject(@PathVariable("component-id") Long componentId) throws IOException {
        return gitlabService.pushProject(componentId);
    }

    @Log("创建项目")
    @ApiOperation("创建项目")
    @PostMapping(value = "/create-project/{component-id}")
    @PreAuthorize("@el.check('component:add')")
    public Result createProject(@PathVariable("component-id") Long componentId) throws IOException {
        return gitlabService.createProject(componentId);
    }

    @Log("上传代码")
    @ApiOperation("上传代码")
    @PostMapping(value = "/push-code/{component-id}")
    @PreAuthorize("@el.check('component:add')")
    public Result pushCode(@PathVariable("component-id") Long componentId) throws IOException {
        return gitlabService.pushCode(componentId);
    }

    @Log("拉取代码")
    @ApiOperation("拉取代码")
    @GetMapping(value = "/pull-code/{component-id}")
    @PreAuthorize("@el.check('component:add')")
    public Result pullCode(@PathVariable("component-id") Long componentId) throws IOException {
        return gitlabService.pullCode(componentId);
    }
}
