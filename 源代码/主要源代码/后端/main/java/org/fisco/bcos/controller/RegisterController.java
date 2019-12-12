package org.fisco.bcos.controller;

import org.fisco.bcos.solidity.M0;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RegisterController {
    @Autowired private M0 m;
    static public Map<String, String> userdb = new HashMap<String, String>();
    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    //@ResponseBody
    public String register(@RequestParam("username") String username,
                    @RequestParam("accType") String accType,
                    @RequestParam("balance") String _balance,
                    @RequestParam("password") String password,
                    Map<String,Object> map) throws Exception {
        if (userdb!=null && userdb.containsKey(username)) {

            map.put("msg", "已有相同用户名,请重新注册");
            return "register";
        } else if (!(accType.equals("bank") || accType.equals("core") || accType.equals("notCore"))) {
            map.put("msg","企业类型有误，请重新注册");
            return "register";
        } else {
            BigInteger balance = new BigInteger(_balance);
            TransactionReceipt receipt = m.createAccount(username, accType, balance).sendAsync().get();
            userdb.put(username, password);
            //注册成功转到登录页面
            map.put("msg", "注册成功,请登陆");
            System.out.println(receipt);
            System.out.println("注册成功++++++++++++++");
            return "login";
        }
    }
}
