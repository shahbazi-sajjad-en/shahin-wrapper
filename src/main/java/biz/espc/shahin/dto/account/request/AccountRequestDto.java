package biz.espc.shahin.dto.account.request;

import biz.espc.shahin.enumeration.bank.Bank;
import lombok.Getter;
import lombok.Setter;

/**
 * provide by ESPC team
 * author: Ebrahim Sheyki
 * modify on: 1/31/2026  03:42 PM
 */
@Getter
@Setter
public class AccountRequestDto {
    private Bank bank;
    private String nationalCode;
    private String sourceAccount;
}
