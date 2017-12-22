package cc.catface.kotlin.module.music.domain


/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */
class Song {

    var fileName: String? = null
    var title: String? = null
    var duration: Int = 0
    var singer: String? = null
    var album: String? = null
    var year: String? = null
    var type: String? = null
    var size: String? = null
    var fileUrl: String? = null

    constructor() : super() {}

    constructor(fileName: String, title: String, duration: Int, singer: String,
                album: String, year: String, type: String, size: String, fileUrl: String) : super() {
        this.fileName = fileName
        this.title = title
        this.duration = duration
        this.singer = singer
        this.album = album
        this.year = year
        this.type = type
        this.size = size
        this.fileUrl = fileUrl
    }

    override fun toString(): String {
        return ("Song [fileName=" + fileName + ", title=" + title
                + ", duration=" + duration + ", singer=" + singer + ", album="
                + album + ", year=" + year + ", type=" + type + ", size="
                + size + ", fileUrl=" + fileUrl + "]")
    }

}  