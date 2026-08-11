//package biz.espc.shahin.threePartyProtocol.serviceImpl;
//
//import biz.espc.shahin.common.dto.account.*;
//import biz.espc.shahin.common.dto.cheque.*;
//import biz.espc.shahin.common.dto.customer.GetBasicCustinfoRequestDto;
//import biz.espc.shahin.common.dto.customer.GetBasicCustinfoResponseDto;
//import biz.espc.shahin.common.dto.customer.GetDetailCustinfoRequestDto;
//import biz.espc.shahin.common.dto.customer.GetDetailCustinfoResponseDto;
//import biz.espc.shahin.common.dto.loan.GetLoanInfoRequestDto;
//import biz.espc.shahin.common.dto.loan.GetLoanInfoResponseDto;
//import biz.espc.shahin.common.dto.loan.GetLoanStatementRequestDto;
//import biz.espc.shahin.common.dto.loan.GetLoanStatementResponseDto;
//import biz.espc.shahin.common.dto.token.CommonTokenRequestDto;
//import biz.espc.shahin.common.dto.token.CommonTokenResponseDto;
//import biz.espc.shahin.common.dto.twoWay.billPayment.BillPaymentValidationRequestDto;
//import biz.espc.shahin.common.dto.twoWay.billPayment.BillPaymentValidationResponseDto;
//import biz.espc.shahin.common.dto.twoWay.billPayment.PayBillRequestDto;
//import biz.espc.shahin.common.dto.twoWay.billPayment.PayBillResponseDto;
//import biz.espc.shahin.common.dto.twoWay.blockAmount.*;
//import biz.espc.shahin.common.dto.twoWay.card.*;
//import biz.espc.shahin.common.dto.twoWay.token.TwoPartyTokenResponseDto;
//import biz.espc.shahin.common.dto.twoWay.token.TwoWayTokenRequestDto;
//import biz.espc.shahin.common.enumeration.BankEnum;
//import biz.espc.shahin.common.exception.GeneralException;
//import biz.espc.shahin.common.service.ShahinCommonService;
//import biz.espc.shahin.common.util.DigitalSignature;
//import biz.espc.shahin.common.util.UniRestUtils;
//import com.google.gson.Gson;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.UUID;
//
//import static biz.espc.shahin.common.util.UniRestUtils.post;
//
//
//@Service
//public class ShahinCommonServiceImpl implements ShahinCommonService {
//
//    private final DigitalSignature digitalSignature;
//    private final Gson gson = new Gson();
//    @Value("${shahin.service.base.url}")
//    private String shahinBaseUrl;
//    @Value("${shahin.client.id}")
//    private String clientId;
//    @Value("${shahin.client.secret}")
//    private String clientSecret;
//    @Value("${shahin.get.account.info.path}")
//    private String getAccountInfoPath;
//    @Value("${shahin.get.account.balance.path}")
//    private String getAccountBalancePath;
//    @Value("${shahin.get.account.list.path}")
//    private String getAccountListPath;
//    @Value("${shahin.get.account.statement.path}")
//    private String getAccountStatementPath;
//    @Value("${shahin.get.iban.path}")
//    private String getIbanPath;
//    @Value("${shahin.get.iban.info.path}")
//    private String getIbanInfoPath;
//    @Value("${shahin.get.legal.account.info.path}")
//    private String getLegalAccountInfoPath;
//    @Value("${shahin.bill.payment.validation.path}")
//    private String billPaymentValidationPath;
//    @Value("${shahin.pay.bill.path}")
//    private String payBillPath;
//    @Value("${shahin.block.amount.path}")
//    private String blockAmountPath;
//    @Value("${shahin.block.amount.inquiry.path}")
//    private String blockAmountInquiryPath;
//    @Value("${shahin.unblock.amount.transfer.path}")
//    private String unblockAmountTransferPath;
//    @Value("${shahin.card.balance.path}")
//    private String cardBalancePath;
//    @Value("${shahin.card.transfer.path}")
//    private String cardTransferPath;
//    @Value("${shahin.get.card.info.path}")
//    private String getCardInfoPath;
//    @Value("${shahin.get.card.trans.path}")
//    private String getCardTransPath;
//    @Value("${shahin.pay.bill.card.path}")
//    private String payBillByCardPath;
//    @Value("${shahin.cheque.accept.path}")
//    private String chequeAcceptPath;
//    @Value("${shahin.cheque.inquiry.holder.path}")
//    private String chequeInquiryHolderPath;
//    @Value("${shahin.cheque.inquiry.transfer.path}")
//    private String chequeInquiryTransferPath;
//    @Value("${shahin.cheque.register.path}")
//    private String chequeRegisterPath;
//    @Value("${shahin.cheque.transfer.path}")
//    private String chequeTransferPath;
//    @Value("${shahin.get.chequebook.list.path}")
//    private String getChequebookListPath;
//    @Value("${shahin.get.chequebook.statement.path}")
//    private String getChequebookStatementPath;
//    @Value("${shahin.get.basic.customer.info.path}")
//    private String getBasicCustomerInfoPath;
//    @Value("${shahin.get.detail.customer.info.path}")
//    private String getDetailCustomerInfoPath;
//    @Value("${shahin.get.loan.statement.path}")
//    private String getLoanStatementPath;
//    @Value("${shahin.get.loan.info.path}")
//    private String getLoanInfoPath;
//    private String basicAth;
//    @Value("${shahin.get.token.path}")
//    private String getTokenPath;
//    @Value("${shahin.token.base.url}")
//    private String shahinTokenBaseUrl;
//    @Value("${shahin.ignore.ssl}")
//    private boolean ignoreSSL;
//
//    public ShahinCommonServiceImpl( DigitalSignature digitalSignature, Environment environment) {
//        this.digitalSignature = digitalSignature;
//    }
//
//    protected HashMap<String, String> getHeader(String accessToken, String signature) {
//        return new HashMap<>() {{
//            put("Content-Type", "application/json");
//            put("X-Obh-timestamp", String.valueOf(new Date().getTime()));
//            put("X-Obh-uuid", UUID.randomUUID().toString());
//            if (accessToken != null)
//                put("Authorization", accessToken);
//            if (signature != null)
//                put("X-Obh-signature", signature);
//        }};
//    }
//
//    private String createUrlPath(String path) {
//        return shahinBaseUrl + path;
//    }
//
//    private <T> T convertModel(Object o, Class<T> tClass) {
//        String json = gson.toJson(o);
//        return gson.fromJson(json, tClass);
//    }
//
//    public String createBearerToken(String accessToken) {
//        return "Bearer " + accessToken;
//    }
//
//    @Override
//    public TwoPartyTokenResponseDto getTwoWayToken(BankEnum bank) {
//        String tokenResStr = UniRestUtils.post(shahinTokenBaseUrl + getTokenPath
//                , null, new HashMap<>() {{
//                    put("Authorization", basicAth);
//                }}, TwoWayTokenRequestDto.builder().grantType("client_credentials").bank(bank).build(), ignoreSSL);
//        return gson.fromJson(tokenResStr, TwoPartyTokenResponseDto.class);
//    }
//
//    @Override
//    public CommonTokenResponseDto generateToken(String serviceType, Object requestObj) {
//        CommonTokenRequestDto commonTokenRequestDto = this.convertModel(requestObj, CommonTokenRequestDto.class);
//        return switch (serviceType) {
//            case "TWO_WAY" ->
//                    convertModel(this.getTwoWayToken(commonTokenRequestDto.getBank()), CommonTokenResponseDto.class);
//            case "THREE_WAY" -> CommonTokenResponseDto.builder().accessToken("3W").build();
//            case "THREE_WAY_TRUSTED" -> CommonTokenResponseDto.builder().accessToken("3WT").build();
//            default -> throw new GeneralException("Choose service type!");
//        };
//    }
//
//    public String generateSignature(String httpMethod, String urlPath, Object requestDto) {
//        return digitalSignature.createDigitalSignature1(httpMethod, urlPath, getHeader(null, null), gson.toJson(requestDto), clientId, clientSecret);
//    }
//
//
//    // <editor-fold desc="Account">
//    @Override
//    public GetAccountInfoResponseDto getAccountInfo(AccountRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getAccountInfoPath, requestDto);
//        String responseStr = post(this.createUrlPath(getAccountInfoPath), requestDto, getHeader("authStr", signature), null, true);
//        return gson.fromJson(responseStr, GetAccountInfoResponseDto.class);
//    }
//
//    @Override
//    public GetAccountBalanceResponseDto getAccountBalance(AccountRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getAccountBalancePath, requestDto);
//        String responseStr = post(createUrlPath(getAccountBalancePath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, GetAccountBalanceResponseDto.class);
//    }
//
//    @Override
//    public AccountResponseDto getAccountList(AccountRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getAccountListPath, requestDto);
//        String responseStr = post(createUrlPath(getAccountListPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, AccountResponseDto.class);
//    }
//
//    @Override
//    public GetAccountStatementResponseDto getAccountStatement(AccountStatementRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getAccountStatementPath, requestDto);
//        String responseStr = post(createUrlPath(getAccountStatementPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, GetAccountStatementResponseDto.class);
//    }
//
//    @Override
//    public GetIbanResponseDto getIban(GetIbanRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getIbanPath, requestDto);
//        String responseStr = post(createUrlPath(getIbanPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, GetIbanResponseDto.class);
//    }
//
//    @Override
//    public GetIbanInfoResponseDto getIbanInfo(AccountRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getIbanInfoPath, requestDto);
//        String responseStr = post(createUrlPath(getIbanInfoPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, GetIbanInfoResponseDto.class);
//    }
//
//    @Override
//    public GetLegalAccountInfoResponseDto getLegalAccountInfo(AccountRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getLegalAccountInfoPath, requestDto);
//        String responseStr = post(createUrlPath(getLegalAccountInfoPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, GetLegalAccountInfoResponseDto.class);
//    }
//    // </editor-fold>
//
//    // <editor-fold desc="BillPayment">
//    @Override
//    public BillPaymentValidationResponseDto billPaymentValidation(BillPaymentValidationRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", billPaymentValidationPath, requestDto);
//        String responseStr = post(createUrlPath(billPaymentValidationPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, BillPaymentValidationResponseDto.class);
//    }
//
//    @Override
//    public PayBillResponseDto payBill(PayBillRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", payBillPath, requestDto);
//        String responseStr = post(createUrlPath(payBillPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, PayBillResponseDto.class);
//    }
//    // </editor-fold>
//
//    // <editor-fold desc="BlockAmount">
//    @Override
//    public BlockAmountResponseDto blockAmount(BlockAmountRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", blockAmountPath, requestDto);
//        String responseStr = post(createUrlPath(blockAmountPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, BlockAmountResponseDto.class);
//    }
//
//    @Override
//    public BlockAmountInquiryResponseDto blockAmountInquiry(BlockAmountInquiryRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", blockAmountInquiryPath, requestDto);
//        String responseStr = post(createUrlPath(blockAmountInquiryPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, BlockAmountInquiryResponseDto.class);
//    }
//
//    @Override
//    public UnblockAndTransferResponseDto unblockAmountAndTransfer(UnblockAndTransferRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", unblockAmountTransferPath, requestDto);
//        String responseStr = post(createUrlPath(unblockAmountTransferPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, UnblockAndTransferResponseDto.class);
//    }
//    // </editor-fold>
//
//    // <editor-fold desc="Card">
//    @Override
//    public CardBalanceResponseDto cardBalance(CardBalanceRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", cardBalancePath, requestDto);
//        String responseStr = post(createUrlPath(cardBalancePath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, CardBalanceResponseDto.class);
//    }
//
//    @Override
//    public CardTransferResponseDto cardTransfer(CardTransferRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", cardTransferPath, requestDto);
//        String responseStr = post(createUrlPath(cardTransferPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, CardTransferResponseDto.class);
//    }
//
//    @Override
//    public GetCardInfoResponseDto getCardInfo(GetCardInfoRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getCardInfoPath, requestDto);
//        String responseStr = post(createUrlPath(getCardInfoPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, GetCardInfoResponseDto.class);
//    }
//
//    @Override
//    public CardStatementResponseDto getCardTrans(CardStatementRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getCardTransPath, requestDto);
//        String responseStr = post(createUrlPath(getCardTransPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, CardStatementResponseDto.class);
//    }
//
//    @Override
//    public PayBillByCardResponseDto payBillByCard(PayBillByCardRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", payBillByCardPath, requestDto);
//        String responseStr = post(createUrlPath(payBillByCardPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, PayBillByCardResponseDto.class);
//    }
//    // </editor-fold>
//
//    // <editor-fold desc="Cheque">
//    @Override
//    public ChequeAcceptResponseDto chequeAccept(ChequeAcceptRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", chequeAcceptPath, requestDto);
//        String responseStr = post(createUrlPath(chequeAcceptPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, ChequeAcceptResponseDto.class);
//    }
//
//    @Override
//    public ChequeInquiryByHolderResponseDto chequeInquiryByHolder(ChequeInquiryByHolderRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", chequeInquiryHolderPath, requestDto);
//        String responseStr = post(createUrlPath(chequeInquiryHolderPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, ChequeInquiryByHolderResponseDto.class);
//    }
//
//    @Override
//    public ChequeInquiryTransferResponseDto chequeInquiryTransfer(ChequeInquiryTransferRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", chequeInquiryTransferPath, requestDto);
//        String responseStr = post(createUrlPath(chequeInquiryTransferPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, ChequeInquiryTransferResponseDto.class);
//    }
//
//    @Override
//    public ChequeRegisterResponseDto chequeRegister(ChequeRegisterRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", chequeRegisterPath, requestDto);
//        String responseStr = post(createUrlPath(chequeRegisterPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, ChequeRegisterResponseDto.class);
//    }
//
//    @Override
//    public ChequeTransferResponseDto chequeTransfer(ChequeTransferRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", chequeTransferPath, requestDto);
//        String responseStr = post(createUrlPath(chequeTransferPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, ChequeTransferResponseDto.class);
//    }
//
//    @Override
//    public GetChequebookListResponseDto getChequebookList(GetChequebookListRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getChequebookListPath, requestDto);
//        String responseStr = post(createUrlPath(getChequebookListPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, GetChequebookListResponseDto.class);
//    }
//
//    @Override
//    public GetChequeStatementResponseDto getChequeStatement(GetChequeStatementRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getChequebookStatementPath, requestDto);
//        String responseStr = post(createUrlPath(getChequebookStatementPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, GetChequeStatementResponseDto.class);
//    }
//    // </editor-fold>
//
//    // <editor-fold desc="Customer">
//    @Override
//    public GetBasicCustinfoResponseDto getBasicCustomerInfo(GetBasicCustinfoRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getBasicCustomerInfoPath, requestDto);
//        String responseStr = post(createUrlPath(getBasicCustomerInfoPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, GetBasicCustinfoResponseDto.class);
//    }
//
//    @Override
//    public GetDetailCustinfoResponseDto getDetailCustomerInfo(GetDetailCustinfoRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getDetailCustomerInfoPath, requestDto);
//        String responseStr = post(createUrlPath(getDetailCustomerInfoPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, GetDetailCustinfoResponseDto.class);
//    }
//    // </editor-fold>
//
//    // <editor-fold desc="Loan">
//    @Override
//    public GetLoanStatementResponseDto getLoanStatement(GetLoanStatementRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getLoanStatementPath, requestDto);
//        String responseStr = post(createUrlPath(getLoanStatementPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, GetLoanStatementResponseDto.class);
//    }
//
//    @Override
//    public GetLoanInfoResponseDto getLoanInfo(GetLoanInfoRequestDto requestDto, String serviceType) {
//        CommonTokenResponseDto tokenResponseDto = this.generateToken(serviceType, requestDto);
////        var authStr = this.createBearerToken(tokenResponseDto.getAccessToken());
//        var signature = this.generateSignature("POST", getLoanInfoPath, requestDto);
//        String responseStr = post(createUrlPath(getLoanInfoPath), requestDto, getHeader("authStr", signature));
//        return gson.fromJson(responseStr, GetLoanInfoResponseDto.class);
//    }
//    // </editor-fold>
//
//}
