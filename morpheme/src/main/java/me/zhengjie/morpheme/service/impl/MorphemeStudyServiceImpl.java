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
package me.zhengjie.morpheme.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.domain.*;
import me.zhengjie.morpheme.repository.*;
import me.zhengjie.morpheme.service.MorphemeStudyService;
import me.zhengjie.utils.DateUtil;
import me.zhengjie.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Jason Shen
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2023-05-24
 **/
@Service
@RequiredArgsConstructor
public class MorphemeStudyServiceImpl implements MorphemeStudyService {
    private static final Logger logger = LoggerFactory.getLogger(MorphemeStudyServiceImpl.class);
    private final MorphemeRepository morphemeRepository;
    private final DifferentMorphemeRepository differentMorphemeRepository;
    private final WordRepository wordRepository;
    private final UserStatusRepository userStatusRepository;
    private final WordDeductionRepository wordDeductionRepository;
    private final WordMeaningRepository wordMeaningRepository;
    private final StudyEventRepository studyEventRepository;
    private final StudyRecordDayRepository studyRecordDayRepository;
    private final StudyMorphemeStaticsRepository studyMorphemeStaticsRepository;
    private final StudyWordStaticsRepository studyWordStaticsRepository;
    private final StudyAffixStaticRepository studyAffixStaticRepository;
    private final WordAffixRepository wordAffixRepository;
    private final AffixDeductionRelationRepository affixDeductionRelationRepository;
    private final RedisUtils redisUtils;

    private final int[] DICT_LEVELS = new int[]{0, 1, 2, 4, 7, 15, 30, 999};

    private List<Morpheme> all;
    private Map<Long, Morpheme> morphemeMap = new LinkedHashMap<>();
    private Map<Long, WordDetail> wordDetailMap = new LinkedHashMap<>();
    private Map<Long, WordAffix> affixMap = new LinkedHashMap<>();

    private UserStatus currentUser;
    private Map<Long, List<DifferentMorpheme>> differsMap = new LinkedHashMap<>();
    private Map<Long, List<Word>> wordsMap = new LinkedHashMap<>();

    private Map<Long, List<WordDeduction>> deductionMap = new LinkedHashMap<>();
    private Map<Long, List<WordMeaning>> meaningsMap = new LinkedHashMap<>();

    private int morphemeIndex = -1;
    private int wordIndex = -1;

    @PostConstruct
    private void init() {
        //TODO: 只有调试接口时为true，必须是 false才可以
        boolean quickStart = false;
        if (!quickStart) {
            loadMorpheme();
            loadAffix();
        }
    }

    private void getUserStatus(Long uid) {
        UserStatus userStatus = userStatusRepository.findOneByUserId(uid);
        if (userStatus == null) {
            currentUser = new UserStatus();
            currentUser.setUserId(uid);

            Morpheme first = all.get(0);
            currentUser.setMorphemeId(first.getId());
            List<Word> words = wordsMap.get(first.getId());
            currentUser.setWordId(words.get(0).getId());
            userStatusRepository.save(currentUser);
            morphemeIndex = 0;
            wordIndex = 0;
        } else {
            currentUser = userStatus;
            morphemeIndex = indexOfMorpheme(currentUser.getMorphemeId());
            wordIndex = indexOfWord(currentUser.getMorphemeId(), currentUser.getWordId());
        }
    }

    private void setMorpheme(Long morphemeId) {
        List<DifferentMorpheme> differs = differentMorphemeRepository.getByMorphemeId(morphemeId);
        List<Word> words = wordRepository.getByMorphemeId(morphemeId);

        differsMap.put(morphemeId, differs);
        wordsMap.put(morphemeId, words);

        for (int i = 0; i < words.size(); i++) {
            Word word = words.get(i);
            setWord(word.getId());
            WordDetail wordDetail = getWordDetail(word);
            wordDetailMap.put(word.getId(), wordDetail);
        }
    }

    public void printProgressBar(int current, int total) {
        StringBuilder progressBar = new StringBuilder();
        int progress = (int) (current * 100.0 / total);
        int filledBlocks = progress / 2;
        for (int i = 0; i < filledBlocks; i++) {
            progressBar.append("█");
        }
        for (int i = filledBlocks; i < 50; i++) {
            progressBar.append(" ");
        }
        logger.info("[{}%] [{}]", progress, progressBar);
    }

