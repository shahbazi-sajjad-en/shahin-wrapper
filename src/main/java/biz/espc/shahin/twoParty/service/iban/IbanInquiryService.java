package biz.espc.shahin.twoParty.service.iban;

import biz.espc.shahin.dto.iban.IbanRequestDto;
import biz.espc.shahin.dto.iban.IbanResponseDto;
import biz.espc.shahin.dto.iban.IbanValidationRequestDto;
import biz.espc.shahin.dto.iban.IbanValidationResponseDto;
import biz.espc.shahin.enumeration.bank.Bank;
import biz.espc.shahin.exception.CommonException;
import biz.espc.shahin.exception.ExceptionDto;
import biz.espc.shahin.security.service.TokenProvider;
import biz.espc.shahin.util.ShahinUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * author: Ebrahim Sheyki
 * Created on: 2/9/2026  4:12 PM
 */
@Service
public class IbanInquiryService {

    @Value("${shahin.get.iban.info.path}")
    private String getIbanInfoPath;
    @Value("${shahin.get.iban.numbre.path}")
    private String getIbanNumberPath;
    @Value("${shahin.validate.iban.by.national.code.path}")
    private String validateIbanNumberPath;

    private final WebClient shahinWebClient;
    private final TokenProvider tokenProvider;

    public IbanInquiryService(WebClient shahinWebClient, TokenProvider tokenProvider) {
        this.shahinWebClient = shahinWebClient;
        this.tokenProvider = tokenProvider;
    }

    public Mono<IbanResponseDto.Combined> getIbanNumber(final IbanRequestDto requestDto) {

        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {

                    requestDto.setNationalCode(token.user_name());
                    requestDto.setSourceAccount(token.user_name());

                     return shahinWebClient.post()
                            .uri(getIbanNumberPath)
                            .headers(headers -> ShahinUtil.setHeader(headers, token.access_token()))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e ->
                                                    Mono.error(new CommonException(
                                                            response.statusCode(),
                                                            e.message(),
                                                            CollectionUtils.firstElement(e.fields()))
                                                    )))
                            .bodyToMono(IbanResponseDto.class)
                            .map(IbanResponseDto::customizeResponse);
                });
    }

    public Mono<IbanResponseDto.Combined> getIbanInfoInquiry(final IbanRequestDto requestDto) {

        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {

                    requestDto.setNationalCode(token.user_name());
                    requestDto.setSourceAccount(token.user_name());

                    return shahinWebClient.post()
                            .uri(getIbanInfoPath)
                            .headers(headers -> ShahinUtil.setHeader(headers, token.access_token()))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e -> Mono.error(new CommonException(
                                                    response.statusCode(),
                                                    e.message(),
                                                    CollectionUtils.firstElement(e.fields()))
                                            )))
                            .bodyToMono(IbanResponseDto.class)
                            .map(IbanResponseDto::customizeResponse);
                });
    }

    public Mono<IbanValidationResponseDto.Combined> validateIbanByNationalCode(final IbanValidationRequestDto requestDto) {

        final Bank bank = Bank.BSI;
        return tokenProvider.getValidToken(bank)
                .flatMap(token -> {

                    requestDto.setNationalCode(token.user_name());

                    return shahinWebClient.post()
                            .uri(validateIbanNumberPath)
                            .headers(headers -> ShahinUtil.setHeader(headers, token.access_token()))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e ->
                                                    Mono.error(new CommonException(
                                                            response.statusCode(),
                                                            e.message(),
                                                            CollectionUtils.firstElement(e.fields()))
                                                    )))
                            .bodyToMono(IbanValidationResponseDto.class)
                            .map(IbanValidationResponseDto::getResponse);
                });
    }

}
