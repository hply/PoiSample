package cn.iyuxuan.poi

import org.json.JSONObject
import org.json.JSONString

fun Map<String,Any>?.toJson(): String {
    if (this == null) {
        return ""
    }
    return JSONObject(this).toString()
}