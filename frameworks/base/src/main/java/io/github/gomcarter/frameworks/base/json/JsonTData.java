package io.github.gomcarter.frameworks.base.json;

public class JsonTData<T> extends JsonObject {

    private T extra;

    public JsonTData() {

    }

    public JsonTData(T data) {
        this.code = 0;
        this.extra = data;
        this.success = true;
    }

    public T getExtra() {
        return extra;
    }

    public JsonTData setExtra(T extra) {
        this.extra = extra;
        return this;
    }
}
