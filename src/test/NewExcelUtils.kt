package test

import cn.iyuxuan.poi.bean.LangBean
import cn.iyuxuan.poi.map.ArrayMap
import cn.iyuxuan.poi.ui.ToastUtils.toast
import cn.iyuxuan.poi.utils.StringUtils
import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.json.JSONObject
import java.io.File

object NewExcelUtils {

    /**
     * @param excelFilePath 需要读取的语言包excel
     * @param languageMap 配置文件
     * */
    fun readExcel2Map(excelFilePath: String,
                      languageMap: JSONObject): ArrayMap<String, ArrayList<LangBean>> {
        val file = File(excelFilePath)
        if (!file.exists()) {
            toast("Excel文案路径不存在[$excelFilePath]")
        }
        val aPackage = OPCPackage.open(excelFilePath)
        //获取excel的第一个sheet
        val excelSheet = XSSFWorkbook(aPackage).getSheetAt(0)
        //获取总行数
        val rowNum = excelSheet.lastRowNum + 1
        val result = ArrayMap<String, ArrayList<LangBean>>()
        //获取第一行国家语言,所以人工校验一下产品给的excel第一行不要留空行
        val languageRow = excelSheet.getRow(0)
        //根据国家竖列遍历excel，注意index需要从1开始，0列，全是code名
        for (langColumnIndex in 1 until languageRow.lastCellNum) {
            //语言包国家名，e.g:en/ar/tw
            val langName = languageRow.getCell(langColumnIndex).stringCellValue
            //跳过空列和配置中不存在的列
            if (StringUtils.isEmpty(langName) || !languageMap.has(langName)) {
                continue
            }
            //取出配置文件和excel国家的映射编码，要用这个创建文件夹
            val countryCode = languageMap.optString(langName)
            val fileDirSuffix = if (countryCode.isEmpty()) "values" else "values-$countryCode"
            val values = ArrayList<LangBean>()
            for (rowIndex in 1 until rowNum) {
                //语言包code所在的行对象
                val codeXssRow = excelSheet.getRow(rowIndex)
                val langCode = codeXssRow.getCell(0).stringCellValue
                val cell = codeXssRow.getCell(langColumnIndex)
                if (null != cell && StringUtils.isNotEmpty(cell.stringCellValue)) {
                    values.add(LangBean(langCode,cell.stringCellValue))
                }
            }
            result[fileDirSuffix] = values
        }
        return result
    }
}