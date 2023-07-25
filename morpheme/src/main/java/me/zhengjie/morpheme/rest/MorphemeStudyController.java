package me.zhengjie.morpheme.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.morpheme.domain.MorphemeStudy;
import me.zhengjie.morpheme.service.MorphemeService;
import me.zhengjie.morpheme.service.MorphemeStudyService;
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
    @Log("当前词根")
    @ApiOperation("当前词根")
    @GetMapping(value = "/morpheme-current")
    @PreAuthorize("@el.check('morpheme:list')")
    public MorphemeStudy curretnMorphemeStudy() {
        return morphemeStudyService.current();
    }

    @Log("当前词根")
    @ApiOperation("当前词根")
    @GetMapping(value = "/morpheme-next")
    @PreAuthorize("@el.check('morpheme:list')")
    public MorphemeStudy nextnMorphemeStudy() {
        return morphemeStudyService.next();
    }

    @Log("当前词根")
    @ApiOperation("当前词根")
    @GetMapping(value = "/morpheme-previous")
    @PreAuthorize("@el.check('morpheme:list')")
    public MorphemeStudy lastnMorphemeStudy() {
        return morphemeStudyService.previous();
    }
}
