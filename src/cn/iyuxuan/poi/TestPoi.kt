package cn.iyuxuan.poi

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xssf.usermodel.XSSFWorkbook

fun main(args: Array<String>) {
    val excelPath = "language_test.xlsx"
    val excelSheet = XSSFWorkbook(OPCPackage.open(excelPath)).getSheetAt(0)
    val row = excelSheet.getRow(3)
    excelSheet.rowIterator().forEach {

    }
    for (i in 0 until excelSheet.lastRowNum){
        println(excelSheet.getRow(i).getCell(0).stringCellValue)
    }
    row.forEachIndexed { index, cell ->
        println("index=$index;value=${cell.stringCellValue}")
    }
}