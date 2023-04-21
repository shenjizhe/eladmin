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
    public final class ConfigKey{
        /**
         * git token 的配置项名称
         */
        public static final String GIT_LAB = "git-token";
        public static final String GIT_URL = "git-url";
        public static final String GIT_NAMESPACE = "git-namespace";
        public static final String SSH_PUBLIC_KEY = "ssh-public-key";
        public static final String SSH_PRIVATE_KEY = "ssh-private-key";
        public static final String CODE_FACTORY_URL = "code-factory-url";
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
}
