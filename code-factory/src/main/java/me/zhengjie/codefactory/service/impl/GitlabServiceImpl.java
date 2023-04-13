package me.zhengjie.codefactory.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.base.Const;
import me.zhengjie.base.ErrorCode;
import me.zhengjie.base.Result;
import me.zhengjie.codefactory.domain.Component;
import me.zhengjie.codefactory.domain.Config;
import me.zhengjie.codefactory.repository.ComponentRepository;
import me.zhengjie.codefactory.repository.ConfigRepository;
import me.zhengjie.codefactory.service.GitlabService;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.ProjectApi;
import org.gitlab4j.api.UserApi;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.SshKey;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/11 17:28
 */
@Service
@RequiredArgsConstructor
public class GitlabServiceImpl implements GitlabService {

    private final ConfigRepository configRepository;
    private final ComponentRepository componentRepository;
    private GitLabApi gitLabApi;
    private String namespace;

    @PostConstruct
    void init() {
        final Config token = configRepository.findByKey(Const.ConfigKey.GIT_LAB);
        final Config url = configRepository.findByKey(Const.ConfigKey.GIT_URL);
        namespace = configRepository.findByKey(Const.ConfigKey.GIT_NAMESPACE).getValue();
        gitLabApi = new GitLabApi(url.getValue(), token.getValue());

        final Config byKey = configRepository.findByKey(Const.ConfigKey.SSH_PUBLIC_KEY);
        try {
            final UserApi userApi = gitLabApi.getUserApi();
            if (!containKey(userApi, Const.Name.SSH_TITLE)) {
                setSshkey(userApi, Const.Name.SSH_TITLE, byKey.getValue());
            }
        } catch (GitLabApiException e) {
            e.printStackTrace();
        }
    }

    private Boolean containKey(UserApi userApi, String keyTitle) throws GitLabApiException {
        for (SshKey key : userApi.getSshKeys()) {
            if (key.getTitle().equals(Const.Name.SSH_TITLE)) {
                return true;
            }
        }
        return false;
    }

    private void setSshkey(UserApi userApi, String keyTitle, String content) throws GitLabApiException {
        userApi.addSshKey(Const.Name.SSH_TITLE, content);
    }

    @Override
    public Result createProject(Long componentId) {
        final Component component = componentRepository.getById(componentId);
        Project projectSettings = new Project()
                .withName(component.getName())
                .withDescription(component.getComment())
                .withIssuesEnabled(true)
                .withMergeRequestsEnabled(true)
                .withWikiEnabled(true)
                .withSnippetsEnabled(true)
                .withPublic(false);
        try {
            Project newProject = gitLabApi.getProjectApi().createProject(projectSettings);
            return Result.success(newProject);
        } catch (GitLabApiException e) {
            return Result.exception(ErrorCode.GitlabError, e);
        }
    }

    @Override
    public Result pushCode(Long componentId, String path) {
        final Component component = componentRepository.getById(componentId);
        final ProjectApi projectApi = gitLabApi.getProjectApi();

        try {
            Project project = projectApi.getProject(namespace, component.getName());
            return Result.fail(ErrorCode.Fail);
        } catch (GitLabApiException e) {
            e.printStackTrace();
            return Result.exception(ErrorCode.GitlabError, e);
        }
    }
}
