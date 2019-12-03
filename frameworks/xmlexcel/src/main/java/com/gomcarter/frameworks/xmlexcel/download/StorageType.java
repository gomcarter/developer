package com.gomcarter.frameworks.xmlexcel.download;

import com.gomcarter.frameworks.base.common.NacosClientUtils;

public enum StorageType {
    /**
     * 文件存放本地的一个文件目录下，nfs挂载此目录，即可在公网上下载
     */
    nfs {
        @Override
        public String getDownloadUrl(Downloader downloader, String fileName) {
            // FIXME
            return NacosClientUtils.getConfigAsString("nfs", "STORAGE_TYPE") + fileName;
        }
    },
    /**
     * 放到 oss
     */
    oss {
        @Override
        public String getDownloadUrl(Downloader downloader, String fileName) {
            // FIXME
            return null;
        }
    };

    public abstract String getDownloadUrl(Downloader downloader, String fileName);
}
