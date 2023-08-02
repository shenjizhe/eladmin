package me.zhengjie.morpheme.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.morpheme.domain.MorphemePair;
import me.zhengjie.morpheme.service.MorphemeStudyService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "词根学习")
@RequestMapping("/api/morpheme-study")
public class MorphemeStudyController {
    private final MorphemeStudyService morphemeStudyService;

    @Log("当前版本")
    @ApiOperation("当前版本")
    @GetMapping(value = "/version")
    @PreAuthorize("@el.check('morpheme:list')")
    public String version() {
        //TODO: 每次改变需要更新版本
        return "v1.0.0";
    }
    @Log("当前")
    @ApiOperation("当前")
    @GetMapping(value = "/morpheme-current")
    @PreAuthorize("@el.check('morpheme:list')")
    public MorphemePair curretnMorphemeStudy() {
        Long uid = SecurityUtils.getCurrentUserId();
        return morphemeStudyService.current(uid);
    }

    @Log("下一条")
    @ApiOperation("下一条")
    @GetMapping(value = "/morpheme-next")
    @PreAuthorize("@el.check('morpheme:list')")
    public MorphemePair nextnMorphemeStudy() {
        Long uid = SecurityUtils.getCurrentUserId();
        return morphemeStudyService.next(uid);
    }

    @Log("上一条")
    @ApiOperation("上一条")
    @GetMapping(value = "/morpheme-previous")
    @PreAuthorize("@el.check('morpheme:list')")
    public MorphemePair lastnMorphemeStudy() {
        Long uid = SecurityUtils.getCurrentUserId();
        return morphemeStudyService.previous(uid);
    }

    @Log("是否是第一条")
    @ApiOperation("是否是第一条")
    @GetMapping(value = "/is-first")
    @PreAuthorize("@el.check('morpheme:list')")
    public Boolean isFirst() {
        Long uid = SecurityUtils.getCurrentUserId();
        return morphemeStudyService.isFirst(uid);
    }
    @Log("是否是最后一条")
    @ApiOperation("是否是最后一条")
    @GetMapping(value = "/is-last")
    @PreAuthorize("@el.check('morpheme:list')")
    public Boolean isLast() {
        Long uid = SecurityUtils.getCurrentUserId();
        return morphemeStudyService.isLast(uid);
    }
}
