/*
 * Copyright 2021-2099 the original author or authors.
 *
 * @File: Result.java
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

/** 通用结果 @Author: JasonShen @Date: 2020/8/12 14:12 */
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 结果对象 success boolean 是否成功 errCode int 错误码（0：成功） msg String 消息
 *
 * @author JasonShen
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private int errCode;
    private String message;
    private T data;

    /**
     * 是否成功
     *
     * @return 是否成功
     */
    public Boolean isSuccess() {
        return errCode == ErrorCode.Success.getCode();
    }

    /**
     * 结果
     *
     * @param code 编码
     * @param <T> 返回泛型
     * @return 结果
     */
    public static <T> Result<T> result(ErrorCode code) {
        return of(code.getCode(), code.getMessage(), null);
    }

    /**
     * 结果
     *
     * @param code 编码
     * @param data 数据
     * @param <T> 返回泛型
     * @return 结果
     */
    public static <T> Result<T> result(ErrorCode code, T data) {
        return of(code.getCode(), code.getMessage(), data);
    }

    /**
     * 成功
     *
     * @param message 消息
     * @param data 数据
     * @param <T> 返回泛型
     * @return 结果
     */
    public static <T> Result<T> success(String message, T data) {
        return of(ErrorCode.Success.getCode(), message, data);
    }

    /**
     * 成功
     *
     * @param message 消息
     * @param <T> 返回泛型
     * @return 结果
     */
    public static <T> Result<T> success(String message) {
        return success(message, null);
    }

    /**
     * 成功
     *
     * @param <T> 返回泛型
     * @return 结果
     */
    public static <T> Result<T> success() {
        return success(ErrorCode.Success.getMessage(), null);
    }

    /**
     * 成功
     *
     * @param data 数据
     * @param <T> 返回泛型
     * @return 结果
     */
    public static <T> Result<T> success(T data) {
        return success(ErrorCode.Success.getMessage(), data);
    }

    /**
     * 失败
     *
     * @param errCode 编码
     * @param message 消息
     * @param <T> 返回泛型
     * @return 结果
     */
    public static <T> Result<T> fail(int errCode, String message) {
        return of(errCode, message, null);
    }

    /**
     * 失败
     *
     * @param errCode 编码
     * @param <T> 返回泛型
     * @return 结果
     */
    public static <T> Result<T> fail(ErrorCode errCode) {
        return of(errCode.getCode(), errCode.getMessage(), null);
    }

    /**
     * 失败
     *
     * @param errCode 编码
     * @param <T> 返回泛型
     * @return 结果
     */
    public static <T> Result<T> fail(ErrorCode errCode, T data) {
        return of(errCode.getCode(), errCode.getMessage(), data);
    }

    /**
     * 异常
     *
     * @param errCode 编码
     * @param message 消息
     * @param <T> 返回泛型
     * @return 结果
     */
    public static <T> Result<T> exception(int errCode, String message, T ex) {
        return of(errCode, message, ex);
    }

    public static <T> Result<T> exception(ErrorCode code, Throwable ex) {
        return of(code.getCode(), code.getMessage(), (T) ex);
    }

    public static <T> Result<T> of(int errCode, String message, T data) {
        return new Result<>(errCode, message, data);
    }
}
