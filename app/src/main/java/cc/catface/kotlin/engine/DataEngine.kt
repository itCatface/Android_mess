package cc.catface.kotlin.engine

import cc.catface.clibrary.util.extension.d
import cc.catface.clibrary.project.yiyuan.Constant
import cc.catface.kotlin.domain.*
import com.google.gson.Gson
import org.json.JSONObject
import java.net.URL

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
object DataEngine {


    /****************************************************************************易源数据
     * 笑话大全
     */
    fun getJokeData(page: Int, maxResult: Int = 10) = Gson().fromJson(URL(Constant.yyXHDQ(page, maxResult)).readText(), JokeResult::class.java).showapi_res_body.contentlist


    /**
     * 静态搞笑图
     */
    fun getJokePicData(page: Int, maxResult: Int = 5) = Gson().fromJson(URL(Constant.yyXHDQPIC(page, maxResult)).readText(), JokePicResult::class.java).showapi_res_body.contentlist


    /**
     * 动态搞笑图
     */
    fun getGifData(page: Int, maxResult: Int = 5) = Gson().fromJson(URL(Constant.yyXHDQGIF(page, maxResult)).readText(), GifResult::class.java).showapi_res_body.contentlist


    /**
     * 英文励志
     *
     * @desc 虽然设置30条但每次后台最多只返回十条左右
     */
    fun getSoupData(count: Int = 30) = Gson().fromJson(URL(Constant.yyYWLZ(count)).readText(), SoupResult::class.java).showapi_res_body.data


    /**
     * 花瓣福利
     */
    fun getPetalData(type: Int, page: Int, num: Int = 10): ArrayList<Petal>? {

        val iterator = Gson().fromJson(URL(Constant.yyHBFL(type, num, page)).readText(), PetalResult::class.java).showapi_res_body.entrySet().iterator()

        val pics = ArrayList<Petal>()

        while (iterator.hasNext()) {
            val element = iterator.next()
            try {
                val pic = Gson().fromJson(element.value, Petal::class.java)
                pics.add(pic)
            } catch (e: Exception) {
                d(e.toString())
            }
        }

        return if (pics.size > 0) pics else null
    }


    /**
     * pm2.5
     */
    fun getPmRankData() = Gson().fromJson(URL(Constant.yyPMRank()).readText(), PmRankResult::class.java).showapi_res_body.list

    fun getPmCityList(): List<String>? {
        var cityList = ArrayList<String>()
        var cityArr: Array<String>

        val list = Gson().fromJson(URL(Constant.yyPMRank()).readText(), PmRankResult::class.java).showapi_res_body.list
        for (x in list) {
            cityList!!.add(x.area)
        }

        return cityList
    }

    fun getPmCityData(city: String = "合肥") = Gson().fromJson(URL(Constant.yyPMCity(city)).readText(), PmCityResult::class.java).showapi_res_body


    /******************************************************************************谷歌API
     * 所在省市
     */
    fun localCity() = JSONObject(URL(Constant.URL_LOCAL()).readText()).optString("city")
}