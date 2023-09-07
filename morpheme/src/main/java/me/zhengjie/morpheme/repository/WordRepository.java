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
            "\tw.id, \n" +
            "\tw.text, \n" +
            "\tw.deduction, \n" +
            "\tw.nature, \n" +
            "\tw.is_derive, \n" +
            "\tw.description, \n" +
            "\tw.phonetic\n" +
            "FROM\n" +
            "\tword_morpheme_relation AS r\n" +
            "\tINNER JOIN\n" +
            "\tmorpheme AS m\n" +
            "\tON \n" +
            "\t\tr.morpheme_id = m.id\n" +
            "\tINNER JOIN\n" +
            "\tword AS w\n" +
            "\tON \n" +
            "\t\tr.word_id = w.id\n" +
            "WHERE\n" +
            "\tm.id = :morphemeId",
            nativeQuery = true
    )
    List<Word> getByMorphemeId(@Param("morphemeId") Long morphemeId);

    @Query(value = "SELECT * FROM word WHERE text LIKE CONCAT('%', :text, '%')", nativeQuery = true)
    List<Word> searchWord(String text);

    @Query(value = "SELECT w.* FROM word w INNER JOIN word_morpheme_relation wmr on w.id=wmr.word_id where wmr.morpheme_id=:morphemeId", nativeQuery = true)
    List<Word> searchWordByMorphemeId(Long morphemeId);
}