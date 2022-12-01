package com.davydov.DownLoadVSVGO.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ListEmailImpl implements ListEmail {

  List<String> listEmail = new ArrayList<>();

  @Override
  public List<String> getList() {
    return listEmail;
  }

  @Override
  public void addEmail(String email) {
    listEmail.add(email);
  }
}
