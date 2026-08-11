package biz.espc.shahin.threeParty.service;

import biz.espc.shahin.dto.account.AccountBalanceResponseDto;
import biz.espc.shahin.dto.account.AccountInfoResponseDto;
import biz.espc.shahin.dto.account.AccountStatementResponseDto;
import biz.espc.shahin.dto.account.request.AccountRequestDto;
import biz.espc.shahin.dto.account.request.AccountStatementRequestDto;
import biz.espc.shahin.dto.customer.CustomerAccountListDto;
import biz.espc.shahin.exception.CommonException;
import biz.espc.shahin.exception.ExceptionDto;
import biz.espc.shahin.security.service.AuthClientService;
import biz.espc.shahin.security.service.TokenProvider;
import biz.espc.shahin.util.ShahinUtil;
import org.springframework.beans.factory.annotation.Value;
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
public class ThreePartyAccountService {

    private final WebClient shahinWebClient;
    private final TokenProvider tokenProvider;

    @Value("${shahin.get.account.info.path}")
    private String getAccountInformationPath;
    @Value("${shahin.get.account.balance.path}")
    private String getAccountBalancePath;
    @Value("${shahin.get.account.statement.path}")
    private String getAccountStatementListPath;
    @Value("${shahin.get.account.list.path}")
    private String getAccountListPath;

    public ThreePartyAccountService(WebClient shahinWebClient, TokenProvider tokenProvider) {
        this.shahinWebClient = shahinWebClient;
        this.tokenProvider = tokenProvider;
    }

    public Mono<AccountInfoResponseDto.Combined> getThreePartyAccountInfo(AccountRequestDto requestDto) {

        return tokenProvider.getThreePartyValidToken(requestDto)
                .flatMap(token -> {

                    String fromAccount = firstOrNull(token.accounts());
                    requestDto.setNationalCode(token.user_name());
                    requestDto.setSourceAccount(fromAccount);
                    requestDto.setBank(token.bank());

                    return shahinWebClient.post()
                            .uri(getAccountInformationPath)
                            .headers(headers -> ShahinUtil.setHeader(headers, token.access_token()))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e -> Mono.error(new CommonException(
                                                    response.statusCode(),
                                                    e.message(),
                                                    CollectionUtils.firstElement(e.fields()))
                                            )))
                            .bodyToMono(AccountInfoResponseDto.class)
                            .map(AccountInfoResponseDto::getResponse);
                });
    }

    public Mono<AccountBalanceResponseDto.Combined> getAccountRemainBalance(AccountRequestDto requestDto) {

        return tokenProvider.getThreePartyValidToken(requestDto)
                .flatMap(token -> {

                    String fromAccount = firstOrNull(token.accounts());
                    requestDto.setNationalCode(token.user_name());
                    requestDto.setSourceAccount(fromAccount);
                    requestDto.setBank(token.bank());

                    return shahinWebClient.post()
                            .uri(getAccountBalancePath)
                            .headers(headers -> ShahinUtil.setHeader(headers, token.access_token()))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, res -> res.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e -> Mono.error(new CommonException(
                                                            res.statusCode(),
                                                            e.message(),
                                                            CollectionUtils.firstElement(e.fields()))
                                            )))
                            .bodyToMono(AccountBalanceResponseDto.class)
                            .map(AccountBalanceResponseDto::getResponse);
                });
    }

    public Mono<AccountStatementResponseDto.Combined> getAccountStatementList(AccountStatementRequestDto requestDto) {

        return null;
//        return tokenProvider.getThreePartyValidToken(requestDto)
//                .flatMap(token -> {
//                    String fromAccount = firstOrNull(token.accounts());
//                    requestDto.setNationalCode(token.user_name());
//                    requestDto.setSourceAccount(fromAccount);
//                    requestDto.setBank(token.bank());
//
//                    return shahinWebClient.post()
//                            .uri(getAccountStatementListPath)
//                            .headers(headers -> ShahinUtil.setHeader(headers, token.access_token()))
//                            .bodyValue(requestDto)
//                            .retrieve()
//                            .onStatus(HttpStatusCode::isError, res -> res.bodyToMono(ExceptionDto.class)
//                                            .map(ExceptionDto::getExceptionResponse)
//                                            .flatMap(e -> Mono.error(new CommonException(
//                                                            res.statusCode(),
//                                                            e.message(),
//                                                            CollectionUtils.firstElement(e.fields()))
//                                            )))
//                            .bodyToMono(AccountStatementResponseDto.class)
//                            .map(AccountStatementResponseDto::getResponse);
//                });
    }

    public Mono<CustomerAccountListDto.Combined> getCustomerAccountList(AccountRequestDto requestDto) {

        return tokenProvider.getThreePartyValidToken(requestDto)
                .flatMap(token -> {

                    String fromAccount = firstOrNull(token.accounts());
                    requestDto.setNationalCode(token.user_name());
                    requestDto.setSourceAccount(fromAccount);
                    requestDto.setBank(token.bank());

                    return shahinWebClient.post()
                            .uri(getAccountListPath)
                            .headers(headers -> ShahinUtil.setHeader(headers, token.access_token()))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, res -> res.bodyToMono(ExceptionDto.class)
                                    .map(ExceptionDto::getExceptionResponse)
                                    .flatMap(e -> Mono.error(new CommonException(
                                            res.statusCode(),
                                            e.message(),
                                            CollectionUtils.firstElement(e.fields()))
                                    )))
                            .bodyToMono(CustomerAccountListDto.class)
                            .map(CustomerAccountListDto::getResponse);
                });
    }
}