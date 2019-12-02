package io.github.gomcarter.frameworks.xmlexcel;

import io.github.gomcarter.frameworks.xmlexcel.config.Format;
import io.github.gomcarter.frameworks.xmlexcel.config.Header;
import io.github.gomcarter.frameworks.xmlexcel.config.Style;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * create by liyin on 2016年4月6日 13:20:55
 */
public class Appender {

    static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public enum DefaultStyle {
        DEFAULT(Style.of("Default")),
        TABLE(Style.of("t01")),
        HEADER(Style.of("s01", null, true, "#00B050", Format.defaults)),
        TEXT(Style.of("s02", null, false, null, Format.defaults)),
        FLOAT(Style.of("s03", null, false, null, Format.number)),
        INTEGER(Style.of("s04", null, false, null, Format.integer));

        public Style style;

        DefaultStyle(Style style) {
            this.style = style;
        }

        public static Collection<Style> getStyles() {
            return Arrays.stream(values()).map(s -> s.style)
                    .collect(Collectors.toList());
        }
    }

    static Function defaultCellStyle = value -> {
        if (value instanceof Number && value != null) {
            if (((Number) value).longValue() == ((Number) value).floatValue()) {
                return DefaultStyle.INTEGER.style.getKey();
            }
            return DefaultStyle.FLOAT.style.getKey();
        } else {
            return DefaultStyle.TEXT.style.getKey();
        }
    };

    /**
     * UTF-8格式标记
     */
    private static final String BOM_TAG = new String(new byte[]{(byte) 0xef, (byte) 0xbb, (byte) 0xbf}, DEFAULT_CHARSET);

    public static byte[] start() {
        return (BOM_TAG + "<?xml version=\"1.0\"?>\n" +
                "<?mso-application progid=\"Excel.Sheet\"?>\n" +
                "<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\n" +
                " xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n" +
                " xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\n" +
                " xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\n" +
                " xmlns:html=\"http://www.w3.org/TR/REC-html40\">\n" +
                "<DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">\n" +
                "<Version>12.00</Version>\n" +
                "</DocumentProperties>\n" +
                "<ExcelWorkbook xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" +
                "<ProtectStructure>False</ProtectStructure>\n" +
                "<ProtectWindows>False</ProtectWindows>\n" +
                "</ExcelWorkbook>").getBytes(DEFAULT_CHARSET);
    }

    /**
     * 预定义样式, 后期将样式写到cell上即可
     *
     * @param styles
     * @return
     */
    public static byte[] styles(Collection<Style> styles) {
        String g = Stream.concat(DefaultStyle.getStyles().stream(), styles == null ? Stream.empty() : styles.stream())
                .map(s -> String.format("<Style ss:ID=\"%s\">" +
                                "<Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:WrapText=\"1\"/>" +
                                "<Borders/>" +
                                "<Font ss:FontName=\"宋体\" x:CharSet=\"134\" ss:Size=\"11\" ss:Color=\"%s\" %s/> " +
                                //Interior
                                "%s" +
                                //number format
                                "%s" +
                                "</Style>",
                        s.getKey(),
                        //make the default color black if configured color is null
                        Optional.ofNullable(s.getColor()).orElse("#000000"),
                        s.isBold() ? "ss:Bold=\"1\"" : "",
                        s.getInterior() == null ? "" : ("<Interior ss:Pattern=\"Solid\" ss:Color=\"" + s.getInterior() + "\"/>"),
                        s.getFormat() == null ? "" : s.getFormat().get()))
                .reduce((a, b) -> a + b)
                .orElse("");

        return String.format("<Styles>%s</Styles>", g).getBytes(DEFAULT_CHARSET);
    }

