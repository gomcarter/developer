package io.github.gomcarter.frameworks.xmlexcel.writer;

import io.github.gomcarter.frameworks.xmlexcel.config.Style;
import io.github.gomcarter.frameworks.xmlexcel.Appender;
import io.github.gomcarter.frameworks.xmlexcel.DataFormatterParser;
import io.github.gomcarter.frameworks.xmlexcel.config.Header;

import java.io.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liyin on 2016年4月6日 13:20:55
 */
public class XmlExcel implements Closeable {

    private OutputStream os;

    private List<Header> headers;

    private Set<String> sheets = new HashSet<>();

    private Collection<Style> styles;

    private boolean parsed = false;

    public static XmlExcel of(String fileName) throws FileNotFoundException {
        return of(fileName, null);
    }

    public static XmlExcel of(String fileName, List<Header> headers) throws FileNotFoundException {
        return of(fileName, headers, null);
    }

    public static XmlExcel of(String fileName, List<Header> headers, Collection<Style> styles) throws FileNotFoundException {
        return new XmlExcel(new FileOutputStream(fileName), headers, styles);
    }

    public static XmlExcel of(File file) throws FileNotFoundException {
        return of(file, null);
    }

    public static XmlExcel of(File file, List<Header> headers) throws FileNotFoundException {
        return of(file, headers, null);
    }

    public static XmlExcel of(File file, List<Header> headers, Collection<Style> styles) throws FileNotFoundException {
        return new XmlExcel(new FileOutputStream(file), headers, styles);
    }

    public static XmlExcel of(OutputStream os) {
        return of(os, null);
    }

    public static XmlExcel of(OutputStream os, List<Header> headers) {
        return of(os, headers, null);
    }

    public static XmlExcel of(OutputStream os, List<Header> headers, Collection<Style> styles) {
        return new XmlExcel(os, headers, styles);
    }

    private XmlExcel() {
    }

    private XmlExcel(OutputStream os, List<Header> headers, Collection<Style> styles) {
        if (null == os) {
            throw new RuntimeException("The output could not be null");
        }

        this.os = os;
        this.headers = headers;
        this.styles = styles;
    }

    /**
     * 直接写入数据
     *
     * @param dataList Collection < map< header,  value  > >
     * @throws IOException
     */
    public XmlExcel write(Collection<Object> dataList) throws IOException {
        return write(null, dataList);
    }

    /**
     * 直接写入数据
     *
     * @param sheetName    sheet名称
     * @param dataList list < map< header,  value  > >
     * @throws IOException
     */
    public XmlExcel write(String sheetName, Collection<Object> dataList) throws IOException {
        //写头
        return this.start(sheetName)
                //加主体内容
                .appendBody(dataList)
                //加尾部
                .finish();
    }

    public XmlExcel addSheet() throws IOException {
        return this.addSheet(null);
    }

    public XmlExcel addSheet(String sheetName) throws IOException {
        sheetName = sheetName == null ? ("sheet" + (sheets.size() + 1)) : sheetName;
        if (sheets.contains(sheetName)) {
            sheetName = sheetName + "(" + (sheets.size() + 1) + ")";
        }

        this.os.write(sheets.size() > 0 ? Appender.nextSheet(sheetName) : Appender.sheet(sheetName));

        if (this.headers != null) {
            this.os.write(Appender.header(this.headers));
        }

        sheets.add(sheetName);
        return this;
    }

    public XmlExcel start(String sheetName) throws IOException {
        this.os.write(Appender.start());
        this.os.write(Appender.styles(this.styles));
        return this.addSheet(sheetName);
    }

    /**
     * 开始
     * @throws IOException
     */
    public XmlExcel start() throws IOException {
        return this.start(null);
    }

    public XmlExcel appendBody(Collection<? extends Object> dataList) throws IOException {
        if(dataList == null || dataList.isEmpty()) {
            return this;
        }
        //如果未设置头，则根据数据解析一个头出来
        if (headers == null) {
            headers = Appender.parseHeaders(dataList);
        }

        //解析头和数据的maping
        if (!parsed) {
            DataFormatterParser.parse(headers, dataList.iterator().next());
            parsed = true;
        }

        this.os.write(Appender.rows(headers, dataList));
        return this;
    }

    public XmlExcel finish() throws IOException {
        this.os.write(Appender.end());
        return this;
    }

    @Override
    public void close() throws IOException {
        if (this.os != null) {
            this.os.close();
        }
    }

    public XmlExcel setHeaders(List<Header> headers) {
        this.parsed = false;
        this.headers = headers;
        return this;
    }
}
