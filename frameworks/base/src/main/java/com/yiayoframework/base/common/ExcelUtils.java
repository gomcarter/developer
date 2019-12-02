package com.yiayoframework.base.common;

import com.yiayoframework.base.exception.CustomException;
import jxl.CellView;
import jxl.Range;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * @authher 李银 2017年12月2日 08:10:35
 */
public class ExcelUtils {

    public static List<Map<String, String>> upload(MultipartFile multipartFile) throws IOException {
        return upload(multipartFile.getInputStream(), multipartFile.getOriginalFilename());
    }

    protected static List<Map<String, String>> upload(InputStream is, String fileName) {
        String fileSuffix = YiayoStringUtils.getFileSuffix(fileName);
        if (!"xls".contains(fileSuffix)) {
            throw new CustomException("excel解析失败，请上传excel2003格式的文档！");
        }
        List<Map<String, String>> dataList = null;
        try {
            dataList = ExcelUtils.readWithHeader(is);
        } catch (Exception e1) {
            throw new CustomException("excel解析失败，请上传excel2003格式的文档！", e1);
        }
        if (CollectionUtils.isEmpty(dataList)) {
            throw new CustomException("上传的文件无内容或有误！");
        }
        return dataList;
    }

    public static List<Map<String, String>> readWithHeader(File file, Integer sheetNumber) throws BiffException, IOException {

        Workbook book = Workbook.getWorkbook(file);
        Sheet sheet = book.getSheet(sheetNumber);
        return _readSheet(sheet);
    }

    public static List<Map<String, String>> readWithHeader(File file, String sheetName) throws BiffException, IOException {
        Workbook book = Workbook.getWorkbook(file);
        for (Sheet sheet : book.getSheets()) {
            if (sheetName.equals(sheet.getName().trim())) {
                return _readSheet(sheet);
            }
        }
        return null;
    }

    private static List<Map<String, String>> _readSheet(Sheet sheet) {

        int rows = sheet.getRows();
        int cos = sheet.getColumns();

        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Map<Integer, String> indexMap = new HashMap<Integer, String>();
        for (int j = 0; j < cos; j++) {
            String header = sheet.getCell(j, 0).getContents();
            if (StringUtils.isNotBlank(header)) {
                indexMap.put(j, StringUtils.trim(header));
            }
        }

        boolean hasMerged = false;
        Map<String, String> mergedValue = new HashMap<String, String>();
        Range[] ranges = sheet.getMergedCells();
        if (ranges != null) {
            for (Range space : ranges) {
                int rf = space.getTopLeft().getRow();
                int rt = space.getBottomRight().getRow();

                int cf = space.getTopLeft().getColumn();
                int ct = space.getBottomRight().getColumn();

                String spaceValue = space.getTopLeft().getContents();

                if (StringUtils.isNotBlank(spaceValue)) {
                    hasMerged = true;
                    for (int m = rf; m <= rt; m++) {
                        for (int n = cf; n <= ct; n++) {
                            mergedValue.put(m + "-" + n, spaceValue.trim());
                        }
                    }
                }
            }
        }

        for (int i = 1; i < rows; i++) {
            Map<String, String> rowMap = new LinkedHashMap<String, String>();
            boolean add = false;
            for (int j = 0; j < cos; j++) {
                String header = indexMap.get(j);
                if (header != null) {
                    String cellValue = StringUtils.trim(sheet.getCell(j, i).getContents());
                    if (StringUtils.isNotBlank(cellValue)) {
                        rowMap.put(header, cellValue);
                        add = true;
                    } else {
                        if (hasMerged) {
                            String key = i + "-" + j;
                            String value = mergedValue.get(key);
                            rowMap.put(header, value);
                            add = true;
                        } else {
                            rowMap.put(header, null);
                        }
                    }
                }
            }
            if (add/* !rowMap.isEmpty() */) {
                result.add(rowMap);
            }
        }
        return result;
    }

    public static List<Map<String, String>> readWithHeader(File file) throws BiffException, IOException {
        Workbook book = Workbook.getWorkbook(file);
        Sheet sheet = book.getSheet(0);
        return _readSheet(sheet);
    }

