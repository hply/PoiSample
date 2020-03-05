package cn.iyuxuan.poi.bean

data class LangBean(
        val code: String,
        val value: String
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LangBean

        if (code != other.code) return false
        return true
    }

    override fun hashCode(): Int {
        return code.hashCode()
    }
}