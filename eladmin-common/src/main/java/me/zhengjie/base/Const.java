package me.zhengjie.base;

/**
 * 公共常量
 *
 * @author Jason Shen
 * @version 1.0
 * @date 2023/4/11 17:31
 */
public final class Const {
    /**
     * 配置 KEY
     */
    public final class ConfigKey {
        /**
         * git token 的配置项名称
         */
        public static final String GIT_LAB = "git-token";
        public static final String GIT_URL = "git-url";
        public static final String GIT_NAMESPACE = "git-namespace";
        public static final String SSH_PUBLIC_KEY = "ssh-public-key";
        public static final String SSH_PRIVATE_KEY = "ssh-private-key";
        public static final String CODE_FACTORY_URL = "code-factory-url";
        public static final String CICD_SERVER = "cicd-server-config";
    }

    /**
     * 脚本 KEY
     */
    public final class ScripyKey {
        public static final String GIT_CLONE = "git-clone";
    }

    /**
     * Gitlab 李的 KEY
     */
    public final class GitlabKey {
        public static final String PROJECT_NAME_KEY = "project-name";
    }

    /**
     * 系统内的名称
     */
    public final class Name {
        /**
         * ssh 的 title
         */
        public static final String SSH_TITLE = "code-factory";
    }

    public final class CICD {
        public static final String IP_KEY = "ip";
        public static final String PORT_KEY = "port";
        public static final String USER_KEY = "user";
        public static final String PASSWORD_KEY = "password";
    }
}
