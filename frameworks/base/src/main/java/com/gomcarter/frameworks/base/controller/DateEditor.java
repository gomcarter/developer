package com.gomcarter.frameworks.base.controller;

import com.gomcarter.frameworks.base.common.CustomDateUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * @authher gomcarter 2017年12月2日 08:10:35
 */
public class DateEditor extends PropertyEditorSupport {

    public DateEditor() {
        super();
    }

    @Override
    public void setAsText(String text) {
        if (StringUtils.isBlank(text)) {
            setValue(null);
        } else {
            if (StringUtils.isNumeric(text)) {
                setValue(new Date(Long.valueOf(text)));
            } else {
                setValue(CustomDateUtils.fromStringWithTime(text));
            }
        }
    }
}
