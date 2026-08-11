package biz.espc.shahin.util;

import biz.espc.shahin.enumeration.bank.Bank;
import biz.espc.shahin.exception.CommonException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

/**
 * author: Ebrahim Sheyki
 * Modified on: 2/17/2026  3:26 PM
 */
public final class ShahinUtil {

    public static final String CLIENT_CREDENTIALS = "client_credentials";
    public static final String TEHRAN_ZONE_ID = "Asia/Tehran";
    public static final String INVALID_IBAN = "INVALID_IBAN";
    public static final String INITIAL_THE_FIRST_STATE = "INITIAL_THE_FIRST_STATE";
    public static final String SUCCESS_COMMENT = "SUCCESS";
    public static final String TRANSACTION_FAILED_COMMENT = "TRANSACTION_FAILED";

    private ShahinUtil() {
    }

    /**
     * Legacy helper - no OBH signature generated here.
     */
    public static void setHeader(HttpHeaders headers, String token) {
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Obh-uuid", UUID.randomUUID().toString());
        headers.set("X-Obh-timestamp", String.valueOf(Instant.now().toEpochMilli()));
    }

    /**
     * Legacy helper - no OBH signature generated here.
     */
    public static void setHeaders(HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Obh-uuid", UUID.randomUUID().toString());
        headers.set("X-Obh-timestamp", String.valueOf(Instant.now().toEpochMilli()));
    }

    /**
     * Recommended helper for OBH1 signed requests.
     */
    public static void setObh1SignedHeaders(HttpHeaders headers,
                                            String requestBody,
                                            String httpMethod,
                                            String url,
                                            String clientId,
                                            String clientSecret,
                                            DigitalSignature digitalSignature) {

        String uuid = UUID.randomUUID().toString();
        String timestamp = String.valueOf(Instant.now().toEpochMilli());

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Obh-uuid", uuid);
        headers.set("X-Obh-timestamp", timestamp);

        String signature = digitalSignature.getOBH1Sign(
                uuid,
                timestamp,
                requestBody,
                httpMethod,
                clientId,
                clientSecret,
                url
        );

        headers.set("X-Obh-signature", signature);
    }

    /**
     * Recommended helper for OBH1 signed requests with bearer token.
     */
    public static void setObh1SignedHeaders(HttpHeaders headers,
                                            String token,
                                            String requestBody,
                                            String httpMethod,
                                            String url,
                                            String clientId,
                                            String clientSecret,
                                            DigitalSignature digitalSignature) {

        headers.setBearerAuth(token);
        setObh1SignedHeaders(headers, requestBody, httpMethod, url, clientId, clientSecret, digitalSignature);
    }

    /**
     * Recommended helper for OBH2 signed requests.
     */
    public static void setObh2SignedHeaders(HttpHeaders headers,
                                            String requestBody,
                                            String httpMethod,
                                            String url,
                                            String clientId,
                                            String p12Path,
                                            String p12Password,
                                            DigitalSignature digitalSignature) {

        String uuid = UUID.randomUUID().toString();
        String timestamp = String.valueOf(Instant.now().toEpochMilli());

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Obh-uuid", uuid);
        headers.set("X-Obh-timestamp", timestamp);

        String signature = digitalSignature.getOBH2Sign(
                uuid,
                timestamp,
                requestBody,
                httpMethod,
                clientId,
                url,
                p12Path,
                p12Password
        );

        headers.set("X-Obh-signature", signature);
    }

    public static <T> T firstOrNull(T[] array) {
        return array == null || array.length == 0 ? null : array[0];
    }

    public static <T> boolean search(T[] array, String index) {
        return array != null && Arrays.stream(array).anyMatch(x -> String.valueOf(x).equals(index));
    }

    public static Bank requireBankFromIban(String iban) {
        Bank bank = Bank.fromIban(iban).orElse(null);
        if (bank == null) {
            throw new CommonException(HttpStatus.BAD_REQUEST, INVALID_IBAN, iban);
        }
        return bank;
    }
}