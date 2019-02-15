package com.trustchain.chargeline.solidity.ChargingLine;

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
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
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
public class ChargingLine extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610c9d806100206000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630a9970cf1461007d578063216411a7146101a157806395e57f65146101cc578063ad6c2f77146102c3578063ca8f8ff314610370578063f18858b5146103b1575b600080fd5b34801561008957600080fd5b506100a8600480360381019080803590602001909291905050506104a8565b60405180851515151581526020018481526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156100fc5780820151818401526020810190506100e1565b50505050905090810190601f1680156101295780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610162578082015181840152602081019050610147565b50505050905090810190601f16801561018f5780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b3480156101ad57600080fd5b506101b6610625565b6040518082815260200191505060405180910390f35b3480156101d857600080fd5b5061023d60048036038101908080359060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610632565b604051808315151515815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561028757808201518184015260208101905061026c565b50505050905090810190601f1680156102b45780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b3480156102cf57600080fd5b506102ee60048036038101908080359060200190929190505050610997565b6040518083815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610334578082015181840152602081019050610319565b50505050905090810190601f1680156103615780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b34801561037c57600080fd5b5061039b60048036038101908080359060200190929190505050610a53565b6040518082815260200191505060405180910390f35b3480156103bd57600080fd5b5061042260048036038101908080359060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610a89565b604051808315151515815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561046c578082015181840152602081019050610451565b50505050905090810190601f1680156104995780820380516001836020036101000a031916815260200191505b50935050505060405180910390f35b6000806060806104b785610a53565b600014156105165760008560206040519081016040528060008152506040805190810160405280600e81526020017f6c696e65206e6f74206578697374000000000000000000000000000000000000815250935093509350935061061e565b600160008087815260200190815260200160002060000154600080888152602001908152602001600020600101808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105d85780601f106105ad576101008083540402835291602001916105d8565b820191906000526020600020905b8154815290600101906020018083116105bb57829003601f168201915b505050505090506040805190810160405280600c81526020017f7375636365737366756c6c79000000000000000000000000000000000000000081525093509350935093505b9193509193565b6000600280549050905090565b60006060600080600061064487610a53565b600014151561079e577f441803fc345c3cd8368bd276709a53832890fc408731f8b9a2271475987e97236000888860405180841515151581526020018381526020018060200180602001838103835284818151815260200191508051906020019080838360005b838110156106c65780820151818401526020810190506106ab565b50505050905090810190601f1680156106f35780820380516001836020036101000a031916815260200191505b50838103825260228152602001807f6661696c65642077697468206c696e65207265636f726420686173206578697381526020017f742e0000000000000000000000000000000000000000000000000000000000008152506040019550505050505060405180910390a160006040805190810160405280601681526020017f6c696e65207265636f7264206861732065786973742e000000000000000000008152509450945061098d565b600280549050925082600014156107b857600191506107de565b6002600184038154811015156107ca57fe5b906000526020600020015490506001810191505b60028290806001815401808255809150509060018203906000526020600020016000909192909190915055508160016000898152602001908152602001600020819055508660008089815260200190815260200160002060000181905550856000808981526020019081526020016000206001019080519060200190610865929190610bcc565b507f441803fc345c3cd8368bd276709a53832890fc408731f8b9a2271475987e97236001888860405180841515151581526020018381526020018060200180602001838103835284818151815260200191508051906020019080838360005b838110156108df5780820151818401526020810190506108c4565b50505050905090810190601f16801561090c5780820380516001836020036101000a031916815260200191505b508381038252600c8152602001807f7375636365737366756c6c7900000000000000000000000000000000000000008152506020019550505050505060405180910390a160016040805190810160405280600c81526020017f7375636365737366756c6c790000000000000000000000000000000000000000815250945094505b5050509250929050565b6000602052806000526040600020600091509050806000015490806001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a495780601f10610a1e57610100808354040283529160200191610a49565b820191906000526020600020905b815481529060010190602001808311610a2c57829003601f168201915b5050505050905082565b60006002805490506000141515610a7f5760016000838152602001908152602001600020549050610a84565b600090505b919050565b60006060610a9684610a53565b60001415610adf5760006040805190810160405280600e81526020017f6c696e65206e6f7420657869737400000000000000000000000000000000000081525091509150610bc5565b826000808681526020019081526020016000206001019080519060200190610b08929190610bcc565b507f86262b17d4e3f8783b172b749b87c29dff01ef01644293ccdeb9c63305e9bf0384846040518083815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610b72578082015181840152602081019050610b57565b50505050905090810190601f168015610b9f5780820380516001836020036101000a031916815260200191505b50935050505060405180910390a160016020604051908101604052806000815250915091505b9250929050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610c0d57805160ff1916838001178555610c3b565b82800160010185558215610c3b579182015b82811115610c3a578251825591602001919060010190610c1f565b5b509050610c489190610c4c565b5090565b610c6e91905b80821115610c6a576000816000905550600101610c52565b5090565b905600a165627a7a723058209c51ed8097ae7bd406a7cf7d8fb8e46df9b12e2ce0caaad91f5d9fc1c1c2d7970029";

    public static final String FUNC_GETSHAREDLINE = "GetSharedLine";

    public static final String FUNC_SHAREDLINENUMBER = "SharedLineNumber";

    public static final String FUNC_ADDLINE = "AddLine";

    public static final String FUNC_LINESMAP = "LinesMap";

    public static final String FUNC_ISEXIST = "isExist";

    public static final String FUNC_UPDATESHAREDLINE = "updateSharedLine";

    public static final Event ADDLINEEVENT_EVENT = new Event("AddLineEvent", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event UPDATELINEEVENT_EVENT = new Event("UpdateLineEvent", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected ChargingLine(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ChargingLine(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<Tuple4<Boolean, BigInteger, String, String>> GetSharedLine(BigInteger lineId) {
        final Function function = new Function(FUNC_GETSHAREDLINE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(lineId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple4<Boolean, BigInteger, String, String>>(
                new Callable<Tuple4<Boolean, BigInteger, String, String>>() {
                    @Override
                    public Tuple4<Boolean, BigInteger, String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<Boolean, BigInteger, String, String>(
                                (Boolean) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> SharedLineNumber() {
        final Function function = new Function(FUNC_SHAREDLINENUMBER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> AddLine(BigInteger lineId, String macAddr) {
        final Function function = new Function(
                FUNC_ADDLINE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(lineId), 
                new org.web3j.abi.datatypes.Utf8String(macAddr)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple2<BigInteger, String>> LinesMap(BigInteger param0) {
        final Function function = new Function(FUNC_LINESMAP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple2<BigInteger, String>>(
                new Callable<Tuple2<BigInteger, String>>() {
                    @Override
                    public Tuple2<BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, String>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> isExist(BigInteger lineId) {
        final Function function = new Function(FUNC_ISEXIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(lineId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> updateSharedLine(BigInteger lineId, String macAddr) {
        final Function function = new Function(
                FUNC_UPDATESHAREDLINE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(lineId), 
                new org.web3j.abi.datatypes.Utf8String(macAddr)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<AddLineEventEventResponse> getAddLineEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDLINEEVENT_EVENT, transactionReceipt);
        ArrayList<AddLineEventEventResponse> responses = new ArrayList<AddLineEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddLineEventEventResponse typedResponse = new AddLineEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.status = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.lineId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.macAddr = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.errors = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AddLineEventEventResponse> addLineEventEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, AddLineEventEventResponse>() {
            @Override
            public AddLineEventEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDLINEEVENT_EVENT, log);
                AddLineEventEventResponse typedResponse = new AddLineEventEventResponse();
                typedResponse.log = log;
                typedResponse.status = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.lineId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.macAddr = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.errors = (String) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<AddLineEventEventResponse> addLineEventEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDLINEEVENT_EVENT));
        return addLineEventEventObservable(filter);
    }

    public List<UpdateLineEventEventResponse> getUpdateLineEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(UPDATELINEEVENT_EVENT, transactionReceipt);
        ArrayList<UpdateLineEventEventResponse> responses = new ArrayList<UpdateLineEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UpdateLineEventEventResponse typedResponse = new UpdateLineEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.lineId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.macAddr = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<UpdateLineEventEventResponse> updateLineEventEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, UpdateLineEventEventResponse>() {
            @Override
            public UpdateLineEventEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(UPDATELINEEVENT_EVENT, log);
                UpdateLineEventEventResponse typedResponse = new UpdateLineEventEventResponse();
                typedResponse.log = log;
                typedResponse.lineId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.macAddr = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<UpdateLineEventEventResponse> updateLineEventEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(UPDATELINEEVENT_EVENT));
        return updateLineEventEventObservable(filter);
    }

    public static RemoteCall<ChargingLine> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ChargingLine.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ChargingLine> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ChargingLine.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static ChargingLine load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ChargingLine(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static ChargingLine load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ChargingLine(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class AddLineEventEventResponse {
        public Log log;

        public Boolean status;

        public BigInteger lineId;

        public String macAddr;

        public String errors;
    }

    public static class UpdateLineEventEventResponse {
        public Log log;

        public BigInteger lineId;

        public String macAddr;
    }
}
