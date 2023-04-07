package me.zhengjie.utils;

import lombok.Data;

/**
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/7 9:14
 */
@Data
public class SshKeyPair{
    private String privateKey;
    private String publicKey;
}
