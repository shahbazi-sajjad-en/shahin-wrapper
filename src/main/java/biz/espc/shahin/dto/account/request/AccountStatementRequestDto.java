package biz.espc.shahin.dto.account.request;

import biz.espc.shahin.enumeration.bank.Bank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountStatementRequestDto {
    private String fromDate;
    private Bank bank;
    private String nationalCode;
    private String sourceAccount;
    private String toDate;
    private String fromTime;
    private String toTime;

    private String pageSize;
    private String lastRecord;
}
