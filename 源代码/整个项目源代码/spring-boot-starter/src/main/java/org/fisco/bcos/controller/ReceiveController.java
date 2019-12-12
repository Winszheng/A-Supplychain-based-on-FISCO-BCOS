package org.fisco.bcos.controller;

import org.fisco.bcos.domain.Receipt;
import org.fisco.bcos.solidity.M0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReceiveController {
    @Autowired private M0 m;
    @GetMapping("/receive")
    public String list(Model model,
                       HttpSession session) throws Exception {
        String username = session.getAttribute("loginUser").toString();
        System.out.println("正在receive页面的是:"+username);
        int n=m.getMyReceiNum(username).send().intValue();
        System.out.println("应收账款记录数量"+n);
        List<Receipt> allReceipt = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            BigInteger index = m.getMyReceiveIndex(username, new BigInteger(i + "")).send();
            Receipt receipt = new Receipt();
            receipt.setNo(index.intValue());
            receipt.setPayer(m.checkReceiptByNum(index).send().getValue1());
            receipt.setPayee(m.checkReceiptByNum(index).send().getValue2());
            receipt.setAmount(m.checkReceiptByNum(index).send().getValue3().intValue());
            String day = m.checkReceiptByNum(index).send().getValue4().intValue() + "/" +
                    m.checkReceiptByNum(index).send().getValue5().intValue() + "/" +
                    m.checkReceiptByNum(index).send().getValue6().intValue() + "";
            receipt.setDay(day);
            boolean _bool = m.checkReceiptByNum(index).send().getValue7();
            System.out.println("结算状态："+_bool);
            if (_bool) {
                receipt.setStatus("已结算");
            } else {
                receipt.setStatus("未结算");
            }
            allReceipt.add(receipt);
        }
        model.addAttribute("allReceipt", allReceipt);
        System.out.println("应收账款展示完毕");
        return "receive";
    }
}
