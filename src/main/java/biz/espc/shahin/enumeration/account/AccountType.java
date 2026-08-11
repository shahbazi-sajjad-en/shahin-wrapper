package biz.espc.shahin.enumeration.account;

import biz.espc.shahin.exception.CommonException;
import org.springframework.http.HttpStatus;

import java.util.Locale;


public enum AccountType {
    CURRENT_ACCOUNT_JAM,
    QARZOLHASANE,
    SHORT_TERM_ACCOUNT,
    REAL_SAVING_ACCOUNT,
    LEGAL_SAVING_ACCOUNT,
    REAL_CURRENT_ACCOUNT,
    LEGAL_CURRENT_ACCOUNT,
    SAVING_ACCOUNT,
    SPECIAL_LEGAL_ACCOUNT,
    CurrentAccount;

    public static AccountType fromInput(String input) {
        if (input == null || input.isBlank()) {
            throw new CommonException(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_ACCOUNT_TYPE",
                    "accountType is null or blank"
            );
        }

        String normalized = input.trim().toUpperCase(Locale.ROOT);

        try {
            return AccountType.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new CommonException(
                    HttpStatus.BAD_REQUEST,
                    "INVALID_ACCOUNT_TYPE",
                    "unsupported accountType: " + input
            );
        }
    }

    public String toProviderValue() {
        return switch (this) {
            case CURRENT_ACCOUNT_JAM -> "currentAccountJam";
            case QARZOLHASANE -> "qarzolHasane";
            case SHORT_TERM_ACCOUNT -> "shortTermAccount";
            case REAL_SAVING_ACCOUNT -> "realSavingAccount";
            case LEGAL_SAVING_ACCOUNT -> "galSavingAccount";
            case REAL_CURRENT_ACCOUNT -> "realCurrentAccount";
            case LEGAL_CURRENT_ACCOUNT -> "egalCurrentAccount";
            case SAVING_ACCOUNT -> "savingAccount17";
            case SPECIAL_LEGAL_ACCOUNT -> "specialLegalAccount";
            case CurrentAccount -> null;
        };
    }
}
