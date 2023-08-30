package me.zhengjie.morpheme.rest;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.morpheme.domain.*;
import me.zhengjie.morpheme.service.MorphemeStudyService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Path;
import java.time.LocalDate;
import java.util.Collections;
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
    public List<Morpheme> getNewMorphemes(@RequestParam(value = "date", required = false)
                                          @DateTimeFormat(pattern = "yyyy-MM-dd")
                                          LocalDate date) {
        Long uid = SecurityUtils.getCurrentUserId();
        if (date == null) {
            date = LocalDate.now();
        }
        return morphemeStudyService.getNewMorphemes(uid, date);
    }

    @Log("取得今天学习的单词")
    @ApiOperation("取得今天学习的单词")
    @GetMapping(value = "/word-today")
    @PreAuthorize("@el.check('morpheme:list')")
    public List<WordDetail> getNewWords(@RequestParam(value = "date", required = false)
                                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                                        LocalDate date) {
        Long uid = SecurityUtils.getCurrentUserId();
        if (date == null) {
            date = LocalDate.now();
        }
        return morphemeStudyService.getNewWords(uid, date);
    }

    @Log("取得今天所学知识")
    @ApiOperation("取得今天所学知识")
    @GetMapping(value = "/study-today")
    @PreAuthorize("@el.check('morpheme:list')")
    public StudyRecord getNewDatas(@RequestParam(value = "date", required = false)
                                   @DateTimeFormat(pattern = "yyyy-MM-dd")
                                   LocalDate date,
                                   @RequestParam(value = "shuffle", required = false, defaultValue = "true")
                                   Boolean shuffle) {
        Long uid = SecurityUtils.getCurrentUserId();
        if (date == null) {
            date = LocalDate.now();
        }
        return morphemeStudyService.getNewDatas(uid, date, shuffle);
    }

    @Log("查询今天该复习的词根")
    @ApiOperation("查询今天该复习的词根")
    @GetMapping(value = "/review-morphemes")
    @PreAuthorize("@el.check('morpheme:list')")
    public List<Morpheme> getReviewMorphemes(
            @RequestParam(value = "shuffle", required = false, defaultValue = "true")
            Boolean shuffle) {
        Long uid = SecurityUtils.getCurrentUserId();
        LocalDate today = LocalDate.now();
        return morphemeStudyService.getReviewMorphemes(uid, today, shuffle);
    }

    @Log("复习词根")
    @ApiOperation("复习词根")
    @PostMapping(value = "/review-morphemes/{morpheme-id}/{event-type}")
    @PreAuthorize("@el.check('morpheme:list')")
    public StudyMorphemeStatics reviewMorpheme(
            @PathVariable("morpheme-id") Long morphemeId,
            @PathVariable("event-type") int eventType) {
        Long uid = SecurityUtils.getCurrentUserId();
        LocalDate today = LocalDate.now();
        return morphemeStudyService.reviewMorpheme(uid, today, morphemeId, eventType);
    }

    @Log("查询今天该复习的单词")
    @ApiOperation("查询今天该复习的单词")
    @GetMapping(value = "/review-words")
    @PreAuthorize("@el.check('morpheme:list')")
    public List<WordDetail> getReviewWords(
            @RequestParam(value = "shuffle", required = false, defaultValue = "true")
            Boolean shuffle) {
        Long uid = SecurityUtils.getCurrentUserId();
        LocalDate today = LocalDate.now();
        return morphemeStudyService.getReviewWords(uid, today, shuffle);
    }

    @Log("复习单词")
    @ApiOperation("复习单词")
    @PostMapping(value = "/review-words/{word-id}/{event-type}")
    @PreAuthorize("@el.check('morpheme:list')")
    public StudyWordStatics reviewWord(
            @PathVariable("word-id") Long wordId,
            @PathVariable("event-type") int eventType) {
        Long uid = SecurityUtils.getCurrentUserId();
        LocalDate today = LocalDate.now();
        return morphemeStudyService.reviewWord(uid, today, wordId, eventType);
    }
}
