package com.gomcarter.frameworks.base.exception;

import com.gomcarter.frameworks.base.json.ErrorCode;

/**
 * @author gomcarter
 */
public class NoLoginException extends CustomException {

    private static final long serialVersionUID = -2712130137572517505L;

    public NoLoginException() {
        super(ErrorCode.noLogin);
    }
}
