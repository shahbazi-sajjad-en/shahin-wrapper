package biz.espc.shahin.twoParty.api.account;

import biz.espc.shahin.dto.account.*;
import biz.espc.shahin.dto.account.request.AccountRequestDto;
import biz.espc.shahin.dto.account.request.AccountStatementRequestDto;
import biz.espc.shahin.dto.customer.CustomerAccountListDto;
import biz.espc.shahin.dto.iban.IbanRequestDto;
import biz.espc.shahin.dto.iban.IbanResponseDto;
import biz.espc.shahin.dto.iban.IbanValidationRequestDto;
import biz.espc.shahin.dto.iban.IbanValidationResponseDto;
import biz.espc.shahin.twoParty.service.account.TwoPartyAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * author: Ebrahim Sheyki
 * Created on: 1/28/2026  9:27 AM
 */
@RestController
@RequestMapping(path = "two-party/account/api")
public class TwoPartyAccountApi {

    private final TwoPartyAccountService accountService;

    public TwoPartyAccountApi(TwoPartyAccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("get-account-info")
    public Mono<AccountInfoResponseDto.Combined> getAccountInfo(@RequestBody AccountRequestDto requestDto) {
        return accountService.getAccountInfo(requestDto);
    }

    @PostMapping("get-account-remain-balance")
    public Mono<AccountBalanceResponseDto.Combined> getAccountRemainBalance(@RequestBody AccountRequestDto requestDto) {
        return accountService.getAccountRemainBalance(requestDto);
    }

    @PostMapping("get-account-statement-list")
    public Mono<AccountStatementResponseDto.Combined> getAccountStatementList(@RequestBody AccountStatementRequestDto dto) {
        return accountService.getAccountStatementList(dto);
    }

    @PostMapping("get-account-statement-page")
    public Mono<AccountStatementResponseDto.Combined> getAccountStatementPage(@RequestBody AccountStatementRequestDto dto) {
        return accountService.getAccountStatementPage(dto);
    }

    @PostMapping("get-customer-account-list")
    public Mono<CustomerAccountListDto.Combined> getCustomerAccountList(@RequestBody AccountRequestDto requestDto) {
        return accountService.getCustomerAccountList(requestDto);
    }

    @PostMapping("get-iban")
    public Mono<IbanResponseDto.Combined> getIban(@RequestBody IbanRequestDto requestDto) {
        return accountService.getIban(requestDto);
    }

    @PostMapping("get-iban-info")
    public Mono<IbanValidationResponseDto.Combined> getIbanInfo(@RequestBody IbanValidationRequestDto requestDto) {
        return accountService.getIbanInfo(requestDto);
    }
}