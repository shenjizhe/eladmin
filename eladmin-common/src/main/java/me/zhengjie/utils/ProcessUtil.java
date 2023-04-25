package me.zhengjie.utils;

import java.io.IOException;

/**
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/24 13:27
 */
public class ProcessUtil {
    public ProcessUtil() {
    }

    public Integer execute(String command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = null;
        process = pb.start();
        int exitCode = process.waitFor();
        return exitCode;
    }
}
