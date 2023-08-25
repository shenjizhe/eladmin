package me.zhengjie.modules.security.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/system")
@Api(tags = "系统基本信息")
public class SystemController {
    @Log("当前版本")
    @ApiOperation("当前版本")
    @GetMapping(value = "/version")
//    @PreAuthorize("@el.check('morpheme:list')")
    public String version() {
        //TODO: 每次改变需要更新版本
        return "v1.0.0";
    }

    @Log("获取用户ID")
    @ApiOperation("获取用户ID")
    @GetMapping(value = "/uuid")
    public Long uuid() {
        //TODO: 每次改变需要更新版本
        return SecurityUtils.getCurrentUserId();
    }
}
