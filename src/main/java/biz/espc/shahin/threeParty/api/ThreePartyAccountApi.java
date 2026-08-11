package biz.espc.shahin.threeParty.api;

import biz.espc.shahin.dto.account.AccountBalanceResponseDto;
import biz.espc.shahin.dto.account.AccountInfoResponseDto;
import biz.espc.shahin.dto.account.AccountStatementResponseDto;
import biz.espc.shahin.dto.account.request.AccountRequestDto;
import biz.espc.shahin.dto.account.request.AccountStatementRequestDto;
import biz.espc.shahin.dto.customer.CustomerAccountListDto;
import biz.espc.shahin.threeParty.service.ThreePartyAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/shahin/threeParty")
public class ThreePartyAccountApi {

    private final ThreePartyAccountService service;

    public ThreePartyAccountApi(ThreePartyAccountService service) {
        this.service = service;
    }

    // todo : SHOULD REPLACE THREE PROTOCOL API TO A APPROPRIATE CLASS  (Considering Single Responsibility)
    // ----------------------------------three-party------------------------------------------------------------------
    @PostMapping("get-three-party-account-info")
    public Mono<AccountInfoResponseDto.Combined> getThreePartyAccountInfo(@RequestBody AccountRequestDto requestDto) {
        return service.getThreePartyAccountInfo(requestDto);
    }

    @PostMapping("get-three-party-remain-balance")
    public Mono<AccountBalanceResponseDto.Combined> getAccountRemainBalance(@RequestBody AccountRequestDto requestDto) {
        return service.getAccountRemainBalance(requestDto);
    }

    @PostMapping("get-account-statement-list")
    public Mono<AccountStatementResponseDto.Combined> getAccountStatementList(@RequestBody AccountStatementRequestDto dto) {
        return service.getAccountStatementList(dto);
    }

    @PostMapping("get-customer-account-list")
    public Mono<CustomerAccountListDto.Combined> getCustomerAccountList(@RequestBody AccountRequestDto requestDto) {
        return service.getCustomerAccountList(requestDto);
    }

}
