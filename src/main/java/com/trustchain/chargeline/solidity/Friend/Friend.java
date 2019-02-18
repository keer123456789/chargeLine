package com.trustchain.chargeline.solidity.Friend;

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
public class Friend extends Contract {
    private static final String BINARY = "60806040526000805534801561001457600080fd5b50610d06806100246000396000f300608060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680634d3d096b1461006757806374c55cb2146100e8578063a2ff790a146101f5578063fe00d99314610220575b600080fd5b34801561007357600080fd5b506100ce600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610415565b604051808215151515815260200191505060405180910390f35b3480156100f457600080fd5b506101db600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061051d565b604051808215151515815260200191505060405180910390f35b34801561020157600080fd5b5061020a6108cf565b6040518082815260200191505060405180910390f35b34801561022c57600080fd5b5061024b600480360381019080803590602001909291905050506108dc565b604051808615151515815260200180602001806020018060200180602001858103855289818151815260200191508051906020019080838360005b838110156102a1578082015181840152602081019050610286565b50505050905090810190601f1680156102ce5780820380516001836020036101000a031916815260200191505b50858103845288818151815260200191508051906020019080838360005b838110156103075780820151818401526020810190506102ec565b50505050905090810190601f1680156103345780820380516001836020036101000a031916815260200191505b50858103835287818151815260200191508051906020019080838360005b8381101561036d578082015181840152602081019050610352565b50505050905090810190601f16801561039a5780820380516001836020036101000a031916815260200191505b50858103825286818151815260200191508051906020019080838360005b838110156103d35780820151818401526020810190506103b8565b50505050905090810190601f1680156104005780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b6000806000600380549050915060008214156104345760009250610516565b600190505b8181111515610515576104fa600160008381526020019081526020016000206002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104ef5780601f106104c4576101008083540402835291602001916104ef565b820191906000526020600020905b8154815290600101906020018083116104d257829003601f168201915b505050505085610b47565b156105085760019250610516565b8080600101915050610439565b5b5050919050565b60008060008061052c87610415565b156105de577fd364d5f23d8200b42fbfecb36aacb52daf54aecf85687fdc28b239a7e1982ca46040518080602001806020018060200180602001858103855260008152602001602001858103845260008152602001602001858103835260008152602001602001858103825260118152602001807f467269656e64206861732065786974212100000000000000000000000000000081525060200194505050505060405180910390a1600093506108c5565b60016000540160008190555060038054905092508260001415610604576001915061062a565b60036001840381548110151561061657fe5b906000526020600020015490506001810191505b600382908060018154018082558091505090600182039060005260206000200160009091929091909150555081600260008054815260200190815260200160002081905550866001600080548152602001908152602001600020600201908051906020019061069a929190610c35565b5085600160008054815260200190815260200160002060010190805190602001906106c6929190610c35565b5084600160008054815260200190815260200160002060030190805190602001906106f2929190610c35565b506000546001600080548152602001908152602001600020600001819055507fd364d5f23d8200b42fbfecb36aacb52daf54aecf85687fdc28b239a7e1982ca48787876040518080602001806020018060200180602001858103855288818151815260200191508051906020019080838360005b83811015610781578082015181840152602081019050610766565b50505050905090810190601f1680156107ae5780820380516001836020036101000a031916815260200191505b50858103845287818151815260200191508051906020019080838360005b838110156107e75780820151818401526020810190506107cc565b50505050905090810190601f1680156108145780820380516001836020036101000a031916815260200191505b50858103835286818151815260200191508051906020019080838360005b8381101561084d578082015181840152602081019050610832565b50505050905090810190601f16801561087a5780820380516001836020036101000a031916815260200191505b508581038252600c8152602001807f7375636365737366756c6c79000000000000000000000000000000000000000081525060200197505050505050505060405180910390a1600193505b5050509392505050565b6000600380549050905090565b6000606080606080600180600088815260200190815260200160002060020160016000898152602001908152602001600020600101600160008a81526020019081526020016000206003016040805190810160405280600781526020017f7375636365737300000000000000000000000000000000000000000000000000815250929190828054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109f55780601f106109ca576101008083540402835291602001916109f5565b820191906000526020600020905b8154815290600101906020018083116109d857829003601f168201915b50505050509250818054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610a915780601f10610a6657610100808354040283529160200191610a91565b820191906000526020600020905b815481529060010190602001808311610a7457829003601f168201915b50505050509150808054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610b2d5780601f10610b0257610100808354040283529160200191610b2d565b820191906000526020600020905b815481529060010190602001808311610b1057829003601f168201915b505050505090509450945094509450945091939590929450565b600081518351141515610b5d5760009050610c2f565b816040518082805190602001908083835b602083101515610b935780518252602082019150602081019050602083039250610b6e565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902060001916836040518082805190602001908083835b602083101515610bfa5780518252602082019150602081019050602083039250610bd5565b6001836020036101000a0380198251168184511680821785525050505050509050019150506040518091039020600019161490505b92915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610c7657805160ff1916838001178555610ca4565b82800160010185558215610ca4579182015b82811115610ca3578251825591602001919060010190610c88565b5b509050610cb19190610cb5565b5090565b610cd791905b80821115610cd3576000816000905550600101610cbb565b5090565b905600a165627a7a72305820b757f4909ed823f19ef9b2eb0dbce1980f8ca88737045bf8d23f13e4cbc8ae4e0029";

