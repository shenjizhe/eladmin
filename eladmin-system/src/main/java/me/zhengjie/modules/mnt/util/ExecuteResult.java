package me.zhengjie.modules.mnt.util;

import lombok.Data;

/**
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/6 14:37
 */
@Data
public class ExecuteResult {
    private String command;
    private Integer success;
    private String result;
}
