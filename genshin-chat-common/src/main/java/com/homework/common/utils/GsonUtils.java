package com.homework.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GsonUtils {

    // 单例 Gson 对象（线程安全）
    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss") // 日期格式化
            .disableHtmlEscaping()                // 避免 html 标签转义
            .create();

    private GsonUtils() {
        // 工具类不允许实例化
    }

    /**
     * 对象转 JSON 字符串
     */
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * JSON 转对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    /**
     * JSON 转对象（支持复杂泛型，比如 List<User>）
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    /**
     * JSON 转 List
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        Type type = TypeToken.getParameterized(List.class, clazz).getType();
        return GSON.fromJson(json, type);
    }

    /**
     * JSON 转 Map
     */
    public static <K, V> Map<K, V> fromJsonToMap(String json, Class<K> keyClass, Class<V> valueClass) {
        Type type = TypeToken.getParameterized(Map.class, keyClass, valueClass).getType();
        return GSON.fromJson(json, type);
    }

    /**
     * 格式化 JSON（美化输出）
     */
    public static String toPrettyJson(String json) {
        JsonElement element = JsonParser.parseString(json);
        return new GsonBuilder().setPrettyPrinting().create().toJson(element);
    }

    /**
     * 获取内部的 Gson 实例（如需自定义操作）
     */
    public static Gson getGson() {
        return GSON;
    }
}
