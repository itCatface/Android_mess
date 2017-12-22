package cc.catface.clibrary.util.net.http.src.progress;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * create by wyh
 *
 * @desc download by using multi thread
 */
public class DownloadT {


    public String url = "http://dldir1.qq.com/qqyy/pc/QQPlayer_Setup_39_936.exe";
    private String savePath = "/";  //下载文件存放目录
    private int threadCount = 3;    //线程数量

    /**
     * 构造方法
     *
     * @param url         要下载文件的网络路径
     * @param savePath    保存下载文件的目录
     * @param threadCount 开启的线程数量,默认为 3
     */
    public DownloadT(String url, String savePath, int threadCount) {
        this.url = url;
        this.savePath = savePath;
        this.threadCount = threadCount;
    }

    /**
     * 下载文件
     */
    public void download(OnDownloadListener listener) throws Exception {

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(1000000);
        conn.setReadTimeout(8000);

        if (200 == conn.getResponseCode()) {
            // 下载文件总长度
            int totalLength = conn.getContentLength();
            System.out.println(totalLength);

            // 本地创建占位文件(大小与要下载的文件大小一致)
            RandomAccessFile randomAccessFile = new RandomAccessFile(new File(savePath, getFileName(url)), "rw");
            randomAccessFile.setLength(totalLength);

            // 分割文件长度
            int eachLenth = totalLength / threadCount;

            // 分配每个线程需要下载的长度
            for (int threadId = 0; threadId < threadCount; threadId++) {
                // 各线程起始下载位置
                int startIndex = threadId * eachLenth;
                // 各线程结束下载位置
                int endIndex = (threadId + 1) * eachLenth - 1;
                // 最后一个线程将下载所有剩余长度
                if (threadId == (threadCount - 1)) {
                    endIndex = totalLength - 1;
                }

                // 开启子线程进行下载
                new DownloadThread(threadId, startIndex, endIndex, listener).start();
                System.out.println("线程_" + threadId + "的下载起点是 " + startIndex + "  下载终点是: " + endIndex);
            }
        }

    }

    private class DownloadThread extends Thread {

        private int threadId;
        private int startIndex;
        private int endIndex;
        private OnDownloadListener listener;

        public DownloadThread(int threadId, int startIndex, int endIndex, OnDownloadListener listener) {
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.listener = listener;

        }

        @Override public void run() {
            System.out.println("线程" + threadId + "开始下载");
            try {
                //分段请求网络连接,分段将文件保存到本地.

                //加载下载位置的文件
                File eachThreadFile = new File(savePath, "download_temp_" + threadId + ".dt");
                RandomAccessFile eachThreadStream = null;
                if (eachThreadFile.exists()) {//如果文件存在
                    eachThreadStream = new RandomAccessFile(eachThreadFile, "rwd");
                    String startIndex_str = eachThreadStream.readLine();
                    if (null == startIndex_str || "".equals(startIndex_str)) {
                        this.startIndex = startIndex;
                    } else {
                        this.startIndex = Integer.parseInt(startIndex_str) - 1;//设置下载起点
                    }
                } else {
                    eachThreadStream = new RandomAccessFile(eachThreadFile, "rwd");
                }


                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(1000000);
                conn.setReadTimeout(8000);

                // 设置分段下载的头信息
                // Range：分段数据请求时会用到[格式: Range bytes=0-1024  or bytes:0-1024]
                conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);

                // 206：部分资源请求成功 || 200：全部资源请求成功
                if (conn.getResponseCode() == 206) {
                    InputStream is = conn.getInputStream();

                    // 将已创建好的占位文件拿出来
                    RandomAccessFile randomAccessFile = new RandomAccessFile(new File(savePath, getFileName(url)), "rw");

                    // 文件写入的开始位置
                    randomAccessFile.seek(startIndex);


                    // 将网络数据流写入本地
                    byte[] buffer = new byte[1024];
                    int length = -1;
                    int total = 0;//记录本次下载文件的大小
                    while ((length = is.read(buffer)) > 0) {
                        randomAccessFile.write(buffer, 0, length);
                        total += length;
                        System.out.println(threadId + "已下载" + total);
                        listener.onProgress(threadId, total);
                        /*
                         * 将当前现在到的位置保存到文件中
                         */
                        eachThreadStream.seek(0);
                        eachThreadStream.write((startIndex + total + "").getBytes("UTF-8"));
                    }

                    eachThreadStream.close();
                    is.close();
                    randomAccessFile.close();
                    cleanTemp(eachThreadFile);//删除临时文件
                    System.out.println("线程" + threadId + "下载完毕");
                    listener.onSuc(threadId);
                } else {
                    listener.onErr("响应码是" + conn.getResponseCode() + ". 服务器不支持多线程下载");
                }
            } catch (Exception e) {
                listener.onErr(e.toString());
            }

        }
    }

    //删除线程产生的临时文件
    private synchronized void cleanTemp(File file) {
        file.delete();
    }

    //获取下载文件的名称
    private String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    /**
     * interface of download process
     */
    public interface OnDownloadListener {
        void onSuc(int threadId);

        void onProgress(int threadId, int progress);

        void onErr(String error);
    }
}
