package com.trustchain.chargeline.util;

import com.trustchain.chargeline.solidity.ChargingLine.ChargingLine;
import com.trustchain.chargeline.solidity.Friend.Friend;
import com.trustchain.chargeline.solidity.Scan.Scan;
import com.trustchain.chargeline.solidity.ScanRelation.ScanRelation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

@Component
public class ContractUtil {

    @Value("${web3_url}")
    private String web3_url;
    @Value("${ChargingLine_address}")
    private  String ChargingLine_address;
    @Value("${ScanRelation_address}")
    private String ScanRelation_address;
    @Value("${Scan_address}")
    private String Scan_address;
    @Value("${Friend_address}")
    private String Friend_address;
    @Value("${account_address}")
    private String account_address;




    public ChargingLine ChargingLineLoad(){
        Web3j web3j=Web3j.build(new HttpService(web3_url));
        TransactionManager clientTransactionManager=new ClientTransactionManager(web3j,account_address) ;
        ContractGasProvider contractGasProvider=new DefaultGasProvider();
        return  ChargingLine.load(ChargingLine_address,web3j,clientTransactionManager,contractGasProvider.getGasPrice(),contractGasProvider.getGasLimit());
    }
    public ScanRelation ScanRelationLoad(){
        Web3j web3j=Web3j.build(new HttpService(web3_url));
        TransactionManager clientTransactionManager=new ClientTransactionManager(web3j,account_address) ;
        ContractGasProvider contractGasProvider=new DefaultGasProvider();
        return ScanRelation.load(ScanRelation_address,web3j,clientTransactionManager,contractGasProvider.getGasPrice(),contractGasProvider.getGasLimit());
    }
    public Scan ScanLoad(){
        Web3j web3j=Web3j.build(new HttpService(web3_url));
        TransactionManager clientTransactionManager=new ClientTransactionManager(web3j,account_address) ;
        ContractGasProvider contractGasProvider=new DefaultGasProvider();
        return Scan.load(Scan_address,web3j,clientTransactionManager,contractGasProvider.getGasPrice(),contractGasProvider.getGasLimit());
    }
    public Friend FriendLoad(){
        Web3j web3j=Web3j.build(new HttpService(web3_url));
        TransactionManager clientTransactionManager=new ClientTransactionManager(web3j,account_address) ;
        ContractGasProvider contractGasProvider=new DefaultGasProvider();
        return Friend.load(Friend_address,web3j,clientTransactionManager,contractGasProvider.getGasPrice(),contractGasProvider.getGasLimit());
    }


}
