package com.yiayoframework.base.exception;

import com.yiayoframework.base.json.ErrorCode;

/**
 * @ClassName: NoLoginException
 * @Description: 自定义异常类
 * @author: 李银
 * @date: 2018年4月13日 14:26:15
 */
public class NoLoginException extends CustomException {

    private static final long serialVersionUID = -2712130137572517505L;

    public NoLoginException() {
        super(ErrorCode.noLogin);
    }
}
