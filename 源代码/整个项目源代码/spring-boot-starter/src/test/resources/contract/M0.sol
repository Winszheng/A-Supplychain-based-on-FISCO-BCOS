pragma solidity ^0.4.25;
/*暂时来说，合约端写完了*/

contract M0{
    /*第一部分：存储信息的struct*/
    struct Account{ //公司
        string name;    //公司名字
        string accType;    //公司类型:银行　核心企业　非核心企业—— bank core notCore
        uint balance;   //余额
        string status;  //检验是否有效
    }
    struct receipt{ //应收账款
        string payer;   //欠款方
        string payee;   //收款方
        uint amount;     //金额
        uint year;  //账单到期日
        uint month;
        uint day;
        bool status;  //如果银行参与认证，可以修改账单的状态，比如从没有融资资格变成可以融资,true:可以融资
        bool finish;    //判断是否结算完毕，true:结算完毕
    }
    /*第二部分：映射和数组*/
    mapping(string => Account) accountMap;  //存储账户的mapping:string:name
    mapping(uint=>receipt) receiptMap; //存储应收账款的mapping:uint=>no.
    uint receiptNum=0;

    mapping(string => uint []) myDue;   //我欠别人的
    mapping(string => uint []) myRecei; //别人欠我的
    /*第三部分：基本功能
        因为对金融行业存在很多不熟悉，所以处理方法并不专业。
    */
    /*1.注册*/
    function createAccount(string name, string accType, uint balance) public {
        if(keccak256(abi.encodePacked(accountMap[name].status))==keccak256(abi.encodePacked("V")))return;  //公司已存在
        accountMap[name]=Account(name,accType,balance,"V");
    }
    /*2.采购商品，签发应收账款——制造欠条*/
    function transaction(string payer, string payee, uint year, uint month, uint day, uint amount) public {
        bool status=false;
        if(keccak256(abi.encodePacked(accountMap[payer].accType))==keccak256(abi.encodePacked("core")))status=true;
        receiptMap[receiptNum]=receipt(payer,payee,amount,year,month,day,status,false);
        myDue[payer].push(receiptNum);
        myRecei[payee].push(receiptNum);
        receiptNum++;
    }
    /*3.应收账款的转让上链
        两种情况：
            (1)修改债权人
            (2)分割欠条
        处理：无论转让多少，都是欠条金额的变更和新欠条的产生，
             区别在于，当完整转让欠条的时候，对上一个债权人来说，金额清零且“还清”
             暂时不考虑变更日期等细节,可能之后再修改
    */
    function transfer(string payer, string payee, uint no, uint amount) public {
        //确定欠条的债权人是payer且未还清且金额有效
        if(keccak256(abi.encodePacked(receiptMap[no].payee))!=keccak256(abi.encodePacked(payer)) || receiptMap[no].finish!=false || receiptMap[no].amount<amount)return;

        receiptMap[no].amount-=amount;
        if(receiptMap[no].amount==0)receiptMap[no].finish=true;

        receiptMap[receiptNum]=receipt(receiptMap[no].payer,payee,amount,receiptMap[no].year,receiptMap[no].month,receiptMap[no].day,receiptMap[no].status,false);
        myRecei[payee].push(receiptNum);
        receiptNum++;
    }
    /*4.利用应收款项融资上链
        并没有发生债权人的的变更之类,而是多借了一笔钱
        借到钱的企业，balance会增加
        没有处理日期的问题，因为不知道怎么处理
    */
    function finance(string payer, string bank, uint no, uint amount,uint year, uint month, uint day) public {
        //判断能不能融资
        if(keccak256(abi.encodePacked(receiptMap[no].payee))!=keccak256(abi.encodePacked(payer)) || receiptMap[no].finish==true || receiptMap[no].amount<amount)return;
        receiptMap[receiptNum]=receipt(payer,bank,amount,year,month,day,false,false);
        myDue[payer].push(receiptNum);
        myRecei[bank].push(receiptNum);
        receiptNum++;
        accountMap[payer].balance+=amount;
    }
    /*5.应收账款支付结算上链
        还钱，改变账单状态
        改变债权人和债务人的balance
    */
    function payBack(string payer, string payee, uint no) public {
        if(keccak256(abi.encodePacked(receiptMap[no].payer))!=keccak256(abi.encodePacked(payer)) || keccak256(abi.encodePacked(receiptMap[no].payee))!=keccak256(abi.encodePacked(payee)))return;
        receiptMap[no].finish = true;
        accountMap[payer].balance-=receiptMap[no].amount;
        accountMap[payee].balance+=receiptMap[no].amount;
    }
    /*测试
    function show() constant public returns(string) {
        return "M0.sol";
    }*/
    /*6.展示应还账款——solidity没办法返回结构体和变成数组,
        （1）拿到手里的账单数量
        （2）查询账单
    */
    function getMyDueNum(string payer) constant public returns (uint) {
        return myDue[payer].length;
    }
    function getMyDueIndex(string payer, uint i) constant public returns(uint) {
        return myDue[payer][i];
    }
    function checkReceiptByNum(uint no) constant public returns(string payer, string payee, uint amount, uint year,uint month, uint day, bool finish){
        return (receiptMap[no].payer,receiptMap[no].payee,receiptMap[no].amount,receiptMap[no].year,receiptMap[no].month,receiptMap[no].day,receiptMap[no].finish);
    }
    /*7.展示应收账款
        （1）拿到手里的账单数量
        （2）查询账单
    */
    function getMyReceiNum(string payee) constant public returns (uint){
        return myRecei[payee].length;
    }
    function getMyReceiveIndex(string payee, uint i) constant public returns (uint) {
        return myRecei[payee][i];
    }
}
