package biz.espc.shahin.twoParty.service.transaction;

import biz.espc.shahin.dto.token.AuthResponseDto;
import biz.espc.shahin.dto.transaction.TransactionRequestDto;
import biz.espc.shahin.dto.transaction.TransactionResponseDto;
import biz.espc.shahin.entity.TransactionRequest;
import biz.espc.shahin.enumeration.bank.Bank;
import biz.espc.shahin.enumeration.transaction.TransactionStatus;
import biz.espc.shahin.exception.CommonException;
import biz.espc.shahin.exception.ExceptionDto;
import biz.espc.shahin.mapper.TransactionRequestMapper;
import biz.espc.shahin.repository.TransactionRequestRepository;
import biz.espc.shahin.security.service.TokenProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.time.LocalDateTime;

import static biz.espc.shahin.util.ShahinUtil.*;


@Service
@RequiredArgsConstructor
public class AchTransactionService {

    private final WebClient shahinWebClient;
    private final TokenProvider tokenProvider;
    private final TransactionRequestRepository repository;
    private final TransactionRequestMapper mapper;
    @Value("${shahin.transaction.path}")
    private URI achTransactionPath;

    public Mono<TransactionResponseDto.Combined> doAchTransaction(TransactionRequestDto requestDto) {
        TransactionRequest entity = mapper.toEntity(requestDto);

        entity.changeStatus(TransactionStatus.INITIALIZE, INITIAL_THE_FIRST_STATE);
        entity.setToAccount(requestDto.getDestinationAccountNumber());
        entity.setRefCode(requestDto.getRefCode());
        entity.setCreatedAt(LocalDateTime.now());

        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {
                    initializeDtoFromToken(requestDto, token);
                    return executeTransaction(requestDto, token, entity);
                });
    }

    private @NonNull Mono<TransactionResponseDto.Combined> executeTransaction(TransactionRequestDto requestDto,
                                                                              AuthResponseDto token, TransactionRequest entity) {

        return save(entity).flatMap(saved ->  callShahinApi(requestDto, token)
                                .flatMap(response ->  handleSuccess(saved, response))
                                .onErrorResume(error ->   handleFailure(saved, error)));
    }

    private void initializeDtoFromToken(TransactionRequestDto requestDto, AuthResponseDto token) {
        final Bank destinationBank = requireBankFromIban(requestDto.getDestinationAccountNumber());
        final String fromAccount = firstOrNull(token.accounts());//todo : should handle the token properties and accounts
        requestDto.setNationalCode(token.user_name());
        requestDto.setBank(token.bank());
        requestDto.setDestinationBank(destinationBank);
        requestDto.setFromAccount(fromAccount);
    }

    private Mono<TransactionResponseDto.Combined> callShahinApi(TransactionRequestDto requestDto, AuthResponseDto token) {

        return shahinWebClient.post()
                .uri(achTransactionPath)
                .headers(headers -> setHeader(headers, token.access_token()))
                .bodyValue(requestDto)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::mapError)
                .bodyToMono(TransactionResponseDto.class)
                .map(TransactionResponseDto::getResponse);
    }

    private Mono<? extends Throwable> mapError(ClientResponse response) {

        return response.bodyToMono(ExceptionDto.class)
                .map(ExceptionDto::getExceptionResponse)
                .map(e ->
                        new CommonException(response.statusCode(), e.message(),
                                CollectionUtils.firstElement(e.fields())));
    }

    private Mono<TransactionResponseDto.Combined> handleSuccess(TransactionRequest entity, TransactionResponseDto.Combined response) {
        entity.changeStatus(TransactionStatus.SUCCESS, SUCCESS_COMMENT);
        entity.setShahinUUID(response.uuid());
        entity.setResponseDate(LocalDateTime.now());

        return save(entity).thenReturn(response);
    }

    private Mono<TransactionResponseDto.Combined> handleFailure(TransactionRequest entity, Throwable error) {
        entity.changeStatus(TransactionStatus.FAILED, TRANSACTION_FAILED_COMMENT);
        entity.setErrorMessage(error.getMessage());

        return save(entity).then(Mono.error(error));
    }

    private Mono<TransactionRequest> save(TransactionRequest entity) {

        return Mono.fromCallable(() -> repository.save(entity))
                .subscribeOn(Schedulers.boundedElastic());
    }

}
