package biz.espc.shahin.security.service;

import biz.espc.shahin.dto.account.request.AccountRequestDto;
import biz.espc.shahin.dto.token.AuthResponseDto;
import biz.espc.shahin.enumeration.bank.Bank;
import reactor.core.publisher.Mono;

/**
 * author: Ebrahim Sheyki
 * Created on: 2/16/2026  9:47 AM
 */
public interface TokenProvider {

    Mono<AuthResponseDto> getValidToken(Bank bank);

    Mono<AuthResponseDto> getThreePartyValidToken(AccountRequestDto requestDto);

//    Mono<AuthenticationResponseDto> generateTrustedThreePartyToken(AccountRequestDto requestDto);

}
