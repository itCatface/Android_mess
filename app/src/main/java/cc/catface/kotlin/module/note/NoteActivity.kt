package cc.catface.kotlin.module.note

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cc.catface.clibrary.base.BaseActivity
import cc.catface.clibrary.util.extension.t
import cc.catface.clibrary.util.sensor.VibratorT
import cc.catface.clibrary.util.view.recyclerview.divider.ItemDecorationDivider
import cc.catface.clibrary.util.view.recyclerview.drag.DragItemCallback
import cc.catface.clibrary.util.view.recyclerview.drag.OnDragItemClickListener
import cc.catface.clibrary.util.view.recyclerview.swipe.SwipeItemListener
import cc.catface.kotlin.R
import cc.catface.kotlin.domain.Note
import cc.catface.kotlin.domain.greendao_gen.NoteDao
import cc.catface.kotlin.engine.adapters.NoteAdapter
import cc.catface.kotlin.navigator.App
import cc.catface.kotlin.view.group.WeatherTopView
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.find
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 *
 * layoutPosition[notifyDataSetChanged后不能立即获取，需等布局结束后才能获取] & adapterPosition可以立刻获取最新的position
 */
class NoteActivity : BaseActivity(R.layout.activity_note) {

    private var mData: MutableList<Note>? = ArrayList()
    private var mAdapter: NoteAdapter? = null
    val layoutManager = LinearLayoutManager(this)

    private var noteDao: NoteDao? = null

    override fun create() {
        initData()
        initView()
        initEvent()
    }


    private fun initData() {
        noteDao = App.noteDao
        mData = noteDao!!.queryBuilder().list()
    }


    private fun initView() {
        wtv_note.setTitle("备忘录")
        wtv_note.setLeftTwoIcon(R.string.fa_search)
        wtv_note.setRightOneIcon(R.string.fa_plus)
        wtv_note.setRightTwoIcon(R.string.fa_reorder)

        rv_note.setHasFixedSize(true)
        rv_note.layoutManager = layoutManager
        // manager.orientation = LinearLayoutManager.HORIZONTAL
        // manager = GridLayoutManager(this, 4)
        // StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        rv_note.addItemDecoration(ItemDecorationDivider(this, ItemDecorationDivider.VERTICAL))

        mAdapter = NoteAdapter(mData, rv_note, layoutManager, NoteSwipeItemListener())
        rv_note.adapter = mAdapter
    }


    private fun initEvent() {
        wtv_note.setOnClickListener(WeatherTopView.OnClickListener { view ->
            when (view!!.id) {
                R.id.tv_left_one -> {
                    this@NoteActivity.finish()
                }

                R.id.tv_left_two -> {
                    dialog(NOTE_SEARCH)
                }

                R.id.tv_right_two -> {
                    popupWindow(view)
                    /*for (x in 1..70) {
                        val noteAdded = Note(SystemClock.currentThreadTimeMillis(), "第$x 条记录哦$x 自古多情空余恨，此恨绵绵无绝期呐", SystemClock.currentThreadTimeMillis(), "默认", Random().nextInt(5))
                        noteDao!!.insert(noteAdded)
                    }*/
                }

                R.id.tv_right_one -> {
                    dialog(NOTE_ADD)
                }
            }
        })


        /* DragItemCallback & OnDragItemClickListener */
        val helper = ItemTouchHelper(DragItemCallback(mAdapter))
        helper.attachToRecyclerView(rv_note)
        rv_note.addOnItemTouchListener(object : OnDragItemClickListener(rv_note) {
            override fun onLongClick(viewHolder: RecyclerView.ViewHolder?) {
                super.onLongClick(viewHolder)
                if (viewHolder!!.layoutPosition != mData!!.size - 1) {
                    helper.startDrag(viewHolder)
                    VibratorT(this@NoteActivity).vibrate(60)
                }
            }
        })
    }


    inner class NoteSwipeItemListener : SwipeItemListener {
        override fun onItemClick(holder: RecyclerView.ViewHolder?, position: Int, content: String?) {
            dialog(NOTE_UPDATE)
            VibratorT(this@NoteActivity).vibrate(40)
        }

        override fun onLeftMenuClick(position: Int) {
        }

        override fun onRightMenuClick(position: Int) {
        }
    }


