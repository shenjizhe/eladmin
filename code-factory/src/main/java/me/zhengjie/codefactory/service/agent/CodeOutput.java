package me.zhengjie.codefactory.service.agent;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/21 13:08
 */
@Data
@JSONType
public class CodeOutput implements Serializable {
    @JSONField
    private String componentName;
    @JSONField
    private List<String> items;
}
