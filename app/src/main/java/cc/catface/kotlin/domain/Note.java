package cc.catface.kotlin.domain;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 *
 * @desc 需支持greendao
 */
@Entity
public class Note {


    @Id
    private long id;
    private String content;
    private long date;
    private String tag;
    private int stars;

    @Keep
    public Note() {
    }

    @Keep
    public Note(long id, String content, long date, String tag, int stars) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.tag = tag;
        this.stars = stars;
    }




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }


    @Override public String toString() {
        return "Note{" + "id=" + id + ", content='" + content + '\'' + ", date=" + date + ", tag='" + tag + '\'' + ", stars=" + stars + '}';
    }
}
