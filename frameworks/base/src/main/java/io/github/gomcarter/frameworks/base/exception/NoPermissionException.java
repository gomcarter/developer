package io.github.gomcarter.frameworks.base.exception;

import io.github.gomcarter.frameworks.base.json.ErrorCode;

/**
 * @ClassName: NoPermissionException
 * @Description: 自定义异常类
 * @author: gomcarter
 * @date: 2018年4月13日 14:26:15
 */
public class NoPermissionException extends CustomException {
    public NoPermissionException() {
        super(ErrorCode.noPermission);
    }
}