    public static List<Map<String, String>> readWithHeader(InputStream is) throws IOException, BiffException {
        Workbook book = Workbook.getWorkbook(is);
        Sheet sheet = book.getSheet(0);
        return _readSheet(sheet);
    }

    public static void write(OutputStream out, String[][] cells) throws IOException, RowsExceededException, WriteException {
        WritableWorkbook book = Workbook.createWorkbook(out);
        WritableSheet sheet = book.createSheet("sheet1", 0);

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Label label = new Label(j, i, cells[i][j]);
                sheet.addCell(label);
            }
        }
        book.write();
        book.close();
    }

    public static void write(OutputStream out, List<Map<String, String>> dataList) throws IOException, RowsExceededException, WriteException {
        WritableWorkbook book = Workbook.createWorkbook(out);
        WritableSheet sheet = book.createSheet("sheet1", 0);

        if (dataList != null && !dataList.isEmpty()) {
            //取列数最大的那一行,用于初始化列头
            int maxRow = 0;
            int maxCols = 0;
            int current = 0;
            for (Map<String, String> rowMap : dataList) {
                if (rowMap.keySet().size() > maxCols) {
                    maxCols = rowMap.keySet().size();
                    maxRow = current;
                }
                current++;
            }
            //初始化header,
            int index = 0;
            Map<String, Integer> headerMap = new HashMap<String, Integer>();
            for (String header : dataList.get(maxRow).keySet()) {
                Label label = new Label(index, 0, header);
                headerMap.put(header, index);
                sheet.addCell(label);
                index++;
            }

            //行
            int rowIndex = 1;
            for (Map<String, String> rowMap : dataList) {
                for (String header : rowMap.keySet()) {
                    Label label = new Label(headerMap.get(header), rowIndex, rowMap.get(header));
                    sheet.addCell(label);
                }
                rowIndex++;
            }

            for (Integer col : headerMap.values()) {
                CellView view = new CellView();
                view.setAutosize(true);
                sheet.setColumnView(col, view);
            }
        }
        book.write();
        book.close();
    }

    public static void writeObject(OutputStream out, List<Map<String, Object>> dataList) throws IOException, RowsExceededException, WriteException {
        WritableWorkbook book = Workbook.createWorkbook(out);
        WritableSheet sheet = book.createSheet("sheet1", 0);
        //sheet.mergeCells(arg0, arg1, arg2, arg3);
        if (dataList != null && !dataList.isEmpty()) {
            //取列数最大的那一行,用于初始化列头
            int maxRow = 0;
            int maxCols = 0;
            int current = 0;
            for (Map<String, Object> rowMap : dataList) {
                if (rowMap.keySet().size() > maxCols) {
                    maxCols = rowMap.keySet().size();
                    maxRow = current;
                }
                current++;
            }
            //初始化header,
            int index = 0;
            Map<String, Integer> headerMap = new HashMap<String, Integer>();
            for (String header : dataList.get(maxRow).keySet()) {
                Label label = new Label(index, 0, header);
                headerMap.put(header, index);
                sheet.addCell(label);
                index++;
            }

            //行
            int rowIndex = 1;
            for (Map<String, Object> rowMap : dataList) {
                for (String header : rowMap.keySet()) {
                    Object value = rowMap.get(header);
                    if (value != null) {
                        WritableCell cell = null;
                        if (value instanceof Number) {
                            cell = new jxl.write.Number(headerMap.get(header), rowIndex, Double.valueOf(value.toString()));
                        } else if (value instanceof Date) {
                            cell = new jxl.write.DateTime(headerMap.get(header), rowIndex, (Date) value);
                        } else {
                            cell = new Label(headerMap.get(header), rowIndex, value.toString().trim());
                        }

                        sheet.addCell(cell);
                    }
                }
                rowIndex++;
            }
            for (Integer col : headerMap.values()) {
                CellView view = new CellView();
                view.setAutosize(true);
                sheet.setColumnView(col, view);
            }
        }
        book.write();
        book.close();
    }
}
