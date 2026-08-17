package biz.espc.shahin.dto.outbound.transaction;

import biz.espc.shahin.util.ShahinUtil;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static biz.espc.shahin.util.ShahinUtil.TEHRAN_ZONE_ID;

/**
 * DTO representing the response of an RTGS transaction inquiry API.
 * <p>
 * This record captures the overall transaction response including:
 * <ul>
 *     <li>transactionState: status of the transaction (e.g., SUCCESS, FAILED)</li>
 *     <li>transactionTime: timestamp in epoch milliseconds</li>
 *     <li>uuid: unique transaction identifier</li>
 *     <li>respObject: detailed information about the RTGS transaction(s)</li>
 * </ul>
 * <p>
 * It provides a {@link Combined} record to simplify extraction of the first transaction
 * from the inquiryResults array for easier consumption.
 * </p>
 * <p>
 * Author: Ebrahim Sheyki<br>
 * Created on: 2026-02-07 6:40 PM
 * </p>
 */
public record RtgsTransactionInquiryDto(
        String transactionState,
        long transactionTime,
        UUID uuid,
        RtgsTransactionInquiry respObject
) {

    /**
     * Encapsulates detailed results of the RTGS inquiry.
     * <p>
     * The inquiryResults array contains all individual transaction records returned by the bank.
     * </p>
     */
    public record RtgsTransactionInquiry(
            InquiryResults[] inquiryResults
    ) { }

    /**
     * Represents an individual RTGS transaction result.
     * <p>
     * Each field corresponds to a property returned from the external RTGS API.
     * </p>
     */
    public record InquiryResults(
            String status,
            String messageDate,
            String transactionNumber,
            String trackingCode,
            BigDecimal amount,
            String senderAccountNumber,
            String receiverAccountNumber,
            String messageDetails,
            String bankIranOriginalKey
    ) { }

    /**
     * Simplified representation of an RTGS inquiry response.
     * <p>
     * This record extracts the first {@link InquiryResults} (if available) and combines it with top-level
     * response information such as transactionState, transactionTime, and UUID.
     * </p>
     */
    public record Combined(
            String transactionState,
            LocalDateTime transactionTime,
            UUID uuid,
            BigDecimal amount,
            String status,
            String messageDate,
            String transactionNumber,
            String trackingCode,
            String senderAccountNumber,
            String receiverAccountNumber,
            String messageDetails,
            String bankIranOriginalKey
    ) {

        /**
         * Maps an {@link RtgsTransactionInquiryDto} into a {@link Combined} record.
         * <p>
         * Converts the transactionTime from epoch milliseconds to {@link LocalDateTime} using the Tehran timezone.
         * Uses {@link ShahinUtil#firstOrNull(Object[])} to safely extract the first element from the inquiryResults array.
         *
         * @param dto the original RTGS inquiry response DTO
         * @return a Combined record with flattened transaction details
         */
        public static Combined success(RtgsTransactionInquiryDto dto) {

            InquiryResults r = ShahinUtil.firstOrNull(dto.respObject().inquiryResults());

            return new Combined(
                    dto.transactionState(),
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.transactionTime()), ZoneId.of(TEHRAN_ZONE_ID)),
                    dto.uuid(),
                    r != null ? r.amount : null,
                    r != null ? r.status : null,
                    r != null ? r.messageDate : null,
                    r != null ? r.transactionNumber : null,
                    r != null ? r.trackingCode : null,
                    r != null ? r.senderAccountNumber : null,
                    r != null ? r.receiverAccountNumber : null,
                    r != null ? r.messageDetails : null,
                    r != null ? r.bankIranOriginalKey : null
            );
        }
    }

    /**
     * Convenience method to transform this DTO into a {@link Combined} record.
     * <p>
     * Handles null-safety and timestamp conversion automatically.
     *
     * @return a flattened Combined representation of the first transaction result
     */
    public Combined getResponse() {
        return Combined.success(this);
    }
}