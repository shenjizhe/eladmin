package me.zhengjie.codefactory.service;

import me.zhengjie.base.Result;
import me.zhengjie.codefactory.domain.Component;
import org.gitlab4j.api.models.Project;

/**
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/11 17:27
 */
public interface GitlabService {
    Result createProject(Long componentId);

    Result pushCode(Long componentId);

    Result pushProject(Long componentId);

    Project containProject(Component component);
}
