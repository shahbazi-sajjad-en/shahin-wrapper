package biz.espc.shahin.twoParty.api.transaction;

import biz.espc.shahin.dto.outbound.transaction.TransactionRequestDto;
import biz.espc.shahin.dto.outbound.transaction.TransactionResponseDto;
import biz.espc.shahin.twoParty.service.transaction.LocalTransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * author: Ebrahim Sheyki
 * Created on: 2/9/2026  1:26 PM
 */
@RestController
@RequestMapping(path = "two-party/local/api")
public class LocalTransactionController {

    private final LocalTransactionService localTransactionService;

    public LocalTransactionController(LocalTransactionService localTransactionService) {
        this.localTransactionService = localTransactionService;
    }


    @PostMapping("/local")
    public Mono<TransactionResponseDto.Combined> doLocalTransaction(@RequestBody TransactionRequestDto requestDto) {
        return localTransactionService.doLocalTransaction(requestDto);
    }
}
