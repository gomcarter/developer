package com.gomcarter.developer.params;


/**
 * @author gomcarter
 * @ClassName JArgs
 * @Description
 * @date 2019-06-17 16:41:01
 */
public class JArgs {

    private Class key;

    private Object value;

    public Class getKey() {
        return key;
    }

    public JArgs setKey(String key) throws ClassNotFoundException {
        this.key = Class.forName(key);
        return this;
    }

    public Object getValue() {
        return value;
    }

    public JArgs setValue(Object value) {
        if (this.key == Long.class) {
            this.value = Long.valueOf(value.toString());
        } else if (this.key == Integer.class){
            this.value = Integer.valueOf(value.toString());
        } else {
            this.value = value;
        }
        return this;
    }
}
