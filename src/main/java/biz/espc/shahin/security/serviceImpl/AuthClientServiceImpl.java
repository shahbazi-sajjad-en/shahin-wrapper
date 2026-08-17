package biz.espc.shahin.security.serviceImpl;

import biz.espc.shahin.dto.account.request.AccountRequestDto;
import biz.espc.shahin.dto.token.AuthResponseDto;
import biz.espc.shahin.enumeration.bank.Bank;
import biz.espc.shahin.security.service.AuthClientService;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static biz.espc.shahin.util.ShahinUtil.CLIENT_CREDENTIALS;

/**
 * author: Ebrahim Sheyki
 * Created on: 2/7/2026  9:47 AM
 */
@Service
public class AuthClientServiceImpl implements AuthClientService {

    private final WebClient authWebClient;
    @Value("${shahin.get.token.path}")
    private String shahinGenerateTokenUrl;
    @Value("${shahin.basic.auth.two.party.username}")
    private String shahinBasicUsername;
    @Value("${shahin.basic.auth.two.party.password}")
    private String shahinBasicPassword;

    private final static String CODE = "123";
    private final static String CLIENT_ID = "FYcXMiA1Pw";
    private final static String REDIRECT_URL = "https://94.184.141.168:38453/v0.3/obh/authorization";
    // todo should get the right national code
    private final static String NATIONAL_CODE = "0021254855465";

//    @Value("${shahin.third.party.auth.redirect.uri}")
//    private String shahinRedirectUrl;


    public AuthClientServiceImpl(
            WebClient.Builder webClientBuilder,
            @Value("${shahin.token.base.url}")
            String shahinAuthenticationBaseUrl
    ) {
        this.authWebClient = webClientBuilder
                .baseUrl(shahinAuthenticationBaseUrl)
                .build();
    }

    @Override
    public Mono<AuthResponseDto> generateToken(Bank bank) {

        return authWebClient.post()
                .uri(uri -> preparedAuthRequestParam(bank, uri))
                .headers(h -> h.setBasicAuth(shahinBasicUsername, shahinBasicPassword))
                .retrieve()
                .bodyToMono(AuthResponseDto.class);
    }

    public Mono<AuthResponseDto> getTokenFromRedirectUri() {

        return authWebClient.get()
                .uri(this::preparedAuthRequest)
                .headers(h -> h.setBasicAuth(shahinBasicUsername, shahinBasicPassword))
                .retrieve()
                .bodyToMono(AuthResponseDto.class);
    }

    /**
     * create a well known comment to illustrate the business of the two and three protocol
     */
    @Override
    public Mono<AuthResponseDto> getThreePartyToken(AccountRequestDto requestDto) {

        return authWebClient.post()
//                .uri(uriBuilder ->  setThirdPartyAuthRequestParam(requestDto, uriBuilder))
                .headers(h -> h.setBasicAuth(shahinBasicUsername, shahinBasicPassword))
                .retrieve()
                .bodyToMono(AuthResponseDto.class);
    }

    @Override
    public Mono<AuthResponseDto> getTrustedThreePartyToken(AccountRequestDto requestDto) {
        return authWebClient.post()
//                .uri(uri -> setThirdPartyAuthRequestParam(requestDto, uri))
                .headers(h -> h.setBasicAuth(shahinBasicUsername, shahinBasicPassword))
                .retrieve()
                .bodyToMono(AuthResponseDto.class);
    }

    @Nonnull
    private URI preparedAuthRequest(UriBuilder uri) {
        return uri.path(REDIRECT_URL)
                .queryParam("grant_type", CLIENT_CREDENTIALS)
                .build();
    }

    @Nonnull
    private URI preparedAuthRequestParam(Bank bank, UriBuilder uri) {
        return uri.path(shahinGenerateTokenUrl)
                .queryParam("grant_type", CLIENT_CREDENTIALS)
                .queryParam("bank", bank)
                .build();
    }
//
//    @Nonnull
//    private URI setThirdPartyAuthRequestParam(AccountRequestDto requestDto, UriBuilder uri) {
//        return uri.path(shahinGenerateTokenUrl)
//                .queryParam("grant_type", CLIENT_CREDENTIALS)
////                .queryParam("clientId", CLIENT_ID)
////                .queryParam("nationalCode", NATIONAL_CODE)
//                .queryParam("bank", requestDto.getBank())
////                .queryParam("code", CODE)
////                .queryParam("period", period)
////                .queryParam(destination)
////                .queryParam(String.valueOf(amount))
//                .build();
//    }
}
