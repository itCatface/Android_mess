package cc.catface.clibrary.util.net.http.okhttp.progress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cc.catface.clibrary.util.net.http.Config;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wyh
 *
 * @desc you can getInstance the progress of real time in downloading process
 */

public class DownLoadT {

    private static DownLoadT mInstance;
    private final OkHttpClient okHttpClient;


    /**
     * getInstance OkHttpClient by using or not using single instance
     */
    public static DownLoadT getInstance() {
        if (Config.OKHTTP_WITH_SINGLE_INSTANCE) {
            if (null == mInstance) {
                synchronized (DownLoadT.class) {
                    if (null == mInstance) mInstance = new DownLoadT();
                }
            }

        } else {
            mInstance = new DownLoadT();
        }

        return mInstance;
    }

    private DownLoadT() {
        okHttpClient = new OkHttpClient();
    }


    /**
     * @desc download file with process listener
     */
    public void download(final String url, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                listener.onErr(e.toString());
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[4096];
                int len = 0;
                FileOutputStream fos = null;

                String savePath = isExistDir(saveDir); // the dir of the file
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, getNameFromUrl(url));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);

                        listener.onProgress(progress); // download progress
                    }
                    fos.flush();

                    listener.onSuc(); // download completed
                } catch (Exception e) {
                    listener.onErr(e.toString());

                } finally {
                    try {
                        if (is != null) is.close();
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                        listener.onErr(e.toString());
                    }
                }
            }
        });
    }


    /**
     * check the dir is exists
     */
    private String isExistDir(String saveDir) throws IOException {

        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();

        return savePath;
    }


    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    /**
     * interface of download process
     */
    public interface OnDownloadListener {
        void onSuc();

        void onProgress(int progress);

        void onErr(String error);
    }
}