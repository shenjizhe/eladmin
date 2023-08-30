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

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import me.zhengjie.morpheme.domain.*;
import me.zhengjie.morpheme.repository.*;
import me.zhengjie.morpheme.service.MorphemeStudyService;
import me.zhengjie.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;

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

    private final int[] DICT_LEVELS = new int[]{0, 1, 2, 4, 7, 15, 30, 999};

    private List<Morpheme> all;
    private Map<Long, Morpheme> morphemeMap = new LinkedHashMap<>();
    private Map<Long, WordDetail> wordDetailMap = new LinkedHashMap<>();
    private UserStatus currentUser;
    private Map<Long, List<DifferentMorpheme>> differsMap = new LinkedHashMap<>();
    private Map<Long, List<Word>> wordsMap = new LinkedHashMap<>();

    private Map<Long, List<WordDeduction>> deductionMap = new LinkedHashMap<>();
    private Map<Long, List<WordMeaning>> meaningsMap = new LinkedHashMap<>();

    private int morphemeIndex = -1;
    private int wordIndex = -1;

    @PostConstruct
    private void init() {
        loadMorpheme();
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
        saveStudyDay(morpheme, word, now);

        saveStudyMorphemeStatics(uid, now, morpheme.getId());
        saveStudyWordStatics(uid, now, word.getId());
    }

    private void saveStudyMorphemeStatics(Long uid, LocalDate now, Long id) {
        StudyMorphemeStatics statics = new StudyMorphemeStatics();
        statics.setUid(uid);
        statics.setObjectId(id);
        long count = studyMorphemeStaticsRepository.count(Example.of(statics));
        if (count == 0) {
            initStatics(statics, now);
            studyMorphemeStaticsRepository.save(statics);
        }
    }

    private void saveStudyWordStatics(Long uid, LocalDate now, Long id) {
        StudyWordStatics statics = new StudyWordStatics();
        statics.setUid(uid);
        statics.setObjectId(id);
        long count = studyWordStaticsRepository.count(Example.of(statics));
        if (count == 0) {
            initStatics(statics, now);
            studyWordStaticsRepository.save(statics);
        }
    }

    private void saveUserStatus(Morpheme morpheme, Word word) {
        currentUser.setWordId(word.getId());
        currentUser.setMorphemeId(morpheme.getId());
        userStatusRepository.save(currentUser);
    }

    private void saveStudyDay(Morpheme morpheme, Word word, LocalDate date) {
        saveDataDay(date, 0, morpheme, word);
        saveDataDay(date, 1, morpheme, word);
    }

    private void saveDataDay(LocalDate today, int objectType, Morpheme morpheme, Word word) {
        StudyRecordDay example = new StudyRecordDay();
        example.setUid(currentUser.getUserId());
        example.setObjectType(objectType);
        example.setDate(DateUtil.getTimestamp(today));
        example.setObjectId(objectType == 0 ? morpheme.getId() : word.getId());
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
        StudyMorphemeStatics example = new StudyMorphemeStatics();
        example.setUid(uid);
        example.setStudyTimes(0);

        List<StudyMorphemeStatics> list = studyMorphemeStaticsRepository.findAll(Example.of(example));
        List<Morpheme> all = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Morpheme morpheme = morphemeMap.get(list.get(i).getObjectId());
            all.add(morpheme);
        }
        return all;
    }

    @Override
    public List<WordDetail> getNewWords(Long uid, LocalDate date) {
        StudyWordStatics example = new StudyWordStatics();
        example.setUid(uid);
        example.setStudyTimes(0);

        List<StudyWordStatics> list = studyWordStaticsRepository.findAll(Example.of(example));
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
        List<Morpheme> morphemes = new ArrayList<>();
        List<Long> list = studyMorphemeStaticsRepository.morphemeNeedToReview(uid, today);
        for (int i = 0; i < list.size(); i++) {
            morphemes.add(morphemeMap.get(list.get(i)));
        }

        if (shuffle) {
            Collections.shuffle(morphemes);
        }
        return morphemes;
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

    private StudyStaticsBase getStatics(JpaRepository jpa, Long uid, LocalDate today, Long objectId, int eventType, Class<? extends StudyStaticsBase> cls) {
        StudyStaticsBase search = null;
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
        switch (eventType) {
            case 1:
                Integer simpleTimes = staticOne.getSimpleTimes();
                staticOne.setSimpleTimes(simpleTimes + 1);
                break;
            case 2:
                Integer confuseTimes = staticOne.getConfuseTimes();
                staticOne.setConfuseTimes(confuseTimes + 1);
                break;
            case 3:
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
    public List<WordDetail> getReviewWords(Long uid, LocalDate today, Boolean shuffle) {
        List<WordDetail> words = new ArrayList<>();
        List<Long> list = studyWordStaticsRepository.wordNeedToReview(uid, today);
        for (int i = 0; i < list.size(); i++) {
            words.add(wordDetailMap.get(list.get(i)));
        }

        if (shuffle) {
            Collections.shuffle(words);
        }
        return words;
    }

    @Override
    public StudyWordStatics reviewWord(Long uid, LocalDate today, Long wordId, int eventType) {
        return (StudyWordStatics) getStatics(studyWordStaticsRepository, uid, today, wordId, eventType, StudyWordStatics.class);
    }
}