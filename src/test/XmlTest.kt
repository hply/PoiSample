package test

import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

const val out_xml_demo_path = "src/strings.xml"
const val in_xml_demo_path = "src/strings_backup.xml"

fun main() {
    val dom = load(in_xml_demo_path)
    if (null == dom) {
        print("excel文件不存在或者不可读！")
        return
    }
    val resourcesElement = dom.rootElement
    val elements = resourcesElement.elements("string")
    val list = arrayListOf<Pair<String, String>>()
    elements.forEach {
        val lanCode: String = it.attributeValue("name")
        val lanValue: String = it.text
        list.add(Pair(lanCode, lanValue))
    }
    val newList = arrayListOf<Pair<String, String>>()

    var tempLength = 10
    while (true) {
        if (newList.size in 1..100) {
            break
        }
        newList.clear()
        list.filter {
            it.second.length < tempLength
        }.forEach {
            newList.add(it)
        }
        tempLength--
    }
    elements.forEach {
        resourcesElement.remove(it)
    }
    newList.forEach {
        val stringEle: Element = resourcesElement.addElement("string")
        stringEle.addAttribute("name", it.first)
        stringEle.text = it.second
    }
    //命名空间
//    resourcesElement.addNamespace("android","http://schemas.android.com/apk/res/android")
//    resourcesElement.addNamespace("app","http://schemas.android.com/apk/res-auto")
//    resourcesElement.addNamespace("tools","http://schemas.android.com/tools")
    write(dom, out_xml_demo_path)
}

fun load(fileName: String): Document? {
    var document: Document? = null
    try {
        document = SAXReader().read(File(fileName)) // 读取XML文件,获得document对象
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return document
}

fun write(dom: Document, xmlFilePath: String) {
    val format: OutputFormat = OutputFormat.createPrettyPrint()
    format.encoding = "utf-8"
    val streamWriter = OutputStreamWriter(FileOutputStream(xmlFilePath), "UTF-8")
    val writer = XMLWriter(streamWriter, format)
    writer.write(dom)
    writer.flush()
    writer.close()
}