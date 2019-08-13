package cn.iyuxuan.poi;

import cn.iyuxuan.poi.utils.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.function.Consumer;

public class Test {

    public static void main(String[] args) throws Exception {
        JSONObject configInfo = new JSONObject(StringUtils.readFile2String("language-map.json"));
        String inputPath = configInfo.get("input_file").toString();
        String outputPath = configInfo.get("output_path").toString();
        JSONObject languageMap = configInfo.getJSONObject("language-map");

        XSSFSheet excelSheet = new XSSFWorkbook(OPCPackage.open(inputPath)).getSheetAt(0);
//        HSSFSheet excelSheet = new HSSFWorkbook(new FileInputStream(inputPath)).getSheetAt(0);
        int rowNum = excelSheet.getLastRowNum();
//        int columnNum = excelSheet.getRow(0).getPhysicalNumberOfCells();
        HashMap<String, TreeMap<String, String>> newLanguageMap = new HashMap<>();
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

            TreeMap<String, String> codeValueMap = new TreeMap<>();
            for (int rowIndex = 1; rowIndex < rowNum; rowIndex++) {
                //语言包code所在的行对象
                XSSFRow codeXssfRow = excelSheet.getRow(rowIndex);
                String langCode = codeXssfRow.getCell(0).getStringCellValue();
                codeValueMap.put(langCode,codeXssfRow.getCell(langColumnIndex).getStringCellValue());
            }
            newLanguageMap.put(fileDirSuffix,codeValueMap);
        }
        StringUtils.log(new JSONObject(newLanguageMap));
    }
}