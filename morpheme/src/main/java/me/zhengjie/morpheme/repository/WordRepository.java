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
package me.zhengjie.morpheme.repository;

import me.zhengjie.morpheme.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Jason Shen
 * @website https://eladmin.vip
 * @date 2023-05-24
 **/
public interface WordRepository extends JpaRepository<Word, Long>, JpaSpecificationExecutor<Word> {
    @Query(value = "SELECT\n" +
            "\tw.*\n" +
            "FROM\n" +
            "\tword_morpheme_relation r\n" +
            "\tINNER JOIN morpheme m ON r.morpheme_id = m.id\n" +
            "\tINNER JOIN word w ON r.word_id = w.id \n" +
            "WHERE\n" +
            "\tm.id = :morphememId",
            nativeQuery = true
    )
    List<Word> getByMorphemeId(@Param("morphemeId") Long morphemeId);
}