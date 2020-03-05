package test

import cn.iyuxuan.poi.bean.LangBean
import test.NewXmlUtils.myDistinctBy

fun main() {
    val list = listOf(
            LangBean("1","1"),
            LangBean("2","2"),
            LangBean("3","3"),
            LangBean("1","11"),
            LangBean("1","111"),
            LangBean("2","22"),
            LangBean("3","00000000")
    )
    val newList = list.myDistinctBy{
        it.code
    }
    newList.forEach {
        println("code=${it.code};\tvalue=${it.value}")
    }
}