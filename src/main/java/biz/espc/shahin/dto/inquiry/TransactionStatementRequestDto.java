package biz.espc.shahin.dto.inquiry;


import biz.espc.shahin.enumeration.bank.Bank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionStatementRequestDto {

    private Bank bank;
    private String fromDate;
    private String toDate;
    private String toTime;
    private String searchArgument;
    private String searchValues;

}