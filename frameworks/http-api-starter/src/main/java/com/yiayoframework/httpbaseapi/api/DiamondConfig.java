package com.yiayoframework.httpbaseapi.api;

/**
 * @author 李银 2018年2月28日 18:31:53
 */
public class DiamondConfig {
    private String group;
    private String dataId;

    public static DiamondConfig valueOf(String group, String dataId) {
        return new DiamondConfig(group, dataId);
    }

    private DiamondConfig() {
    }

    public DiamondConfig(String group, String dataId) {
        this.group = group;
        this.dataId = dataId;
    }

    public String getDataId() {
        return dataId;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "DiamondConfig{" +
                "dataId='" + dataId + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
