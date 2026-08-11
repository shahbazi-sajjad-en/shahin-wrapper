package biz.espc.shahin.dto.transaction;

import biz.espc.shahin.enumeration.bank.Bank;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static biz.espc.shahin.util.ShahinUtil.TEHRAN_ZONE_ID;

/**
 * author: Ebrahim Sheyki
 * Created on: 2/8/2026  11:55 AM
 */

public record TransactionResponseDto(
        String transactionState,
        long transactionTime,
        UUID uuid,
        TransactionDetailDto respObject
) {

    public record TransactionDetailDto(
            String sourceAccountNumber,
            String destinationAccountNumber,
            BigDecimal amount,
            Bank sourceBank,
            Bank destinationBank,
            String transferType,
            String traceNumber
    ) {
    }

    public record Combined(
            String transactionState,
            LocalDateTime transactionTime,
            UUID uuid,
            BigDecimal amount,
            String sourceAccountNumber,
            String destinationAccountNumber,
            Bank sourceBank,
            Bank destinationBank,
            String transferType,
            String traceNumber

    ) {
        public static Combined success(TransactionResponseDto dto) {
            TransactionDetailDto r = dto.respObject();

            return new Combined(
                    dto.transactionState(),
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.transactionTime()), ZoneId.of(TEHRAN_ZONE_ID)),
                    dto.uuid(),
                    r != null ? r.amount : null,
                    r != null ? r.sourceAccountNumber : null,
                    r != null ? r.destinationAccountNumber : null,
                    r != null ? r.sourceBank : null,
                    r != null ? r.destinationBank : null,
                    r != null ? r.transferType : null,
                    r != null ? r.traceNumber : null
            );
        }
    }

    public Combined getResponse() {
        return Combined.success(this);
    }

    @Override
    public String toString() {
        return "TransactionResponseDto{" +
                "transactionState='" + transactionState + '\'' +
                ", transactionTime=" + transactionTime +
                ", uuid=" + uuid +
                ", respObject=" + respObject +
                '}';
    }
}
