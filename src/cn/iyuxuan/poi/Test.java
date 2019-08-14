package cn.iyuxuan.poi;

import cn.iyuxuan.poi.utils.ExcelUtils;
import cn.iyuxuan.poi.utils.StringUtils;
import cn.iyuxuan.poi.utils.XmlResourcesUtils;
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
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class Test {

    public static void main(String[] args) throws Exception {
        JSONObject configInfo = new JSONObject(StringUtils.readFile2String("language-map.json"));
        String inputPath = configInfo.get("input_file").toString();
        String outputPath = configInfo.get("output_path").toString();
        JSONObject languageMap = configInfo.getJSONObject("language-map");
        //读取配置文件input_file路径下的excel
        HashMap<String, TreeMap<String, String>> excelMap = ExcelUtils.readExcel(inputPath, languageMap);
        //解析现有的xml文件中的多语言code
        HashMap<String, TreeMap<String, String>> currentXml2Map = XmlResourcesUtils.readXml2Map(outputPath);
        currentXml2Map.putAll(excelMap);
        for (Map.Entry<String, TreeMap<String,String>> entry : currentXml2Map.entrySet()) {
            XmlResourcesUtils.writeXmlString(outputPath,entry.getKey(),entry.getValue());
        }
        StringUtils.log(new JSONObject(currentXml2Map));
    }
}