package me.zhengjie.morpheme.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.morpheme.domain.*;
import me.zhengjie.morpheme.repository.MorphemeRepository;
import me.zhengjie.morpheme.repository.WordRepository;
import me.zhengjie.morpheme.service.MorphemeStudyService;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "词根学习")
@RequestMapping("/api/morpheme-study")
public class MorphemeStudyController {
    private final MorphemeStudyService morphemeStudyService;
    private final WordRepository wordRepository;
    private final MorphemeRepository morphemeRepository;

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

    @Log("查询单词")
    @ApiOperation("查询单词")
    @GetMapping(value = "/search-words/")
    @PreAuthorize("@el.check('morpheme:list')")
    public List<WordDetail> searchWord(
            @RequestParam("text") String text) {
        return morphemeStudyService.searchWord(text);
    }

    @Log("查询词根")
    @ApiOperation("查询词根")
    @GetMapping(value = "/search-morphemes/")
    @PreAuthorize("@el.check('morpheme:list')")
    public List<Morpheme> searchMorphemes(
            @RequestParam("text") String text) {
        return morphemeStudyService.searchMorpheme(text);
    }

    @Log("词根查单词")
    @ApiOperation("词根查单词")
    @GetMapping(value = "/search-word-by-morpheme/{morpheme-id}")
    @PreAuthorize("@el.check('morpheme:list')")
    public List<WordDetail> searchWordsByMorpheme(
            @PathVariable("morpheme-id") Long morphemeId) {
        return morphemeStudyService.searchWordsByMorpheme(morphemeId);
    }

    @Log("查询今天用户的统计")
    @ApiOperation("查询今天用户的统计")
    @GetMapping(value = "/user-statics-today/")
    @PreAuthorize("@el.check('morpheme:list')")
    public UserStaticsByDay getTodayStatics() {
        Long uid = SecurityUtils.getCurrentUserId();
        LocalDate today = LocalDate.now();
        return morphemeStudyService.getTodayStatics(uid,today);
    }

    @PostMapping
    @Log("创建词缀(一次性)")
    @ApiOperation("创建词缀(一次性)")
    @PreAuthorize("@el.check('wordAffix:add')")
    public int builAlldWordAffix(){
        return morphemeStudyService.buildAllAffix();
    }

    @Log("查询今天该复习的词缀")
    @ApiOperation("查询今天该复习的词缀")
    @GetMapping(value = "/review-affixes")
    @PreAuthorize("@el.check('morpheme:list')")
    public List<WordAffix> getReviewAffixes(
            @RequestParam(value = "shuffle", required = false, defaultValue = "true")
            Boolean shuffle) {
        Long uid = SecurityUtils.getCurrentUserId();
        LocalDate today = LocalDate.now();
        return morphemeStudyService.getReviewAffixes(uid, today, shuffle);
    }

    @Log("复习词根")
    @ApiOperation("复习词根")
    @PostMapping(value = "/review-affixes/{affix-id}/{event-type}")
    @PreAuthorize("@el.check('morpheme:list')")
    public StudyMorphemeStatics reviewAffixe(
            @PathVariable("affix-id") Long affixId,
            @PathVariable("event-type") int eventType) {
        Long uid = SecurityUtils.getCurrentUserId();
        LocalDate today = LocalDate.now();
        return morphemeStudyService.reviewAffix(uid, today, affixId, eventType);
    }
}
