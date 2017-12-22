package cc.catface.clibrary.util.net.json;

import com.alibaba.fastjson.JSON;

/**
 * fastjson
 *      example     compile 'com.alibaba:fastjson:1.2.38'
 *
 *      github      https://github.com/alibaba/fastjson
 */
public class JsonT {

    /**
     * object --> json
     */
    public static String toJson(Object obj) {

        if (null == obj)
            return "obj == null, plz check param obj";

        String json = JSON.toJSONString(obj);
        if (null != json && !"".equals(json)) {
            return json;
        } else {
            return "";
        }
    }


    /**
     * json --> object
     */
    public static <T> T toObj(String json, Class<T> clz) {

        if (null == json)
            throw new RuntimeException("return json is null");

        return JSON.parseObject(json, clz);
    }
}
