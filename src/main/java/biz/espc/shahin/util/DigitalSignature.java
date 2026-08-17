package biz.espc.shahin.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DigitalSignature {

    private static final String OBH1 = "OBH1-HMAC-SHA256";
    private static final String OBH2 = "OBH2-RSA-SHA256";

    private final ObjectMapper objectMapper;
    private final ImportCA importCA;

    public DigitalSignature(ImportCA importCA) {
        this.importCA = importCA;
        this.objectMapper = new ObjectMapper();
    }

    public String getOBH1Sign(String uuid,
                              String timeStamp,
                              String jsonBody,
                              String httpRequestMethod,
                              String clientId,
                              String secret,
                              String url) {

        Map<String, String> headers = new TreeMap<>();
        headers.put("X-Obh-Uuid", safe(uuid));
        headers.put("X-Obh-Timestamp", safe(timeStamp));

        return createDigitalSignature1(
                httpRequestMethod,
                url,
                headers,
                jsonBody,
                clientId,
                secret
        );
    }

    public String getOBH2Sign(String uuid,
                              String timeStamp,
                              String jsonBody,
                              String httpRequestMethod,
                              String clientId,
                              String url,
                              String p12Path,
                              String p12Password) {

        Map<String, String> headers = new TreeMap<>();
        headers.put("X-Obh-Uuid", safe(uuid));
        headers.put("X-Obh-Timestamp", safe(timeStamp));

        String canonicalRequest = getCanonicalRequest(httpRequestMethod, url, headers, jsonBody);
        String stringToSign = getCanonicalHash(canonicalRequest);
        String signedHeaders = String.join(";", createSignHeader(headers));

        try {
            PrivateKey privateKey = importCA.loadPrivateKey(p12Path, p12Password);
            byte[] signatureBytes = signWithRsa(privateKey, stringToSign);
            String signatureHex = EncryptionUtil.toHex(signatureBytes);

            return OBH2 + ";" + clientId + ";" + signedHeaders + ";" + signatureHex;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create OBH2 RSA signature", e);
        }
    }

    public String createDigitalSignature1(String httpRequestMethod,
                                          String requestURI,
                                          Map<String, String> headerNameAndValueMap,
                                          String jsonBody,
                                          String clientID,
                                          String clientSecret) {

        try {
            String canonicalRequest = getCanonicalRequest(
                    httpRequestMethod,
                    requestURI,
                    headerNameAndValueMap,
                    jsonBody
            );

            String stringToSign = getCanonicalHash(canonicalRequest);
            String signedHeaders = String.join(";", createSignHeader(headerNameAndValueMap));
            String signature = getSignature(clientID, clientSecret, stringToSign);

            return OBH1 + ";" + clientID + ";" + signedHeaders + ";" + signature;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create OBH1 HMAC signature", e);
        }
    }

    private String getSignature(String clientID, String clientSecret, String stringToSign) {
        byte[] signKey = getSignKey(clientID, clientSecret);
        return EncryptionUtil.toHex(EncryptionUtil.getKeyedHash(signKey, stringToSign));
    }

    public String getCanonicalRequest(String httpRequestMethod,
                                      String requestURI,
                                      Map<String, String> headerNameAndValueMap,
                                      String jsonBody) {

        String method = safe(httpRequestMethod).trim().toUpperCase(Locale.ROOT);
        String canonicalUri = normalizeUri(requestURI);
        String canonicalHeaders = getCanonicalHeaders(headerNameAndValueMap);
        String signedHeaders = String.join(";", createSignHeader(headerNameAndValueMap));
        String payloadHash = getPayloadHash(jsonBody);

        return method + "\n"
                + canonicalUri + "\n"
                + canonicalHeaders + "\n"
                + signedHeaders + "\n"
                + payloadHash;
    }

    private String getCanonicalHeaders(Map<String, String> headerNameAndValueMap) {
        if (headerNameAndValueMap == null || headerNameAndValueMap.isEmpty()) {
            return "";
        }

        Map<String, String> normalized = new TreeMap<>();

        for (Map.Entry<String, String> entry : headerNameAndValueMap.entrySet()) {
            String key = normalizeHeaderName(entry.getKey());
            String value = normalizeHeaderValue(entry.getValue());
            normalized.put(key, value);
        }

        return normalized.entrySet()
                .stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining("\n"));
    }

    public String getCanonicalHash(String canonicalRequest) {
        byte[] hash = EncryptionUtil.encryptSHA256(safe(canonicalRequest));
        return EncryptionUtil.toHex(hash);
    }

    private List<String> createSignHeader(Map<String, String> headerNameAndValueMap) {
        if (headerNameAndValueMap == null || headerNameAndValueMap.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> headers = new ArrayList<>();
        for (String key : headerNameAndValueMap.keySet()) {
            headers.add(normalizeHeaderName(key));
        }

        headers.sort(Comparator.naturalOrder());
        return headers;
    }

    private byte[] getSignKey(String clientID, String clientSecret) {
        String year = String.valueOf(java.time.Year.now().getValue());
        String baseKey = year + safe(clientID) + safe(clientSecret);
        return EncryptionUtil.encryptSHA256(baseKey);
    }

    private String getPayloadHash(String jsonBody) {
        String normalizedPayload = normalizeJsonPayload(jsonBody);
        return EncryptionUtil.toHex(EncryptionUtil.encryptSHA256(normalizedPayload));
    }

    private byte[] signWithRsa(PrivateKey privateKey, String stringToSign) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(safe(stringToSign).getBytes(StandardCharsets.UTF_8));
            return signature.sign();
        } catch (Exception e) {
            throw new IllegalStateException("RSA signing failed", e);
        }
    }

    private String normalizeUri(String requestURI) {
        if (StringUtils.isBlank(requestURI)) {
            return "/";
        }

        try {
            URI uri = new URI(requestURI);

            String path = uri.getPath();
            if (StringUtils.isBlank(path)) {
                return "/";
            }
            return path;
        } catch (URISyntaxException e) {
            String raw = requestURI.trim();

            int queryIndex = raw.indexOf('?');
            if (queryIndex >= 0) {
                raw = raw.substring(0, queryIndex);
            }

            if (!raw.startsWith("/")) {
                raw = "/" + raw;
            }

            return raw;
        }
    }

    private String normalizeHeaderName(String name) {
        return safe(name).trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeHeaderValue(String value) {
        return safe(value).trim().replaceAll("\\s+", " ");
    }

    private String normalizeJsonPayload(String jsonBody) {
        if (StringUtils.isBlank(jsonBody)) {
            return "";
        }

        String trimmed = jsonBody.trim();

        try {
            Object json = objectMapper.readValue(trimmed, Object.class);
            return objectMapper.writeValueAsString(json);
        } catch (JsonProcessingException e) {
            return trimmed;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}