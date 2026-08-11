package biz.espc.shahin.twoParty.service.transaction;

import biz.espc.shahin.dto.CurlLogConstants;
import biz.espc.shahin.dto.inquiry.TransactionStatementRequestDto;
import biz.espc.shahin.dto.token.AuthResponseDto;
import biz.espc.shahin.dto.transaction.AchInquiryResponseDto;
import biz.espc.shahin.dto.transaction.RtgsTransactionInquiryDto;
import biz.espc.shahin.enumeration.bank.Bank;
import biz.espc.shahin.exception.CommonException;
import biz.espc.shahin.exception.ExceptionDto;
import biz.espc.shahin.security.service.TokenProvider;
import biz.espc.shahin.util.DigitalSignature;
import biz.espc.shahin.util.ShahinUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


/**
 * author: Ebrahim Sheyki
 * Created on: 1/28/2026  10:25 AM
 */
@Service
public class TransactionInquiryService {
    
    private final WebClient shahinWebClient;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final DigitalSignature digitalSignature;
    
    @Value("${shahin.ach.transactions.inquiry.path}")
    private String getAchTransactionInquiryPath;
    
    @Value("${shahin.rtgs.transactions.inquiry.path}")
    private String getRtgsTransactionInquiryPath;
    
    @Value("${shahin.transactions.inquiry.path}")
    private String getTransactionPath;
    
    @Value("${shahin.get.national.identity.path}")
    private String getNationalCodeIdentityPath;
    
    private final String obhClientId = "your-client-id";
    private final String obhClientSecret = "your-client-secret";
    
    public TransactionInquiryService(WebClient shahinWebClient, TokenProvider tokenProvider, ObjectMapper objectMapper, DigitalSignature digitalSignature) {
        this.shahinWebClient = shahinWebClient;
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
        this.digitalSignature = digitalSignature;
    }
    
    public Mono<AchInquiryResponseDto.Combined> getAchTransactionInquiry(TransactionStatementRequestDto requestDto) {
        
        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {
                    requestDto.setBank(token.bank());
                    
                    String jsonBody;
                    try {
                        jsonBody = objectMapper.writeValueAsString(requestDto);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new CommonException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "REQUEST_SERIALIZATION_FAILED",
                                e.getMessage()
                        ));
                    }
                    
                    return shahinWebClient.post()
                            .uri(getAchTransactionInquiryPath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers ->
                                    ShahinUtil.setObh1SignedHeaders(
                                            headers,
                                            token.access_token(),
                                            jsonBody,
                                            HttpMethod.POST.name(),
                                            getAchTransactionInquiryPath,
                                            obhClientId,
                                            obhClientSecret,
                                            digitalSignature
                                    ))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e -> Mono.error(
                                                    new CommonException(
                                                            response.statusCode(),
                                                            e.message(),
                                                            CollectionUtils.firstElement(e.fields())
                                                    ))))
                            .bodyToMono(AchInquiryResponseDto.class)
                            .map(AchInquiryResponseDto::getResponse)
                            .onErrorMap(ex -> {
                                if (ex instanceof CommonException) return ex;
                                return new CommonException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "GET_ACH_TRANSACTION_INQUIRY_CALL_FAILED",
                                        ex.getMessage()
                                );
                            });
                });
    }
    
    public Mono<RtgsTransactionInquiryDto.Combined> getRtgsTransactionInquiry(TransactionStatementRequestDto requestDto) {
        
        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {
                    requestDto.setBank(token.bank());
                    
                    String jsonBody;
                    try {
                        jsonBody = objectMapper.writeValueAsString(requestDto);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new CommonException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "REQUEST_SERIALIZATION_FAILED",
                                e.getMessage()
                        ));
                    }
                    
                    return shahinWebClient.post()
                            .uri(getRtgsTransactionInquiryPath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers ->
                                    ShahinUtil.setObh1SignedHeaders(
                                            headers,
                                            token.access_token(),
                                            jsonBody,
                                            HttpMethod.POST.name(),
                                            getRtgsTransactionInquiryPath,
                                            obhClientId,
                                            obhClientSecret,
                                            digitalSignature
                                    ))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e -> Mono.error(
                                                    new CommonException(
                                                            response.statusCode(),
                                                            e.message(),
                                                            CollectionUtils.firstElement(e.fields())
                                                    ))))
                            .bodyToMono(RtgsTransactionInquiryDto.class)
                            .map(RtgsTransactionInquiryDto::getResponse)
                            .onErrorMap(ex -> {
                                if (ex instanceof CommonException) return ex;
                                return new CommonException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "GET_RTGS_TRANSACTION_INQUIRY_CALL_FAILED",
                                        ex.getMessage()
                                );
                            });
                });
    }
    
    
