package biz.espc.shahin.security.controller;

import biz.espc.shahin.dto.account.request.AccountRequestDto;
import biz.espc.shahin.dto.token.AuthResponseDto;
import biz.espc.shahin.enumeration.bank.Bank;
import biz.espc.shahin.security.service.AuthClientService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * author: Ebrahim Sheyki
 * Created on: 2/2/2026  3:13 PM
 */
@RestController
@RequestMapping(path = "client/authentication/api")
public class AuthClientApi {

    private final AuthClientService authClientService;

    public AuthClientApi(AuthClientService authClientService) {
        this.authClientService = authClientService;
    }

    @PostMapping("two-party-token")
    public Mono<AuthResponseDto> generateToken(@RequestParam Bank bankEnum) {
        return authClientService.generateToken(bankEnum);
    }

    @PostMapping("three-party-token")
    public Mono<AuthResponseDto> getThreePartyToken(@RequestBody AccountRequestDto requestDto) {
        return authClientService.getThreePartyToken(requestDto);
    }

    @PostMapping("three-party-token/trusted")
    public Mono<AuthResponseDto> getTrustedThreePartyToken(@RequestBody AccountRequestDto requestDto) {
        return authClientService.getTrustedThreePartyToken(requestDto);
    }
}
