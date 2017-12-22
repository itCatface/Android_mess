package cc.catface.kotlin.module.music.engine

import android.content.Context
import cc.catface.kotlin.module.music.domain.Song
import android.provider.MediaStore


/**
 * Created by catfaceWYH --> tel|wechat|qq 13012892925
 */
object AudioUtils {
    /**
     * 获取sd卡所有的音乐文件
     *
     * @return
     * @throws Exception
     */
    fun getAllSongs(context: Context): ArrayList<Song> {

        var songs: ArrayList<Song>? = null

        val cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.YEAR, MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATA),
                MediaStore.Audio.Media.MIME_TYPE + "=? or "
                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
                arrayOf("audio/mpeg", "audio/x-ms-wma"), null)

        songs = ArrayList()

        if (cursor.moveToFirst()) {

            var song: Song? = null

            do {
                song = Song()
                // 文件名
                song!!.fileName = cursor.getString(1)
                // 歌曲名
                song!!.title = cursor.getString(2)
                // 时长
                song!!.duration = cursor.getInt(3)
                // 歌手名
                song!!.singer = cursor.getString(4)
                // 专辑名
                song!!.album = cursor.getString(5)
                // 年代
                if (cursor.getString(6) != null) {
                    song!!.year = cursor.getString(6)
                } else {
                    song!!.year = "未知"
                }
                // 歌曲格式
                if ("audio/mpeg" == cursor.getString(7).trim()) {
                    song!!.type = "mp3"
                } else if ("audio/x-ms-wma" == cursor.getString(7).trim()) {
                    song!!.type = "wma"
                }
                // 文件大小
                if (cursor.getString(8) != null) {
                    val size = cursor.getInt(8) / 1024f / 1024f
                    song!!.size = ("" + size).substring(0, 4) + "M"
                } else {
                    song!!.size = "未知"
                }
                // 文件路径
                if (cursor.getString(9) != null) {
                    song!!.fileUrl = cursor.getString(9)
                }
                songs!!.add(song)
            } while (cursor.moveToNext())

            cursor.close()

        }
        return songs
    }
}