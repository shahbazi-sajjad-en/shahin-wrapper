package biz.espc.shahin.twoParty.api.transaction;

import biz.espc.shahin.dto.outbound.transaction.TransactionRequestDto;
import biz.espc.shahin.dto.outbound.transaction.TransactionResponseDto;
import biz.espc.shahin.twoParty.service.transaction.RtgsTransactionService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * author: Ebrahim Sheyki
 * Created on: 2/7/2026  5:06 PM
 */
@RestControllerAdvice
@RestController
@RequestMapping(path = "two-party/rtgs/api")
public class RtgsTransactionController {

    private final RtgsTransactionService rtgsTransactionService;

    public RtgsTransactionController(RtgsTransactionService rtgsTransactionService) {
        this.rtgsTransactionService = rtgsTransactionService;
    }

    @PostMapping("/rtgs")
    public Mono<TransactionResponseDto.Combined> doRtgsTransaction(@RequestBody TransactionRequestDto requestDto) {
        return rtgsTransactionService.doRtgsTransaction(requestDto);
    }
}
