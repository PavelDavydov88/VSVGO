package com.davydov.DownLoadVSVGO.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class AppConfig {

  @Bean
  public JavaMailSenderImpl javaMailSender() {

    Properties properties = new Properties();

    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.connectiontimeout", "2000");
    properties.put("mail.smtp.timeout", "2000");
    properties.put("mail.smtp.writetimeout", "2000");
    properties.put("mail.smtp.socketFactory.port", "465");
    properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

    javaMailSender.setJavaMailProperties(properties);
    javaMailSender.setUsername("");
    javaMailSender.setPassword("");
    javaMailSender.setHost("smtp.mail.ru");
    javaMailSender.setPort(465);
    return javaMailSender;
  }

  @Bean
  public SimpleMailMessage simpleMailMessage() {
    return new SimpleMailMessage();
  }
}
