
package com.yiayoframework.base.json;

/**
 * 共
 * 第一位：用户类型（10-enterprise; 11- goods; 12-xxx）<br>
 * 第六~十位：错误类型
 */
public enum ErrorCode {
    //HTTP CLIENT ERROR
    httpStatusNotOk(-200, "服务器请求超时,请稍后重试"),
    socketTimeOut(-201, "服务器响应超时,请稍后重试"),
    connectTimeOut(-202, "服务器请求超时,请稍后重试"),

    dbDuplicateError(-3001, "记录已存在！"),

    nullPointer(-1, "内部错误！"),

    noLogin(-401, "未登录！"),

    noPermission(-402, "无权操作！"),

    nonConcurrency(-403, "提交慢了，已经被别人抢占！"),

    paramError(-404, "参数错误！"),

    sqlError(-405, "数据错误！");

    int code;
    String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static boolean isHttpConnectError(Integer code) {
        return code != null
                && (code.equals(ErrorCode.httpStatusNotOk.getCode())
                || code.equals(ErrorCode.connectTimeOut.getCode())
                || code.equals(ErrorCode.socketTimeOut.getCode()));
    }
}
