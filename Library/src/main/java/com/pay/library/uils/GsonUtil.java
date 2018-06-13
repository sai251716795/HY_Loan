package com.pay.library.uils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 特殊JSon数据解析
 */
public class GsonUtil {

    /**
     * 将json数据串转换成对象数据
     *
     * @param json json字符串
     * @param cls 对象
     * @return class
     * @throws IllegalAccessException 1
     * @throws InstantiationException 1
     */
    public static <T> T fromJson(String json, Class<T> cls) throws InstantiationException, IllegalAccessException {
        T obj = null;
        Gson gson = new Gson();
        obj = cls.newInstance();
        obj = gson.fromJson(json, cls);
        return obj;
    }

    /**
     * 将对象转化成Json 数据串
     */
    public static String objToJson(Object obj) {
        return new Gson().toJson(obj);
    }

    /**
     * 将List Json数据串转换成List集合
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> cls) throws Exception {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects) {
            arrayList.add(new Gson().fromJson(jsonObject, cls));
        }
        return arrayList;
    }

    /**
     * 将List Json数据串转换成Map集合
     *
     * @param json
     * @return
     */
    public static Map jsonToHashMap(String json) throws Exception {
        Type type = new TypeToken<HashMap>() {
        }.getType();
        HashMap jsonObjects = new Gson().fromJson(json, type);
        return jsonObjects;
    }
}
