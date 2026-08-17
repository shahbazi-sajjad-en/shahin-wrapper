package biz.espc.shahin.dto.iban;

import biz.espc.shahin.enumeration.bank.Bank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IbanValidationRequestDto {
    private Bank bank;
    private String nationalCode;
    private String sourceAccount;
}
