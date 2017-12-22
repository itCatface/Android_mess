package cc.catface.clibrary.util.net.http.src.progress;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;


/**
 * Created by wyh
 * --
 *
 * @desc you can getInstance the progress of real time in uploading process
 */

public class UploadT {

    private static UpDownloadHandler mUpDownloadHandler = new UpDownloadHandler(Looper.getMainLooper());

    private static class UploadTHolder {
        public static final UploadT mUploadT = new UploadT();
    }

    public static UploadT getInstance() {
        return UploadTHolder.mUploadT;
    }

    public void upload(String url, Map<String, String> params, Map<String, String> fileMap, Map<String, String> headers, HttpCallback callback) {

        /**
         * total size of files
         */
        long totalLength = 0;
        for (String key : fileMap.keySet()) {
            totalLength += new File(fileMap.get(key)).getTotalSpace() / 1024;  // file size[kb]
            System.out.println(totalLength + "====");
        }


        OutputStream os = null;
        DataInputStream dis = null;

        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716"; // BOUNDARY is delimiter of request headers and uploading files
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            // headers(cookie)
            if (headers != null) {
                for (String key : headers.keySet()) {
                    conn.addRequestProperty(key, headers.get(key));
                }
            }

            conn.setRequestProperty("Connection", "Keep-Alive");
            // conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            os = new DataOutputStream(conn.getOutputStream());

            // params
            if (null != params) {
                StringBuffer sb = new StringBuffer();
                for (String key : params.keySet()) {
                    if (null == key) continue;
                    sb.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    sb.append("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n");
                    sb.append(params.get(key));
                }
                os.write(sb.toString().getBytes());
            }

            // files
            if (fileMap != null) {
                for (String key : fileMap.keySet()) {
                    if (null == key) continue;

                    File file = new File(fileMap.get(key));
                    String filename = file.getName();
                    String contentType = new MimetypesFileTypeMap().getContentType(file);
                    if (filename.endsWith(".png")) {
                        contentType = "image/png";
                    }
                    if (null == contentType || "".equals(contentType)) {
                        contentType = "application/octet-stream";
                    }

                    StringBuffer sb = new StringBuffer();
                    sb.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    sb.append("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + filename + "\"\r\n");
                    sb.append("Content-Type:" + contentType + "\r\n\r\n");

                    os.write(sb.toString().getBytes());

                    dis = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    long sum = 0;
                    byte[] bufferOut = new byte[1024 * 10];
                    while ((bytes = dis.read(bufferOut)) != -1) {
                        os.write(bufferOut, 0, bytes);

                        sum += bytes;
                        int progress = (int) (sum * 1.0f / totalLength);
                        System.out.println(sum + "===" + progress);
                        sendMsg(progress + "", HttpCallback.STATE_PROGRESS, callback);  // uploading progress
                    }
                    dis.close();
                }
            }


            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            os.write(endData);
            os.flush();
            os.close();

            // read the stream content returned from the service
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            res = buffer.toString();
            reader.close();

        } catch (Exception e) {
            sendMsg(e.toString(), HttpCallback.STATE_ERR, callback);

        } finally {

            try {
                if (null != dis) dis.close();
                if (null != os) os.close();

                if (conn != null) conn.disconnect();

            } catch (IOException e) {
                sendMsg(e.toString(), HttpCallback.STATE_ERR, callback);
            }
        }

        sendMsg(res, HttpCallback.STATE_SUC, callback);    // uploading completed
    }


    /**
     * HANDLER————————________————————________————————________————————________————————________————————HANDLER
     * --
     * callback with handler
     */
    private void sendMsg(String result, int state, HttpCallback callback) {
        Message msg = Message.obtain();

        switch (state) {
            case HttpCallback.STATE_PROGRESS:
                msg.what = mUpDownloadHandler.MSG_REQUEST_PROGRESS;
                break;

            case HttpCallback.STATE_SUC:
                msg.what = UpDownloadHandler.MSG_REQUEST_SUC;
                break;

            case HttpCallback.STATE_ERR:
                msg.what = UpDownloadHandler.MSG_REQUEST_ERR;
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        msg.obj = callback;
        msg.setData(bundle);
        mUpDownloadHandler.sendMessage(msg);
    }
}
