package cc.catface.mvpfirst.v

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
interface IUserView {
    fun getID(): Int
    fun getUsername(): String
    fun getAge(): Int
    fun setUsername(username: String)
    fun setAge(age: Int)
    fun onSuc()
}