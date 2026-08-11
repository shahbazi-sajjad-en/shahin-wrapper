package biz.espc.shahin.dto.account;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static biz.espc.shahin.util.ShahinUtil.TEHRAN_ZONE_ID;

/**
 * author: Ebrahim Sheyki
 * modify on: 1/31/2026  03:54 PM
 */
public record AccountInfoResponseDto(
        String transactionState,
        long transactionTime,
        UUID uuid,
        AccountInfoDetailDto respObject
) {

    public record AccountInfoDetailDto(
            String bank,
            String nationalCode,
            String accountNumber,
            String branch,
            String accountCreationTime,
            String customerNumber,
            String accountOwnerName,
            String accountType,
            String accountStatus
    ) {
    }

    public record Combined(
            String transactionState,
            LocalDateTime transactionTime,
            UUID uuid,
            String bank,
            String nationalCode,
            String accountNumber,
            String branch,
            String accountCreationTime,
            String customerNumber,
            String accountOwnerName,
            String accountType,
            String accountStatus
    ) {
        public static Combined success(AccountInfoResponseDto dto) {
            AccountInfoDetailDto r = dto.respObject();

            LocalDateTime transactionTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(dto.transactionTime()),
                    ZoneId.of(TEHRAN_ZONE_ID));

            return new Combined(
                    dto.transactionState(),
                    transactionTime,
                    dto.uuid(),
                    r != null ? r.bank() : null,
                    r != null ? r.nationalCode() : null,
                    r != null ? r.accountNumber() : null,
                    r != null ? r.branch() : null,
                    r != null ? r.accountCreationTime() : null,
                    r != null ? r.customerNumber() : null,
                    r != null ? r.accountOwnerName() : null,
                    r != null ? r.accountType() : null,
                    r != null ? r.accountStatus() : null
            );
        }
    }

    public Combined getResponse() {
        return Combined.success(this);
    }

}
