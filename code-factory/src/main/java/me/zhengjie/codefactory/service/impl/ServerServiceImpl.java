/*
 *  Copyright 2019-2020 Jason Shen
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.codefactory.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.codefactory.domain.Config;
import me.zhengjie.codefactory.domain.Script;
import me.zhengjie.codefactory.domain.Server;
import me.zhengjie.codefactory.repository.ConfigRepository;
import me.zhengjie.codefactory.repository.ScriptRepository;
import me.zhengjie.codefactory.repository.ServerRepository;
import me.zhengjie.codefactory.service.ScriptService;
import me.zhengjie.codefactory.service.ServerService;
import me.zhengjie.codefactory.service.dto.ServerDto;
import me.zhengjie.codefactory.service.dto.ServerQueryCriteria;
import me.zhengjie.codefactory.service.mapstruct.ServerMapper;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Jason Shen
 * @website https://eladmin.vip
 * @description 服务实现
 * @date 2023-04-10
 **/
@Service
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;
    private final ServerMapper serverMapper;
    private final ScriptRepository scriptRepository;
    private final ScriptService scriptService;
    private final ConfigRepository configRepository;

    @Override
    public Map<String, Object> queryAll(ServerQueryCriteria criteria, Pageable pageable) {
        Page<Server> page = serverRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(serverMapper::toDto));
    }

    @Override
    public List<ServerDto> queryAll(ServerQueryCriteria criteria) {
        return serverMapper.toDto(serverRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public ServerDto findById(Long id) {
        Server server = serverRepository.findById(id).orElseGet(Server::new);
        ValidationUtil.isNull(server.getId(), "Server", "id", id);
        return serverMapper.toDto(server);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerDto create(Server resources) {
        return serverMapper.toDto(serverRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Server resources) {
        Server server = serverRepository.findById(resources.getId()).orElseGet(Server::new);
        ValidationUtil.isNull(server.getId(), "Server", "id", resources.getId());
        server.copy(resources);
        serverRepository.save(server);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            serverRepository.deleteById(id);
        }
    }

    @Override
    public String execute(Long id, Long scriptId) {
        final Optional<Server> serverOpt = serverRepository.findById(id);
        final Optional<Script> scriptOpt = scriptRepository.findById(scriptId);
        return execute(serverOpt.get(), scriptOpt.get());
    }

    @Override
    public String execute(Long id, String key) {
        final Optional<Server> serverOpt = serverRepository.findById(id);
        final Script script = scriptRepository.findByKey(key);
        return execute(serverOpt.get(), script);
    }

    @Override
    public Boolean copyFile(Long id, String path, String content) {
        final Optional<Server> serverOpt = serverRepository.findById(id);
        return copy(serverOpt.get(), path, content);
    }

    @Override
    public Boolean copyFileByKey(Long id, String path, String configKey) {
        final Config config = configRepository.findByKey(configKey);
        final Optional<Server> serverOpt = serverRepository.findById(id);
        return copy(serverOpt.get(), path, config.getValue());
    }

    public String execute(Server deploy, Script script) {
        if (deploy != null && script != null) {
            final String scriptText = scriptService.getScriptText(script);
            ExecuteShellUtil executeShellUtil = ExecuteShellUtil.createByPassword(deploy.getIp(), deploy.getPort(), deploy.getAccount(), deploy.getPassword());
            return executeShellUtil.forceExecute(scriptText);
        }
        return null;
    }

    @Override
    public String execute(String ip, Integer port, String username, String password, String command) {
        ExecuteShellUtil executeShellUtil = ExecuteShellUtil.createByPassword(ip, port, username, password);
        return executeShellUtil.forceExecute(command);
    }

    public Boolean copy(Server deploy, String filePath, String content) {
        if (deploy != null) {
            ExecuteShellUtil executeShellUtil = ExecuteShellUtil.createByPassword(deploy.getIp(), deploy.getPort(), deploy.getAccount(), deploy.getPassword());
            return executeShellUtil.forcecopyToFile(filePath, content);
        }
        return false;
    }

    @Override
    public void download(List<ServerDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ServerDto server : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("账号", server.getAccount());
            map.put("IP地址", server.getIp());
            map.put("名称", server.getName());
            map.put("密码", server.getPassword());
            map.put("私钥", server.getRsa());
            map.put("公钥", server.getPub());
            map.put("系统", server.getSystem());
            map.put("系统版本", server.getVersion());
            map.put("端口", server.getPort());
            map.put("进度", server.getStep());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}