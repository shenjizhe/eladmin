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
import me.zhengjie.codefactory.service.agent.CodeFactoryAgent;
import me.zhengjie.codefactory.service.agent.CodeOutput;
import me.zhengjie.codefactory.service.agent.CodeOutputItem;
import org.gitlab4j.api.*;
import org.gitlab4j.api.models.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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
    private final CodeFactoryAgent agent;

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
        Namespace namespace = new Namespace();
//        namespace.setName(Const.ConfigKey.GIT_NAMESPACE);
        Project projectSettings = new Project()
                .withName(component.getName())
//                .withNamespace(namespace)
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
    public Result pushCode(Long componentId) {
        final Component component = componentRepository.getById(componentId);
        final ProjectApi projectApi = gitLabApi.getProjectApi();
        final CommitsApi commitsApi = gitLabApi.getCommitsApi();
        try {
            final String name = component.getName();
            final List<Project> projects = projectApi.getProjects();
            Project project = null;
            for (Project pro : projects) {
                final String pname = pro.getNamespace().getName();
                if ("gitlab".equals(pname) || name.equals(pro.getName())) {
                    project = pro;
                    break;
                }
            }
//            Project project = projectApi.getProject("gitlab", name);

            final String generate = agent.generate(12L);
            final CodeOutput output = agent.output(generate);

            final List<String> items = output.getItems();
            List<CommitAction> actions = new ArrayList<>();

            for (String uuid : items) {
                CodeOutputItem item = agent.outputItem(uuid);
                if (item.getSavePath() != null) {
                    actions.add(
                            new CommitAction()
                                    .withAction(CommitAction.Action.CREATE)
                                    .withFilePath(item.getFullPath())
                                    .withContent(item.getCode()));
                }
            }
            Commit commit = gitLabApi.getCommitsApi().createCommit(
                    project,
                    "master",
                    "initial",
                    null,
                    null,
                    null,
                    actions);


            return Result.success(commit);
        } catch (GitLabApiException e) {
            e.printStackTrace();
            return Result.exception(ErrorCode.GitlabError, e);
        }
    }

    @Override
    public Result pushProject(Long componentId) {
        final Result project = createProject(componentId);
        if(project.isSuccess()){
            final Result result = pushCode(componentId);
            return result;
        }else{
            return project;
        }
    }
}
