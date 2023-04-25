package me.zhengjie.codefactory.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import me.zhengjie.base.Const;
import me.zhengjie.base.ErrorCode;
import me.zhengjie.base.Result;
import me.zhengjie.codefactory.domain.Component;
import me.zhengjie.codefactory.domain.Script;
import me.zhengjie.codefactory.repository.ComponentRepository;
import me.zhengjie.codefactory.repository.ScriptRepository;
import me.zhengjie.codefactory.service.*;
import me.zhengjie.utils.ProcessUtil;
import org.gitlab4j.api.models.Project;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/24 12:31
 */
@Service
@RequiredArgsConstructor
public class CICDServiceImpl implements CICDService {
    private ProcessUtil processUtil;
    private final ScriptRepository scriptRepository;
    private final ScriptService scriptService;
    private final ComponentRepository componentRepository;
    private final GitlabService gitlabService;
    private final ConfigService configService;
    private final ServerService serverService;

    @PostConstruct
    private void init() {
        processUtil = new ProcessUtil();
    }

    @Override
    public Result downloadComponent(Long componentId) {
        final Optional<Component> componentOptional = componentRepository.findById(componentId);
        if (componentOptional.isPresent()) {
            final Component component = componentOptional.get();
            final Project project = gitlabService.containProject(component);
            if (project != null) {
                final Script script = scriptRepository.findByKey(Const.ScripyKey.GIT_CLONE);
                Map<String, String> map = new HashMap<>();
                map.put(Const.GitlabKey.PROJECT_NAME_KEY, project.getName());
                // 执行脚本下载代码
                final String scriptText = scriptService.getScriptText(script, map);
                try {
                    final JSONObject json = (JSONObject) configService.getByKey(Const.ConfigKey.CICD_SERVER);
                    final String ip = json.getString(Const.CICD.IP_KEY);
                    final Integer port = json.getInteger(Const.CICD.PORT_KEY);
                    final String username = json.getString(Const.CICD.USER_KEY);
                    final String password = json.getString(Const.CICD.PASSWORD_KEY);

                    final String result = serverService.execute(ip, port, username, password, scriptText);
                    return Result.result(ErrorCode.Success, result);
                } catch (Exception ex) {
                    return Result.exception(ErrorCode.DownloadFail, ex);
                }
            } else {
                return Result.fail(ErrorCode.NotExistResource, componentId);
            }
        } else {
            return Result.fail(ErrorCode.NotExistResource, componentId);
        }
    }
}
