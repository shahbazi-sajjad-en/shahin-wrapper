package biz.espc.shahin.security.serviceImpl;

import biz.espc.shahin.dto.account.request.AccountRequestDto;
import biz.espc.shahin.dto.token.AuthResponseDto;
import biz.espc.shahin.enumeration.bank.Bank;
import biz.espc.shahin.security.service.AuthClientService;
import biz.espc.shahin.security.service.TokenProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class InMemoryTokenProvider implements TokenProvider {

    private final AuthClientService authClientService;

    private volatile String accessToken;
    private volatile Instant expiryTime;
    private AuthResponseDto cachedResponse;
    private Mono<AuthResponseDto> refreshingMono;

    public InMemoryTokenProvider(AuthClientService authClientService) {
        this.authClientService = authClientService;
    }

    @Override
    public Mono<AuthResponseDto> getValidToken(Bank bank) {

        if (isTokenValid()) {
            return Mono.just(cachedResponse);
        }

        if (refreshingMono != null) {
            return refreshingMono;
        }

        synchronized (this) {

            if (isTokenValid()) {
                return Mono.just(cachedResponse);
            }

            if (refreshingMono == null) {

                refreshingMono = authClientService.generateToken(bank)
                        .map(response -> {
                            this.cachedResponse = response;
                            this.accessToken = response.access_token();
                            this.expiryTime = Instant.now()
                                    .plusSeconds(response.expires_in() - 30);
                            return response;
                        })
                        .doFinally(signal -> refreshingMono = null)
                        .cache();
            }

            return refreshingMono;
        }
    }

    private boolean isTokenValid() {
        return accessToken != null
                && expiryTime != null
                && Instant.now().isBefore(expiryTime);
    }

    @Override
    public Mono<AuthResponseDto> getThreePartyValidToken(AccountRequestDto requestDto) {
        if (isTokenValid()) {
            return Mono.just(cachedResponse);
        }

        if (refreshingMono != null) {
            return refreshingMono;
        }

        synchronized (this) {

            if (isTokenValid()) {
                return Mono.just(cachedResponse);
            }

            if (refreshingMono == null) {

                // todo : consider Period to handle the time here
                refreshingMono = authClientService.getThreePartyToken(requestDto)
                        .map(response -> {
                            this.cachedResponse = response;
                            this.accessToken = response.access_token();
                            this.expiryTime = Instant.now()
                                    .plusSeconds(response.expires_in() - 30);
                            return response;
                        })
                        .doFinally(signal -> refreshingMono = null)
                        .cache();
            }

            return refreshingMono;
        }
    }
}


