package org.fisco.bcos.autoconfigure;

import org.fisco.bcos.constants.GasConstants;
import org.fisco.bcos.solidity.M0;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContractConfig {
    @Autowired private Web3j web3j;
    @Autowired private Credentials credentials;
    public String abi="[{\"constant\":true,\"inputs\":[{\"name\":\"payee\",\"type\":\"string\"},{\"name\":\"i\",\"type\":\"uint256\"}],\"name\":\"isMyReceiValid\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"payer\",\"type\":\"string\"},{\"name\":\"payee\",\"type\":\"string\"},{\"name\":\"year\",\"type\":\"uint256\"},{\"name\":\"month\",\"type\":\"uint256\"},{\"name\":\"day\",\"type\":\"uint256\"},{\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"transaction\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"payer\",\"type\":\"string\"}],\"name\":\"getMyDueNum\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"payer\",\"type\":\"string\"},{\"name\":\"bank\",\"type\":\"string\"},{\"name\":\"no\",\"type\":\"uint256\"},{\"name\":\"amount\",\"type\":\"uint256\"},{\"name\":\"year\",\"type\":\"uint256\"},{\"name\":\"month\",\"type\":\"uint256\"},{\"name\":\"day\",\"type\":\"uint256\"}],\"name\":\"finance\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"payer\",\"type\":\"string\"},{\"name\":\"i\",\"type\":\"uint256\"}],\"name\":\"isMyDueValid\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"accType\",\"type\":\"string\"},{\"name\":\"balance\",\"type\":\"uint256\"}],\"name\":\"createAccount\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"payer\",\"type\":\"string\"},{\"name\":\"payee\",\"type\":\"string\"},{\"name\":\"no\",\"type\":\"uint256\"},{\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"transfer\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"payer\",\"type\":\"string\"},{\"name\":\"payee\",\"type\":\"string\"},{\"name\":\"no\",\"type\":\"uint256\"}],\"name\":\"payBack\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"payee\",\"type\":\"string\"}],\"name\":\"getMyReceiNum\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"n\",\"type\":\"uint256\"}],\"name\":\"checkReceiptByNum\",\"outputs\":[{\"name\":\"payer\",\"type\":\"string\"},{\"name\":\"payee\",\"type\":\"string\"},{\"name\":\"amount\",\"type\":\"uint256\"},{\"name\":\"year\",\"type\":\"uint256\"},{\"name\":\"month\",\"type\":\"uint256\"},{\"name\":\"day\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"}]";


    @Bean
    public M0 getM0() throws Exception {
        M0 m = M0.deploy(web3j,
                        credentials,
                        new StaticGasProvider(
                                GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))
                        .send();
        System.out.println("合约地址:"+m.getContractAddress());
        System.out.println("部署成功.");
        return m;
    }
}
