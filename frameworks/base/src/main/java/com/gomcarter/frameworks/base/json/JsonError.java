package com.gomcarter.frameworks.base.json;

public class JsonError extends JsonObject {

    private Object extra;

    public JsonError() {
    }

    public JsonError(String msg) {
        this(msg, -1);
    }

    public JsonError(ErrorCode erroCode) {
    	this(erroCode.getMsg(), erroCode.getCode());
    }

    public JsonError(String msg, Integer code) {
        if (code == null) {
            code = -1;
        }
        this.code = code;
        this.message = msg;
        this.success = false;
    }

    public Object getExtra() {
        return extra;
    }

    public JsonError setExtra(Object extra) {
        this.extra = extra;
        return this;
    }
}
