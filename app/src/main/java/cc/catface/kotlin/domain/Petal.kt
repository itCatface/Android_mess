package cc.catface.kotlin.domain

import com.google.gson.JsonObject

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */

data class PetalResult(val showapi_res_code: String,
                       val showapi_res_error: String,
                       val showapi_res_body: JsonObject)

data class Petal(val title: String,
                 val thumb: String,
                 val url: String)
