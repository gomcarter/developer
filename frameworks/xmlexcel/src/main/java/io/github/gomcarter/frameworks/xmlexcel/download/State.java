package io.github.gomcarter.frameworks.xmlexcel.download;

/**
 * @author gomcarter on 2019-11-11 23:41:30
 */
public enum State {
    /**
     * 没跑过
     */
    nothing,
    /**
     * 正在计算中
     */
    running,
    /**
     * 计算完成
     */
    finish;
    //任务状态待补充
}
