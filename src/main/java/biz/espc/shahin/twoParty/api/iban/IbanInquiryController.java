package biz.espc.shahin.twoParty.api.iban;

import biz.espc.shahin.dto.iban.IbanRequestDto;
import biz.espc.shahin.dto.iban.IbanResponseDto;
import biz.espc.shahin.dto.iban.IbanValidationRequestDto;
import biz.espc.shahin.dto.iban.IbanValidationResponseDto;
import biz.espc.shahin.twoParty.service.iban.IbanInquiryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * author: Ebrahim Sheyki
 * Created on: 1/29/2026  9:29 AM
 */
@RestController
@RequestMapping(path = "iban/inquiry/api")
public class IbanInquiryController {

    private final IbanInquiryService ibanInquiryService;

    public IbanInquiryController(IbanInquiryService ibanInquiryService) {
        this.ibanInquiryService = ibanInquiryService;
    }

    @PostMapping("get-iban-number")
    public Mono<IbanResponseDto.Combined> getIbanNumberInquiry(@RequestBody IbanRequestDto requestDto) {
        return ibanInquiryService.getIbanNumber(requestDto);
    }

    @PostMapping("get-iban-information")
    public Mono<IbanResponseDto.Combined> getIbanInfoInquiry(@RequestBody IbanRequestDto requestDto) {
        return ibanInquiryService.getIbanInfoInquiry(requestDto);
    }

    @PostMapping("validate-iban-number-by-nationalCode")
    public Mono<IbanValidationResponseDto.Combined> validateIbanNumber(@RequestBody IbanValidationRequestDto requestDto) {
        return ibanInquiryService.validateIbanByNationalCode(requestDto);
    }
}
