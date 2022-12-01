package com.davydov.DownLoadVSVGO.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ListRGE {

  private Map<String, List<String>> map = new HashMap<>();

  public Map<String, List<String>> getMap() {
    return map;
  }

  public void addRGE(String rge, String login, String password) {
    map.put(rge, List.of(login, password));
  }
}
