package biz.espc.shahin.twoParty.api.transaction;

import biz.espc.shahin.dto.inquiry.TransactionStatementRequestDto;
import biz.espc.shahin.dto.transaction.AchInquiryResponseDto;
import biz.espc.shahin.dto.transaction.RtgsTransactionInquiryDto;
import biz.espc.shahin.twoParty.service.transaction.TransactionInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "transaction/inquiry/api")
public class TransactionInquiryController {

    private final TransactionInquiryService transactionInquiryService;

    @PostMapping("/get-ach-transaction-inquiry")
    public Mono<AchInquiryResponseDto.Combined> getAchTransactionInquiry(
            @RequestBody TransactionStatementRequestDto requestDto) {
        return transactionInquiryService.getAchTransactionInquiry(requestDto);
    }

    @PostMapping("/get-rtgs-transaction-inquiry")
    public Mono<RtgsTransactionInquiryDto.Combined> getRtgsTransactionInquiry(
            @RequestBody TransactionStatementRequestDto requestDto) {
        return transactionInquiryService.getRtgsTransactionInquiry(requestDto);
    }
}
