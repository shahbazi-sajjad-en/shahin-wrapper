package biz.espc.shahin.twoParty.api.transaction;

import biz.espc.shahin.dto.transaction.TransactionRequestDto;
import biz.espc.shahin.dto.transaction.TransactionResponseDto;
import biz.espc.shahin.twoParty.service.transaction.AchTransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * author: Ebrahim Sheyki
 * Created on: 2/8/2026  11:48 AM
 */
@RestController
@RequestMapping(path = "two-party/bank-transaction/api")
public class AchTransactionController {

    private final AchTransactionService bankTransactionService;

    public AchTransactionController(AchTransactionService bankTransactionService) {
        this.bankTransactionService = bankTransactionService;
    }

    @PostMapping("/ach")
    public Mono<TransactionResponseDto.Combined> doAchTransaction(@RequestBody TransactionRequestDto requestDto) {
        return bankTransactionService.doAchTransaction(requestDto);
    }
}
