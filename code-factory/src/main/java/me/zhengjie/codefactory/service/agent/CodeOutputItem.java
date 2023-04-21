package me.zhengjie.codefactory.service.agent;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

/**
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/21 17:34
 */
@Data
@JSONType
public class CodeOutputItem {
    @JSONField
    private String savePath;
    @JSONField
    private String fileName;
    @JSONField
    private String code;

    public String getFullPath() {
        return savePath + fileName;
    }
}
