package org.fisco.bcos.controller;

import jnr.ffi.annotations.In;
import org.fisco.bcos.solidity.M0;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigInteger;

@Controller
public class TransactionController {
    String contractAddress="0x201c27026ba323f87df0668166fdc9bedb73b236";
    private Web3j web3j;
    private TransactionManager credentials;
    @Autowired private M0 m;
            //=M0.load(contractAddress,web3j,credentials,new StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT));
    @GetMapping("/transaction")
    public String home() {
        return "transaction";
    }

    @PostMapping("/transaction")
    public String transaction(@RequestParam("payer") String payer,
                              @RequestParam("payee") String payee,
                              @RequestParam("amount") String _amount,
                              @RequestParam("year") String _year,
                              @RequestParam("month") String _month,
                              @RequestParam("day") String _day,
                              RedirectAttributes model) throws Exception {
        if (!validDay(_year, _month, _day)) {
            model.addFlashAttribute("msg", "日期有误,提交失败");
        }else if(RegisterController.userdb != null && RegisterController.userdb.containsKey(payer) && RegisterController.userdb.containsKey(payee)){
           BigInteger amount = new BigInteger(_amount);
           BigInteger year = new BigInteger(_year);
           BigInteger month = new BigInteger(_month);
           BigInteger day = new BigInteger(_day);
           TransactionReceipt receipt =  m.transaction(payer,payee,year,month,day,amount).send();
           System.out.println("采购成功+++++++++++");
           System.out.println(receipt);
           //解决表单重复提交的问题
           model.addFlashAttribute("msg", "采购成功");
        }else{
           model.addFlashAttribute("msg", "信息有误,提交失败");
        }
        return "redirect:/transaction";
    }
    public boolean validDay(String _year, String _month, String _day){
        int year = Integer.parseInt(_year);
        int month =Integer.parseInt(_month);
        int day = Integer.parseInt(_day);
        if(year<2019) return false;//毕竟2019了
        int[] m = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) {
            m[1] = 29;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > m[month - 1]) {
            return false;
        }
        return true;
    }
}
