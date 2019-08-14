package cn.iyuxuan.poi.utils;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TreeMap;

public class ExcelUtils {

    /**
     * @param excelFilePath excel文件所在的文件路径
     * @param languageMap   语言code描述和国家编码对应的关系
     */
    public static HashMap<String, TreeMap<String, String>> readExcel(String excelFilePath,
                                                                     JSONObject languageMap) throws Exception {
        XSSFSheet excelSheet = new XSSFWorkbook(OPCPackage.open(excelFilePath)).getSheetAt(0);
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
                XSSFCell cell = codeXssfRow.getCell(langColumnIndex);
                if (null != cell && StringUtils.isNotEmpty(cell.getStringCellValue())) {
                    codeValueMap.put(langCode, cell.getStringCellValue());
                }
            }
            newLanguageMap.put(fileDirSuffix, codeValueMap);
        }
        return newLanguageMap;
    }

}
