package com.davydov.DownLoadVSVGO.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Service
public class DateImpl implements Date {

  @Value("${date.load}")
  int days;

  @Override
  public List<String> getDate() {
    System.out.println("n= " + days + " from Date.class");
    Calendar calendar = Calendar.getInstance();
    List<String> listDate = new LinkedList<>();
    java.util.Date date;
    for (int i = 0; i <= (days - 1); i++) {
      calendar.add(Calendar.DATE, 1);
      date = calendar.getTime();
      SimpleDateFormat format_yyyy = new SimpleDateFormat("yyyy");
      SimpleDateFormat format_MM = new SimpleDateFormat("MM");
      SimpleDateFormat format_dd = new SimpleDateFormat("dd");
      listDate.add(
          format_dd.format(date) + "." + format_MM.format(date) + "." + format_yyyy.format(date));
    }
    return listDate;
  }
}
