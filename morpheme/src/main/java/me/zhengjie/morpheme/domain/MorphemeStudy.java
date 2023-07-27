package me.zhengjie.morpheme.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MorphemeStudy {
    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "字典ID")
    private Long dictionaryId;

    @ApiModelProperty(value = "编号")
    private Integer number;

    @ApiModelProperty(value = "词素文本")
    private String text;

    @ApiModelProperty(value = "词素类型")
    private Integer type;

    @ApiModelProperty(value = "词源")
    private String source;

    @ApiModelProperty(value = "中文含义")
    private String meaningChinese;

    @ApiModelProperty(value = "英文含义")
    private String meaningEnglish;

    @ApiModelProperty(value = "说明")
    private String description;

    @ApiModelProperty(value = "相关子项目")
    private List<DifferentMorpheme> items;

    @ApiModelProperty(value = "索引")
    private int index;

    public void copy(Morpheme source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
