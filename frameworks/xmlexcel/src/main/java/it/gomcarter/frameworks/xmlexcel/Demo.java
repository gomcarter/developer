package it.gomcarter.frameworks.xmlexcel;

import it.gomcarter.frameworks.xmlexcel.config.Header;
import it.gomcarter.frameworks.xmlexcel.config.Style;
import it.gomcarter.frameworks.xmlexcel.writer.XmlExcel;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author liyin on 2018年5月7日 16:59:09
 */
public class Demo {

    /**
     * 示例
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
//        writeMap();

        writeObject();

//        multplySheet();
    }

    private static void multplySheet() throws Exception {
        List<Object> dataList1 = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            dataList1.add(new DemoClass((long) i, "name", new Date(), new BigDecimal(i)));
        }

        List<Header> headers1 = Header.of(
                Header.of("id", "ID"),
                Header.of("name", "名称", true),
                Header.of("time", "时间"),
                //对于key: C01在下面定义。C01可以随意，只要唯一即可
                Header.of("decimal").setCellStyle(s -> ((BigDecimal) s).intValue() % 2 == 0 ? "C01" : null)
        );

        List<Header> headers2 = new ArrayList<Header>() {{
            add(Header.of("数字字符串"));
            add(Header.of("字符串", true));
            add(Header.of("标记为字符串的数字"));
            add(Header.of("小数的字符串", true));
            add(Header.of("中文时间", true));
            add(Header.of("时间对象"));
            add(Header.of("日期字符串1", true));
            add(Header.of("日期字符串2"));
            add(Header.of("时间字符串1"));
            add(Header.of("时间字符串2", /*data formatter*/s -> ((Map) s).get("时间字符串2") + "1111"));
            add(Header.of("BigDecimal one", true));
            add(Header.of("BigDecimal"));
            add(Header.of("double").setCellStyle(s -> ((Double) s).intValue() % 2 == 0 ? "C01" : null));
            add(Header.of("float"));
            add(Header.of("Long"));
            add(Header.of("big double"));
        }};


        List<Object> dataList2 = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("数字字符串", "0000112312");
            map.put("字符串", "asdaasd");
//            map.put("标记为字符串的数字", "'0001232123");
            map.put("小数的字符串", "24891.24");
            map.put("中文时间", "2016年4月6日 14:46:56");
            map.put("时间对象", new Date());
            map.put("日期字符串1", "2014/04/06");
            map.put("日期字符串2", "2014-04-07");
            map.put("时间字符串1", "2014/04/07 11:11:11");
            map.put("时间字符串2", "2014-04-07 22:22:22");
            map.put("BigDecimal one", BigDecimal.ONE);
            map.put("BigDecimal", new BigDecimal(1239129));
            map.put("double", (double) i);
            map.put("float", (float) i);
            map.put("Long", new Random().nextLong());
            map.put("big double", new Random().nextDouble());
            dataList2.add(map);
        }

        Collection<Style> styles = Arrays.asList(Style.of("C01", "#FF0000", "#00FF00"));
        try (XmlExcel excel = XmlExcel.of("D:\\mutiplysheet.xls", headers1, styles)
                .start("first sheet")
                .appendBody(dataList1)
                .setHeaders(headers2)
                .addSheet("second sheet")
                .appendBody(dataList2)
                .finish()) {
        }
    }

    private static void writeObject() throws Exception {
        List<Object> dataList = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            dataList.add(new DemoClass((long) i, "name", new Date(), new BigDecimal(i)));
        }


        List<Header> headers = Header.of(
                Header.of("id", "ID"),
                Header.of("name", "名称", true),
                Header.of("time", "时间"),
                //对于key: C01在下面定义。C01可以随意，只要唯一即可
                Header.of("decimal").setCellStyle(s -> ((BigDecimal) s).intValue() % 2 == 0 ? "C01" : null)
        );

        //自定义样式
        Collection<Style> styles = Arrays.asList(Style.of("C01", "#FF0000", "#00FF00"));
        try (XmlExcel excel = XmlExcel.of("D:\\写对象自定义header.xls", headers, styles)
                .start()
                .appendBody(dataList)
                //可以多次appendBody
                //addSheet()//第二个sheet等，或者更新header再插入第sheet
                .finish()) {
        }

        try (XmlExcel excel = XmlExcel.of("D:\\写对象自解析header.xls").write(dataList)) {
        }

    }

    private static void writeMap() throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

        List<Header> headers = new ArrayList<Header>() {{
            add(Header.of("数字字符串"));
            add(Header.of("字符串", true));
            add(Header.of("标记为字符串的数字"));
            add(Header.of("小数的字符串", true));
            add(Header.of("中文时间", true));
            add(Header.of("时间对象"));
            add(Header.of("日期字符串1", true));
            add(Header.of("日期字符串2"));
            add(Header.of("时间字符串1"));
            add(Header.of("时间字符串2", /*data formatter*/s -> ((Map) s).get("时间字符串2") + "1111"));
            add(Header.of("BigDecimal one", true));
            add(Header.of("BigDecimal"));
            add(Header.of("double").setCellStyle(s -> ((Double) s).intValue() % 2 == 0 ? "C01" : null));
            add(Header.of("float"));
            add(Header.of("Long"));
            add(Header.of("big double"));
        }};


        for (int i = 0; i < 5; ++i) {
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("数字字符串", "0000112312");
            map.put("字符串", "asdaasd");
//            map.put("标记为字符串的数字", "'0001232123");
            map.put("小数的字符串", "24891.24");
            map.put("中文时间", "2016年4月6日 14:46:56");
            map.put("时间对象", new Date());
            map.put("日期字符串1", "2014/04/06");
            map.put("日期字符串2", "2014-04-07");
            map.put("时间字符串1", "2014/04/07 11:11:11");
            map.put("时间字符串2", "2014-04-07 22:22:22");
            map.put("BigDecimal one", BigDecimal.ONE);
            map.put("BigDecimal", new BigDecimal(1239129));
            map.put("double", (double) i);
            map.put("float", (float) i);
            map.put("Long", new Random().nextLong());
            map.put("big double", new Random().nextDouble());
            dataList.add(map);
        }

        Collection<Style> styles = Arrays.asList(Style.of("C01", "#FF0000", "#00FF00"));
        try (XmlExcel excel = XmlExcel.of("D:\\自定义header.xls", headers, styles)
                .start()
                .appendBody((List) dataList)
                //可以多次appendBody
                //addSheet()//第二个sheet等，或者更新header再插入第sheet
                .finish()) {
        }

        try (XmlExcel excel = XmlExcel.of("D:\\自解析header.xls").write((List) dataList)) {
        }
    }

    private static class DemoClass {
        private Long id;
        private String name;
        private Date time;
        private BigDecimal decimal;

        public DemoClass(Long id, String name, Date time, BigDecimal decimal) {
            this.id = id;
            this.name = name;
            this.time = time;
            this.decimal = decimal;
        }

        public Long getId() {
            return id;
        }

        public DemoClass setId(Long id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public DemoClass setName(String name) {
            this.name = name;
            return this;
        }

        public Date getTime() {
            return time;
        }

        public DemoClass setTime(Date time) {
            this.time = time;
            return this;
        }

        public BigDecimal getDecimal() {
            return decimal;
        }

        public DemoClass setDecimal(BigDecimal decimal) {
            this.decimal = decimal;
            return this;
        }
    }

}
