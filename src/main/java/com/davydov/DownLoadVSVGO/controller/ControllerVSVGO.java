package com.davydov.DownLoadVSVGO.controller;

import com.davydov.DownLoadVSVGO.service.EmailCredentials;
import com.davydov.DownLoadVSVGO.service.ListEmailImpl;
import com.davydov.DownLoadVSVGO.service.ListRGE;
import com.davydov.DownLoadVSVGO.service.StartLoad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Controller
public class ControllerVSVGO {

  private final JavaMailSenderImpl javaMailSender;

  private final StartLoad startLoad;

  private final ListRGE listRGE;

  private final ListEmailImpl listEmailImpl;

  private final EmailCredentials emailCredentials;

  @Autowired
  public ControllerVSVGO(JavaMailSenderImpl javaMailSender, StartLoad startLoad, ListRGE listRGE, ListEmailImpl listEmailImpl, EmailCredentials emailCredentials) {
    this.javaMailSender = javaMailSender;
    this.startLoad = startLoad;
    this.listRGE = listRGE;
    this.listEmailImpl = listEmailImpl;
    this.emailCredentials = emailCredentials;
  }

  @GetMapping("/start")
  public String getStart(Model model)  {
    System.out.println("email= " + emailCredentials.getLogin());
    model.addAttribute(emailCredentials);
    startLoad.setStart(true);
    return "start";
  }

  @PostMapping("/start")
  public String saveStart(EmailCredentials emailCredentials, Model model) {
    this.emailCredentials.setLogin(emailCredentials.getLogin());
    this.emailCredentials.setPassword(emailCredentials.getPassword());
    this.emailCredentials.setEmail(emailCredentials.getEmail());
    javaMailSender.setUsername(this.emailCredentials.getEmail());
    javaMailSender.setPassword(this.emailCredentials.getPassword());
    model.addAttribute(this.emailCredentials);
    return "addEmail";
  }

  @GetMapping("/addEmail")
  public String getAddEmail() {
    for (String st : listEmailImpl.getList()) {
      System.out.println("email for dispatching =" + st);
    }
    return "addEmail";
  }

  @PostMapping("/addEmail")
  public String saveEmail(@ModelAttribute("mail") String emailadd) {
    this.listEmailImpl.addEmail(emailadd);
    for (String st : listEmailImpl.getList()) {
      System.out.println("email for dispatching =" + st);
    }
    return "addEmail";
  }

  @GetMapping("/addRGE")
  public String getAddRGE(Model model) {
    return "addRGE";
  }

  @PostMapping("/addRGE")
  public String saveRGE(
      @ModelAttribute("RGE") String rge,
      @ModelAttribute("login") String login,
      @ModelAttribute("password") String password) {
    listRGE.addRGE(rge, login, password);
    for (Map.Entry<String, List<String>> map : listRGE.getMap().entrySet()) {
      System.out.print(map.getKey() + " ");
    }
    return "addRGE";
  }

  @GetMapping("/startLoad")
  public String startLoad() {

      startLoad.doStart();

    return "startLoad";
  }

  @GetMapping("/stop")
  public String getStop() {
    startLoad.setStart(false);
    return "stop";
  }
}
