package biz.espc.shahin.security.service;

import biz.espc.shahin.dto.account.request.AccountRequestDto;
import biz.espc.shahin.dto.token.AuthResponseDto;
import biz.espc.shahin.enumeration.bank.Bank;
import reactor.core.publisher.Mono;

/**
 * author: Ebrahim Sheyki
 * Created on: 2/7/2026  9:46 AM
 */
public interface AuthClientService {

    Mono<AuthResponseDto> generateToken(Bank bank);

    Mono<AuthResponseDto> getThreePartyToken(AccountRequestDto requestDto);

    Mono<AuthResponseDto> getTokenFromRedirectUri();

    Mono<AuthResponseDto> getTrustedThreePartyToken(AccountRequestDto requestDto);

}
