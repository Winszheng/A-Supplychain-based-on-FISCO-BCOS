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
public class FinanceController {
    @Autowired private M0 m;
    @GetMapping("/finance")
    public String finance(Model model) {
        return "finance";
    }

    @PostMapping("/finance")
    public String finance(@RequestParam("payer") String payer,
                          @RequestParam("bank") String bank,
                          @RequestParam("no") BigInteger no,
                          @RequestParam("amount") BigInteger amount,
                          @RequestParam("year") BigInteger year,
                          @RequestParam("month") BigInteger month,
                          @RequestParam("day") BigInteger day,
                          RedirectAttributes model) throws Exception {
        if (RegisterController.userdb != null && RegisterController.userdb.containsKey(payer) && RegisterController.userdb.containsKey(bank)) {
            TransactionReceipt receipt = m.finance(payer, bank, no, amount, year, month, day).send();
            System.out.println("finance receipt:" + receipt);
            model.addFlashAttribute("msg", "提交成功");
        } else {
            model.addFlashAttribute("msg", "信息有误,提交失败");
        }
        return "redirect:/finance";
    }
}
