package cc.catface.mvpfirst.p

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
interface IUserPresenter {
    fun saveUser(id: Int, username: String, age: Int)
    fun loadUser(id: Int)
}