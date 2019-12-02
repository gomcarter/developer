package it.gomcarter.frameworks.httpapi.api;

/**
 * @author gomcarter 2018年2月28日 18:31:53
 */
public class NacosConfig {
    private String group;
    private String dataId;

    public static NacosConfig valueOf(String group, String dataId) {
        return new NacosConfig(group, dataId);
    }

    private NacosConfig() {
    }

    public NacosConfig(String group, String dataId) {
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
        return "NacosConfig{" +
                "dataId='" + dataId + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
