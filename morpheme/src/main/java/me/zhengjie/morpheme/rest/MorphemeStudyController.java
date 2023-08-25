package me.zhengjie.morpheme.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.morpheme.domain.Morpheme;
import me.zhengjie.morpheme.domain.MorphemePair;
import me.zhengjie.morpheme.domain.StudyRecord;
import me.zhengjie.morpheme.domain.Word;
import me.zhengjie.morpheme.service.MorphemeStudyService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "词根学习")
@RequestMapping("/api/morpheme-study")
public class MorphemeStudyController {
    private final MorphemeStudyService morphemeStudyService;

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
    @Log("取得今天学习的词根")
    @ApiOperation("取得今天学习的词根")
    @GetMapping(value = "/morpheme-today")
    @PreAuthorize("@el.check('morpheme:list')")
    public List<Morpheme> getNewMorphemes(@RequestParam(value = "date",required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd")
                                  LocalDate date) {
        Long uid = SecurityUtils.getCurrentUserId();
        if(date == null){
            date = LocalDate.now();
        }
        return morphemeStudyService.getNewMorphemes(uid, date);
    }

    @Log("取得今天学习的单词")
    @ApiOperation("取得今天学习的单词")
    @GetMapping(value = "/word-today")
    @PreAuthorize("@el.check('morpheme:list')")
    public List<Word> getNewWords(@RequestParam(value = "date",required = false)
                                      @DateTimeFormat(pattern = "yyyy-MM-dd")
                                      LocalDate date) {
        Long uid = SecurityUtils.getCurrentUserId();
        if(date == null){
            date = LocalDate.now();
        }
        return morphemeStudyService.getNewWords(uid,date);
    }

    @Log("取得今天所学知识")
    @ApiOperation("取得今天所学知识")
    @GetMapping(value = "/study-today")
    @PreAuthorize("@el.check('morpheme:list')")
    public StudyRecord getNewDatas(@RequestParam(value = "date",required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd")
                                  LocalDate date) {
        Long uid = SecurityUtils.getCurrentUserId();
        if(date == null){
            date = LocalDate.now();
        }
        return morphemeStudyService.getNewDatas(uid,date);
    }
}