    public static byte[] sheet(String sheetName) {
        //TODO: consider the column width to be configurable
//        return ("<Worksheet ss:Name=\"" + sheetName + "\">\n" +
//                "  <Table ss:ExpandedColumnCount=\"16\" ss:ExpandedRowCount=\"6\" x:FullColumns=\"1\"\n" +
//                "   x:FullRows=\"1\" ss:DefaultColumnWidth=\"80.25\" ss:DefaultRowHeight=\"50.0625\">\n" +
//                "   <Column ss:Index=\"2\" ss:AutoFitWidth=\"0\" ss:Width=\"219.75\"/>\n" +
//                "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"194.25\"/>\n" +
//                "   <Column ss:Index=\"10\" ss:Width=\"81\" ss:Span=\"4\"/>\n" +
//                "   <Column ss:Index=\"15\" ss:StyleID=\"s71\" ss:Width=\"156.75\"/>\n" +
//                "   <Column ss:Width=\"156.75\"/>").getBytes(DEFAULT_CHARSET);

        return ("<Worksheet ss:Name=\"" + sheetName + "\">" +
                "<Table x:FullColumns=\"1\" x:FullRows=\"1\" " +
                "ss:ExpandedColumnCount=\"16\" " +
                "ss:DefaultColumnWidth=\"80.25\" " +
                "ss:DefaultRowHeight=\"50\"  " +
                "ss:ExpandedRowCount=\"120000\">").getBytes(DEFAULT_CHARSET);
    }

    /**
     * @param sheetName ： 外部控制sheetName不重复
     * @return
     */
    public static byte[] nextSheet(String sheetName) {
        return ("</Table></Worksheet>" +
                "<Worksheet ss:Name=\"" + sheetName + "\">" +
                "<Table x:FullColumns=\"1\" x:FullRows=\"1\" " +
                "ss:DefaultColumnWidth=\"80.25\" " +
                "ss:DefaultRowHeight=\"50\"  " +
                "ss:ExpandedRowCount=\"120000\">").getBytes(DEFAULT_CHARSET);
    }

    public static byte[] header(List<Header> headers) {
        StringBuilder define = new StringBuilder();
//        StringBuffer row  = new StringBuffer(" <Row ss:AutoFitHeight=\"0\" ss:Height=\"63\">");
        StringBuilder row = new StringBuilder(" <Row ss:AutoFitHeight=\"0\">");
        for (int i = 0; i < headers.size(); ++i) {
            Header header = headers.get(i);

            /*设置列宽*/
            Integer width = header.getWidth();
            if (null != width && 0 < width) {
                define.append("<Column ss:Index=\"")
                        .append(i)
                        .append("\" ss:Width=\"")
                        .append(width)
                        .append("\" />");
            }

            /*添加表头*/
            row.append("<Cell ");
            Object styleId;
            if (header.getHeaderStyle() != null) {
                styleId = header.getHeaderStyle().apply(header);
            } else {
                styleId = DefaultStyle.HEADER.style.getKey();
            }
            if (styleId != null) {
                row.append(" ss:StyleID=\"")
                        .append(styleId.toString())
                        .append("\"");
            }

            row.append("><Data ss:Type=\"String\">")
                    .append(header.getName())
                    .append("</Data></Cell>");

        }
        row.append("</Row>");

        return define.append(row).toString().getBytes(DEFAULT_CHARSET);
    }

    public static byte[] row(List<Header> headers, Map<String, Object> element) {
        return rows(headers, Collections.singletonList(element));
    }

    /**
     * @param headers
     * @param elements List< map< headerkey, data > >
     * @return
     */

    public static byte[] rows(List<Header> headers, Collection<? extends Object> elements) {
        if (elements == null || elements.size() <= 0) {
            return new byte[0];
        }

        List<Header> headerList = Optional.ofNullable(headers).orElse(parseHeaders(elements));

        if (headers.stream().anyMatch(Header::getAutoMerge)) {
            //需要自动合并
            return merge(headerList, elements);
        } else {
            //不需要自动合并
            return directly(headerList, elements);
        }
    }

    private static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

