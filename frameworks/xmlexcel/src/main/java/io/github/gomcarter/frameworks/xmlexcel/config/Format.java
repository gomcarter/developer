package io.github.gomcarter.frameworks.xmlexcel.config;

/**
 * @author gomcarter on 2018年5月4日 10:41:52
 */
public enum Format {
    number {
        @Override
        public String get() {
            return "<NumberFormat ss:Format=\"#,##0.000_);\\(#,##0.000\\)\"/>";
        }
    },
    integer {
        @Override
        public String get() {
            return "<NumberFormat ss:Format=\"#,##0_);\\(#,##0\\)\"/>";
        }
    },
    defaults {
        @Override
        public String get() {
            return "<NumberFormat ss:Format=\"@\"/>";
        }
    };

    public abstract String get();
}
