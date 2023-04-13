/*
 * Copyright 2021-2099 the original author or authors.
 *
 * @File: error-code.vm
 * @Author: JasonShen
 * @Date: 2021-09-15 11:47:26
 * @Email: thirdlucky@126.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.zhengjie.base;

/** 2 * @Author: JasonShen 3 * @Date: 2020/8/12 14:16 4 */
public enum ErrorCode {
    /*
    00????. 默认错误( 预留 )
    */
    Success(0x00000000, "成功"),
    Fail(0xffffffff, "失败"),
    Unknown(0x00000001, "未知"),

    /*
    01????.系统错误
    */
    ArgumentError(0x010001, "参数错误"),
    AppIDError(0x010002, "app id不存在"),
    DiscardError(0x010003, "接口已经被废弃"),
    InterfaceNotFound(0x010004, "接口不存在"),

    ServerError(0x010101, "服务器状态异常"),
    DBError(0x010102, "数据库状态异常"),

    TokenError(0x010201, "token失效"),
    AuthError(0x010202, "没有权限"),
    RefuseError(0x010203, "访问被拒绝"),
    TimeoutError(0x010204, "请求超时"),

    NotExistResource(0x010301, "资源不存在"),
    NotExistUser(0x010302, "用户不存在"),
    DuplicateResource(0x010303, "资源重复"),

    ShellNotFound(0x010401,"shell 脚本无法执行"),
    ShellFail(0x010402,"shell 脚本执行失败"),

  /*
  02???? 依赖服务错误
  */

    /*
    03???? 三方服务错误
    */
    AppIdError(0x030001, "App Id 错误"),
    AppKeyError(0x030002, "App key 错误"),
    AppSecurityError(0x030003, "App security 错误"),
    StateError(0x030004, "依赖的服务错误"),
    AlreadyUsed(0x030005, "已经被使用"),
    /** 手机验证码服务的错误 */
    SmsCodeNotExpire(0x00110001, "原验证码未过期"),
    SmsCodeExpire(0x00110002, "校验码过期"),
    SmsCodeNotExist(0x00110003, "无此校验码"),
    SmsCodeSaveFail(0x00110004, "保存验证码失败"),
    SmsCodeSendFail(0x00110005, "发送验证码失败"),
    SmsCodeGenerateErrorNotFound(0x00110006, "生成验证码失败，原因未知"),
    SmsCodeCheckErrorNotFound(0x00110007, "校验验证码失败，原因未知"),

    /*
    09???? 三方服务错误
    */
    VelocityError(0x090001, "velocity 执行错误"),
    /*
   10???? gitlab 错误
   */
    GitlabError(0x100001, "gitlab 执行错误"),
    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /** @return the code */
    public int getCode() {
        return code;
    }

    /** @param code the code to set */
    public void setCode(int code) {
        this.code = code;
    }

    /** @return the message */
    public String getMessage() {
        return message;
    }

    /** @param message the message to set */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 结果是否正确
     *
     * @return 是否正确
     */
    public Boolean isSuccess() {
        return this.code == Success.code;
    }
}