    public static final String FUNC_ISEXIST = "isExist";

    public static final String FUNC_ADDFRIEND = "addFriend";

    public static final String FUNC_GETFRIENDTOTAL = "getFriendTotal";

    public static final String FUNC_GETFRIENDBYID = "getFriendById";

    public static final Event ADDFRIENDEVENT_EVENT = new Event("addFriendEvent", 
            Arrays.<TypeReference<?>>asList(),
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected Friend(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Friend(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<Boolean> isExist(String _openid) {
        final Function function = new Function(FUNC_ISEXIST, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_openid)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> addFriend(String _useropenid, String _username, String _date) {
        final Function function = new Function(
                FUNC_ADDFRIEND, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_useropenid), 
                new org.web3j.abi.datatypes.Utf8String(_username), 
                new org.web3j.abi.datatypes.Utf8String(_date)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getFriendTotal() {
        final Function function = new Function(FUNC_GETFRIENDTOTAL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple5<Boolean, String, String, String, String>> getFriendById(BigInteger id) {
        final Function function = new Function(FUNC_GETFRIENDBYID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple5<Boolean, String, String, String, String>>(
                new Callable<Tuple5<Boolean, String, String, String, String>>() {
                    @Override
                    public Tuple5<Boolean, String, String, String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<Boolean, String, String, String, String>(
                                (Boolean) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (String) results.get(4).getValue());
                    }
                });
    }

    public List<AddFriendEventEventResponse> getAddFriendEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDFRIENDEVENT_EVENT, transactionReceipt);
        ArrayList<AddFriendEventEventResponse> responses = new ArrayList<AddFriendEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddFriendEventEventResponse typedResponse = new AddFriendEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.openid = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.username = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.date = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.result = (String) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<AddFriendEventEventResponse> addFriendEventEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, AddFriendEventEventResponse>() {
            @Override
            public AddFriendEventEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDFRIENDEVENT_EVENT, log);
                AddFriendEventEventResponse typedResponse = new AddFriendEventEventResponse();
                typedResponse.log = log;
                typedResponse.openid = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.username = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.date = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.result = (String) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<AddFriendEventEventResponse> addFriendEventEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDFRIENDEVENT_EVENT));
        return addFriendEventEventObservable(filter);
    }

    public static RemoteCall<Friend> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Friend.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Friend> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Friend.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Friend load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Friend(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Friend load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Friend(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class AddFriendEventEventResponse {
        public Log log;

        public String openid;

        public String username;

        public String date;

        public String result;
    }
}
