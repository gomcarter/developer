package it.gomcarter.frameworks.base.exception;

import it.gomcarter.frameworks.base.json.ErrorCode;

/**
 * @ClassName: NonConcurrencyException
 * @Description: 自定义异常类
 * @author: gomcarter
 * @date: 2018年4月13日 14:26:15
 */
public class NonConcurrencyException extends CustomException {
    public NonConcurrencyException() {
        super(ErrorCode.nonConcurrency);
    }
}
