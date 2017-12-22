package cc.catface.clibrary.util.net.http.src.full;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import cc.catface.clibrary.util.net.http.src.IHttp;


/**
 * request service by using URLConnection
 *
 * @desc you can request service by using GET or POST
 */

public class HttpT implements IHttp {

    private static HttpT mInstance;

    public static HttpT getInstance() {
        if (null == mInstance) {
            synchronized (HttpT.class) {
                if (null == mInstance) {
                    mInstance = new HttpT();
                }
            }
        }

        return mInstance;
    }

    /**
     * GET————————________————————________————————________————————________————————________————————GET
     */
    @Override
    public String get(String url, Map<String, String> params) {
        String totalUrl = "";   // complete request url

        String result = "";
        BufferedReader reader = null;
        try {

            String urlSuffix = "";
            if (null != params) {
                for (String key : params.keySet()) {
                    urlSuffix.concat(key + "=" + params.get(key) + "&");
                }

                totalUrl = url + "?" + urlSuffix.substring(0, urlSuffix.length() - 1);
            }

            totalUrl = url;
            URL realUrl = new URL(totalUrl);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            // Map<String, List<String>> map = connection.getHeaderFields();    // getInstance all header fields
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));    // read stream from url
            String line = "";
            while ((line = reader.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            result = "getInstance request exception :: " + e.toString();

        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    /**
     * POST————————________————————________————————________————————________————————________————————POST
     */
    @Override
    public String post(String url, Map<String, String> params) {
        PrintWriter out = null;
        BufferedReader reader = null;
        String result = "";
        try {

            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true); // must set if use post
            conn.setDoInput(true);  // must set if use post
            out = new PrintWriter(conn.getOutputStream());

            String totalUrl = "";
            String urlSuffix = "";
            if (null != params) {
                for (String key : params.keySet()) {
                    urlSuffix += (key + "=" + params.get(key) + "&");
                }

                totalUrl = urlSuffix.substring(0, urlSuffix.length() - 1);
            }

            out.print(totalUrl);

            out.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            result = "getInstance request exception :: " + e.toString();

        } finally {
            try {
                if (out != null) out.close();
                if (reader != null) reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


}