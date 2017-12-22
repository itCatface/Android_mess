package cc.catface.kotlin.domain

/**
 * Created by Administrator on 2017/11/30/030.
 */

data class JokePicResult(
        val showapi_res_code: Int,
        val showapi_res_error: String,
        val showapi_res_body: JokePicBody
)

data class JokePicBody(
        val allNum: Int,
        val allPages: Int,
        val contentlist: List<JokePic>,
        val currentPage: Int,
        val maxResult: Int
)

data class JokePic(
        val ct: String,
        val img: String,
        val title: String,
        val type: Int
)