//    public Mono<TransactionInquiryResponseDto.Combined> getTransactionInquiry(TransactionInquiryRequestDto requestDto) {
//
//        return tokenProvider.getValidToken(requestDto.getBank())
//                .flatMap(token -> {
//                    final String fromAccount = firstOrNull(token.accounts());
//                    requestDto.setBank(token.bank());
//                    requestDto.setNationalCode(token.user_name());
//                    requestDto.setSourceAccount(fromAccount);
//
//                    String jsonBody;
//                    try {
//                        jsonBody = objectMapper.writeValueAsString(requestDto);
//                    } catch (JsonProcessingException e) {
//                        return Mono.error(new CommonException(
//                                HttpStatus.INTERNAL_SERVER_ERROR,
//                                "REQUEST_SERIALIZATION_FAILED",
//                                e.getMessage()
//                        ));
//                    }
//
//                    return shahinWebClient.post()
//                            .uri(getTransactionPath)
//                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
//                            .headers(headers ->
//                                    ShahinUtil.setObh1SignedHeaders(
//                                            headers,
//                                            token.access_token(),
//                                            jsonBody,
//                                            HttpMethod.POST.name(),
//                                            getTransactionPath,
//                                            obhClientId,
//                                            obhClientSecret,
//                                            digitalSignature
//                                    ))
//                            .bodyValue(requestDto)
//                            .retrieve()
//                            .onStatus(HttpStatusCode::isError, response ->
//                                    response.bodyToMono(ExceptionDto.class)
//                                            .map(ExceptionDto::getExceptionResponse)
//                                            .flatMap(e -> Mono.error(
//                                                    new CommonException(
//                                                            response.statusCode(),
//                                                            e.message(),
//                                                            CollectionUtils.firstElement(e.fields())
//                                                    ))))
//                            .bodyToMono(TransactionInquiryResponseDto.class)
//                            .map(TransactionInquiryResponseDto::getResponse)
//                            .onErrorMap(ex -> {
//                                if (ex instanceof CommonException) return ex;
//                                return new CommonException(
//                                        HttpStatus.INTERNAL_SERVER_ERROR,
//                                        "GET_TRANSACTION_INQUIRY_CALL_FAILED",
//                                        ex.getMessage()
//                                );
//                            });
//                });
//    }
//
//    public Mono<PersonalInformationDetailDto.Combined> getNationalIdentity(PersonalInformationDto requestDto) {
//
//        Bank bank = Bank.BSI;
//        return tokenProvider.getValidToken(bank)
//                .flatMap(token -> {
//                    requestDto.setNationalCode(token.user_name());
//
//                    String jsonBody;
//                    try {
//                        jsonBody = objectMapper.writeValueAsString(requestDto);
//                    } catch (JsonProcessingException e) {
//                        return Mono.error(new CommonException(
//                                HttpStatus.INTERNAL_SERVER_ERROR,
//                                "REQUEST_SERIALIZATION_FAILED",
//                                e.getMessage()
//                        ));
//                    }
//
//                    return shahinWebClient.post()
//                            .uri(getNationalCodeIdentityPath)
//                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
//                            .headers(headers ->
//                                    ShahinUtil.setObh1SignedHeaders(
//                                            headers,
//                                            token.access_token(),
//                                            jsonBody,
//                                            HttpMethod.POST.name(),
//                                            getNationalCodeIdentityPath,
//                                            obhClientId,
//                                            obhClientSecret,
//                                            digitalSignature
//                                    ))
//                            .bodyValue(requestDto)
//                            .retrieve()
//                            .onStatus(HttpStatusCode::isError, response ->
//                                    response.bodyToMono(ExceptionDto.class)
//                                            .map(ExceptionDto::getExceptionResponse)
//                                            .flatMap(e -> Mono.error(
//                                                    new CommonException(
//                                                            response.statusCode(),
//                                                            e.message(),
//                                                            CollectionUtils.firstElement(e.fields())
//                                                    ))))
//                            .bodyToMono(PersonalInformationDetailDto.class)
//                            .map(PersonalInformationDetailDto::getResponse)
//                            .onErrorMap(ex -> {
//                                if (ex instanceof CommonException) return ex;
//                                return new CommonException(
//                                        HttpStatus.INTERNAL_SERVER_ERROR,
//                                        "GET_NATIONAL_IDENTITY_CALL_FAILED",
//                                        ex.getMessage()
//                                );
//                            });
//                });
//    }

}