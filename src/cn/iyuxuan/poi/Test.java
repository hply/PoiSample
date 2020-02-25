package cn.iyuxuan.poi;

import cn.iyuxuan.poi.map.ArrayMap;
import cn.iyuxuan.poi.utils.ExcelUtils;
import cn.iyuxuan.poi.utils.StringUtils;
import cn.iyuxuan.poi.utils.XmlResourcesUtils;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;

public class Test {

    public static void main(String[] args) throws Exception {
        JSONObject configInfo = new JSONObject(StringUtils.readFile2String("language-map_test.json"));
        String inputPath = configInfo.get("input_file").toString();
        String outputPath = configInfo.get("output_path").toString();
        JSONObject languageMap = configInfo.getJSONObject("language-map");
        //读取配置文件input_file路径下的excel
        ArrayMap<String, ArrayMap<String, String>> excelMap = ExcelUtils.readExcel(inputPath, languageMap);
        //解析现有的xml文件中的多语言code
        ArrayMap<String, ArrayMap<String, String>> currentXml2Map = XmlResourcesUtils.readXml2Map(outputPath);
        excelMap.forEach((countryKey, stringStringArrayMap) -> {
            if (currentXml2Map.containsKey(countryKey)){
                currentXml2Map.get(countryKey).putAllArrayMap(stringStringArrayMap);
            }else {
                currentXml2Map.put(countryKey,stringStringArrayMap);
            }
        });
        for (Map.Entry<String, ArrayMap<String,String>> entry : currentXml2Map.entrySet()) {
            XmlResourcesUtils.writeXmlString(outputPath,entry.getKey(),entry.getValue());
        }
        StringUtils.log(new JSONObject(currentXml2Map));
        //

    }
}