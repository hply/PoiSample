package test

import org.dom4j.Document
import org.dom4j.io.SAXReader
import java.io.File

fun main() {
    var document: Document? = null
    try {
        document = SAXReader().read(File("src/strings.xml")) // 读取XML文件,获得document对象
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

}