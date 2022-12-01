package com.davydov.DownLoadVSVGO.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class StartLoad {

  private static final Logger log = LoggerFactory.getLogger(StartLoad.class);

  @Value("${date.load}")
  int days;

  @Autowired
  EmailCredentials emailCredentials;

  @Autowired
  MailSender mailSender;

  @Autowired
  Date date;

  @Autowired
  ListRGE listRGEMap;

  @Autowired
  SimpleMailMessage msg;

  @Autowired
  ListEmail listEmail;

  private boolean start = true;

  public void setStart(boolean start) {
    this.start = start;
  }

  public void doStart() {

    List<String> listDate = date.getDate();
    Map<String, List<String>> map = listRGEMap.getMap();
    Loader loader = new Loader("xxxx", "xxxx", "xxxx", listDate);
    while (start) {
      loader.setWrite(new StringBuilder());
      loader.setListDate(date.getDate());
      loader.setCount(0);
      for (Map.Entry<String, List<String>> mapRGE : map.entrySet()) {
        loader
            .setLogin(mapRGE.getValue().get(0))
            .setPassword(mapRGE.getValue().get(1))
            .setRge(mapRGE.getKey());
        loader.doAuthorization();
      }
      if (loader.getCount() == map.size() * days) {
        String[] strings = listEmail.getList().toArray(new String[0]);
        StringBuilder write;
        write = loader.getWrite();
        System.out.println(write);
        System.out.println("send mail");
        msg.setFrom(emailCredentials.getEmail());
        msg.setTo(strings);
        msg.setSubject("VSVGO");
        msg.setText(String.valueOf(write));
        mailSender.send(msg);
        log.info("close mail");
        log.info("поток уснул до завтра");
        try {
          Thread.sleep(
              24 * 60 * 60 * 1000
                  - ((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - 1) * 60 * 60 * 1000
                      - 15 * 60 * 60 * 1000));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } else {
        System.out.println("поток уснул на 2 мин");
        try {
          Thread.sleep(2 * 60 * 1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println("поток был остановлен");
  }
}
