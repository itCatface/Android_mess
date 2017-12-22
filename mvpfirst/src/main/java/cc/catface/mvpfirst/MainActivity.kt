package cc.catface.mvpfirst

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.SpannableStringBuilder
import cc.catface.clibrary.t
import cc.catface.mvpfirst.p.UserPresenter
import cc.catface.mvpfirst.v.IUserView
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class MainActivity : AppCompatActivity(), IUserView {

    private var mUserPresenter: UserPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mUserPresenter = UserPresenter(this)
        initEvent()
    }

    private fun initEvent() {
        bt_save.setOnClickListener { mUserPresenter?.saveUser(getID(), getUsername(), getAge()) }
        bt_load.setOnClickListener { mUserPresenter?.loadUser(getID()) }
    }


    override fun getID(): Int {
        val id = et_id.text.toString().trim()
        return if (id.isNotEmpty()) id.toInt() else 0
    }

    override fun getUsername() = et_username.text.toString().trim()

    override fun getAge(): Int {
        val age = et_age.text.toString().trim()
        return if (age.isNotEmpty()) age.toInt() else 0
    }

    override fun setUsername(username: String) {
        et_username.text = SpannableStringBuilder(username)
    }

    override fun setAge(age: Int) {
//        et_age.text = Editable(age.toString())
        et_age.text = SpannableStringBuilder(age.toString())
    }

    override fun onSuc() {
        et_id.setText("")
        et_username.setText("")
        et_age.setText("")
        t("保存成功")
    }
}
