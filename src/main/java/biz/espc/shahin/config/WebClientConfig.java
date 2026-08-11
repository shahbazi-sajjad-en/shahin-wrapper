package biz.espc.shahin.config;

import biz.espc.shahin.dto.CurlLogConstants;
import com.google.gson.Gson;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

/**
 * provided by ESPC software team
 * created on 1/28/2026 at 10:49 AM
 */
@Configuration
public class WebClientConfig {

    private static final Logger log = LoggerFactory.getLogger(WebClientConfig.class);

    @Bean
    public WebClient shahinWebClient(
            WebClient.Builder builder,
            @Value("${shahin.service.base.url}") String baseUrl) {

        return builder
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public WebClient authWebClient(
            WebClient.Builder builder,
            @Value("${shahin.token.base.url}") String baseUrl) {

        return builder
                .baseUrl(baseUrl)
                .build();
    }


    @Bean
    public WebClient shahinThreePartyDirect(
            WebClient.Builder builder,
            @Value("${shahin.three.party.direct.url}") String baseUrl) {

        return builder
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    @Primary
    @SneakyThrows
    public WebClient.Builder webClientBuilder() {
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create()
                .secure(sslSpec -> sslSpec.sslContext(sslContext));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(curlLoggingFilter())
                .filter(logRequest())
                .filter(logResponse());
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Inbound request: {} {}",
                    clientRequest.method(),
                    clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Outbound response status: {}",
                    clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

//    @Bean
//    public WebClient webClientWithToken(TokenProvider tokenProvider) {
//
//        return WebClient.builder()
//                .filter((request, next) ->
//                        tokenProvider.getValidToken(request)
//                                .flatMap(token -> {
//                                    ClientRequest newRequest = ClientRequest.from(request)
//                                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
//                                            .build();
//                                    return next.exchange(newRequest);
//
//                                })).build();
//    }




    private ExchangeFilterFunction curlLoggingFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            StringBuilder curl = new StringBuilder("curl -X ")
                    .append(request.method())
                    .append(" '")
                    .append(request.url())
                    .append("'");

            request.headers().forEach((name, values) -> {
                for (String value : values) {
                    String safeValue = "Authorization".equalsIgnoreCase(name)
                            ? maskToken(value)
                            : value;
                    curl.append(" -H '").append(name).append(": ").append(value).append("'");
                }
            });

            Object body = request.attribute(CurlLogConstants.CURL_BODY_ATTR).orElse(null);
            if (body != null) {
                String escaped = body.toString().replace("'", "'\"'\"'");
                curl.append(" --data '").append(escaped).append("'");
            }

            log.info("Outgoing request as cURL: {}", curl);
            return Mono.just(request);
        });
    }

    private String maskToken(String token) {
        if (token == null || token.length() < 20) return "***";
        return token.substring(0, 15) + "...***";
    }
}