    private static byte[] merge(List<Header> headerList, Collection<? extends Object> elements) {
        Map<String, Pair> toBeMergedCache = new HashMap<>();
        List<Map<String, Pair>> rows = elements
                .stream()
                .map(row -> new HashMap<String, Pair>() {{
                    for (Header h : headerList) {
                        Object value = null;
                        String key = h.getName();
                        Function formatter = h.getDataFormatter();
                        if (formatter != null) {
                            value = formatter.apply(row);
                        }

                        Pair<Integer, Object> current = new Pair(0, value);
                        put(key, current);

                        if (h.getAutoMerge()) {
                            //如果是需要merge的列；
                            Pair<Integer, Object> prev = toBeMergedCache.get(key);
                            if (prev == null) {
                                toBeMergedCache.put(key, current);
                            } else {
                                //看当前和之前的值是否相等，相等则标记合并+1，不相等换新的
                                if (Objects.equals(prev.value, value)) {
                                    //key 相当于向下合并多少单元格
                                    prev.key++;
                                    //key 等于 -1 相当于表示改cell不显示
                                    current.key = -1;
                                } else {
                                    toBeMergedCache.put(key, current);
                                }
                            }
                        }
                    }
                }})
                .collect(Collectors.toList());
        return body(headerList, rows);
    }

    private static byte[] body(List<Header> headerList, List<Map<String, Pair>> elements) {
        return elements.stream()
                .map(row -> {
                    StringBuilder sb = new StringBuilder("<Row ss:AutoFitHeight=\"0\">");
                    for (int i = 1; i <= headerList.size(); ++i) {
                        Header h = headerList.get(i - 1);
                        Pair<Integer, Object> pair = row.get(h.getName());

                        Object value = pair.value;
                        int key = pair.key;
                        if (key < 0) {
                            //小于0表示被合并了，这一个单元格不写数据
                            continue;
                        } else if (key > 0) {
                            //大于零表示第一个开始合并，需要标明往下合并多少单元格
                            sb.append("<Cell ss:Index=\"").append(i).append("\" ").append("ss:MergeDown=\"").append(pair.key).append("\" ");
                        } else {
                            //表示未被合并，本来应该写 MergeDown=0，往下合并0个单元格
                            sb.append("<Cell ss:Index=\"").append(i).append("\" ");
                        }

                        Object styleId = Optional.ofNullable(h.getCellStyle()).orElse(defaultCellStyle).apply(value);
                        if (styleId != null) {
                            sb.append("ss:StyleID=\"")
                                    .append(styleId)
                                    .append("\"");
                        }
                        sb.append("><Data ");

                        Object v = format(value);
                        if (v instanceof Number) {
                            sb.append("ss:Type=\"Number\"");
                        } else {
                            sb.append("ss:Type=\"String\"");
                        }

                        sb.append(">")
                                .append(v == null ? "" : v.toString())
                                .append("</Data></Cell>");
                    }
                    return sb.append("</Row>\n").toString();
                })
                .reduce((a, b) -> a + b)
                .orElse("")
                .getBytes(DEFAULT_CHARSET);
    }

    private static byte[] directly(List<Header> headerList, Collection<? extends Object> elements) {
        List<Map<String, Pair>> rows = elements.stream()
                .map(row -> new HashMap<String, Pair>() {{
                    for (Header h : headerList) {
                        Object value = null;
                        Function formatter = h.getDataFormatter();
                        if (formatter != null) {
                            value = formatter.apply(row);
                        }

                        put(h.getName(), new Pair<>(0, value));
                    }
                }})
                .collect(Collectors.toList());
        return body(headerList, rows);
    }

    public static byte[] end() {
        return "</Table></Worksheet></Workbook>".getBytes(DEFAULT_CHARSET);
    }

    public static List<Header> parseHeaders(Collection<? extends Object> rows) {
        return rows.stream()
                .limit(1)
                .map(s -> {
                    if (Arrays.stream(s.getClass().getGenericInterfaces())
                            .anyMatch(m -> ((ParameterizedTypeImpl) m).getRawType() == Map.class)) {

                        return new ArrayList<Header>() {{
                            ((Map) s).forEach((key, value) -> add(Header.of(key + "")));
                        }};
                    } else {
                        return new ArrayList<Header>() {{
                            Arrays.stream(s.getClass().getDeclaredFields())
                                    .forEach(s -> add(Header.of(s.getName())));
                        }};
                    }
                })
                .findAny()
                .orElse(null);
    }


    public static Object format(Object data) {
        if (data == null) {
            return null;
        } else if (data instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(data);
        } else if (data instanceof String) {
            return ((String) data).replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;");
        }
        return data;
    }

}
