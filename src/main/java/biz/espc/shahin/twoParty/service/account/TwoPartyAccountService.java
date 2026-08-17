package biz.espc.shahin.twoParty.service.account;

import biz.espc.shahin.dto.CurlLogConstants;
import biz.espc.shahin.dto.account.AccountBalanceResponseDto;
import biz.espc.shahin.dto.account.AccountInfoResponseDto;
import biz.espc.shahin.dto.account.AccountStatementResponseDto;
import biz.espc.shahin.dto.account.request.AccountRequestDto;
import biz.espc.shahin.dto.account.request.AccountStatementRequestDto;
import biz.espc.shahin.dto.customer.CustomerAccountListDto;
import biz.espc.shahin.dto.iban.IbanRequestDto;
import biz.espc.shahin.dto.iban.IbanResponseDto;
import biz.espc.shahin.dto.iban.IbanValidationRequestDto;
import biz.espc.shahin.dto.iban.IbanValidationResponseDto;
import biz.espc.shahin.enumeration.account.AccountType;
import biz.espc.shahin.enumeration.bank.Bank;
import biz.espc.shahin.exception.CommonException;
import biz.espc.shahin.exception.ExceptionDto;
import biz.espc.shahin.security.service.AuthClientService;
import biz.espc.shahin.security.service.TokenProvider;
import biz.espc.shahin.util.DigitalSignature;
import biz.espc.shahin.util.ShahinUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static biz.espc.shahin.util.ShahinUtil.firstOrNull;

/**
 * managed by cbaas.service.bank
 * created by KAINO team on  04/30/2026 at 12:28 PM
 */
@Service
public class TwoPartyAccountService {

    private final WebClient shahinWebClient;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final DigitalSignature digitalSignature;


    @Value("${shahin.get.account.info.path}")
    private String getAccountInformationPath;
    @Value("${shahin.get.account.balance.path}")
    private String getAccountBalancePath;
    @Value("${shahin.get.account.statement.path}")
    private String getAccountStatementListPath;
    @Value("${shahin.get.account.statement.page.path}")
    private String getAccountStatementPagePath;
    @Value("${shahin.get.account.list.path}")
    private String getAccountListPath;
    @Value("${shahin.service.path.two-party.get-iban}")
    private String getIbanPath;
    @Value("${shahin.service.path.two-party.get-iban-info}")
    private String getIbanInfoPath;


    @Value("shahin.client.id")
    private String obhClientId;
    @Value("shahin.client.secret")
    private String obhClientSecret;


    public TwoPartyAccountService(WebClient shahinWebClient, TokenProvider tokenProvider, ObjectMapper objectMapper, DigitalSignature digitalSignature, AuthClientService authClientService) {
        this.shahinWebClient = shahinWebClient;
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
        this.digitalSignature = digitalSignature;
    }

