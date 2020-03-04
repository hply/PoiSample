package test

import cn.iyuxuan.poi.toJson
import cn.iyuxuan.poi.utils.ExcelUtils
import cn.iyuxuan.poi.utils.StringUtils
import org.json.JSONObject

fun main() {
    val configInfo = JSONObject(StringUtils.readFile2String("language-map_test.json"))
    val inputPath = configInfo["input_file"].toString()
    val outputPath = configInfo["output_path"].toString()
    val languageMap = configInfo.getJSONObject("language-map")
    //读取配置文件input_file路径下的excel
    val excelMap = ExcelUtils.readExcel(inputPath, languageMap)
    //println(excelMap.toJson())

    val testEn = excelMap["values"]?.map {
        Pair<String, String>(it.key, it.value)
    }
    testEn?.let {
        TestXmlUtils.write("src/test8888","my_xml",it)
    }

}