package me.zhengjie.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/7 9:02
 */
public class SshUtil {
    public static SshKeyPair keyGen() {
        JSch jsch = new JSch();
        KeyPair keyPair = null;
        try {
            keyPair = KeyPair.genKeyPair(jsch, KeyPair.RSA);
            final SshKeyPair sshKeyPair = new SshKeyPair();

            OutputStream outputStream = new ByteArrayOutputStream();
            keyPair.writePublicKey(outputStream, "code-factory");
            StringBuilder sb = new StringBuilder();
            sb.append(outputStream);
            sshKeyPair.setPublicKey(sb.toString());

            outputStream = new ByteArrayOutputStream();
            keyPair.writePrivateKey(outputStream);
            sb = new StringBuilder();
            sb.append(outputStream);
            sshKeyPair.setPrivateKey(sb.toString());
            return sshKeyPair;
        } catch (JSchException e) {
            e.printStackTrace();
            return null;
        }finally {
            if(keyPair != null){
                keyPair.dispose();
            }
        }
    }

    public static void main(String[] args) {
        final SshKeyPair sshKeyPair = SshUtil.keyGen();
    }
}
