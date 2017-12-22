package cc.catface.kotlin.domain

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */

data class PmRankResult(
        val showapi_res_code: Int,
        val showapi_res_error: String,
        val showapi_res_body: PmRankBody
)

data class PmRankBody(
        val ret_code: Int,
        val list: List<PmRank>
)

data class PmRank(
        val o3: String,
        val area_code: String,
        val pm2_5: String,
        val primary_pollutant: String,
        val ct: String,
        val num: String,
        val co: String,
        val area: String,
        val no2: String,
        val aqi: String,
        val quality: String,
        val pm10: String,
        val o3_8h: String
)