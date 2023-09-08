package me.zhengjie.morpheme.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author DD2020002
 */
@Data
public class WordAffixBase implements Serializable {
    private String text;
    private Integer affix;
}
