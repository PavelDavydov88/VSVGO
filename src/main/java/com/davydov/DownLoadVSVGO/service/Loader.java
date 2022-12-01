package com.davydov.DownLoadVSVGO.service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Loader implements IAuthorization {
  private String login, password, rge;
  List<String> listDate;
  private int count;

  public int getCount() {
    return count;
  }

  private StringBuilder write;

  public StringBuilder getWrite() {
    return write;
  }

  public void setWrite(StringBuilder write) {
    this.write = write;
  }

  public void setListDate(List<String> listDate) {
    this.listDate = listDate;
  }

  public Loader setLogin(String login) {
    this.login = login;
    return this;
  }

  public Loader setRge(String rge) {
    this.rge = rge;
    return this;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public Loader setPassword(String password) {
    this.password = password;
    return this;
  }

  public Loader(String login, String password, String rge, List listDate) {
    this.login = login;
    this.password = password;
    this.rge = rge;
    this.listDate = listDate;
  }

  @Override
  public void doAuthorization() {
    try {
      Map<String, List<String>> mapLoader = new LinkedHashMap<>();
      CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
      CookieHandler.setDefault(cookieManager);
      String myURL = "https://br.so-ups.ru/webapi/Auth/AuthByUserName";
      String params = "UserName=" + login + "&PassWord=" + password;
      byte[] data;
      URL url = new URL(myURL);
      disableCertificates();
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestProperty("Content-Length", "" + params.getBytes().length);
      OutputStream os = conn.getOutputStream();
      data = params.getBytes("UTF-8");
      os.write(data);
      conn.connect();
      conn.getInputStream();
      for (String list : listDate) {
        url =
            new URL(
                "https://br.so-ups.ru/webapi/api/Export/Xml/Ego.aspx?date=" + list + "&ids=" + rge);
        InputStream inputStream = url.openStream();
        BufferedReader text = new BufferedReader(new InputStreamReader(inputStream));
        List<String> listWithRge = new LinkedList<>();
        while (text.ready()) {
          String readline = text.readLine();
          if (readline.contains("<sta>")) {
            int index = readline.indexOf("<sta>");
            listWithRge.add(readline.substring(index + 5, index + 6));
          }
        }
        mapLoader.put(list, listWithRge);
      }
      write.append(rge);
      for (Map.Entry<String, List<String>> map : mapLoader.entrySet()) {
        write.append(" " + map.getKey() + " ");
        for (String s : map.getValue()) {
          write.append(s + " ");
        }
        System.out.println("data= " + map.getKey() + " " + map.getValue() + " ");
        if (map.getValue().size() != 0) count++;
      }
      write.append("\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void disableCertificates() {
    TrustManager[] trustAllCerts =
        new TrustManager[] {
          new X509TrustManager() {

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
              return null;
            }

            @Override
            public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {}

            @Override
            public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {}
          }
        };
    try {
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new java.security.SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    } catch (Exception e) {
    }
  }
}
