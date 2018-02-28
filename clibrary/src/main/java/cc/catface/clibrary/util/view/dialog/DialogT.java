package cc.catface.clibrary.util.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by catfaceWYH --> tel|wechat|qq 130 128 92925
 */

public class DialogT {

    AlertDialog.Builder mBuilder;

    private static DialogT mInstance;
    public static DialogT getInstance(Activity activity) {
        if (null == mInstance) {
            synchronized (DialogT.class) {
                if (null == mInstance) {
                    mInstance = new DialogT(activity);
                }
            }
        }

        return mInstance;
    }

    private DialogT(Activity activity) {
        mBuilder = new AlertDialog.Builder(activity);
    }

    public void notice(int iconId, String title, String msg, String btPositiveStr, String btNegativeStr, final Callback callback) {
        mBuilder.setIcon(iconId).setTitle(title).setMessage(msg).setPositiveButton(btPositiveStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.positive();
            }
        }).setNegativeButton(btNegativeStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.negative();
            }
        }).show();
    }

    public void items(int iconId, String title, String[] items, final Callback callback) {
        mBuilder.setIcon(iconId).setTitle(title).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.positive();
            }
        }).show();
    }


    public interface Callback {
        void positive();
        void negative();
    }

}
