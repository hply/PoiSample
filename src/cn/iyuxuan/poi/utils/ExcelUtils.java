package cn.iyuxuan.poi.utils;

import cn.iyuxuan.poi.map.ArrayMap;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public class ExcelUtils {

    /**
     * @param excelFilePath excel文件所在的文件路径
     * @param languageMap   语言code描述和国家编码对应的关系
     */
    public static ArrayMap<String, ArrayMap<String, String>> readExcel(String excelFilePath,
                                                                       JSONObject languageMap) throws Exception {
        File file = new File(excelFilePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("Excel文案路径不存在[" + excelFilePath + "]");
        }
        OPCPackage aPackage = OPCPackage.open(excelFilePath);
        XSSFSheet excelSheet = new XSSFWorkbook(aPackage).getSheetAt(0);
//        HSSFSheet excelSheet = new HSSFWorkbook(new FileInputStream(inputPath)).getSheetAt(0);
        int rowNum = excelSheet.getLastRowNum() + 1;
//        int columnNum = excelSheet.getRow(0).getPhysicalNumberOfCells();
        ArrayMap<String, ArrayMap<String, String>> newLanguageMap = new ArrayMap<>();
        //获取第一行国家语言
        XSSFRow languageRow = excelSheet.getRow(0);
        //根据国家遍历
        for (int langColumnIndex = 1; langColumnIndex < languageRow.getLastCellNum(); langColumnIndex++) {
            String langName = languageRow.getCell(langColumnIndex).getStringCellValue();
            if (StringUtils.isEmpty(langName) || !languageMap.has(langName)) {
                continue;
            }
            String countryCode = languageMap.optString(langName);
            String fileDirSuffix = countryCode.isEmpty() ? "values" : ("values-" + countryCode);
            //StringUtils.log(fileDirSuffix);

            ArrayMap<String, String> codeValueMap = new ArrayMap<>();
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                //语言包code所在的行对象
                XSSFRow codeXssfRow = excelSheet.getRow(rowIndex);
                String langCode = codeXssfRow.getCell(0).getStringCellValue();
                XSSFCell cell = codeXssfRow.getCell(langColumnIndex);
                if (null != cell && StringUtils.isNotEmpty(cell.getStringCellValue())) {
                    codeValueMap.put(langCode, cell.getStringCellValue());
                }
            }
            newLanguageMap.put(fileDirSuffix, codeValueMap);
        }
        return newLanguageMap;
    }

    /**
     * map导出成excel
     */
    public static void map2Excel(ArrayMap<String, String> treeMap,
                                 String path) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();//创建Excel文件(Workbook)
        HSSFSheet sheet = workbook.createSheet("Sheet");//创建工作表(Sheet)
        FileOutputStream out = new FileOutputStream(path);
        for (int i = 0; i < treeMap.size(); i++) {
            String key = treeMap.keyAt(i);
            String value = treeMap.valueAt(i);
            HSSFRow row = sheet.createRow(i);
            HSSFCell cellCode = row.createCell(0);
            HSSFCell cellValue = row.createCell(1);
            cellCode.setCellValue(key);
            cellValue.setCellValue(value);
        }
        workbook.write(out);//保存Excel文件
    }

    /**
     * @param lanMap 国家-语言包k-v
     */
    public static void export2Excel(ArrayMap<String, ArrayMap<String, String>> lanMap,
                                    String path) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();//创建Excel文件(Workbook)
        HSSFSheet sheet = workbook.createSheet("Sheet");//创建工作表(Sheet)
        FileOutputStream out = new FileOutputStream(path);
        //创建title行
        HSSFRow countryTitle = sheet.createRow(0);
        HSSFCell zer0_zero = countryTitle.createCell(0);
        zer0_zero.setCellValue("code");
        //创建国家,title
        for (int lanIndex = 0; lanIndex < lanMap.size(); lanIndex++) {
            HSSFCell titleCell = countryTitle.createCell(lanIndex + 1);
            titleCell.setCellValue(lanMap.keyAt(lanIndex));
            //
            ArrayMap<String, String> currLanMap = lanMap.valueAt(lanIndex);
            for (int codeIndex = 0; codeIndex < currLanMap.size(); codeIndex++) {
                String code = currLanMap.keyAt(codeIndex);
                String value = currLanMap.valueAt(codeIndex);
                HSSFRow codeRow = sheet.getRow(codeIndex + 1);
                if (codeRow == null) {
                    codeRow = sheet.createRow(codeIndex + 1);
                }
                HSSFCell codeRowCell = codeRow.getCell(0);
                if (codeRowCell == null) {
                    codeRowCell = codeRow.createCell(0);
                }
                codeRowCell.setCellValue(code);
                HSSFCell valueCell = codeRow.createCell(lanIndex + 1);
                valueCell.setCellValue(value);
            }
        }
        workbook.write(out);//保存Excel文件
    }
}
