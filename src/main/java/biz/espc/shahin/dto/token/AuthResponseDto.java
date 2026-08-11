package biz.espc.shahin.dto.token;

import biz.espc.shahin.enumeration.bank.Bank;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.UUID;

/**
 * provided by ESPC software team
 * created on 2/16/2026 at 03:21 PM
 */
public record AuthResponseDto(
        String access_token,
        String token_type,
        long expires_in,
        String user_name,
        String[] scope,
        Bank bank,
        String[] accounts,
        long amount,
        String jti,
        long iat,
        long nbf,
        String iss,
        long exp
) {

    @Override
    public @NonNull String toString() {
        return "AuthenticationResponseDto{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in=" + expires_in +
                ", user_name='" + user_name + '\'' +
                ", scope=" + Arrays.toString(scope) +
                ", bank=" + bank +
                ", accounts=" + Arrays.toString(accounts) +
                ", amount=" + amount +
                ", jti=" + jti +
                ", iat='" + iat + '\'' +
                ", nbf='" + nbf + '\'' +
                ", iss='" + iss + '\'' +
                ", exp='" + exp + '\'' +
                '}';
    }
}
