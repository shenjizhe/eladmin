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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
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

    private List<Morpheme> all;
    private Map<Long, Morpheme> morphemeMap = new LinkedHashMap<>();
    private Map<Long,WordDetail> wordDetailMap = new LinkedHashMap<>();
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

    private void saveAll() {
        Morpheme morpheme = all.get(morphemeIndex);
        Word word = wordsMap.get(morpheme.getId()).get(wordIndex);
        LocalDate now = LocalDate.now();
        saveUserStatus(morpheme, word);
        saveStudyEvent(morpheme, word, now);
        saveStudyDay(morpheme, word, now);
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

        saveAll();
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

        saveAll();
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

        saveAll();
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
        List<Morpheme> all = studyRecordDayRepository.findMorphemes(uid, DateUtil.getTimestamp(date));
        return all;
    }

    @Override
    public List<Word> getNewWords(Long uid, LocalDate date) {
        List<Word> all = studyRecordDayRepository.findAllWords(uid, DateUtil.getTimestamp(date));
        return all;
    }

    @Override
    public StudyRecord getNewDatas(Long uid, LocalDate date) {
        StudyRecord studyRecord = new StudyRecord();
        studyRecord.setDate(date);

        List<Morpheme> morphemes = studyRecordDayRepository.findMorphemes(uid, DateUtil.getTimestamp(date));
        studyRecord.setMorphemes(morphemes);

        List<Word> words = studyRecordDayRepository.findAllWords(uid, DateUtil.getTimestamp(date));
        List<WordDetail> details = new ArrayList<>();
        for (int i = 0; i < words.size(); i++) {
            WordDetail detail = wordDetailMap.get(words.get(i));
            details.add(detail);
        }
        studyRecord.setWords(details);

        return studyRecord;
    }

    @Override
    public List<Morpheme> getReviewMorphemes(Long uid, LocalDate today) {
        List<Morpheme> morphemes = new ArrayList<>();
// 计算正常日期（今天 - 上次日期 >= level , level != 999）

        List<Long> list = studyRecordDayRepository.morphemeNeedToReview(uid, today);
        for (int i = 0; i < list.size(); i++) {
            morphemes.add(morphemeMap.get(list.get(i)));
        }
// 概率计算
        return morphemes;
    }

    @Override
    public StudyMorphemeStatics reviewMorpheme(Long uid, LocalDate today, Long morphemeId, int eventType) {
//        TODO: 记录 study event

//        计算等级和概率
//        记录统计数据
        return null;
    }

    @Override
    public List<WordDetail> getReviewWords(Long uid, LocalDate today) {
        List<WordDetail> words = new ArrayList<>();
// 计算正常日期（今天 - 上次日期 >= level , level != 999）

        List<Long> list = studyRecordDayRepository.wordNeedToReview(uid, today);
        for (int i = 0; i < list.size(); i++) {
            words.add(wordDetailMap.get(list.get(i)));
        }
// 概率计算
        return words;
    }

    @Override
    public StudyWordStatics reviewWord(Long uid, LocalDate today, int eventType, int eventType1) {
        return null;
    }
}