    private void setWord(Long wordId) {
        List<WordDeduction> deductions = wordDeductionRepository.getByWordId(wordId);
        List<WordMeaning> meanings = wordMeaningRepository.getByWordId(wordId);

        deductionMap.put(wordId, deductions);
        meaningsMap.put(wordId, meanings);
    }

    public void loadMorpheme() {
        differsMap.clear();
        wordsMap.clear();
        morphemeMap.clear();

        all = morphemeRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            Morpheme morpheme = all.get(i);
            setMorpheme(morpheme.getId());
            morphemeMap.put(morpheme.getId(), morpheme);

            printProgressBar(i + 1, all.size());
        }
    }

    private void loadAffix() {
        affixMap.clear();
        List<WordAffix> affixes = wordAffixRepository.findAll();
        for (int i = 0; i < affixes.size(); i++) {
            WordAffix affix = affixes.get(i);
            affixMap.put(affix.getId(), affix);
        }
    }

    private int indexOfMorpheme(Long morphemeId) {
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(morphemeId)) {
                return i;
            }
        }
        return -1;
    }

    private int indexOfWord(Long morphemeId, Long wordId) {
        List<Word> words = wordsMap.get(morphemeId);

        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).getId().equals(wordId)) {
                return i;
            }
        }
        return -1;
    }

    private Boolean isFirstWord() {
        return wordIndex == 0;
    }

    private Boolean isFirstMorpheme() {
        return morphemeIndex == 0;
    }

    private Boolean isLastWord() {
        Morpheme morpheme = all.get(morphemeIndex);
        List<Word> words = wordsMap.get(morpheme.getId());

        return wordIndex == words.size() - 1;
    }

    private Boolean isLastMorpheme() {
        return morphemeIndex == all.size() - 1;
    }

    @Override
    public MorphemeStudy currentMorpheme() {
        Morpheme morpheme = all.get(morphemeIndex);
        MorphemeStudy morphemeStudy = new MorphemeStudy();
        morphemeStudy.copy(morpheme);
        List<DifferentMorpheme> differentMorphemes = differsMap.get(morpheme.getId());
        morphemeStudy.setItems(differentMorphemes);
        morphemeStudy.setIndex(morphemeIndex);

        return morphemeStudy;
    }

    private void saveAll(Long uid) {
        Morpheme morpheme = all.get(morphemeIndex);
        Word word = wordsMap.get(morpheme.getId()).get(wordIndex);
        LocalDate now = LocalDate.now();
        saveUserStatus(morpheme, word);
        saveStudyEvent(morpheme, word, now);
        saveStudyDay(uid, morpheme, word, now);
    }

    private void saveStudyMorphemeStatics(Long uid, LocalDate now, Long id) {
        String key = getUserStaticsKey(uid, now);
        StudyMorphemeStatics statics = new StudyMorphemeStatics();
        statics.setUid(uid);
        statics.setObjectId(id);
        long count = studyMorphemeStaticsRepository.count(Example.of(statics));
        if (count == 0) {
            redisUtils.hincr(key, Const.MORPHEME_NEW, 1.0);
            initStatics(statics, now);
            studyMorphemeStaticsRepository.save(statics);
        } else {
            redisUtils.hincr(key, Const.MORPHEME_OLD, 1.0);
        }
    }

    private void saveStudyWordStatics(Long uid, LocalDate now, Long id) {
        String key = getUserStaticsKey(uid, now);
        StudyWordStatics statics = new StudyWordStatics();
        statics.setUid(uid);
        statics.setObjectId(id);
        long count = studyWordStaticsRepository.count(Example.of(statics));
        if (count == 0) {
            redisUtils.hincr(key, Const.WORD_NEW, 1.0);
            initStatics(statics, now);
            studyWordStaticsRepository.save(statics);
        } else {
            redisUtils.hincr(key, Const.WORD_OLD, 1.0);
        }
    }

    private void saveUserStatus(Morpheme morpheme, Word word) {
        currentUser.setWordId(word.getId());
        currentUser.setMorphemeId(morpheme.getId());
        userStatusRepository.save(currentUser);
    }

    private void saveStudyDay(Long uid, Morpheme morpheme, Word word, LocalDate date) {
        if (!saveDataDay(uid, date, 0, morpheme.getId())) {
            saveStudyMorphemeStatics(uid, date, morpheme.getId());
        }
        if (!saveDataDay(uid, date, 1, word.getId())) {
            saveStudyWordStatics(uid, date, word.getId());
        }
    }

    private Boolean saveDataDay(Long uid, LocalDate today, int objectType, Long objectId) {
        Boolean contains = containsStudyItemToday(uid, today, objectType, objectId);
        if (!contains) {
            StudyRecordDay example = new StudyRecordDay();
            example.setUid(uid);
            example.setObjectType(objectType);
            example.setDate(today);
            example.setObjectId(objectId);
            example.setType(null);
            Optional<StudyRecordDay> one = studyRecordDayRepository.findOne(Example.of(example));
            if (!one.isPresent()) {
                StudyRecordDay search = new StudyRecordDay();
                search.copy(example);
                search.setDate(null);
                long count = studyRecordDayRepository.count(Example.of(search));
                example.setType(count > 0 ? 1 : 0);
                studyRecordDayRepository.save(example);
            }
        }
        return contains;
    }

    private Boolean containsStudyItemToday(Long uid, LocalDate today, int objectType, Long objectId) {
        String key = getStudyTodayKey(uid, today, objectType, objectId);
        boolean contains = redisUtils.hasKey(key);
        if (!contains) {
            Long seconds = DateUtil.seconds2Tomorrow();
            redisUtils.set(key, null, seconds);
        }
        return contains;
    }

    private static String getStudyTodayKey(Long uid, LocalDate today, int objectType, Long objectId) {
        return "study-today::" + uid + "-" + today + "-" + objectType + "-" + objectId;
    }

    private void saveStudyEvent(Morpheme morpheme, Word word, LocalDate date) {
        StudyEvent studyEvent = new StudyEvent(StudyEvent.EventType.Study);
        studyEvent.setTime(DateUtil.getTimestamp(date));
        studyEvent.setUid(currentUser.getUserId());
        studyEvent.setWordId(word.getId());
        studyEvent.setMorphemeId(morpheme.getId());
        studyEventRepository.save(studyEvent);
    }

    @Override
    public MorphemeStudy previousMorpheme() {
        if (morphemeIndex > 0) {
            morphemeIndex--;
            return currentMorpheme();
        }
        return null;
    }

    @Override
    public MorphemeStudy nextMorpheme() {
        if (morphemeIndex < all.size() - 1) {
            morphemeIndex++;
            return currentMorpheme();
        }
        return null;
    }

    @Override
    public WordDetail currentWord() {
        Morpheme morpheme = all.get(morphemeIndex);
        List<Word> words = wordsMap.get(morpheme.getId());
        Word word = words.get(wordIndex);
        WordDetail wordDetail = wordDetailMap.get(word.getId());
        wordDetail.setIndex(wordIndex);
        return wordDetail;
    }

    private WordDetail getWordDetail(Word word) {
        WordDetail wordDetail = new WordDetail();
        wordDetail.copy(word);

        List<WordDeduction> wordDeductions = deductionMap.get(word.getId());
        List<WordMeaning> wordMeanings = meaningsMap.get(word.getId());
        wordDetail.setDeductions(wordDeductions);
        wordDetail.setMeanings(wordMeanings);
        return wordDetail;
    }

    @Override
    public WordDetail nextWord() {
        Morpheme morpheme = all.get(morphemeIndex);
        List<Word> words = wordsMap.get(morpheme.getId());
        if (wordIndex < words.size() - 1) {
            wordIndex++;
            return currentWord();
        }
        return null;
    }

    @Override
    public WordDetail previousWord() {
        if (wordIndex > 0) {
            wordIndex--;
            return currentWord();
        }
        return null;
    }

    @Override
    public MorphemePair current(Long uid) {
        getUserStatus(uid);
        MorphemeStudy morphemeStudy = currentMorpheme();
        WordDetail word = currentWord();

        saveAll(uid);
        return new MorphemePair(morphemeStudy, word);
    }

    @Override
    public MorphemePair next(Long uid) {
        MorphemeStudy morpheme;
        WordDetail word;
        getUserStatus(uid);
        if (isLastWord()) {
            morpheme = nextMorpheme();
            wordIndex = 0;
            word = currentWord();
        } else {
            morpheme = currentMorpheme();
            word = nextWord();
        }

        saveAll(uid);
        return new MorphemePair(morpheme, word);
    }

    @Override
    public MorphemePair previous(Long uid) {
        MorphemeStudy morpheme;
        WordDetail word;
        getUserStatus(uid);
        if (isFirstWord()) {
            morpheme = previousMorpheme();
            List<Word> words = wordsMap.get(morpheme.getId());
            wordIndex = words.size() - 1;
            word = currentWord();
        } else {
            morpheme = currentMorpheme();
            word = previousWord();
        }

        saveAll(uid);
        return new MorphemePair(morpheme, word);
    }

    @Override
    public Boolean isFirst(Long uid) {
        getUserStatus(uid);
        Boolean firstWord = isFirstWord();
        Boolean firstMorpheme = isFirstMorpheme();

        return firstWord && firstMorpheme;
    }

    @Override
    public Boolean isLast(Long uid) {
        getUserStatus(uid);
        Boolean lastWord = isLastWord();
        Boolean lastMorpheme = isLastMorpheme();

        return lastWord && lastMorpheme;
    }

    @Override
    public List<Morpheme> getNewMorphemes(Long uid, LocalDate date) {
        StudyRecordDay studyRecordDay = new StudyRecordDay();
        studyRecordDay.setUid(uid);
        studyRecordDay.setDate(date);
        studyRecordDay.setObjectType(0);
        studyRecordDay.setType(0);

        List<StudyRecordDay> list = studyRecordDayRepository.findAll(Example.of(studyRecordDay));
        List<Morpheme> all = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Morpheme morpheme = morphemeMap.get(list.get(i).getObjectId());
            all.add(morpheme);
        }
        return all;
    }

    @Override
    public List<WordDetail> getNewWords(Long uid, LocalDate date) {
        StudyRecordDay studyRecordDay = new StudyRecordDay();
        studyRecordDay.setUid(uid);
        studyRecordDay.setDate(date);
        studyRecordDay.setObjectType(1);
        studyRecordDay.setType(0);

        List<StudyRecordDay> list = studyRecordDayRepository.findAll(Example.of(studyRecordDay));
        List<WordDetail> all = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            WordDetail word = wordDetailMap.get(list.get(i).getObjectId());
            all.add(word);
        }
        return all;
    }

    @Override
    public StudyRecord getNewDatas(Long uid, LocalDate date, Boolean shuffle) {
        StudyRecord studyRecord = new StudyRecord();
        studyRecord.setDate(date);
        List<Morpheme> morphemes = getNewMorphemes(uid, date);
        studyRecord.setMorphemes(morphemes);
        List<WordDetail> words = getNewWords(uid, date);
        if (shuffle) {
            Collections.shuffle(words);
        }

        studyRecord.setWords(words);

        return studyRecord;
    }

    @Override
    public List<Morpheme> getReviewMorphemes(Long uid, LocalDate today, Boolean shuffle) {
        List<Long> list = studyMorphemeStaticsRepository.morphemeNeedToReview(uid, today);
        list.addAll(morerpheme999ToReview(uid, today));
        return (List<Morpheme>) shuffleList(morphemeMap, shuffle, list);
    }

    @Override
    public List<WordDetail> getReviewWords(Long uid, LocalDate today, Boolean shuffle) {
        List<WordDetail> words = new ArrayList<>();
        List<Long> list = studyWordStaticsRepository.wordNeedToReview(uid, today);
        list.addAll(word999ToReview(uid, today));
        return (List<WordDetail>) shuffleList(wordDetailMap, shuffle, list);
    }

    private List<Long> morerpheme999ToReview(Long uid, LocalDate today) {
        List<Long> list = studyMorphemeStaticsRepository.morerpheme999ToReview(uid, today);
        List<Long> morphemes = object999ToReview(list, 10);
        list.addAll(morphemes);
        return list;
    }

    private List<Long> word999ToReview(Long uid, LocalDate today) {
        List<Long> list = studyWordStaticsRepository.word999ToReview(uid, today);
        List<Long> words = object999ToReview(list, 80);
        list.addAll(words);
        return list;
    }

    private List<Long> affix999ToReview(Long uid, LocalDate today) {
        List<Long> list = studyAffixStaticRepository.affix999ToReview(uid, today);
        List<Long> affixes = object999ToReview(list, 10);
        list.addAll(affixes);
        return list;
    }

    private List<Long> affixFirstReview(Long uid) {
        List<Long> list = studyAffixStaticRepository.affixFirstReview(uid);
        List<Long> affixes = object999ToReview(list, 10);
        return affixes;
    }

    private List<Long> object999ToReview(List<Long> list, int rate) {
        List<Long> objects = new ArrayList<>();
        if (list.size() > 0) {
            int count = list.size() / rate;
            if (count < 1) {
                count = 1;
            }
            for (long l = 0; l < count; ) {
                int index = (int) (Math.random() * list.size());
                Long v = list.get(index);
                if (!objects.contains(v)) {
                    objects.add(v);
                    l++;
                }
            }
        }

        return objects;
    }

    private int newLevel(int oldLevel, int eventType) {
        if (oldLevel == 0) {
            switch (eventType) {
                case 1:
                    return 2;
                case 2:
                case 3:
                    return 1;
            }
        }
        int index = 0;
        for (int i = 0; i < DICT_LEVELS.length; i++) {
            if (oldLevel == DICT_LEVELS[i]) {
                index = i;
                break;
            }
        }

        if (eventType == 1) {
            index += 2;
            if (index > DICT_LEVELS.length - 1) {
                index = DICT_LEVELS.length - 1;
            }
        } else if (eventType == 2) {
            if (index > 2) {
                index -= 1;
            }
        } else if (eventType == 3) {
            if (index < 3) {
                return 1;
            } else {
                index -= 2;
            }
        }
        return DICT_LEVELS[index];
    }

    @Override
    public StudyMorphemeStatics reviewMorpheme(Long uid, LocalDate today, Long morphemeId, int eventType) {
        return (StudyMorphemeStatics) getStatics(studyMorphemeStaticsRepository, uid, today, morphemeId, eventType, StudyMorphemeStatics.class);
    }

    @Override
    public StudyWordStatics reviewWord(Long uid, LocalDate today, Long wordId, int eventType) {
        return (StudyWordStatics) getStatics(studyWordStaticsRepository, uid, today, wordId, eventType, StudyWordStatics.class);
    }

    @Override
    public List<WordDetail> searchWord(String text) {
        List<WordDetail> list = new ArrayList<>();
        if (!text.trim().isEmpty()) {
            List<Word> words = wordRepository.searchWord(text);
            for (int i = 0; i < words.size(); i++) {
                WordDetail detail = wordDetailMap.get(words.get(i).getId());
                list.add(detail);
            }
        }
        return list;
    }

    @Override
    public List<Morpheme> searchMorpheme(String text) {
        if (!text.trim().isEmpty()) {
            List<Morpheme> morphemes = morphemeRepository.searchMorpheme(text);
            return morphemes;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<WordDetail> searchWordsByMorpheme(Long morphemeId) {
        List<WordDetail> list = new ArrayList<>();
        List<Word> words = wordRepository.searchWordByMorphemeId(morphemeId);
        for (int i = 0; i < words.size(); i++) {
            WordDetail detail = wordDetailMap.get(words.get(i).getId());
            list.add(detail);
        }
        return list;
    }

    @Override
    public UserStaticsByDay getTodayStatics(Long uid, LocalDate today) {
        String key = getUserStaticsKey(uid, today);
        if (redisUtils.hasKey(key)) {
            long expire = redisUtils.getExpire(key);
            if (expire < 0) {
                redisUtils.expire(key, Const.SECONDS_ONE_DAY);
            }
            Map<Object, Object> map = redisUtils.hmget(key);
            return new UserStaticsByDay(uid, today, map);

        } else {
            UserStaticsByDay statics = new UserStaticsByDay(uid, today);
            Map<String, Object> map = statics.getMap();
            redisUtils.hmset(key, map, Const.SECONDS_ONE_DAY);
            return statics;
        }
    }

    private String getUserStaticsKey(Long uid, LocalDate today) {
        return "user-statics::" + uid + "-" + today.toString();
    }

    private StudyStaticsBase getStatics(JpaRepository jpa, Long uid, LocalDate today, Long objectId, int eventType, Class<? extends StudyStaticsBase> cls) {
        StudyStaticsBase search = null;
        boolean isMorpheme = cls == StudyMorphemeStatics.class;
        try {
            search = cls.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        search.setUid(uid);
        search.setObjectId(objectId);
        Example<StudyStaticsBase> of = Example.of(search);
        Optional<StudyStaticsBase> one = jpa.findOne(of);
        StudyStaticsBase staticOne = null;
        if (one.isPresent()) {
            staticOne = one.get();
        } else {
            try {
                staticOne = cls.getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            staticOne.setUid(uid);
            staticOne.setObjectId(objectId);
            initStatics(staticOne, today);
        }

        int level = newLevel(staticOne.getMemeryLevel(), eventType);
        staticOne.setMemeryLevel(level);
        String key = getUserStaticsKey(uid, today);

        switch (eventType) {
            case 1:
                redisUtils.hincr(key, isMorpheme ? Const.MORPHEME_SIMPLE : Const.WORD_SIMPLE, 1.0);
                Integer simpleTimes = staticOne.getSimpleTimes();
                staticOne.setSimpleTimes(simpleTimes + 1);
                break;
            case 2:
                redisUtils.hincr(key, isMorpheme ? Const.MORPHEME_CONFUSE : Const.WORD_CONFUSE, 1.0);
                Integer confuseTimes = staticOne.getConfuseTimes();
                staticOne.setConfuseTimes(confuseTimes + 1);
                break;
            case 3:
                redisUtils.hincr(key, isMorpheme ? Const.MORPHEME_FORGET : Const.WORD_FORGET, 1.0);
                Integer forgetTimes = staticOne.getForgetTimes();
                staticOne.setForgetTimes(forgetTimes + 1);
                break;
            default:
                break;
        }
        Integer studyTimes = staticOne.getStudyTimes();
        staticOne.setStudyTimes(studyTimes + 1);
        staticOne.setLastReviewTime(DateUtil.getTimestamp(today));
        staticOne.setLastReviewResult(eventType);

        jpa.save(staticOne);

        return staticOne;
    }

    private static void initStatics(StudyStaticsBase staticOne, LocalDate today) {
        staticOne.setMemeryLevel(0);
        staticOne.setSimpleTimes(0);
        staticOne.setConfuseTimes(0);
        staticOne.setForgetTimes(0);
        staticOne.setStudyTimes(0);
        staticOne.setLastReviewTime(DateUtil.getTimestamp(today));
        staticOne.setLastReviewResult(0);
    }

    @Override
    public int buildAllAffix() {
        List<WordDeduction> deductions = wordDeductionRepository.getAffixDeductions();
        Map<String, Long> map = new LinkedHashMap();

        for (int i = 0; i < deductions.size(); i++) {
            WordDeduction deduction = deductions.get(i);
            String key = deduction.getKey();
            if (!map.containsKey(key)) {
                WordAffix affix = new WordAffix(deduction);
                WordAffix save = wordAffixRepository.save(affix);
                map.put(key, save.getId());
            }
            Long id = map.get(key);
            AffixDeductionRelation relation = new AffixDeductionRelation();
            relation.setAffixId(id);
            relation.setDeductionId(deduction.getId());

            affixDeductionRelationRepository.save(relation);
        }

        return map.size();
    }

    @Override
    public List<WordAffix> getReviewAffixes(Long uid, LocalDate today, Boolean shuffle) {
        List<Long> list = affixFirstReview(uid);
        list.addAll(studyAffixStaticRepository.affixNeedToReview(uid, today));
        list.addAll(affix999ToReview(uid, today));
        return (List<WordAffix>) shuffleList(affixMap, shuffle, list);
    }

    private List shuffleList(Map map, Boolean shuffle, List<Long> list) {
        List results = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            results.add(map.get(list.get(i)));
        }

        if (shuffle) {
            Collections.shuffle(results);
        }
        return results;
    }

    @Override
    public StudyAffixStatic reviewAffix(Long uid, LocalDate today, Long affixId, int eventType) {
        return (StudyAffixStatic) getStatics(studyAffixStaticRepository, uid, today, affixId, eventType, StudyAffixStatic.class);
    }

    @Override
    public List<WordDetail> findWordsByAffixId(Long affixId) {
        List<Long> words = wordRepository.findWordsByAffixId(affixId);

        List<WordDetail> collect = words.stream().map(id -> wordDetailMap.get(id)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public int rebuildDeduction() {
        WordDeduction deduction = new WordDeduction();
        deduction.setIsDerive(true);
        List<WordDeduction> list = wordDeductionRepository.findAll(Example.of(deduction));

        for (int i = 0; i < list.size(); i++) {
            WordDeduction d = list.get(i);
            WordMeaning meaning = new WordMeaning();
            try {
                Word word = wordRepository.findRootWord(d.getSourceText(), d.getWordId());
                if (word == null) {
                    d.setMeaningChinese("");
                    d.setMeaningEnglish("");
                    wordDeductionRepository.save(d);
                } else {
                    meaning.setWordId(word.getId());
                    List<WordMeaning> items = wordMeaningRepository.findAll(Example.of(meaning));
                    if (items.size() > 0) {
                        WordMeaning one = items.get(0);
                        d.setMeaningChinese(one.getMeaningChinese());
                        d.setMeaningEnglish(one.getMeaningEnglish());
                        wordDeductionRepository.save(d);
                    }
                }
            } catch (Exception ex) {
                logger.error("取得推导: {} ,单词 {} 单词失败：{}", d.getId(), d.getWordId(), d.getSourceText());
            }
        }
        return 0;
    }

    @Override
    public int rebuildDeductionPrefix() {
        return rebuildDeductionByType(1);
    }

    @Override
    public int rebuildDeductionSuffix() {
        return rebuildDeductionByType(2);
    }

    @Override
    public int rebuildDeductionMorpheme() {
        return rebuildDeductionByType(0);
    }

    private int rebuildDeductionByType(int type) {
        WordDeduction deduction = new WordDeduction();
        deduction.setAffix(type);
        deduction.setIsDerive(false);
        List<WordDeduction> list = wordDeductionRepository.findAll(Example.of(deduction));

        for (int i = 0; i < list.size(); i++) {
            WordDeduction d = list.get(i);
            List<Meaning> l = null;
            if (type == 1 || type == 2) {
                l = getMeaningAffixes(d);
            } else if (type == 0) {
                l = getMeaningMorpheme(d);
            }

            if (l.size() > 0) {
                Meaning one = l.get(0);
                d.setMeaningChinese(one.getMeaningChinese());
                d.setMeaningEnglish(one.getMeaningEnglish());
                wordDeductionRepository.save(d);
            } else {
                logger.error("取得推导: {} ,单词 {} 词缀失败：{}", d.getId(), d.getWordId(), d.getSourceText());
            }
        }
        return 0;
    }

    private List<Meaning> getMeaningMorpheme(WordDeduction d) {
        Morpheme morpheme = new Morpheme();
        morpheme.setId(d.getMorphemeId());
        List<Meaning> morphemes = morphemeRepository.findAll(Example.of(morpheme)).stream().collect(Collectors.toList());
        return morphemes;
    }

    private List<Meaning> getMeaningAffixes(WordDeduction d) {
        WordAffix affix = new WordAffix();
        affix.setText(d.getSourceText());
        affix.setAffix(d.getAffix());
        List<Meaning> affixes = wordAffixRepository.findAll(Example.of(affix)).stream().collect(Collectors.toList());
        return affixes;
    }
}