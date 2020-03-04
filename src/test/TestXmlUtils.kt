package test

import cn.iyuxuan.poi.bean.LangBean
import org.dom4j.DocumentHelper
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object TestXmlUtils {

    private const val ROOT_TAG = "resources"

    var removeDuplicates = true

    fun write(path: String, fileName: String, codeLanList: List<LangBean>) {
        val xmlFileDir = File(path)
        if (!xmlFileDir.exists()) {
            //新增文件
            val makeSuccess = xmlFileDir.mkdirs()
            if (!makeSuccess) {
                throw IllegalArgumentException("创建存放目录异常")
            }
        }
        val xmlFile = File(xmlFileDir, if (fileName.endsWith(".xml")) {
            fileName
        } else {
            "$fileName.xml"
        })
        //1.创建一个Document对象
        val doc = if (xmlFile.exists()) {
            SAXReader().read(xmlFile)
        } else {
            DocumentHelper.createDocument()
        }
        //2.创建/获取resources根对象
        val root = if (xmlFile.exists()) {
            val curRoot = doc.rootElement
            if (curRoot.name == ROOT_TAG) {
                curRoot
            } else {
                doc.remove(curRoot)
                doc.addElement(ROOT_TAG)
            }
        } else {
            doc.addElement(ROOT_TAG)
        }
        if (removeDuplicates) {
            val elements = root.elementIterator("string")
            if (elements.hasNext()){
                val element = elements.next()
                val code = element.attributeValue("name")
                if (codeExists(codeLanList,code)){
                    root.remove(element)
                }
            }
        }
        codeLanList.forEach {
            val textElement = root.addElement("string")
            textElement.addAttribute("name", it.code)
            textElement.text = it.value
        }
        val format = OutputFormat.createPrettyPrint()
        format.encoding = "utf-8"
        var xw: XMLWriter? = null
        var stream: FileOutputStream? = null
        try {
            stream = FileOutputStream(xmlFile)
            xw = XMLWriter(stream, format).apply {
                isEscapeText = true
                write(doc)
                flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                xw?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                stream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun codeExists(codeLanList: List<LangBean>, code: String?): Boolean {
        codeLanList.forEach {
            if (it.code == code){
                return true
            }
        }
        return false
    }
}