    public Mono<AccountInfoResponseDto.Combined> getAccountInfo(AccountRequestDto requestDto) {

        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {

                    String fromAccount = firstOrNull(token.accounts());
                    requestDto.setNationalCode(token.user_name());
                    requestDto.setSourceAccount(fromAccount);
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
                            .uri(getAccountInformationPath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers ->
                                    ShahinUtil.setObh1SignedHeaders(
                                            headers,
                                            token.access_token(),
                                            jsonBody,
                                            HttpMethod.POST.name(),
                                            getAccountInformationPath,
                                            obhClientId,
                                            obhClientSecret,
                                            digitalSignature
                                    )
                            )
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
                            .bodyToMono(AccountInfoResponseDto.class)
                            .map(AccountInfoResponseDto::getResponse)
                            .onErrorMap(ex -> {
                                if (ex instanceof CommonException) return ex;
                                return new CommonException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "GET_ACCOUNT_INFO_CALL_FAILED",
                                        ex.getMessage()
                                );
                            });
                });
    }

    public Mono<AccountBalanceResponseDto.Combined> getAccountRemainBalance(AccountRequestDto requestDto) {

        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {
                    String fromAccount = firstOrNull(token.accounts());
                    requestDto.setNationalCode(token.user_name());
                    requestDto.setSourceAccount(fromAccount);
                    requestDto.setBank(token.bank());

                    String jsonBody;
                    try {
                        jsonBody = objectMapper.writeValueAsString(requestDto);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new CommonException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                e.getMessage(),
                                null
                        ));
                    }

                    return shahinWebClient.post()
                            .uri(getAccountBalancePath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers ->
                                    ShahinUtil.setObh1SignedHeaders(
                                            headers,
                                            token.access_token(),
                                            jsonBody,
                                            HttpMethod.POST.name(),
                                            getAccountBalancePath,
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
                                                    )
                                            ))
                            )
                            .bodyToMono(AccountBalanceResponseDto.class)
                            .map(AccountBalanceResponseDto::getResponse)
                            .onErrorMap(ex -> {
                                if (ex instanceof CommonException) return ex;
                                return new CommonException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "GET_ACCOUNT_BALANCE_CALL_FAILED",
                                        ex.getMessage()
                                );
                            });
                });
    }

    public Mono<AccountStatementResponseDto.Combined> getAccountStatementList(AccountStatementRequestDto requestDto) {

        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {
                    String fromAccount = firstOrNull(token.accounts());
                    requestDto.setNationalCode(token.user_name());
                    requestDto.setSourceAccount(fromAccount);
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
                            .uri(getAccountStatementListPath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers ->
                                    ShahinUtil.setObh1SignedHeaders(
                                            headers,
                                            token.access_token(),
                                            jsonBody,
                                            HttpMethod.POST.name(),
                                            getAccountStatementListPath,
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
                            .bodyToMono(AccountStatementResponseDto.class)
                            .map(AccountStatementResponseDto::getResponse)
                            .onErrorMap(ex -> {
                                if (ex instanceof CommonException) return ex;
                                return new CommonException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "GET_ACCOUNT_STATEMENT_LIST_CALL_FAILED",
                                        ex.getMessage()
                                );
                            });
                });
    }

    public Mono<AccountStatementResponseDto.Combined> getAccountStatementPage(AccountStatementRequestDto requestDto) {

        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {
                    String fromAccount = firstOrNull(token.accounts());
                    requestDto.setNationalCode(token.user_name());
                    requestDto.setSourceAccount(fromAccount);
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
                            .uri(getAccountStatementPagePath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers ->
                                    ShahinUtil.setObh1SignedHeaders(
                                            headers,
                                            token.access_token(),
                                            jsonBody,
                                            HttpMethod.POST.name(),
                                            getAccountStatementPagePath,
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
                            .bodyToMono(AccountStatementResponseDto.class)
                            .map(AccountStatementResponseDto::getResponse)
                            .onErrorMap(ex -> {
                                if (ex instanceof CommonException) return ex;
                                return new CommonException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "GET_ACCOUNT_STATEMENT_PAGE_CALL_FAILED",
                                        ex.getMessage()
                                );
                            });
                });
    }

    public Mono<CustomerAccountListDto.Combined> getCustomerAccountList(AccountRequestDto requestDto) {

        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {

                    String fromAccount = firstOrNull(token.accounts());
                    requestDto.setNationalCode(token.user_name());
                    requestDto.setSourceAccount(fromAccount);
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
                            .uri(getAccountListPath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers ->
                                    ShahinUtil.setObh1SignedHeaders(
                                            headers,
                                            token.access_token(),
                                            jsonBody,
                                            HttpMethod.POST.name(),
                                            getAccountListPath,
                                            obhClientId,
                                            obhClientSecret,
                                            digitalSignature
                                    ))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e -> Mono.error(new CommonException(
                                                    response.statusCode(),
                                                    e.message(),
                                                    CollectionUtils.firstElement(e.fields())))))
                            .bodyToMono(CustomerAccountListDto.class)
                            .map(CustomerAccountListDto::getResponse)
                            .onErrorMap(ex -> {
                                if (ex instanceof CommonException) return ex;
                                return new CommonException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "GET_CUSTOMER_ACCOUNT_LIST_CALL_FAILED",
                                        ex.getMessage()
                                );
                            });
                });
    }


    public Mono<IbanResponseDto.Combined> getIban(IbanRequestDto requestDto) {

        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {

                    String fromAccount = firstOrNull(token.accounts());
                    requestDto.setNationalCode(token.user_name());
                    requestDto.setSourceAccount(fromAccount);
                    requestDto.setBank(token.bank());

                    String jsonBody;
                    try {
                        if (requestDto.getBank().equals(Bank.MEL) || requestDto.getBank().equals(Bank.RES))
                            requestDto.setAccountType(AccountType.fromInput(requestDto.getAccountType()).toProviderValue());
                        jsonBody = objectMapper.writeValueAsString(requestDto);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new CommonException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "REQUEST_SERIALIZATION_FAILED",
                                e.getMessage()
                        ));
                    }

                    return shahinWebClient.post()
                            .uri(getIbanPath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers -> {
                                ShahinUtil.setObh1SignedHeaders(
                                        headers,
                                        token.access_token(),
                                        jsonBody,
                                        HttpMethod.POST.name(),
                                        getIbanPath,
                                        obhClientId,
                                        obhClientSecret,
                                        digitalSignature
                                );
                            })
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e -> Mono.error(new CommonException(
                                                    response.statusCode(),
                                                    e.message(),
                                                    CollectionUtils.firstElement(e.fields())
                                            )))
                            )
                            .bodyToMono(IbanResponseDto.class)
                            .map(IbanResponseDto::customizeResponse)
                            .onErrorMap(ex -> ex instanceof CommonException ? ex :
                                    new CommonException(
                                            HttpStatus.INTERNAL_SERVER_ERROR,
                                            "GET_IBAN_CALL_FAILED",
                                            ex.getMessage()
                                    ));
                });
    }

    public Mono<IbanValidationResponseDto.Combined> getIbanInfo(IbanValidationRequestDto requestDto) {
        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {
                    final String jsonBody;
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
                            .uri(getIbanInfoPath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers -> {
                                ShahinUtil.setObh1SignedHeaders(
                                        headers,
                                        token.access_token(),
                                        jsonBody,
                                        HttpMethod.POST.name(),
                                        getIbanInfoPath,
                                        obhClientId,
                                        obhClientSecret,
                                        digitalSignature
                                );
                            })
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e -> Mono.error(new CommonException(
                                                    response.statusCode(),
                                                    e.message(),
                                                    CollectionUtils.firstElement(e.fields())
                                            )))
                            )
                            .bodyToMono(IbanValidationResponseDto.class)
                            .map(IbanValidationResponseDto::getResponse)
                            .onErrorMap(ex -> ex instanceof CommonException ? ex :
                                    new CommonException(
                                            HttpStatus.INTERNAL_SERVER_ERROR,
                                            "GET_IBAN_INFO_CALL_FAILED",
                                            ex.getMessage()
                                    ));
                });
    }


}


