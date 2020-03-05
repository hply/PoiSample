package test

import cn.iyuxuan.poi.bean.LangBean
import cn.iyuxuan.poi.utils.ExcelUtils
import cn.iyuxuan.poi.utils.StringUtils
import org.json.JSONObject

fun main() {
    val configInfo = JSONObject(StringUtils.readFile2String("language-map_test.json"))
    val inputPath = configInfo["input_file"].toString()
    val outputPath = configInfo["output_path"].toString()
    val languageMap = configInfo.getJSONObject("language-map")
    //读取配置文件input_file路径下的excel
    val excelMap = NewExcelUtils.readExcel2Map(inputPath, languageMap)
    //println(excelMap.toJson())


    val testEn = excelMap["values"]
//    testEn?.add(LangBean("page_order_detail_complain_autoinfo_msg3","去重语言包新翻译"))
    testEn?.add(LangBean("新增code","新增语言包"))
    testEn?.let {
        NewXmlUtils.write("src/test8888", it)
    }

}