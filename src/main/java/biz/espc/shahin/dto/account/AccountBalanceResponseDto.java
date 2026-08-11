package biz.espc.shahin.dto.account;

import biz.espc.shahin.enumeration.account.AccountType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static biz.espc.shahin.util.ShahinUtil.TEHRAN_ZONE_ID;

/**
 * managed by cbaas.service.bank
 * created by KAINO team on 04/30/2026 at 12:28 PM
 */
public record AccountBalanceResponseDto(
        AccountBalanceDetail respObject,
        String transactionState,
        long transactionTime,
        UUID uuid
) {
    public record AccountBalanceDetail(
            BigDecimal availableBalance,
            BigDecimal effectiveBalance,
            AccountType accountType
    ) {}

    public record Combined(
            String transactionState,
            LocalDateTime transactionTime,
            UUID uuid,
            BigDecimal availableBalance,
            BigDecimal effectiveBalance,
            AccountType accountType) {
        public static Combined combineCustomizeResponse(AccountBalanceResponseDto dto) {
            AccountBalanceDetail r = dto.respObject();

            return new Combined(
                    dto.transactionState(),
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.transactionTime()), ZoneId.of(TEHRAN_ZONE_ID)),
                    dto.uuid(),
                    r != null ? r.availableBalance() : null,
                    r != null ? r.effectiveBalance() : null,
                    r != null ? r.accountType() : null
            );
        }
    }

    public Combined getResponse() {
        return Combined.combineCustomizeResponse(this);
    }

}
