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
package me.zhengjie.utils;

import cn.hutool.core.io.IoUtil;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

/**
 * 执行shell命令
 *
 * @author: ZhangHouYing
 * @date: 2019/8/10
 */
@Slf4j
public class ExecuteShellUtil {

    private Vector<String> stdout;

    Session session;

    public static ExecuteShellUtil createByPassword(final String ipAddress, int port, final String username, final String password){
        return new ExecuteShellUtil(ipAddress, port, username, password, null, null);
    }

    public static ExecuteShellUtil createByPK(final String ipAddress, int port, final String username, final String privateKey){
        return new ExecuteShellUtil(ipAddress, port, username, null, privateKey, null);
    }

    public ExecuteShellUtil(final String ipAddress, int port, final String username, final String password,final String privateKey,final String publicKey){
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, ipAddress, port);

            session.setConfig("StrictHostKeyChecking", "no");

            if(privateKey != null && !privateKey.trim().isEmpty()){
                try {
                    // 禁用密码身份验证
                    session.setConfig("PreferredAuthentications", "publickey");
                    jsch.addIdentity("code-factory",privateKey.getBytes(),publicKey==null?null:publicKey.getBytes(),null);
                    session.connect(3000);
                }
                catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }else{
                session.setPassword(password);
                session.connect(3000);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public Boolean register(String publicKey) {
        final String command = "echo '" + publicKey + "' >> ~/.ssh/authorized_keys\n";
        final String s = executeResults(command);
        return true;
    }

    public String executeResults(String command) {
        ChannelExec channel = null;
        command = formatCommand(command);
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] buffer = new byte[1024];
            StringBuilder output = new StringBuilder();
            while (true) {
                int bytesRead = in.read(buffer);
                if (bytesRead < 0) {
                    break;
                }
                output.append(new String(buffer, 0, bytesRead));
            }
            String s = output.toString();
            if(s.endsWith("\n")) {
                s = s.substring(0,s.length()-1);
            }
            return s;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

    private String formatCommand(String command) {
        StringBuilder builder = new StringBuilder();
        command = command.replace(";", "\n");

        final String[] split = command.split("\n");
        for (int i = 0; i < split.length; i++) {
            if (!split[i].isEmpty()) {
                builder.append(split[i] + "\n");
            }
        }
        String s = builder.toString();
        return s;
    }

    public int execute(final String command) {
        int returnCode = 0;
        ChannelShell channel = null;
        PrintWriter printWriter = null;
        BufferedReader input = null;
        stdout = new Vector<String>();
        try {
            channel = (ChannelShell) session.openChannel("shell");
            channel.connect();
            input = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            printWriter = new PrintWriter(channel.getOutputStream());
            printWriter.println(command);
            printWriter.println("exit");
            printWriter.flush();
            log.info("The remote command is: ");
            String line;
            while ((line = input.readLine()) != null) {
                stdout.add(line);
                System.out.println(line);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return -1;
        } finally {
            IoUtil.close(printWriter);
            IoUtil.close(input);
            if (channel != null) {
                channel.disconnect();
            }
        }
        return returnCode;
    }

    public void close() {
        if (session != null) {
            session.disconnect();
        }
    }

    public String executeForResult(String command) {
        execute(command);
        StringBuilder sb = new StringBuilder();
        for (String str : stdout) {
            sb.append(str);
        }
        return sb.toString();
    }

    public String executeResult(String command) {
        return executeResults(command);
    }

    public static void main(String[] args) {
        final SshKeyPair sshKeyPair = SshUtil.keyGen();
        final ExecuteShellUtil passUtil = ExecuteShellUtil.createByPassword("192.168.2.218",15120,"root","licai0803");
        passUtil.register(sshKeyPair.getPublicKey());

        final ExecuteShellUtil util = ExecuteShellUtil.createByPK("192.168.2.218",15120,"root",sshKeyPair.getPrivateKey());
    }
}
