package biz.espc.shahin.dto.outbound.transaction;
import biz.espc.shahin.util.ShahinUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static biz.espc.shahin.util.ShahinUtil.TEHRAN_ZONE_ID;

/**
 * DTO representing the response from an ACH/RTG inquiry API.
 * <p>
 * This record captures the overall transaction response including transaction state,
 * time, UUID, and detailed information about the ACH transaction(s) in {@link AchRtgDetailDto}.
 * It also provides a {@link Combined} view to simplify mapping a single result for further processing.
 * </p>
 * <p>
 * Author: Ebrahim Sheyki<br>
 * Created on: 2026-02-08 11:55 AM
 * </p>
 */
public record AchInquiryResponseDto(
        String transactionState,
        long transactionTime,
        UUID uuid,
        AchRtgDetailDto respObject
) {

    /**
     * Encapsulates the detailed result object of an ACH transaction inquiry.
     * Contains an array of {@link InquiryResult} representing each transaction record.
     */
    public record AchRtgDetailDto(
            InquiryResult[] inquiryResults
    ) { }

    /**
     * Represents an individual ACH transaction record from the inquiry.
     * Each field corresponds to a property returned from the external ACH API.
     */
    public record InquiryResult(
            String amount,
            String status,
            String achCycleCompletionDate,
            String achId,
            String messageDate,
            String transactionNumber,
            String trackingCode,
            String achRejectionCode,
            String senderAccountNumber,
            String receiverAccountNumber,
            String achReturnCode,
            String bankIranOriginalKey
    ) { }

    /**
     * Simplified representation of the ACH inquiry response.
     * <p>
     * This record extracts the first {@link InquiryResult} (if available) from the array
     * and combines it with the top-level response information such as transactionState, transactionTime, and UUID.
     * </p>
     */
    public record Combined(
            String transactionState,
            LocalDateTime transactionTime,
            UUID uuid,
            String amount,
            String status,
            String achCycleCompletionDate,
            String achId,
            String trackingCode,
            String achRejectionCode,
            String senderAccountNumber,
            String receiverAccountNumber,
            String achReturnCode,
            String bankIranOriginalKey
    ) {

        /**
         * Maps an {@link AchInquiryResponseDto} into a {@link Combined} record.
         * <p>
         * Uses {@link ShahinUtil#firstOrNull(Object[])} to safely extract the first
         * {@link InquiryResult} from the array, if present.
         * Converts the transactionTime from epoch milliseconds to {@link LocalDateTime} using the Tehran timezone.
         *
         * @param dto the original ACH inquiry response DTO
         * @return a Combined record with flattened transaction details for easy consumption
         */
        public static Combined success(AchInquiryResponseDto dto) {

            InquiryResult r = ShahinUtil.firstOrNull(dto.respObject().inquiryResults());

            LocalDateTime transactionTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(dto.transactionTime()),
                    ZoneId.of(TEHRAN_ZONE_ID));

            return new Combined(
                    dto.transactionState(),
                    transactionTime,
                    dto.uuid(),
                    r != null ? r.amount() : null,
                    r != null ? r.status() : null,
                    r != null ? r.achCycleCompletionDate() : null,
                    r != null ? r.achId() : null,
                    r != null ? r.trackingCode() : null,
                    r != null ? r.achRejectionCode() : null,
                    r != null ? r.senderAccountNumber() : null,
                    r != null ? r.receiverAccountNumber() : null,
                    r != null ? r.achReturnCode() : null,
                    r != null ? r.bankIranOriginalKey() : null
            );
        }
    }

    /**
     * Convenience method to transform this DTO into a {@link Combined} record.
     * <p>
     * Automatically handles array extraction and timestamp conversion.
     *
     * @return a flattened Combined representation of this response
     */
    public Combined getResponse() {
        return Combined.success(this);
    }
}

