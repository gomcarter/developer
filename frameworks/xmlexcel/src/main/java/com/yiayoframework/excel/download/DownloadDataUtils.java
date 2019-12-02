//package com.yiayoframework.excel.download;
//
//
//import com.yiayoframework.base.common.CollectionUtils;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.yiayoframework.excel.config.Header;
//import org.apache.commons.lang3.ObjectUtils;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
///**
// * @authher 李银 2017年12月2日 08:10:35
// */
//public class DownloadDataUtils {
//
//    public static List<Header> genHeader(List<DownloaderTitles> downloadTitles) {
//
//        if (downloadTitles == null || downloadTitles.isEmpty()) {
//            return null;
//        }
//        List<Header> headers = Lists.newArrayList();
//
//        for (DownloaderTitles title : downloadTitles) {
//            Header header = new Header();
//            header.setName(title.getTitle());
//            header.setFillMap(title.getFillMap());
//            header.setIsMergeId(title.getIsMergeId());
//            header.setNeedMergge(title.getNeedMerge());
//            header.setDataGetter(genDataGetter(title));
//            header.setDataParser(title.getDataParser());
//            headers.add(header);
//        }
//        return headers;
//    }
//
//    private static DataGetter genDataGetter(DownloaderTitles title) {
//        List<String> fields = Lists.newArrayList(StringUtils.split(title.getColumn(), '.'));
//        if (StringUtils.isNotBlank(title.getFieldName())) {
//            fields.addAll(Lists.newArrayList(StringUtils.split(title.getFieldName(), '.')));
//        }
//        return initGetter(fields);
//    }
//
//    private static DataGetter initGetter(List<String> fields) {
//        ChainDataGetter getter = new ChainDataGetter();
//        for (String field : fields) {
//            getter.addDataGetter(initGetter(field));
//        }
//        return getter;
//    }
//
//    private static DataGetter initGetter(String field) {
//        if (StringUtils.isNumeric(field)) {
//            return new IndDataGetter(new Integer(field));
//        } else {
//            return new PropDataGetter(field);
//        }
//    }
//
//    public static ExlContent generateCells(List<?> rows, List<Header> exlHeaders) {
//
//        ExlContent content = new ExlContent();
//
//        if (CollectionUtils.isEmpty(rows)) {
//            return content.setExlHeaders(exlHeaders);
//        }
//        content.setExlHeaders(exlHeaders);
//
//        content.setDatas(genDatas(content.getExlHeaders(), rows));
//
//        return content;
//    }
//
//    public static ExlContent generateCellsByTitle(List<?> rows, List<DownloaderTitles> downloadTitles) {
//
//        ExlContent content = new ExlContent();
//
//        if (CollectionUtils.isEmpty(rows)) {
//            return content.setExlHeaders(genHeader(downloadTitles));
//        }
//        content.setExlHeaders(genHeader(downloadTitles));
//
//        content.setDatas(genDatas(content.getExlHeaders(), rows));
//
//        return content;
//    }
//
//    public static List<Object[]> genDatas(List<Header> exlHeaders, List<?> rows) {
//
//        List<Object[]> datas = Lists.newArrayList();
//
//        int size = genSize(exlHeaders, rows.get(0));
//
//        for (Object data : rows) {
//            datas.add(initRowData(size, data, exlHeaders));
//        }
//        return datas;
//    }
//
//    public static List<Map<String, Object>> genMapDatas(List<Header> exlHeaders, List<?> rows) {
//
//        List<Map<String, Object>> result = Lists.newArrayList();
//        for (Object row : rows) {
//            Map<String, Object> map = Maps.newLinkedHashMap();
//            for (Header header : exlHeaders) {
//                map.put(header.getName(), calcValuue(header, header.getDataGetter().getResult(row)));
//            }
//            result.add(map);
//        }
//        return result;
//    }
//
//    private static Object calcValuue(Header header, Object result) {
//        if (result == null) {
//            return null;
//        }
//        if (header.getDataParser() != null) {
//            result = header.getDataParser().apply(result);
//        }
//        if (result != null && header.getFillMap() != null) {
//            result = ObjectUtils.defaultIfNull(header.getFillMap().get(result.toString()), result);
//        }
//        return result;
//    }
//
//    private static Object[] initRowData(int size, Object data, List<Header> exlHeaders) {
//        Object[] res = new Object[size];
//        int ind = 0;
//        for (Header header : exlHeaders) {
//            res[ind] = calcValuue(header, header.getDataGetter().getResult(data));
//            ind++;
//        }
//        return res;
//    }
//
//    private static int genSize(List<Header> exlHeaders, Object data) {
//        if (exlHeaders != null) {
//            return exlHeaders.size();
//        }
//        if (data instanceof Object[]) {
//            return ((Object[]) data).length;
//        }
//        if (data instanceof Collection) {
//            return ((Collection) data).size();
//        }
//        if (data instanceof Map) {
//            return ((Map) data).size();
//        }
//        throw new RuntimeException("生成exl数据失败，没有header数据必须是Map,Collection或者Array");
//    }
//}
