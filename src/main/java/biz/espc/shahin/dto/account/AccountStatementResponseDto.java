package biz.espc.shahin.dto.account;

import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static biz.espc.shahin.util.ShahinUtil.TEHRAN_ZONE_ID;

/**
 * author: Ebrahim Sheyki
 * Created on: 1/28/2026  10:21 AM
 */
public record AccountStatementResponseDto(
        String transactionState,
        long transactionTime,
        UUID uuid,
        StatementDetailResponse respObject
) {

    public record StatementDetailResponse(
            String lastRecord,
            List<AccountStatemen> accountStatementList
    ) { }

    public record AccountStatemen(
            String transactionDate,
            String transactionTime,
            BigDecimal debit,
            BigDecimal credit,
            String description,
            BigDecimal balance,
            String transactionTrace,
            String branchCode,
            String transactionIdentity,
            String statementStatus,
            String sourceAccount,
            String destinationAccount,
            String documentId
    ) { }

    public record Combined(
            String transactionState,
            LocalDateTime transactionTime,
            UUID uuid,
            StatementDetailResponse detailResponse
    ) {

        public static Combined success(AccountStatementResponseDto dto) {

            final LocalDateTime transactionTime =
                    LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(dto.transactionTime()),
                            ZoneId.of(TEHRAN_ZONE_ID)
                    );

            return new Combined(
                    dto.transactionState(),
                    transactionTime,
                    dto.uuid(),
                    dto.respObject
            );
        }
    }

    public Combined getResponse() {
        return Combined.success(this);
    }
}
