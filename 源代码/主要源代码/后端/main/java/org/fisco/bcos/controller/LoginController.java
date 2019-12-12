package org.fisco.bcos.controller;

import com.alibaba.druid.util.StringUtils;
import org.fisco.bcos.solidity.M0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired private M0 m;

    @GetMapping("/login")
    public String login(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String, Object> map, HttpSession session){
        //TODO:查询有没有这个账号
        if (RegisterController.userdb.containsKey(username) && RegisterController.userdb.get(username).equals(password)) {
            session.setAttribute("loginUser",username); //解决不知道用户有没有登录的问题
            String name = session.getAttribute("loginUser").toString();
            System.out.println("正在登陆的是 : "+name);
            return "redirect:/transaction"; //重定向：解决表单重复提交问题
        }else {
            /**
             * 登陆失败，msg，通过模板引擎和前端做交互
             */
            map.put("msg","用户名或密码错误");
            return "login";
        }
    }
}
