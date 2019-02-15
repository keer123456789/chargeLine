package com.trustchain.chargeline.controller;

import com.trustchain.chargeline.domain.Chargline;
import com.trustchain.chargeline.domain.JsonResult;
import com.trustchain.chargeline.solidity.ChargingLine.ChargingLine;
import com.trustchain.chargeline.util.ContractUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

@Controller
@RequestMapping({"instalSmall"})
public class ChargingLineController {

    @Autowired
    ContractUtil contractUtil;

    /**
     * 增加充电线
     * @param chargline
     * @return
     */
    @RequestMapping({"/oleInstalChargLine"})
    @ResponseBody
    public JsonResult oleInstalChargLine(@RequestBody Chargline chargline) throws Exception {
        JsonResult jsonResult =new JsonResult();
        ChargingLine chargingLine=contractUtil.ChargingLineLoad();
        BigInteger receipt=chargingLine.isExist(BigInteger.valueOf(Long.parseLong(chargline.getLineId()))).send();
        System.out.println(receipt.toString());
        if(receipt.equals(BigInteger.valueOf(0))){
            TransactionReceipt transactionReceipt=chargingLine.AddLine(BigInteger.valueOf(Long.parseLong(chargline.getLineId())),chargline.getMacAddr()).send();
//            Thread.sleep(2000);

            jsonResult.setState(1);
            jsonResult.setMessage(chargline.getLineId()+"号数据线入库成功。");

        }else{
            jsonResult.setState(0);
            jsonResult.setMessage(chargline.getLineId()+"号数据线已经在库中。");

        }
        return jsonResult;
    }


    public static void main(String[] args) throws Exception {
        Web3j web3j = Admin.build(new HttpService("http://127.0.0.1:7545"));
        TransactionManager clientTransactionManager = new ClientTransactionManager(web3j, "0x25Dd6542f6434e586845f097BE40D62480E96E6a");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();

        ChargingLine chargingLine= (ChargingLine) ChargingLine.load("0x962fF726dce4C62c35547Ec677f06a77932DE7E0",web3j,clientTransactionManager,contractGasProvider.getGasPrice(),contractGasProvider.getGasLimit());
        TransactionReceipt receipt=chargingLine.AddLine(BigInteger.valueOf(115),"aaaassssss").send();
        System.out.println(chargingLine.getAddLineEventEvents(receipt).get(0).status);




    }

}
