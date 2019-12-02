package it.gomcarter.frameworks.base.json;

public class JsonData extends JsonObject {

	private Object extra;

	public JsonData() {
		this(null, true, null);
	}

	public JsonData(Object data) {
        this(data, true, null);
	}

	public JsonData(Object data, Boolean success, String msg) {
        this.extra =  data;
        this.message = msg;
        if(success != null && success) {
            this.code = 0;
        } else {
            this.code = -1;
        }
        this.success = success;
    }

	public Object getExtra() {
		return extra;
	}

	public JsonData setExtra(Object extra) {
		this.extra = extra;
		return this;
	}
}
