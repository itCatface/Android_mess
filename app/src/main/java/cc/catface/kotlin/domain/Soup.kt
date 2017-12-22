package cc.catface.kotlin.domain

/**
 * Created by yhao on 17-9-8.
 */

data class SoupResult(val showapi_res_code: String,
                      val showapi_res_error: String,
                      val showapi_res_body: SoupBody)

data class SoupBody(val ret_code: String,
                    val ret_message: String,
                    val data: List<Soup>)

data class Soup(val english: String,
                val chinese: String)
