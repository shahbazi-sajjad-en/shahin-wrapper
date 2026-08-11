package biz.espc.shahin.dto.iban;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static biz.espc.shahin.util.ShahinUtil.TEHRAN_ZONE_ID;

public record IbanResponseDto(
        String accountNumber,
        String ibanNumber,
        String statusCode,
        String statusMessage,
        String transactionState,
        long transactionTime, String uuid,
        IbanDetailResponseDto respObject
) {

    public record IbanDetailResponseDto(
            String accountNumber,
            String ibanNumber
    ) {}


    public record Combined(
            String accountNumber,
            String ibanNumber,
            String statusCode,
            String statusMessage,
            String transactionState,
            long transactionTime, String uuid,
            IbanDetailResponseDto respObject
    ) {
        public static Combined customizeResponse(IbanResponseDto dto) {

            return new Combined(
                    dto.accountNumber,
                    dto.ibanNumber,
                    dto.statusCode(),
                    dto.statusMessage(),
                    dto.transactionState(),
                    dto.transactionTime,
                    dto.uuid,
                    dto.respObject
            );
        }
    }

    public static Combined customizeResponse(IbanResponseDto dto) {
        return Combined.customizeResponse(dto);
    }

    @Override
    public String toString() {
        return "IbanResponseDto{" +
                "accountNumber='" + accountNumber + '\'' +
                ", ibanNumber='" + ibanNumber + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", statusMessage='" + statusMessage + '\'' +
                ", transactionState='" + transactionState + '\'' +
                ", transactionTime=" + transactionTime +
                ", uuid='" + uuid + '\'' +
                ", respObject=" + respObject +
                '}';
    }
}
