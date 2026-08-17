package biz.espc.shahin.twoParty.service.suspension;

//import biz.espc.shahin.dto.twoWay.blockAmount.AccountSuspensionRequestDto;
//import biz.espc.shahin.dto.twoWay.blockAmount.SuspensionRequestDto;
//import biz.espc.shahin.dto.twoWay.blockAmount.SuspensionStatusRequestDto;
import biz.espc.shahin.security.service.AuthClientService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * author: Ebrahim Sheyki
 * Created on: 1/29/2026  9:24 AM
 */
@Service
public class TwoPartySuspensionService {

    private final WebClient webClient;
    private final AuthClientService authClientService;

    @Value("${shahin.block.an.account.path}")
    private String shahinSuspendAnAccountPath;

    @Value("${shahin.account.suspension.inquiry.path}")
    private String shahinAccountSuspensionInquiryPath;

    @Value("${shahin.remove.suspension.and.make.transaction.path}")
    private String shahinRemoveSuspensionAndMakeTransaction;

    public TwoPartySuspensionService(@Qualifier("shahinWebClient") WebClient webClient, AuthClientService authClientService) {
        this.webClient = webClient;
        this.authClientService = authClientService;
    }

//    public Mono<ResponseEntity<String>> suspendAnAccount(AccountSuspensionRequestDto requestDto) {
//
//        return webClient.post()
//                .uri(shahinSuspendAnAccountPath)
//                .headers(UniRestUtils::setHeaders)
//                .bodyValue(requestDto)
//                .exchangeToMono(response ->
//                        response.bodyToMono(String.class)
//                                .map(body -> ResponseEntity
//                                        .status(response.statusCode())
//                                        .headers(response.headers().asHttpHeaders())
//                                        .contentType(MediaType.APPLICATION_JSON)
//                                        .body(body))
//                );
//    }

//    public Mono<ResponseEntity<String>> getAccountSuspensionInquiry(SuspensionStatusRequestDto requestDto) {
//
//        return webClient.post()
//                .uri(shahinAccountSuspensionInquiryPath)
//                .headers(UniRestUtils::setHeaders)
//                .bodyValue(requestDto)
//                .exchangeToMono(response ->
//                        response.bodyToMono(String.class)
//                                .map(body -> ResponseEntity
//                                        .status(response.statusCode())
//                                        .headers(response.headers().asHttpHeaders())
//                                        .contentType(MediaType.APPLICATION_JSON)
//                                        .body(body))
//                );
//    }


//    public Mono<ResponseEntity<String>> removeSuspensionAndMakeTransaction(SuspensionRequestDto requestDto) {
//
//        return webClient.post()
//                .uri(shahinRemoveSuspensionAndMakeTransaction)
//                .headers(UniRestUtils::setHeaders)
//                .bodyValue(requestDto)
//                .exchangeToMono(response ->
//                        response.bodyToMono(String.class)
//                                .map(body -> ResponseEntity
//                                        .status(response.statusCode())
//                                        .headers(response.headers().asHttpHeaders())
//                                        .contentType(MediaType.APPLICATION_JSON)
//                                        .body(body))
//                );
//    }
}
