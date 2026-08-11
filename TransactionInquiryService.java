@Service
public class TransactionInquiryService {

    private final WebClient shahinWebClient;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final DigitalSignature digitalSignature;

    @Value("${shahin.ach.transactions.inquiry.path}")
    private String getAchTransactionInquiryPath;

    @Value("${shahin.rtgs.transactions.inquiry.path}")
    private String getRtgsTransactionInquiryPath;

    @Value("${shahin.transactions.inquiry.path}")
    private String getTransactionPath;

    @Value("${shahin.get.national.identity.path}")
    private String getNationalCodeIdentityPath;

    private final String obhClientId = "your-client-id";
    private final String obhClientSecret = "your-client-secret";

    public TransactionInquiryService(WebClient shahinWebClient, TokenProvider tokenProvider, ObjectMapper objectMapper, DigitalSignature digitalSignature) {
        this.shahinWebClient = shahinWebClient;
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
        this.digitalSignature = digitalSignature;
    }

    public Mono<AchInquiryResponseDto.Combined> getAchTransactionInquiry(TransactionStatementRequestDto requestDto) {

        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {
                    requestDto.setBank(token.bank());

                    String jsonBody;
                    try {
                        jsonBody = objectMapper.writeValueAsString(requestDto);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new CommonException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "REQUEST_SERIALIZATION_FAILED",
                                e.getMessage()
                        ));
                    }

                    return shahinWebClient.post()
                            .uri(getAchTransactionInquiryPath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers ->
                                    ShahinUtil.setObh1SignedHeaders(
                                            headers,
                                            token.access_token(),
                                            jsonBody,
                                            HttpMethod.POST.name(),
                                            getAchTransactionInquiryPath,
                                            obhClientId,
                                            obhClientSecret,
                                            digitalSignature
                                    ))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e -> Mono.error(
                                                    new CommonException(
                                                            response.statusCode(),
                                                            e.message(),
                                                            CollectionUtils.firstElement(e.fields())
                                                    ))))
                            .bodyToMono(AchInquiryResponseDto.class)
                            .map(AchInquiryResponseDto::getResponse)
                            .onErrorMap(ex -> {
                                if (ex instanceof CommonException) return ex;
                                return new CommonException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "GET_ACH_TRANSACTION_INQUIRY_CALL_FAILED",
                                        ex.getMessage()
                                );
                            });
                });
    }

    public Mono<RtgsTransactionInquiryDto.Combined> getRtgsTransactionInquiry(TransactionStatementRequestDto requestDto) {

        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {
                    requestDto.setBank(token.bank());

                    String jsonBody;
                    try {
                        jsonBody = objectMapper.writeValueAsString(requestDto);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new CommonException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "REQUEST_SERIALIZATION_FAILED",
                                e.getMessage()
                        ));
                    }

                    return shahinWebClient.post()
                            .uri(getRtgsTransactionInquiryPath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers ->
                                    ShahinUtil.setObh1SignedHeaders(
                                            headers,
                                            token.access_token(),
                                            jsonBody,
                                            HttpMethod.POST.name(),
                                            getRtgsTransactionInquiryPath,
                                            obhClientId,
                                            obhClientSecret,
                                            digitalSignature
                                    ))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e -> Mono.error(
                                                    new CommonException(
                                                            response.statusCode(),
                                                            e.message(),
                                                            CollectionUtils.firstElement(e.fields())
                                                    ))))
                            .bodyToMono(RtgsTransactionInquiryDto.class)
                            .map(RtgsTransactionInquiryDto::getResponse)
                            .onErrorMap(ex -> {
                                if (ex instanceof CommonException) return ex;
                                return new CommonException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "GET_RTGS_TRANSACTION_INQUIRY_CALL_FAILED",
                                        ex.getMessage()
                                );
                            });
                });
    }

    public Mono<TransactionInquiryResponseDto.Combined> getTransactionInquiry(TransactionInquiryRequestDto requestDto) {

        return tokenProvider.getValidToken(requestDto.getBank())
                .flatMap(token -> {
                    final String fromAccount = firstOrNull(token.accounts());
                    requestDto.setBank(token.bank());
                    requestDto.setNationalCode(token.user_name());
                    requestDto.setSourceAccount(fromAccount);

                    String jsonBody;
                    try {
                        jsonBody = objectMapper.writeValueAsString(requestDto);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new CommonException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "REQUEST_SERIALIZATION_FAILED",
                                e.getMessage()
                        ));
                    }

                    return shahinWebClient.post()
                            .uri(getTransactionPath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers ->
                                    ShahinUtil.setObh1SignedHeaders(
                                            headers,
                                            token.access_token(),
                                            jsonBody,
                                            HttpMethod.POST.name(),
                                            getTransactionPath,
                                            obhClientId,
                                            obhClientSecret,
                                            digitalSignature
                                    ))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e -> Mono.error(
                                                    new CommonException(
                                                            response.statusCode(),
                                                            e.message(),
                                                            CollectionUtils.firstElement(e.fields())
                                                    ))))
                            .bodyToMono(TransactionInquiryResponseDto.class)
                            .map(TransactionInquiryResponseDto::getResponse)
                            .onErrorMap(ex -> {
                                if (ex instanceof CommonException) return ex;
                                return new CommonException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "GET_TRANSACTION_INQUIRY_CALL_FAILED",
                                        ex.getMessage()
                                );
                            });
                });
    }

    public Mono<PersonalInformationDetailDto.Combined> getNationalIdentity(PersonalInformationDto requestDto) {

        Bank bank = Bank.BSI;
        return tokenProvider.getValidToken(bank)
                .flatMap(token -> {
                    requestDto.setNationalCode(token.user_name());

                    String jsonBody;
                    try {
                        jsonBody = objectMapper.writeValueAsString(requestDto);
                    } catch (JsonProcessingException e) {
                        return Mono.error(new CommonException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "REQUEST_SERIALIZATION_FAILED",
                                e.getMessage()
                        ));
                    }

                    return shahinWebClient.post()
                            .uri(getNationalCodeIdentityPath)
                            .attribute(CurlLogConstants.CURL_BODY_ATTR, jsonBody)
                            .headers(headers ->
                                    ShahinUtil.setObh1SignedHeaders(
                                            headers,
                                            token.access_token(),
                                            jsonBody,
                                            HttpMethod.POST.name(),
                                            getNationalCodeIdentityPath,
                                            obhClientId,
                                            obhClientSecret,
                                            digitalSignature
                                    ))
                            .bodyValue(requestDto)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(ExceptionDto.class)
                                            .map(ExceptionDto::getExceptionResponse)
                                            .flatMap(e -> Mono.error(
                                                    new CommonException(
                                                            response.statusCode(),
                                                            e.message(),
                                                            CollectionUtils.firstElement(e.fields())
                                                    ))))
                            .bodyToMono(PersonalInformationDetailDto.class)
                            .map(PersonalInformationDetailDto::getResponse)
                            .onErrorMap(ex -> {
                                if (ex instanceof CommonException) return ex;
                                return new CommonException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "GET_NATIONAL_IDENTITY_CALL_FAILED",
                                        ex.getMessage()
                                );
                            });
                });
    }

}
