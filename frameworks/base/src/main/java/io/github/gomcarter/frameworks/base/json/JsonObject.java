package io.github.gomcarter.frameworks.base.json;

public abstract class JsonObject {

    protected Integer code = 0;
    protected String message = "";
    protected boolean success = true;

    public JsonObject() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getTimestamp() {
        return System.currentTimeMillis();
    }
}
