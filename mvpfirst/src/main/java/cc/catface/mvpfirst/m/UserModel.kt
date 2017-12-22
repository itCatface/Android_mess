package cc.catface.mvpfirst.m

import android.util.SparseArray

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class UserModel : IUserModel {
    private var mId = 0
    private var mUsername = ""
    private var mAge = 0
    private var mUsers = SparseArray<User>()


    override fun setId(id: Int) {
        mId = id
    }

    override fun setUsername(username: String) {
        mUsername = username
    }

    override fun setAge(age: Int) {
        mAge = age
    }

    override fun save() {
        mUsers.append(mId, User(mUsername, mAge))
    }

    override fun load(id: Int) = mUsers[mId, User("not found", -1)]
}