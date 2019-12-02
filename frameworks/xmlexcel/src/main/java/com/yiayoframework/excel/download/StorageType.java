package com.yiayoframework.excel.download;//package com.yiayo.download;
//
//import com.tiangou.baseCommon.api.FileUploader;
//import com.tiangou.baseCommon.constants.EnvConstants;
//import com.tiangou.baseCommon.exception.CustomException;
//import com.tiangou.baseCommon.utils.JPDateUtils;
//import org.apache.commons.io.FileUtils;
//
//import java.io.File;
//import java.io.IOException;
//
//public enum StorageType {
//    /**
//     * 文件存放本地的一个文件目录下，nfs挂载此目录，即可在公网上下载
//     */
//    nfs {
//        @Override
//        public String getDownloadUrl(Downloader downloader, String fileName) {
//            if (downloader.getDownloadDomain() == null) {
//                EnvConstants.EnvType type = EnvConstants.getType();
//                switch (type) {
//                    case dev:
//                    case test:
//                    case pre:
//                        downloader.setDownloadDomain("//file.66buy.com.cn");
//                        break;
//                    case online:
//                        downloader.setDownloadDomain("//file.51tiangou.com");
//                        break;
//                    default:
//                        downloader.setDownloadDomain("file:///C:/");
//                }
//            }
//            return downloader.getDownloadDomain() + fileName;
//        }
//    },
//    oss {
//        @Override
//        public String getDownloadUrl(Downloader downloader, String fileName) {
//            return null;
//        }
//    },
//    /**
//     * 和oss一样， 把文件生成之后，上传到文件服务器
//     */
//    upyun {
//        @Override
//        public String getDownloadUrl(Downloader downloader, String fileName) {
//            if (downloader.getDownloadDomain() == null) {
//                EnvConstants.EnvType type = EnvConstants.getType();
//                switch (type) {
//                    case dev:
//                    case test:
//                    case pre:
//                        downloader.setDownloadDomain("//test.img.tg-img.com/");
//                        break;
//                    case online:
//                        downloader.setDownloadDomain("//img1.tg-img.com/");
//                        break;
//                    default:
//                        downloader.setDownloadDomain("file:///C://download/");
//                }
//            }
//
//            try {
//                File f = new File(fileName);
//                String url = FileUploader.uploadFile(f, fileName);
//
//                FileUtils.forceDelete(f);
//
//                return downloader.getDownloadDomain() + url;
//            } catch (IOException e) {
//                throw new CustomException("下载失败", e);
//            }
//        }
//    };
//
//    public abstract String getDownloadUrl(Downloader downloader, String fileName);
//}
