package org.fisco.bcos.controller;

import org.fisco.bcos.solidity.M0;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigInteger;

@Controller
public class PaybackController {
    @Autowired private M0 m;
    @GetMapping("/payback")
    public String payback(Model model) {
        return "payback";
    }
    @PostMapping("/payback")
    public String payback(@RequestParam("payer") String payer,
                          @RequestParam("payee") String payee,
                          @RequestParam("no") String no,
                          RedirectAttributes model) throws Exception {
        if (RegisterController.userdb != null && RegisterController.userdb.containsKey(payer) && RegisterController.userdb.containsKey(payee)) {
            TransactionReceipt receipt = m.payBack(payer, payee, new BigInteger(no)).send();
            System.out.println("payback:"+receipt);
            model.addFlashAttribute("msg", "账款结算成功");
        } else {
            model.addFlashAttribute("msg","信息有误,请重新填写");
        }
        return "redirect:/payback";
    }
}
