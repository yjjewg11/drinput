package com.px.news.doorrecord;

import com.company.news.ProjectProperties;

public class Constants {

  public static String contentType="application/json;charset=UTF-8";
  
  //10分钟，单次扫描的时间区间，区间越小，单独扫描的记录越少
  public static int time_interval=ProjectProperties.getPropertyAsInt("time_interval", 600000);
  
  //10分钟，定时任务的间隔执行时间
  public static int frequency=ProjectProperties.getPropertyAsInt("frequency", 600000);
  
}
