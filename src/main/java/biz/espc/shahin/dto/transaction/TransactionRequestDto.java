package biz.espc.shahin.dto.transaction;

import biz.espc.shahin.enumeration.bank.Bank;
import biz.espc.shahin.enumeration.transaction.TransactionPurpose;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TransactionRequestDto {
    private String nationalCode;
    private BigDecimal amount;
    private Bank destinationBank;
    @JsonProperty("sourceAccount")
    private String fromAccount;
    @JsonProperty("babat")
    private TransactionPurpose purpose;
    private String withdrawDescription;
    private String destinationAccountNumber;
    @JsonProperty("transferID")
    private String transferId;
    private String depositDescription;
    private String smsPass;
    private String destinationAccountName;
    private Bank bank;
    @JsonProperty("paymentID")
    private String paymentId;
    @JsonProperty("transferType")
    private String type;
    @JsonProperty("documentID")
    private String documentId;
    private Long refCode;
}