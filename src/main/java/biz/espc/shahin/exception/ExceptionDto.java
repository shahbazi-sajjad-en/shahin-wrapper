package biz.espc.shahin.exception;

import biz.espc.shahin.util.ShahinUtil;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

/**
 * managed by ESPC
 * created by KAINO team on  04/30/2026 at 12:28 PM
 */
public record ExceptionDto(
        String transactionState,
        long transactionTime,
        UUID uuid,
        ExceptionResponseObject respObject
) {

    private record ExceptionResponseObject(
            String message,
            String errorCode,
            List<SubError> subErrors
    ) {
    }

    private record SubError(
            String field,
            String rejectedValue
    ) {
    }

    public record Combined(
            String transactionState,
            LocalDateTime transactionTime,
            UUID uuid,
            List<String> fields,
            String message,
            List<String> rejectedValues
    ) {
    }

    public Combined getExceptionResponse() {

        ExceptionResponseObject r = respObject;

        List<String> fields = List.of();
        List<String> rejectedValues = List.of();
        String message = null;

        if (r != null) {
            message = r.message();

            if (r.subErrors() != null) {
                fields = r.subErrors()
                        .stream()
                        .map(SubError::field)
                        .toList();

                rejectedValues = r.subErrors()
                        .stream()
                        .map(SubError::rejectedValue)
                        .toList();
            }
        }

        return new Combined(
                transactionState,
                LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(transactionTime),
                        ZoneId.of(ShahinUtil.TEHRAN_ZONE_ID)
                ),
                uuid,
                fields,
                message,
                rejectedValues
        );
    }
}