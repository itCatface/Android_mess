package cc.catface.mvpfirst.p

import cc.catface.mvpfirst.m.UserModel
import cc.catface.mvpfirst.v.IUserView

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class UserPresenter(private val view: IUserView) : IUserPresenter {

    private val mUserModel = UserModel()

    override fun saveUser(id: Int, username: String, age: Int) {
        mUserModel.setId(id)
        mUserModel.setUsername(username)
        mUserModel.setAge(age)
        mUserModel.save()
        view.onSuc()
    }

    override fun loadUser(id: Int) {
        val user = mUserModel.load(id)
        view.setUsername(user.username)
        view.setAge(user.age)
    }
}