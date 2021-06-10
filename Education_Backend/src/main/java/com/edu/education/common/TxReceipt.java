package com.edu.education.common;

import com.bsnbase.sdk.entity.res.fiscobcos.ResKeyEscrow;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;

//兼容本地搭建fisco的交易回执TransactionReceipt和bsn的交易回执ResKeyEscrow
public class TxReceipt {
    public String BlockHash;
    public BigInteger BlockNumber;
    public String TransactionHash;
    public String Output;

    public TxReceipt(TransactionReceipt receipt){
        setBlockHash(receipt.getBlockHash());
        setBlockNumber(receipt.getBlockNumber());
        setTransactionHash(receipt.getTransactionHash());
        setOutput(receipt.getOutput());
    }

    public TxReceipt(ResKeyEscrow receipt){
        setBlockHash(receipt.getBlockHash());
        setBlockNumber(BigInteger.valueOf(receipt.getBlockNumber()));
        setTransactionHash(receipt.getTxId());
        setOutput(receipt.getOutput());
    }

    public void setBlockHash(String blockHash) {
        BlockHash = blockHash;
    }

    public void setBlockNumber(BigInteger blockNumber) {
        BlockNumber = blockNumber;
    }

    public void setTransactionHash(String transactionHash) {
        TransactionHash = transactionHash;
    }

    public void setOutput(String output) {
        Output = output;
    }

    public String getBlockHash() {
        return BlockHash;
    }

    public BigInteger getBlockNumber() {
        return BlockNumber;
    }

    public String getTransactionHash() {
        return TransactionHash;
    }

    public String getOutput() {
        return Output;
    }
}
