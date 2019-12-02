package com.yiayoframework.excel.download;

/**
 * @author 李银 on 2019-11-11 23:41:30
 */
public class TaskStateDto {
    private String state;
    private String key;
    private String url;

    public TaskStateDto() {
    }

    public TaskStateDto(State state, String key, String url) {
        this.state = state.name();
        this.url = url;
        this.key = key;
    }

    public TaskStateDto(State state) {
        this.state = state.name();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state.name();
    }
}
