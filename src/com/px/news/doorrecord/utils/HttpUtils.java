package com.px.news.doorrecord.utils;

import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

public class HttpUtils {
  public static void println(WebConversation conversation, WebRequest request,WebResponse response) throws Exception{
    System.out.println("request:Method='"+request.getMethod()+"',URL='"+request.getURL()+"'"+",ResponseCode="+response.getResponseCode());
    System.out.println("response:");
    System.out.println(response.getText());
  }
  public static void printlnHeader(WebConversation conversation, WebRequest request,WebResponse response) throws Exception{
    System.out.println("request:Method='"+request.getMethod()+"',URL='"+request.getURL()+"'"+",ResponseCode="+response.getResponseCode());
    System.out.println("header:");
    String[] fieldarr=response.getHeaderFieldNames();
    for(int i=0;i<fieldarr.length;i++) {   
      String[] tmp= response.getHeaderFields(fieldarr[i]);
      String tmp_value="";
      if(tmp!=null)for(int k=0;k<tmp.length;k++) {   
        tmp_value+=tmp[k]+";";
      }
      System.out.println(fieldarr[i]+"="+response.getHeaderField(fieldarr[i]));
      
    }
  }
  public static void printjson(String s) throws Exception{
    System.out.println("body:"); 
    System.out.println(s); 
  }
  
  
  
  /**  
   * 获取response header中Content-Disposition中的filename值  
   * @param response  
   * Content-Disposition
: 
attachment; filename=4_headimg_402886fc4d606189014d6061892b0000.jpg
   * @return  
   */  
  public static String getFileNameByResponseHeader(String disposition) {   
      String filename = null;   
      if (disposition != null) {   
          String[] values = disposition.split(";");   
          for(int i=0;i<values.length;i++) {   
            String[] temparr = values[i].split("=");   
              if (temparr[0]!=null&&temparr[0].indexOf("filename")>=0) {   
                  try {   
                      //filename = new String(param.getValue().toString().getBytes(), "utf-8");   
                      //filename=URLDecoder.decode(param.getValue(),"utf-8");   
                      filename =temparr[1];   
                  } catch (Exception e) {   
                      e.printStackTrace();   
                  }   
              }   
          }   
      }   
      return filename;   
  }   

}
