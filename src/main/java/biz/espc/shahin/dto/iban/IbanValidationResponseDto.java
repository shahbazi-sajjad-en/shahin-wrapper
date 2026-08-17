package biz.espc.shahin.dto.iban;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static biz.espc.shahin.util.ShahinUtil.TEHRAN_ZONE_ID;

public record IbanValidationResponseDto(
        String transactionState,
        long transactionTime,
        String uuid,
        IbanDetailResponse respObject
) {

    public record IbanDetailResponse(
            String bank,
            String accountNumber,
            String ibanNumber,
            String firstName,
            String lastName,
            String accountStatus,
            String nationalCode
    ) { }

    public record Combined(
            String transactionState,
            long transactionTime,
            String uuid,
            IbanDetailResponse ibanCheckResult
    ) {
        public static Combined customizeResponse(IbanValidationResponseDto dto) {
            IbanDetailResponse r = dto.respObject();

            return new Combined(
                    dto.transactionState(),
                    dto.transactionTime(),
                    dto.uuid,
                    dto.respObject()
            );
        }
    }

    public Combined getResponse() {
        return Combined.customizeResponse(this);
    }

    @Override
    public String toString() {
        return "IbanValidationResponseDto{" +
                "transactionState='" + transactionState + '\'' +
                ", transactionTime=" + transactionTime +
                ", uuid='" + uuid + '\'' +
                ", respObject=" + respObject +
                '}';
    }
}
