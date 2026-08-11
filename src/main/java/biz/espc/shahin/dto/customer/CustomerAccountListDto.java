package biz.espc.shahin.dto.customer;

import biz.espc.shahin.enumeration.bank.Bank;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static biz.espc.shahin.util.ShahinUtil.TEHRAN_ZONE_ID;

/**
 * author: Ebrahim Sheyki
 * Created on: 2/02/2026  10:24 AM
 */
public record CustomerAccountListDto(
        String transactionState,
        long transactionTime,
        UUID uuid,
        CustomerAccountResponseObject respObject
) {

    public record CustomerAccountResponseObject(List<AccountDto> accounts) {
    }

    public record AccountDto(
            String accountType,
            String accountTypeName,
            String accountNumber,
            Bank bank
    ) {
    }

    public record Combined(
            String transactionState,
            LocalDateTime transactionTime,
            UUID uuid,
            List<AccountDto> accountList
    ) {
        public static Combined combineCustomizeResponse(CustomerAccountListDto dto) {
            CustomerAccountResponseObject r = dto.respObject();

            return new Combined(
                    dto.transactionState(),
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.transactionTime()), ZoneId.of(TEHRAN_ZONE_ID)),
                    dto.uuid(),
                    r.accounts()
            );
        }
    }

    public Combined getResponse() {
        return Combined.combineCustomizeResponse(this);
    }
}
