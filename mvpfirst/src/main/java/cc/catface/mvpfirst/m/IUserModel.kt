package cc.catface.mvpfirst.m

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
interface IUserModel {
    fun setId(id: Int)
    fun setUsername(username: String)
    fun setAge(age: Int)
    fun save()
    fun load(id: Int): User
}