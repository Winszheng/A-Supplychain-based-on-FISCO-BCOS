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
public class TransferController {
    @Autowired private M0 m;

    @GetMapping("/transfer")
    public String transfer(Model model) {
        return "transfer";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam("payer") String payer,
                           @RequestParam("payee") String payee,
                           @RequestParam("no") BigInteger no,
                           @RequestParam("amount") BigInteger amount,
                           RedirectAttributes model) throws Exception {
        /*TODO:之后肯定要改的,怎么改另说,现在无脑植入就对了*/
        if (RegisterController.userdb != null && RegisterController.userdb.containsKey(payer) && RegisterController.userdb.containsKey(payee)) {
            TransactionReceipt receipt = m.transfer(payer, payee, no, amount).send();
            System.out.println("transfer receipt:"+receipt);
            model.addFlashAttribute("msg", "账款转让成功");
        } else {
            model.addFlashAttribute("msg", "信息有误,转让失败");
        }
        return "redirect:/transfer";
    }
}
