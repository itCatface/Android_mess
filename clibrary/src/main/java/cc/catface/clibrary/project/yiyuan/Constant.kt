package cc.catface.clibrary.project.yiyuan

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class Constant {
    companion object {

        /**
         * 易源数据(showapi.com)
         */
        private val YIYUAN_APPID = "51028"
        private val YIYUAN_SECRET = "13114fab5f6845f4b65c18b4ba910fa6"

        fun yyPMCity(city: String = "北京", id: String = "104-29") = "http://route.showapi.com/" + id + "?showapi_appid=${YIYUAN_APPID}&showapi_sign=${YIYUAN_SECRET}" + "&city=$city"
        fun yyPMRank(id: String = "104-41") = "http://route.showapi.com/" + id + "?showapi_appid=${YIYUAN_APPID}&showapi_sign=${YIYUAN_SECRET}"


        fun yyXHDQ(page: Int, maxResult: Int = 10, id: String = "341-1") = "http://route.showapi.com/" + id + "?showapi_appid=${YIYUAN_APPID}&showapi_sign=${YIYUAN_SECRET}" + "&page=$page&maxResult=$maxResult"
        fun yyXHDQPIC(page: Int, maxResult: Int = 5, id: String = "341-2") = "http://route.showapi.com/" + id + "?showapi_appid=${YIYUAN_APPID}&showapi_sign=${YIYUAN_SECRET}" + "&page=$page&maxResult=$maxResult"
        fun yyXHDQGIF(page: Int, maxResult: Int = 5, id: String = "341-3") = "http://route.showapi.com/" + id + "?showapi_appid=${YIYUAN_APPID}&showapi_sign=${YIYUAN_SECRET}" + "&page=$page&maxResult=$maxResult"
        fun yyYWLZ(count: Int = 15, id: String = "1211-1") = "http://route.showapi.com/" + id + "?showapi_appid=${YIYUAN_APPID}&showapi_sign=${YIYUAN_SECRET}" + "&count=$count"

        fun yyHBFL(type: Int, num: Int, page: Int, id: String = "819-1") = "http://route.showapi.com/" + id + "?showapi_appid=${YIYUAN_APPID}&showapi_sign=${YIYUAN_SECRET}" + "&type=$type&num=$num&page=$page"


        /**
         * 获取所在省市
         */
        fun URL_LOCAL() = "http://restapi.amap.com/v3/ip?key=49425b5df5867f5d50b272cc69923ccc"

    }
}