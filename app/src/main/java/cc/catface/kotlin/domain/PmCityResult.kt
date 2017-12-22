package cc.catface.kotlin.domain

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */

data class PmCityResult(
        val showapi_res_code: Int,
        val showapi_res_error: String,
        val showapi_res_body: PmCityBody
)

data class PmCityBody(
        val ret_code: Int,
        val pm: PmCity,
        val siteList: List<Site>
)

data class PmCity(
        val num: String,
        val so2: String,
        val o3: String,
        val area_code: String,
        val pm2_5: String,
        val ct: String,
        val primary_pollutant: String,
        val co: String,
        val area: String,
        val no2: String,
        val aqi: String,
        val quality: String,
        val pm10: String,
        val o3_8h: String
)

data class Site(
        val site_name: String,
        val co: String,
        val so2: String,
        val o3: String,
        val no2: String,
        val aqi: String,
        val quality: String,
        val pm10: String,
        val pm2_5: String,
        val o3_8h: String,
        val primary_pollutant: String,
        val ct: String
)