    val NOTE_ADD = "note_add"
    val NOTE_SEARCH = "note_search"
    val NOTE_UPDATE = "note_update"
    private fun dialog(type: String) {
        val dialog = Dialog(this, R.style.CustomDialog) // AlertDialog中的EditText可能不弹键盘
        val dialogView = View.inflate(this, R.layout.dialog_note_add, null)
        dialog.setContentView(dialogView)
        dialog.show()
        dialog.setCanceledOnTouchOutside(true)
        val window = dialog.window

        val attrs = window.attributes
        attrs.height = ViewGroup.LayoutParams.WRAP_CONTENT
        attrs.width = (windowManager.defaultDisplay.width * 0.8).toInt()
        window.attributes = attrs

        val tv_title: TextView = dialogView.find(R.id.tv_title)
        val et_content: EditText = dialogView.find(R.id.et_content)
        val et_tag: EditText = dialogView.find(R.id.et_tag)
        val rl_star: RelativeLayout = dialogView.find(R.id.rl_star)
        val rb: RatingBar = dialogView.find(R.id.rb)
        val bt_ok: Button = dialogView.find(R.id.bt_ok)
        val bt_cancel: Button = dialogView.find(R.id.bt_cancel)

        when (type) {
            NOTE_ADD -> {
                dialogView.find<Button>(R.id.bt_ok).setOnClickListener {
                    val content = et_content.text.toString()
                    val tag = if (et_tag.text.toString().isEmpty()) "默认" else et_tag.text.toString()
                    val stars = rb.rating.toInt()

                    dialog.dismiss()

                    insertNote(content, tag, stars)
                }

                dialogView.find<Button>(R.id.bt_cancel).setOnClickListener {
                    dialog.dismiss()
                }
            }

            NOTE_SEARCH -> {
                et_tag.visibility = View.GONE
                rl_star.visibility = View.GONE
                tv_title.text = "查询备忘"
                bt_ok.text = "查询"
                bt_cancel.text = "所有备忘"

                bt_ok.setOnClickListener {
                    val content = et_content.text.toString()

                    dialog.dismiss()

                    queryNote(content)
                }

                bt_cancel.setOnClickListener {
                    dialog.dismiss()
                    mData!!.clear()
                    mData!!.addAll(noteDao!!.queryBuilder().list())
                    rv_note.adapter.notifyItemRangeChanged(0, mData!!.size)
                }
            }

            NOTE_UPDATE -> {
                tv_title.text = "修改备忘"
                bt_ok.text = "修改"
                bt_cancel.text = "取消"

                dialogView.find<Button>(R.id.bt_ok).setOnClickListener {
                    val content = et_content.text.toString()
                    val tag = if (et_tag.text.toString().isEmpty()) "默认" else et_tag.text.toString()
                    val stars = rb.rating.toInt()

                    dialog.dismiss()

                    updateNote(content, tag, stars)
                }

                dialogView.find<Button>(R.id.bt_cancel).setOnClickListener {
                    dialog.dismiss()
                }
            }
        }
    }


    var mPopupWindow: PopupWindow? = null
    private fun popupWindow(view: View) {

        if (null == mPopupWindow) {
            val lv = ListView(this)
            lv.setBackgroundColor(Color.WHITE)

            lv.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayOf("按星级排序", "按修改时间排序", "按uuid排序", "乱序排列"))
            lv.setOnItemClickListener { _, _, i, _ ->
                when (i) {
                    0 -> Collections.sort(mData, { t1: Note, t2: Note -> t2.stars - t1.stars })

                    1 -> Collections.sort(mData, { t1: Note, t2: Note -> (t2.date - t1.date).toInt() })

                    2 -> Collections.sort(mData, { t1: Note, t2: Note -> (t2.id - t1.id).toInt() })

                    3 -> Collections.shuffle(mData)
                }

                rv_note.adapter.notifyDataSetChanged()
                mPopupWindow!!.dismiss()
            }

            mPopupWindow = PopupWindow(lv, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            mPopupWindow!!.setBackgroundDrawable(ColorDrawable())
        }

        mPopupWindow!!.showAsDropDown(view, 40, view.height)
    }


    private fun insertNote(content: String, tag: String, stars: Int) {
        if (content.isEmpty()) t("请添加备忘内容...")
        else {
            val noteAdded = Note(SystemClock.currentThreadTimeMillis(), content, SystemClock.currentThreadTimeMillis(), tag, stars)
            noteDao!!.insert(noteAdded)
            mData!!.add(0, noteAdded)
            rv_note.adapter.notifyItemInserted(0)
            rv_note.adapter.notifyItemRangeChanged(0, mData!!.size)
        }
    }

    private fun updateNote(content: String, tag: String, stars: Int) {
        if (content.isEmpty()) t("请添加修改内容...")
        else {
            val noteUpdated = Note(SystemClock.currentThreadTimeMillis(), content, SystemClock.currentThreadTimeMillis(), tag, stars)
            noteDao!!.update(noteUpdated)
            rv_note.adapter.notifyItemRangeChanged(0, mData!!.size)
        }
    }

    private fun queryNote(content: String) {
        if (content.isEmpty()) t("请添加查询内容...")
        else {
            val uniqueNote = noteDao!!.queryBuilder().where(NoteDao.Properties.Content.like("%$content%")).list()
            mData!!.clear()
            mData!!.addAll(uniqueNote)
            rv_note.adapter.notifyItemRangeChanged(0, mData!!.size)
        }
    }
}