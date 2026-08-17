package biz.espc.shahin.twoParty.service.card;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * author: Ebrahim Sheyki
 * Created on: 1/28/2026  10:24 AM
 */
@Service
public class TwoPartyCardService {

    private final WebClient webClient;

    @Value("${shahin.validate.card.by.national.code.path}")
    private String shahinValidateCardPath;

    public TwoPartyCardService(@Qualifier("shahinWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * @param requestDto
     * @return CustomerAccountListDto
     */
//    public Mono<CardResponseDto.Combined> validateCardNumber(GetCardInfoRequestDto requestDto) {

//        final String accessToken = String.valueOf(authClientService
//                .generateToken(BankEnum.BSI)
//                .map(AuthenticationResponseDto::access_token));

//        return webClient.post()
//                .uri(shahinValidateCardPath)
//                .headers(UniRestUtils::setHeaders)
//                .bodyValue(requestDto)
//                .retrieve()
//                .bodyToMono(CardResponseDto.class)
//                .map(CardResponseDto::toCombined);
//    }
}
