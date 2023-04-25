package me.zhengjie.codefactory.service;

import me.zhengjie.base.Result;

/**
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/24 12:30
 */
public interface CICDService {
    Result downloadComponent(Long componentId);
}
