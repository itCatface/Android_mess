package cc.catface.clibrary.util.net.http.okhttp.full;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cc.catface.clibrary.util.net.http.Config;
import cc.catface.clibrary.util.net.http.okhttp.IOH;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @version 1.0.0
 * @attention // OKHttp in gradle
 * compile 'com.squareup.okhttp3:okhttp:3.5.0'
 * compile 'com.squareup.okio:okio:1.11.0'
 */

public class OHT implements IOH {

    private final static String TAG = OHT.class.getSimpleName();

    private static OHHandler mOHHandler = new OHHandler(Looper.getMainLooper());


    private static OHT mInstance;
    private static OkHttpClient mOkHttpClient;

    /**
     * use/not use single instance to getInstance OkHttpClient
     */
    public static OHT getInstance() {
        if (Config.OKHTTP_WITH_SINGLE_INSTANCE) {
            if (null == mInstance) {
                synchronized (OHT.class) {
                    if (null == mInstance) mInstance = new OHT();
                }
            }

        } else {
            mInstance = new OHT();
        }

        return mInstance;
    }

    private OHT() {
        mOkHttpClient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS).build();
    }


    /**
     * use cache save
     */
/*    public OkHttpUtil setCache(Context mContext) {
        File sdcache = mContext.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;


        Cache cache = new Cache(sdcache,cacheSize);
        Cache cache1 = mOkHttpClient.cache();
//        mOkHttpClient.cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        return mInstance;
    }*/


    /**
     * GET————————________————————________————————________————————________————————________————————GET
     * --
     * getInstance with noting | url ==> getInstance
     */
    @Override
    public void get(final String url, final OHCallback callback) {
        try {
            mOkHttpClient.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    sendMessage(e.toString(), false, callback);
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        sendMessage(response.body().string(), true, callback);
                    } else {
                        sendMessage("errorCode:" + response.code(), false, callback);
                    }
                }
            });

        } catch (IllegalArgumentException e) {
            sendMessage(e.toString(), false, callback);
        }
    }


    /**
     * getInstance with json | url + json ==> getInstance
     */
    @Override
    public void get(final String url, final String json, final OHCallback callback) {
        try {
            mOkHttpClient.newCall(new Request.Builder().url(url + "?jsonStr=" + URLEncoder.encode(json, "UTF-8")).build()).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    sendMessage(e.toString(), false, callback);
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        sendMessage(response.body().string(), true, callback);
                    } else {
                        sendMessage("errorCode:" + response.code(), false, callback);
                    }
                }
            });

        } catch (IllegalArgumentException e) {
            sendMessage(e.toString(), false, callback);
        } catch (UnsupportedEncodingException e) {
            sendMessage(e.toString(), false, callback);
        }
    }


    /**
     * getInstance with map | url + map ==> getInstance
     */
    @Override
    public void get(final String url, Map<String, String> params, final OHCallback callback) {
        StringBuilder builder = new StringBuilder().append(url).append("?");

        int position = 0;
        for (String key : params.keySet()) {
            if (position > 0) {
                builder.append("&");
            }
            builder.append(String.format("%s=%s", key, params.get(key)));
            position++;
        }

        try {
            mOkHttpClient.newCall(new Request.Builder().url(builder.toString()).build()).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    sendMessage(e.toString(), false, callback);
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        sendMessage(response.body().string(), true, callback);
                    } else {
                        sendMessage("errorCode:" + response.code(), false, callback);
                    }
                }
            });

        } catch (IllegalArgumentException e) {
            sendMessage(e.toString(), false, callback);
        }
    }


    /**
     * POST————————________————————________————————________————————________————————________————————POST
     *
     * @desc post need add form body
     * --
     * post with nothing | url ==> post
     */
    @Override
    public void post(String url, final OHCallback callback) {
        try {
            mOkHttpClient.newCall(new Request.Builder().url(url).post(new FormBody.Builder().build()).build()).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    sendMessage(e.toString(), false, callback);
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        sendMessage(response.body().string(), true, callback);
                    } else {
                        sendMessage("errorCode:" + response.code(), false, callback);
                    }
                }
            });

        } catch (IllegalArgumentException e) {
            sendMessage(e.toString(), false, callback);
        }
    }


    /**
     * post with json | url + json ==> post
     */
    @Override
    public void post(final String url, final String json, final OHCallback callback) {
        try {
            mOkHttpClient.newCall(new Request.Builder().url(url).post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)).build()).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    sendMessage(e.toString(), false, callback);
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        sendMessage(response.body().string(), true, callback);
                    } else {
                        sendMessage("errorCode:" + response.code(), false, callback);
                    }
                }
            });

        } catch (IllegalArgumentException e) {
            sendMessage(e.toString(), false, callback);
        }
    }


    /**
     * post with map | url + map ==> post
     */
    @Override
    public void post(final String url, final Map<String, String> params, final OHCallback callback) {
        try {
            FormBody.Builder builder = new FormBody.Builder();

            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }

            mOkHttpClient.newCall(new Request.Builder().url(url).post(builder.build()).build()).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    sendMessage(e.toString(), false, callback);
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        sendMessage(response.body().string(), true, callback);
                    } else {
                        sendMessage("errorCode:" + response.code(), false, callback);
                    }
                }
            });

        } catch (IllegalArgumentException e) {
            sendMessage(e.toString(), false, callback);
        }
    }


    /**
     * post with map and without any callback| url + map ==> post
     *
     * @return Response
     */
    @Override
    public Response post(String url, Map<String, String> params) {

        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        RequestBody requestBody = builder.build();

        Request request = new Request.Builder().url(url).post(requestBody).build();

        try {
            return mOkHttpClient.newCall(request).execute();

        } catch (IOException e) {
            return null;
        }
    }


    /**
     * UPLOAD————————________————————________————————________————————________————————________————————UPLOAD
     * --
     * post with map & files | url + map + files ==> post
     */
    @Override
    public void upload(final String url, final Map<String, String> params, final Map<String, String> fileMap, final OHCallback callback) {
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);

            // params of key & value
            // if no params, use null fill this parameter
            if (null != params) {
                for (String key : params.keySet()) {
                    builder.addFormDataPart(key, params.get(key));
                }
            }

            // params of key & file
            for (String key : fileMap.keySet()) {
                File file = new File(fileMap.get(key));
                if (null == file || !file.exists()) {
                    System.out.println("SOS ==> the file from path:: " + fileMap.get(key) + " :: dose not exists!");

                } else {
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
                }
            }

            mOkHttpClient.newCall(new Request.Builder().url(url).post(builder.build()).build()).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    sendMessage(e.toString(), false, callback);
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        sendMessage(response.body().string(), true, callback);
                    } else {
                        sendMessage("errorCode:" + response.body().string(), false, callback);
                    }
                }
            });

        } catch (IllegalArgumentException e) {
            sendMessage(e.toString(), false, callback);
        }
    }


    /**
     * DOWNLOAD————————________————————________————————________————————________————————________————————DOWNLOAD
     * --
     * download file by using OkHttp3
     */
    @Override
    public void download(final String url, final String fileDir, final String fileName, final OHCallback callback) {
        try {
            mOkHttpClient.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    sendMessage(e.toString(), false, callback);
                }

                @Override public void onResponse(Call call, Response response) {
                    FileOutputStream fos = null;
                    InputStream is = null;
                    try {
                        File file = new File(fileDir);
                        if (!file.exists()) {
                            boolean result = file.mkdir();
                            if (!result) {
                                sendMessage("文件目录创建失败...", false, callback);
                                return;
                            }
                        }
                        file = new File(fileDir, fileName);
                        if (!file.exists()) {
                            boolean result = file.createNewFile();
                            if (!result) {
                                sendMessage("文件创建失败...", false, callback);
                                return;
                            }
                        }
                        fos = new FileOutputStream(file);
                        is = response.body().byteStream();
                        byte[] buffer = new byte[2048];
                        int len;
                        while (-1 != (len = is.read(buffer))) {
                            fos.write(buffer, 0, len);
                        }

                        String logStr = "===========================================================================\n".concat("下载地址===" + url + " || 本地存储地址===" + file.getAbsolutePath() + "\n").concat("===========================================================================");

                        // success callback when download completed
                        sendMessage(logStr, true, callback);

                    } catch (IOException e) {
                        sendMessage(e.toString(), false, callback);

                    } finally {
                        try {
                            if (null != fos) fos.close();
                            if (null != is) is.close();

                        } catch (IOException e) {
                            sendMessage(e.toString(), false, callback);
                        }
                    }
                }
            });

        } catch (IllegalArgumentException e) {
            sendMessage(e.toString(), false, callback);
        }
    }


    /**
     * HANDLER————————________————————________————————________————————________————————________————————HANDLER
     * --
     * callback with handler
     */
    private void sendMessage(String result, boolean isRequestSuccess, OHCallback callback) {
        Message message = Message.obtain();
        message.what = isRequestSuccess ? OHHandler.MESSAGE_REQUEST_SUCCESS : OHHandler.MESSAGE_REQUEST_FAILURE;
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        message.obj = callback;
        message.setData(bundle);
        mOHHandler.sendMessage(message);
    }
}
