package me.zhengjie.morpheme.domain;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户学习日统计
 *
 * @author Jason shen
 */
@Data
public class UserStaticsByDay implements Serializable {
    private Long uid;
    private LocalDate date;
    private Integer morphemeNew = 0;
    private Integer morphemeOld = 0;
    private Integer morphemeSimple = 0;
    private Integer morphemeConfuse = 0;
    private Integer morphemeForget = 0;
    private Integer wordNew = 0;
    private Integer wordOld = 0;
    private Integer wordSimple = 0;
    private Integer wordConfuse = 0;
    private Integer wordForget = 0;

    public Map<String, Object> getMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(Const.MORPHEME_NEW, morphemeNew);
        map.put(Const.MORPHEME_OLD, morphemeOld);
        map.put(Const.MORPHEME_SIMPLE, morphemeSimple);
        map.put(Const.MORPHEME_CONFUSE, morphemeConfuse);
        map.put(Const.MORPHEME_FORGET, morphemeForget);

        map.put(Const.WORD_NEW, wordNew);
        map.put(Const.WORD_OLD, wordOld);
        map.put(Const.WORD_SIMPLE, wordSimple);
        map.put(Const.WORD_CONFUSE, wordConfuse);
        map.put(Const.WORD_FORGET, wordForget);

        return map;
    }

    public UserStaticsByDay(Long uid, LocalDate date) {
        this.uid = uid;
        this.date = date;
    }

    public UserStaticsByDay() {
    }

    public UserStaticsByDay(Long uid, LocalDate date, Map<Object, Object> map) {
        this.uid = uid;
        this.date = date;
        this.morphemeNew = (Integer) map.get(Const.MORPHEME_NEW);
        this.morphemeOld = (Integer) map.get(Const.MORPHEME_OLD);
        this.morphemeSimple = (Integer) map.get(Const.MORPHEME_SIMPLE);
        this.morphemeConfuse = (Integer) map.get(Const.MORPHEME_CONFUSE);
        this.morphemeForget = (Integer) map.get(Const.MORPHEME_FORGET);
        this.wordNew = (Integer) map.get(Const.WORD_NEW);
        this.wordOld = (Integer) map.get(Const.WORD_OLD);
        this.wordSimple = (Integer) map.get(Const.WORD_SIMPLE);
        this.wordConfuse = (Integer) map.get(Const.WORD_CONFUSE);
        this.wordForget = (Integer) map.get(Const.WORD_FORGET);
    }
}
