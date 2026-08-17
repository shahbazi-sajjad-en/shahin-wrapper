//package biz.espc.shahin.threePartyProtocol.serviceImpl;
//
//import biz.espc.shahin.common.dto.threeWayTrusted.token.ThreeWayTrustedTokenRequestDto;
//import biz.espc.shahin.common.dto.threeWayTrusted.token.ThreeWayTrustedTokenResponseDto;
//import biz.espc.shahin.threePartyProtocol.service.ThreeWayTrustedService;
//import biz.espc.shahin.common.util.UniRestUtils;
//import com.google.gson.Gson;
//import org.apache.commons.codec.binary.Base64;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.UUID;
//
//@Component
//public class ThreeWayTrustedServiceImpl implements ThreeWayTrustedService {
//
//    @Value("${shahin.service.base.url}")
//    private String shahinBaseUrl;
//    @Value("${shahin.get.token.path}")
//    private String getTokenPath;
//    @Value("${shahin.auth.username}")
//    private static String authUsername;
//    @Value("${shahin.auth.authPassword}")
//    private static String authPassword;
//    private static final String basicAth;
//    private static final HashMap<String, String> header;
//
//    private final Gson gson = new Gson();
//
//    static {
//        String auth = authUsername + ":" + authPassword;
//        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
//        basicAth = "Basic " + new String(encodedAuth);
//
//        header = new HashMap<>() {{
//            put("Content_Type", "application/json");
//            put("X-Obh-timestamp", String.valueOf(System.currentTimeMillis() / 100));
//            put("X-Obh-uuid", UUID.randomUUID().toString());
//            put("X-Obh-signature", UUID.randomUUID().toString());
//        }};
//    }
//
//    private String createUrlPath(String path) {
//        return shahinBaseUrl + path;
//    }
//
//    @Override
//    public ThreeWayTrustedTokenResponseDto getThreeWayToken(ThreeWayTrustedTokenRequestDto requestDto) {
//        String tokenResStr = UniRestUtils.get(
//                createUrlPath(getTokenPath)
//                , requestDto, new HashMap<>() {{
//                    put("Authorization", basicAth);
//                }});
//        return gson.fromJson(tokenResStr, ThreeWayTrustedTokenResponseDto.class);
//    }
//}
