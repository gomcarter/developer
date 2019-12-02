package it.gomcarter.frameworks.base.json;

public class JsonSuccess extends JsonObject {

	private Object extra;

	public JsonSuccess() {
		this("");
	}

	public JsonSuccess(String msg) {
		this.code = 0;
		this.message = msg;
		this.success = true;
	}

	public Object getExtra() {
		return extra;
	}

	public JsonSuccess setExtra(Object extra) {
		this.extra = extra;
		return this;
	}
}
