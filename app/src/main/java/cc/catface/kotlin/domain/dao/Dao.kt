package cc.catface.kotlin.domain.dao

import android.content.Context
import cc.catface.kotlin.domain.greendao_gen.DaoMaster

/**
 * Created by wyh
 */
object Dao {
    fun noteDao(ctx: Context) = DaoMaster(DaoMaster.DevOpenHelper(ctx, "note-db", null).writableDatabase).newSession().noteDao
}