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
package me.zhengjie.morpheme.service;

import me.zhengjie.morpheme.domain.*;

import java.time.LocalDate;
import java.util.List;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author Jason Shen
* @date 2023-05-24
**/
public interface MorphemeStudyService {
    MorphemeStudy currentMorpheme();

    MorphemeStudy previousMorpheme();

    MorphemeStudy nextMorpheme();

    WordDetail currentWord();

    WordDetail nextWord();

    WordDetail previousWord();

    MorphemePair current(Long uid);

    MorphemePair next(Long uid);

    MorphemePair previous(Long uid);

    Boolean isFirst(Long uid);

    Boolean isLast(Long uid);

    List<Morpheme> getNewMorphemes(Long uid, LocalDate date);

    List<WordDetail> getNewWords(Long uid, LocalDate date);

    StudyRecord getNewDatas(Long uid, LocalDate date,Boolean shuffle);

    List<Morpheme> getReviewMorphemes(Long uid, LocalDate today, Boolean shuffle);

    StudyMorphemeStatics reviewMorpheme(Long uid, LocalDate today, Long morphemeId, int eventType);

    List<WordDetail> getReviewWords(Long uid, LocalDate today, Boolean shuffle);

    StudyWordStatics reviewWord(Long uid, LocalDate today, Long wordId, int eventType);
}