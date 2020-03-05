package test

import cn.iyuxuan.poi.bean.LangBean
import cn.iyuxuan.poi.ui.ToastUtils
import org.dom4j.Document
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object NewXmlUtils {

    private const val ROOT_TAG = "resources"

    var removeDuplicates = true

    fun write(dirPath: String, codeLanList: MutableList<LangBean>) {
        if (codeLanList.isEmpty()) {
            ToastUtils.toast("导入数据为空，请检查excel文件或者读取逻辑")
            return
        }
        //文件中的去重
        val newList = codeLanList.myDistinctBy {
            it.code
        }
        val xmlFileDir = File(dirPath)
        if (!xmlFileDir.exists()) {
            //新增文件
            val makeSuccess = xmlFileDir.mkdirs()
            if (!makeSuccess) {
                throw IllegalArgumentException("创建存放目录异常")
            }
        }
        val xmlFile = File(xmlFileDir, "strings.xml")
        //1.创建一个Document对象
        val doc = if (xmlFile.exists()) {
            try {
                SAXReader().read(xmlFile)
            } catch (e: Exception) {
                //现存的xml格式异常，删除新建
                DocumentHelper.createDocument()
            }
        } else {
            DocumentHelper.createDocument()
        }
        //2.创建/获取resources根对象
        val root = if (xmlFile.exists()) {
            val curRoot: Element? = doc.rootElement
            if (curRoot?.name == ROOT_TAG) {
                curRoot
            } else {
                doc.clearContent()
                doc.addElement(ROOT_TAG)
            }
        } else {
            doc.addElement(ROOT_TAG)
        }
        if (removeDuplicates) {
            val elements = root.elementIterator("string")
            while (elements.hasNext()) {
                val it = elements.next()
                val code = it.attributeValue("name")
                if (codeExists(newList, code)) {
                    doc.remove(it)
                    elements.remove()
                }
            }
        }
        newList.forEach {
            val textElement = root.addElement("string")
            textElement.addAttribute("name", it.code)
            textElement.text = it.value
        }
        writeDoc(xmlFile, doc)
    }

    private fun writeDoc(xmlFile: File, doc: Document) {
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
            if (it.code == code) {
                return true
            }
        }
        return false
    }

    inline fun <T, K> Iterable<T>.myDistinctBy(selector: (T) -> K): List<T> {
        //参考kotlin#distinctBy,使用set保存key,比contain效率高
        val set = HashSet<K>()
        val list = ArrayList<T>()
        for (e in this) {
            val key = selector(e)
            if (!set.add(key)) {
                //e这个对象必须实现equals和hasCode方法，不然不知道remove谁
                list.remove(e)
            }
            list.add(e)
        }
        return list
    }
}