package biz.espc.shahin.twoParty.api.suspension;

//import biz.espc.shahin.dto.twoWay.blockAmount.AccountSuspensionRequestDto;
//import biz.espc.shahin.dto.twoWay.blockAmount.SuspensionRequestDto;
//import biz.espc.shahin.dto.twoWay.blockAmount.SuspensionStatusRequestDto;
import biz.espc.shahin.twoParty.service.suspension.TwoPartySuspensionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * author: Ebrahim Sheyki
 * Created on: 2/15/2026  1:09 PM
 */
@RestController
@RequestMapping(path = "two-party/suspension/api")
public class TwoPartySuspensionController {

    private final TwoPartySuspensionService suspensionService;

    public TwoPartySuspensionController(TwoPartySuspensionService suspensionService) {
        this.suspensionService = suspensionService;
    }

//    @PostMapping("suspend-an-account")
//    public Mono<ResponseEntity<String>> suspendAnAccount(@RequestBody AccountSuspensionRequestDto requestDto) {
//        return suspensionService.suspendAnAccount(requestDto);
//    }
//
//    @PostMapping("get-account-suspension-inquiry")
//    public Mono<ResponseEntity<String>> getAccountSuspensionInquiry(@RequestBody SuspensionStatusRequestDto requestDto) {
//        return suspensionService.getAccountSuspensionInquiry(requestDto);
//    }

//    @PostMapping("remove-suspension-and-make-transaction")
//    public Mono<ResponseEntity<String>> removeSuspensionAndMakeTransaction(@RequestBody SuspensionRequestDto requestDto) {
//        return suspensionService.removeSuspensionAndMakeTransaction(requestDto);
//    }
}
