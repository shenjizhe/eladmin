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
import me.zhengjie.morpheme.repository.DifferentMorphemeRepository;
import me.zhengjie.morpheme.repository.MorphemeRepository;
import me.zhengjie.morpheme.repository.UserStatusRepository;
import me.zhengjie.morpheme.repository.WordRepository;
import me.zhengjie.morpheme.service.MorphemeStudyService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jason Shen
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2023-05-24
 **/
@Service
@RequiredArgsConstructor
public class MorphemeStudyServiceImpl implements MorphemeStudyService {
    private final MorphemeRepository morphemeRepository;
    private final DifferentMorphemeRepository differentMorphemeRepository;
    private final WordRepository wordRepository;
    private final UserStatusRepository userStatusRepository;
    private List<Morpheme> all;
    private UserStatus currentUser;
    private Map<Long, List<DifferentMorpheme>> differsMap = new LinkedHashMap<>();
    private Map<Long, List<Word>> wordsMap = new LinkedHashMap<>();

    private int morphemeIndex = -1;
    private int wordIndex = -1;

    @PostConstruct
    private void init() {
        loadMorpheme();
    }

    private void getUserStatus(Long uid){
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
    }

    public void loadMorpheme() {
        differsMap.clear();
        wordsMap.clear();

        all = morphemeRepository.findAll();
        for (int i = 0; i < all.size(); i++) {
            Morpheme morpheme = all.get(i);
            setMorpheme(morpheme.getId());
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
        return morphemeStudy;
    }

    private void saveUserStatus() {
        Morpheme morpheme = all.get(morphemeIndex);
        Word word = wordsMap.get(morphemeIndex).get(wordIndex);
        currentUser.setWordId(word.getId());
        currentUser.setMorphemeId(morpheme.getId());

        userStatusRepository.save(currentUser);
    }

    @Override
    public MorphemeStudy previousMorpheme() {
        if (morphemeIndex > 0) {
            morphemeIndex--;
            saveUserStatus();
            return currentMorpheme();
        }
        return null;
    }

    @Override
    public MorphemeStudy nextMorpheme() {
        if (morphemeIndex < all.size() - 1) {
            morphemeIndex++;
            saveUserStatus();
            return currentMorpheme();
        }
        return null;
    }

    @Override
    public Word currentWord() {
        Morpheme morpheme = all.get(morphemeIndex);
        List<Word> words = wordsMap.get(morpheme.getId());
        return words.get(wordIndex);
    }

    @Override
    public Word nextWord() {
        Morpheme morpheme = all.get(morphemeIndex);
        List<Word> words = wordsMap.get(morpheme.getId());
        if (wordIndex < words.size() - 1) {
            wordIndex++;
            saveUserStatus();
            return currentWord();
        }
        return null;
    }

    @Override
    public Word previousWord() {
        if (wordIndex > 0) {
            wordIndex--;
            saveUserStatus();
            return currentWord();
        }
        return null;
    }

    @Override
    public MorphemePair current(Long uid) {
        getUserStatus(uid);
        MorphemeStudy morphemeStudy = currentMorpheme();
        Word word = currentWord();
        return new MorphemePair(morphemeStudy, word);
    }

    @Override
    public MorphemePair next(Long uid) {
        MorphemeStudy morpheme;
        Word word;

        if (isLastWord()) {
            morpheme = nextMorpheme();
            wordIndex = 0;
            word = currentWord();
        } else {
            morpheme = currentMorpheme();
            word = nextWord();
        }
        return new MorphemePair(morpheme, word);
    }

    @Override
    public MorphemePair previous(Long uid) {
        MorphemeStudy morpheme;
        Word word;

        if (isFirstWord()) {
            morpheme = previousMorpheme();
            List<Word> words = wordsMap.get(morpheme.getId());
            wordIndex = words.size() - 1;
            word = currentWord();
        } else {
            morpheme = currentMorpheme();
            word = previousWord();
        }
        return new MorphemePair(morpheme, word);
    }
}