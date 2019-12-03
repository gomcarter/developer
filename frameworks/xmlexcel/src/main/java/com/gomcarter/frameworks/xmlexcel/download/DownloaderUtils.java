//package com.gomcarter.frameworks.xmlexcel.download;
//
//
//import com.google.common.collect.Lists;
//import com.gomcarter.frameworks.base.common.CustomStringUtils;
//import com.gomcarter.frameworks.base.pager.Pageable;
//import com.gomcarter.frameworks.xmlexcel.config.Header;
//import com.gomcarter.frameworks.xmlexcel.utils.CSVUtils;
//import org.apache.commons.lang3.ArrayUtils;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * @author gomcarter 2017年12月2日 08:10:35
// */
//public class DownloaderUtils {
//
//    public static final int MAX_COUNT = 100000;
//
//    /**
//     * @param downloader
//     * @param params
//     * @param titles
//     * @param path       文件临时存储位置，包含文件名
//     * @param extraData
//     * @throws IOException
//     */
//    public static void generateFile(
//            Downloadable downloader, Map<String, Object> params,
//            List<DownloaderTitles> titles, String path, Object... extraData) throws IOException {
//
//        String suffix = CustomStringUtils.getFileSuffix(path);
//        if ("csv".equalsIgnoreCase(suffix)) {
//            _writeCSV(downloader, params, titles, path, extraData);
//        } else {
//            _writeXML(downloader, params, titles, path, extraData);
//        }
//
//    }
//
//    private static void _writeCSV(
//            Downloadable downloader, Map<String, Object> params,
//            List<DownloaderTitles> titles, String path, Object[] extraData) throws IOException {
//
//        List<Header> headers = genHeader(titles);
//        /*获取文件保存路径*/
//        String savePath = path;
//        /*分页设置为null，表示不使用分页，只执行一次查询*/
//        Pageable pager = downloader.getPager();
//
//        boolean pageable = (pager != null);
//        /*创建文件或清空文件*/
//        if (headers != null) {
//            CSVUtils.createCSV(savePath, headers);
//        }
//        for (int i = 1; true; i++) {
//            List<Map<String, Object>> cells = downloader.generate(params, titles, ArrayUtils.add(extraData, pager));
//
//            /** 这里这么做是为了，即使没有数据也能下载一个空的文件 */
//            if (headers == null && cells != null && !cells.isEmpty()) {
//                headers = XmlExcelAppender.parseHeaders(cells);
//                CSVUtils.createCSV(savePath, headers);
//            }
//
//            if (cells != null && !cells.isEmpty()) {
//                /** 往文件里面追加内容 */
//                CSVUtils.appendCSV(savePath, headers, cells);
//                /** 不支持翻页，或者查询出来的数据不够 */
//                if (!pageable || cells.size() < pager.getPageCount()) {
//                    break;
//                }
//            } else {
//                break;
//            }
//        }
//    }
//
//    private static void _writeXML(
//            Downloadable downloader, Map<String, Object> params,
//            List<DownloaderTitles> titles, String path, Object[] extraData) throws IOException {
//
//        List<Header> headers = DownloadDataUtils.genHeader(titles);
//        /*获取文件保存路径*/
//        String savePath = path;
//        /*分页设置为null，表示不使用分页，只执行一次查询*/
//        Pageable pager = downloader.getPager();
//
//        boolean pageable = (pager != null);
//        /*创建文件或清空文件*/
//        try (XmlExcel excel = XmlExcel.of(savePath, headers)) {
//            for (int i = 1; true; i++) {
//                //设置分页数据
//                if (pageable) {
//                    if (i * pager.getPageCount() >= MAX_COUNT) {
//                        break;
//                    }
//                    params.putAll(pager.turnPage(i).toMap());
//                }
//                List<Map<String, Object>> cells = downloader.generate(params, titles, ArrayUtils.add(extraData, pager));
//
//                if (headers == null && cells != null && !cells.isEmpty()) {
//                    excel.setHeaders(XmlExcelAppender.parseHeaders(cells));
//                }
//
//                if (cells != null && !cells.isEmpty()) {
//                    /* 往文件里面追加内容 */
//                    excel.appendBody(cells);
//                    /*不支持翻页，或者查询出来没有数据*/
//                    if (!pageable || cells.size() < pager.getPageCount()) {
//                        break;
//                    }
//                } else {
//                    break;
//                }
//            }
//            excel.finish();
//        }
//    }
//
//    public static List<Map<String, Object>> generateCells(List<?> rows, List<DownloaderTitles> downloadTitles) {
//
//        if (rows == null && rows.isEmpty()) {
//            return Lists.newArrayList();
//        }
//        List<Header> headers = null;
//
//        if (downloadTitles != null && !downloadTitles.isEmpty()) {
//            headers = DownloadDataUtils.genHeader(downloadTitles);
//        } else {
//            Object first = getNoneNullFirst(rows);
//            if (first != null && first instanceof Map) {
//                headers = XmlExcelAppender.parseHeaders((List<Map<String, Object>>) rows);
//            }
//        }
//        if (headers == null) {
//            throw new RuntimeException("不支持的下载方式");
//        }
//        List<Map<String, Object>> cells = DownloadDataUtils.genMapDatas(headers, rows);
//
//        return cells;
//    }
//
//    private static Object getNoneNullFirst(List<?> rows) {
//        for (Object row : rows) {
//            if (row != null) {
//                return row;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * {@link #generateCells(List, List)}.
//     */
//    @Deprecated
//    public static List<Map<String, Object>> generateCells(List<?> rows, Class<?> cls, List<DownloaderTitles> downloadTitles) {
//
//        return generateCells(rows, downloadTitles);
//    }
//}
