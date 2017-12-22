package cc.catface.kotlin.domain

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */

data class JokeResult(val showapi_res_code: String,
                      val showapi_res_error: String,
                      val showapi_res_body: JokeBody)


data class JokeBody(val allNum: String,
                    val allPages: String,
                    val currentPage: String,
                    val maxResult: String,
                    val contentlist: List<Joke>)


data class Joke(val title: String,
                val text: String)