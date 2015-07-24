package com.px.news.doorrecord.utils;


import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.company.news.springMVC.JsonDateValueProcessor;

public final class JSONUtils {

  public JSONUtils() {}

  public static String getJsonString(Object obj) {
    if ((obj instanceof Array) || (obj instanceof Map) || (obj instanceof Collection))
      return arrayToJSONString(obj);
    else
      return objectToJsonString(obj);
  }

  private static String arrayToJSONString(Object obj) {
    JsonConfig jsonConfig = new JsonConfig();
    jsonConfig.registerJsonValueProcessor(Timestamp.class,new JsonDateValueProcessor());

    String json = JSONArray.fromObject(obj,jsonConfig).toString();
    return json;
  }

  private static String objectToJsonString(Object obj) {
    
    JsonConfig jsonConfig = new JsonConfig();
    jsonConfig.registerJsonValueProcessor(Timestamp.class,new JsonDateValueProcessor());

    String json = JSONObject.fromObject(obj,jsonConfig).toString();
    return json;
  }

  public static List jsonToList(String jsonstr, Object t) {
    JSONArray jsonArray = JSONArray.fromObject(jsonstr);
    List list = JSONArray.toList(jsonArray, t, new JsonConfig());
    return list;
  }

  public static Object jsonToObject(String jsonstr, Class tclazz) {
    JSONObject jsonObject = JSONObject.fromObject(jsonstr);
    Object t = JSONObject.toBean(jsonObject, tclazz);
    return t;
  }
}