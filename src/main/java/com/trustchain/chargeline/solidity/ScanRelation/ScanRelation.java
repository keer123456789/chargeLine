package com.trustchain.chargeline.solidity.ScanRelation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.4.0.
 */
public class ScanRelation extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610820806100206000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063080f78f61461007d578063118c0d84146101925780632204b7c9146101d357806350275aac146102145780635faa776e146102d657806373d9907014610301575b600080fd5b34801561008957600080fd5b5061010c60048036038101908080359060200190929190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929080359060200190929190803590602001909291908035906020019092919050505061034c565b604051808315151515815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561015657808201518184015260208101905061013b565b50505050905090810190601f1680156101835780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b34801561019e57600080fd5b506101bd600480360381019080803590602001909291905050506105ee565b6040518082815260200191505060405180910390f35b3480156101df57600080fd5b506101fe6004803603810190808035906020019092919050505061060e565b6040518082815260200191505060405180910390f35b34801561022057600080fd5b5061023f60048036038101908080359060200190929190505050610644565b6040518086815260200180602001858152602001848152602001838152602001828103825286818151815260200191508051906020019080838360005b8381101561029757808201518184015260208101905061027c565b50505050905090810190601f1680156102c45780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b3480156102e257600080fd5b506102eb610712565b6040518082815260200191505060405180910390f35b34801561030d57600080fd5b50610336600480360381019080803590602001909291908035906020019092919050505061071f565b6040518082815260200191505060405180910390f35b60006060600080600061035e8a61060e565b60001415156103fd577f32a44530680621bad0282f626e0c900946f86e7bb08e3082f0bdee1d6e400d636000898c6000604051808515151515815260200184815260200183815260200182815260200194505050505060405180910390a160006040805190810160405280601f81526020017f6661696c656420776974682072656c6174696f6e206861732065786973742e00815250945094506105e1565b60028054905092508260001415610417576001915061043d565b60026001840381548110151561042957fe5b906000526020600020015490506001810191505b600282908060018154018082558091505090600182039060005260206000200160009091929091909150555081600160008c815260200190815260200160002081905550896000808c815260200190815260200160002060000181905550886000808c815260200190815260200160002060010190805190602001906104c492919061074f565b50876000808c815260200190815260200160002060020181905550866000808c815260200190815260200160002060030181905550856000808c815260200190815260200160002060040181905550600360008981526020019081526020016000208a90806001815401808255809150509060018203906000526020600020016000909192909190915055507f32a44530680621bad0282f626e0c900946f86e7bb08e3082f0bdee1d6e400d636001898c85604051808515151515815260200184815260200183815260200182815260200194505050505060405180910390a160016040805190810160405280600c81526020017f7375636365737366756c6c790000000000000000000000000000000000000000815250945094505b5050509550959350505050565b600060036000838152602001908152602001600020805490509050919050565b6000600280549050600014151561063a576001600083815260200190815260200160002054905061063f565b600090505b919050565b6000602052806000526040600020600091509050806000015490806001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106f65780601f106106cb576101008083540402835291602001916106f6565b820191906000526020600020905b8154815290600101906020018083116106d957829003601f168201915b5050505050908060020154908060030154908060040154905085565b6000600280549050905090565b60036020528160005260406000208181548110151561073a57fe5b90600052602060002001600091509150505481565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061079057805160ff19168380011785556107be565b828001600101855582156107be579182015b828111156107bd5782518255916020019190600101906107a2565b5b5090506107cb91906107cf565b5090565b6107f191905b808211156107ed5760008160009055506001016107d5565b5090565b905600a165627a7a72305820bb51524df760933244b39cfdef48736c21ce95eac5a8bb2f615f0c42288b05830029";

    public static final String FUNC_ADDSCANRELATION = "AddScanRelation";

    public static final String FUNC_GETSUMSCANSFORLINED = "GetSumScansForLined";

    public static final String FUNC_ISRELATIONEXIST = "isRelationExist";

    public static final String FUNC_RELATIONMAP = "RelationMap";

    public static final String FUNC_GETSUMSCANRELATIONS = "GetSumScanRelations";

    public static final String FUNC_LINESCANSTATISTIC = "LineScanStatistic";

    public static final Event ADDSCANRELATIONEVENT_EVENT = new Event("AddScanRelationEvent", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected ScanRelation(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ScanRelation(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> AddScanRelation(BigInteger id, String scanTime, BigInteger lineId, BigInteger adId, BigInteger siteId) {
        final Function function = new Function(
                FUNC_ADDSCANRELATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(id), 
                new org.web3j.abi.datatypes.Utf8String(scanTime), 
                new org.web3j.abi.datatypes.generated.Uint256(lineId), 
                new org.web3j.abi.datatypes.generated.Uint256(adId), 
                new org.web3j.abi.datatypes.generated.Uint256(siteId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> GetSumScansForLined(BigInteger lineId) {
        final Function function = new Function(FUNC_GETSUMSCANSFORLINED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(lineId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> isRelationExist(BigInteger id) {
        final Function function = new Function(FUNC_ISRELATIONEXIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple5<BigInteger, String, BigInteger, BigInteger, BigInteger>> RelationMap(BigInteger param0) {
        final Function function = new Function(FUNC_RELATIONMAP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple5<BigInteger, String, BigInteger, BigInteger, BigInteger>>(
                new Callable<Tuple5<BigInteger, String, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<BigInteger, String, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, String, BigInteger, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (BigInteger) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> GetSumScanRelations() {
        final Function function = new Function(FUNC_GETSUMSCANRELATIONS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> LineScanStatistic(BigInteger param0, BigInteger param1) {
        final Function function = new Function(FUNC_LINESCANSTATISTIC, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public List<AddScanRelationEventEventResponse> getAddScanRelationEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDSCANRELATIONEVENT_EVENT, transactionReceipt);
        ArrayList<AddScanRelationEventEventResponse> responses = new ArrayList<AddScanRelationEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddScanRelationEventEventResponse typedResponse = new AddScanRelationEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._exist = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._lineId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._relationId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._index = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AddScanRelationEventEventResponse> addScanRelationEventEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, AddScanRelationEventEventResponse>() {
            @Override
            public AddScanRelationEventEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDSCANRELATIONEVENT_EVENT, log);
                AddScanRelationEventEventResponse typedResponse = new AddScanRelationEventEventResponse();
                typedResponse.log = log;
                typedResponse._exist = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._lineId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._relationId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._index = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<AddScanRelationEventEventResponse> addScanRelationEventEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDSCANRELATIONEVENT_EVENT));
        return addScanRelationEventEventObservable(filter);
    }

    public static RemoteCall<ScanRelation> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ScanRelation.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ScanRelation> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ScanRelation.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static ScanRelation load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ScanRelation(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static ScanRelation load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ScanRelation(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class AddScanRelationEventEventResponse {
        public Log log;

        public Boolean _exist;

        public BigInteger _lineId;

        public BigInteger _relationId;

        public BigInteger _index;
    }
}
